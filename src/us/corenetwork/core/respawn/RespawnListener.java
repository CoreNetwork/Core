package us.corenetwork.core.respawn;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.respawn.rspawncommands.NoDropCommand;

public class RespawnListener implements Listener {
		
	@EventHandler(ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event)
	{
		if (event.getTarget() instanceof Player)
		{
			//Check for spawn protection
			Player player = (Player) event.getTarget();
			if (ProtectTimer.protectedPlayers.containsKey(player.getName()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event)
	{
		//Check for spawn protection abuse
		if (event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent newEvent = (EntityDamageByEntityEvent) event;
			if (newEvent.getDamager() instanceof Player)
			{
				Player damager = (Player) newEvent.getDamager();
				if (ProtectTimer.protectedPlayers.containsKey(damager.getName()))
				{
					event.setCancelled(true);

					PlayerUtils.Message(RespawnSettings.MESSAGE_SPAWN_PROTECTION_DONT_ABUSE.string(), damager);

					return;
				}

			}
			//Prevent any damage from mobs
			else if (newEvent.getEntity() instanceof Player)
			{
				Player victim = (Player) newEvent.getEntity();

				if (ProtectTimer.protectedPlayers.containsKey(victim.getName()))
				{
					event.setCancelled(true);
					return;
				}
			}
		}

	}

	
	@EventHandler(ignoreCancelled = true)
	public void onFoodchange(FoodLevelChangeEvent event)
	{
		//Check for spawn protection
		Player player = (Player) event.getEntity();
		if (ProtectTimer.protectedPlayers.containsKey(player.getName()))
		{
			event.setCancelled(true);
			return;
		}

	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if (NoDropCommand.blockedPlayers.contains(event.getPlayer().getName()))
			event.setCancelled(true);
	}
}
