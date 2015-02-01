package us.corenetwork.core.claims;

import java.util.LinkedHashMap;
import java.util.Map;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;
import us.corenetwork.core.CLog;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;

public class ClaimsDamageProxy implements Listener {
    private RegisteredListener griefPreventionListener;

    public void init() {
        griefPreventionListener = Util.removeListener("me.ryanhamshire.GriefPrevention.EntityEventHandler", EntityDamageEvent.class);
        if (griefPreventionListener != null) {
            CLog.debug("Proxied GP's PlayerInteractionEvent handler");
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {

        if (event instanceof EntityDamageByEntityEvent && event.getEntity().getType() == EntityType.VILLAGER && ((EntityDamageByEntityEvent) event).getDamager().getType() != EntityType.PLAYER)
        {
            return; //Do not call GP event if villager was hurt.
        }

        try
        {
            griefPreventionListener.callEvent(event);
        }
        catch (EventException e)
        {
            e.printStackTrace();
        }
    }
}
