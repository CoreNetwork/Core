package us.corenetwork.core.claims;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import net.milkbowl.vault.item.Items;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;
import us.corenetwork.core.CLog;
import us.corenetwork.core.PlayerUtils;

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
        for (RegisteredListener listener : PlayerInteractEvent.getHandlerList().getRegisteredListeners()) {
            if (listener.getListener().getClass().getName().equals("me.ryanhamshire.GriefPrevention.PlayerEventHandler")) {
                griefPreventionListener = listener;
            }
        }
        if (griefPreventionListener != null) {
            PlayerInteractEvent.getHandlerList().unregister(griefPreventionListener);
            CLog.info("Proxied GriefPrevention PlayerInteractionEvent");
        }
        griefPreventionDataStore = GriefPrevention.instance.dataStore;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            boolean cancel = false;
            if (griefPreventionListener != null && griefPreventionDataStore != null) {
                PlayerData playerData = griefPreventionDataStore.getPlayerData(event.getPlayer().getUniqueId());
                if (event.getPlayer().getItemInHand().getType() == tool && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (griefPreventionDataStore.getClaimAt(event.getClickedBlock().getLocation(), false, null) == null) {
                        // player clicked outside of a claim or is not resizing a claim
                        if (playerData.claimResizing == null && playerData.claimSubdividing == null) {
                            int maxClaims = getMaxClaims(event.getPlayer());
                            if (getPlayerClaims(event.getPlayer(), event.getClickedBlock().getWorld()) >= maxClaims) {
                                // player has no claims left in this world. cancel event and don't execute GriefPreventions thing
                                PlayerUtils.Message(ClaimsSettings.CLAIMSLIMIT_NO_CLAIMS_LEFT.string().replace("<Claims>", "" + maxClaims), event.getPlayer());
                                cancel = true;
                                event.setCancelled(true);
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
                        claim = griefPreventionDataStore.getClaimAt(event.getClickedBlock().getLocation(), true, null);
                        for (int x = bStart.getBlockX(); x <= bEnd.getBlockX(); x++) {
                            for (int y = 0; y <= 255; y++) {
                                for (int z = bStart.getBlockZ(); z <= bEnd.getBlockZ(); z++) {
                                    Location current = new Location(bStart.getWorld(), x, y, z);
                                    if (!claim.contains(current, true, false)) {
                                        Material mat = ClaimFluids.getLiquidType(current.getBlock().getType());
                                        if (mat != null) {
                                            current.getBlock().setType(Material.AIR);
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
