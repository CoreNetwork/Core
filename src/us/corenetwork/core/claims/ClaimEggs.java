package us.corenetwork.core.claims;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils;

public class ClaimEggs implements Listener
{
    private List<SpawnedChicken> recentlySpawnedChickens = new LinkedList<SpawnedChicken>();

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event)
    {
        Projectile projectile = event.getEntity();

        // Prevent spamming eggs in non-claimed land
        if (projectile.getType() == EntityType.EGG)
        {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter == null)
                return;

            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(projectile.getLocation(), true, null);
            if (claim == null)
                return;

            if (shooter instanceof Player)
            {
                if (claim.allowAccess((Player) shooter) != null)
                {
                    preventSpawning(projectile.getLocation());
                    PlayerUtils.Message(ClaimsSettings.MESSAGE_NO_EGGS_IN_OTHER_CLAIM.string(), (Player) shooter);
                }
            }
            else if (shooter instanceof BlockProjectileSource)
            {
                if (!claim.contains(((BlockProjectileSource) shooter).getBlock().getLocation(), true, false))
                {
                    preventSpawning(projectile.getLocation());
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG)
        {
            final SpawnedChicken spawnedChicken = new SpawnedChicken();
            spawnedChicken.chicken = event.getEntity();
            spawnedChicken.spawnLocation = event.getLocation();

            recentlySpawnedChickens.add(spawnedChicken);

            //Remove chicken from recently spawned list after 1 tick (when all possible egg events for that chicken would have already triggered)
            Bukkit.getScheduler().runTaskLater(CorePlugin.instance, new Runnable()
            {
                @Override
                public void run()
                {
                    recentlySpawnedChickens.remove(spawnedChicken);
                }
            }, 1);
        }
    }

    public void preventSpawning(Location eggLocation)
    {
        Iterator<SpawnedChicken> iterator = recentlySpawnedChickens.iterator();
        while (iterator.hasNext())
        {
            SpawnedChicken spawnedChicken = iterator.next();
            if (locationEqualsIgnoreRotation(spawnedChicken.spawnLocation, eggLocation))
            {
                iterator.remove();
                spawnedChicken.chicken.remove();
            }
        }

    }

    public static boolean locationEqualsIgnoreRotation(Location a, Location b)
    {
        return a.getWorld().equals(b.getWorld()) && a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ();
    }

    private static class SpawnedChicken
    {
        LivingEntity chicken;
        Location spawnLocation;
    }

}
