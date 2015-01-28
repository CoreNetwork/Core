package us.corenetwork.core.claims;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import net.milkbowl.vault.item.Items;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.BlockIterator;
import us.corenetwork.core.CLog;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClaimsAreaProxy implements Listener {
    private RegisteredListener griefPreventionListener;
    private DataStore griefPreventionDataStore;
    private Map<String, Integer> groupLimits = new LinkedHashMap<String, Integer>();
    private Permission permission;
    private Material tool;

    public void loadConfig(YamlConfiguration configuration) {
        ConfigurationSection claimsLimits = configuration.getConfigurationSection("ClaimsLimits");
        if (claimsLimits != null) {
            groupLimits.clear();
            tool = Items.itemByName(claimsLimits.getString("Tool")).getType();
            for (Map.Entry<String, Object> entry : claimsLimits.getConfigurationSection("Groups").getValues(false).entrySet()) {
                if (entry.getValue() instanceof Integer) {
                    groupLimits.put(entry.getKey(), (Integer) entry.getValue());
                }
            }
        }

        RegisteredServiceProvider<Permission> registration = Bukkit.getServicesManager().getRegistration(Permission.class);
        if (registration != null) {
            permission = registration.getProvider();
        }
    }

    public void init() {
        griefPreventionListener = Util.removeListener("me.ryanhamshire.GriefPrevention.PlayerEventHandler", PlayerInteractEvent.class);
        if (griefPreventionListener != null) {
            CLog.debug("Proxied GP's PlayerInteractionEvent handler");
        }
        griefPreventionDataStore = GriefPrevention.instance.dataStore;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            boolean cancel = false;
            if (griefPreventionListener != null && griefPreventionDataStore != null) {

                Block clickedBlock = null;
                PlayerData playerData = griefPreventionDataStore.getPlayerData(event.getPlayer().getUniqueId());
                if (event.getPlayer().getItemInHand().getType() == tool)
                {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    {
                        clickedBlock = event.getClickedBlock();
                    }
                    else if(event.getAction() == Action.RIGHT_CLICK_AIR)
                    {
                        clickedBlock = getTargetBlock(event.getPlayer(), 100);
                    }

                    if (clickedBlock != null)
                    {
                        if (griefPreventionDataStore.getClaimAt(clickedBlock.getLocation(), false, null) == null)
                        {
                            // player clicked outside of a claim or is not resizing a claim
                            if (playerData.claimResizing == null && playerData.claimSubdividing == null)
                            {
                                int maxClaims = getMaxClaims(event.getPlayer());
                                if (getPlayerClaims(event.getPlayer(), clickedBlock.getWorld()) >= maxClaims)
                                {
                                    // player has no claims left in this world. cancel event and don't execute GriefPreventions thing
                                    PlayerUtils.Message(ClaimsSettings.CLAIMSLIMIT_NO_CLAIMS_LEFT.string().replace("<Claims>", "" + maxClaims), event.getPlayer());
                                    cancel = true;
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }

                if (!cancel) {
                    CLog.debug("Calling GP player interaction event via proxy.");
                    Claim claim = playerData.claimResizing;
                    Location bStart = null, bEnd = null;
                    if (claim != null) {
                        bStart = claim.getLesserBoundaryCorner().clone();
                        bEnd = claim.getGreaterBoundaryCorner().clone();
                    }
                    griefPreventionListener.callEvent(event);
                    if (claim != null && playerData.claimResizing == null) {
                        claim = griefPreventionDataStore.getClaimAt(clickedBlock.getLocation(), true, null);
                        World world = claim.getLesserBoundaryCorner().getWorld();
                        ClaimFluids.Range lava = ClaimsModule.instance.claimFluids.getRange(Material.LAVA, world.getName());
                        ClaimFluids.Range water = ClaimsModule.instance.claimFluids.getRange(Material.WATER, world.getName());
                        for (int x = bStart.getBlockX(); x <= bEnd.getBlockX(); x++) {
                            for (int y = 0; y <= 255; y++) {
                                for (int z = bStart.getBlockZ(); z <= bEnd.getBlockZ(); z++) {

                                    Block block = world.getBlockAt(x, y, z);
                                    Material mat = ClaimFluids.getLiquidType(block.getType());
                                    if (mat == Material.WATER && (water == null || (y < water.getMin() || y > water.getMax()))) {
                                        block.setType(Material.AIR);
                                    }
                                    if (mat == Material.LAVA && (lava == null || (y < lava.getMin() || y > lava.getMax()))) {
                                        block.setType(Material.AIR);
                                    }

                                    if(ClaimsModule.instance.claimPerks.isPerkBlock(block)) {
                                        block.setType(Material.AIR);
                                    }
                                }
                            }
                        }


                        //Removing perks armorstands
                        Chunk startChunk = bStart.getChunk();
                        Chunk endChunk = bEnd.getChunk();

                        for (int x = startChunk.getX(); x <= endChunk.getX(); x++) {
                            for (int z = startChunk.getZ(); z <= endChunk.getZ(); z++) {
                                Chunk chunk = world.getChunkAt(x,z);
                                for(Entity e : chunk.getEntities())
                                {
                                    if(e.getType() == EntityType.ARMOR_STAND)
                                    {
                                        int xLoc = e.getLocation().getBlockX();
                                        int zLoc = e.getLocation().getBlockZ();

                                        //is in original claim
                                        if( xLoc >= bStart.getBlockX() && xLoc <= bEnd.getBlockX() && zLoc >= bStart.getBlockZ() && zLoc <= bEnd.getBlockZ())
                                        {
                                            Location current = e.getLocation();
                                            //is in new claim
                                            if (!claim.contains(current, true, false)) {
                                                ArmorStand as = (ArmorStand) e;
                                                if(as.hasArms())
                                                {
                                                    e.remove();
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (EventException e) {
            e.printStackTrace();
        }
    }

    static Block getTargetBlock(Player player, int maxDistance) throws IllegalStateException
    {
        BlockIterator iterator = new BlockIterator(player.getLocation(), player.getEyeHeight(), maxDistance);
        Block result = player.getLocation().getBlock().getRelative(BlockFace.UP);
        while (iterator.hasNext())
        {
            result = iterator.next();
            if(result.getType() != Material.AIR &&
                    result.getType() != Material.STATIONARY_WATER &&
                    result.getType() != Material.LONG_GRASS) return result;
        }

        return result;
    }


    public int getPlayerClaims(Player player, World world) {
        PlayerData playerData = griefPreventionDataStore.getPlayerData(player.getUniqueId());
        int sum = 0;
        for (Claim claim : playerData.getClaims()) {
            if (claim.getChunks().get(0).getWorld() == world) {
                sum ++;
            }
        }
        return sum;
    }

    public int getMaxClaims(Player player) {
        int max = 0;
        for (String group : groupLimits.keySet()) {
            if (permission.playerInGroup(player, group)) {
                int limit = groupLimits.get(group);
                if (limit > max) {
                    max = limit;
                }
            }
        }
        return max;
    }
}
