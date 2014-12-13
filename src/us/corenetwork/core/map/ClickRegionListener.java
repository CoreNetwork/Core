package us.corenetwork.core.map;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.milkbowl.vault.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import us.corenetwork.core.map.clicks.ClickAlias;
import us.corenetwork.core.map.clicks.ClickAliasIndex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClickRegionListener implements Listener {
    WorldGuardPlugin worldGuard;

    {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

        worldGuard = (WorldGuardPlugin) plugin;
    }

    private HashMap<ClickAliasIndex, ClickAlias> regionsToCommandMap = new HashMap<ClickAliasIndex, ClickAlias>();

    public void load(YamlConfiguration config) {
        List clicks = config.getList("Clicks");
        if (clicks != null) {
            for (Object obj : clicks) {
                Map<String, Object> clickAliasMap = (Map<String, Object>) obj;
                List<Material> materials = new LinkedList<Material>();
                List<String> regions = new LinkedList<String>();
                List<String> commands = new LinkedList<String>();

                if (clickAliasMap.containsKey("block")) {
                    Object blockObj = clickAliasMap.get("block");
                    if (blockObj instanceof String) {
                        materials.add(Items.itemByName((String) blockObj).getType());
                    } else if (blockObj instanceof List) {
                        for (String m : (List<String>) blockObj) {
                            materials.add(Items.itemByName(m).getType());
                        }
                    }
                }
                if (clickAliasMap.containsKey("regions")) {
                    Object regionObj = clickAliasMap.get("regions");
                    if (regionObj instanceof String) {
                        regions.add((String) regionObj);
                    } else if (regionObj instanceof List) {
                        regions.addAll((List<String>) regionObj);
                    }
                }
                if (clickAliasMap.containsKey("commands")) {
                    Object commandObj = clickAliasMap.get("commands");
                    if (commandObj instanceof String) {
                        commands.add(((String) commandObj).replaceFirst("/", ""));
                    } else if (commandObj instanceof List) {
                        for (String command : (List<String>) commandObj) {
                            commands.add(command.replaceFirst("/", ""));
                        }
                    }
                }

                ClickAlias alias = new ClickAlias(commands);
                for (Material material : materials) {
                    for (String region : regions) {
                        ClickAliasIndex index = new ClickAliasIndex(material, region);
                        regionsToCommandMap.put(index, alias);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUseBlock(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            RegionManager regionManager = worldGuard.getRegionManager(event.getClickedBlock().getWorld());
            ApplicableRegionSet set = regionManager.getApplicableRegions(event.getClickedBlock().getLocation());
            for (ProtectedRegion region : set) {
                ClickAlias alias = regionsToCommandMap.get(new ClickAliasIndex(event.getClickedBlock().getType(), region.getId()));
                if (alias != null) {
                    alias.execute(event.getPlayer());
                }
            }
        }
    }
}
