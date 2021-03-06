package us.corenetwork.core.respawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import us.corenetwork.core.CLog;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.respawn.rspawncommands.NoDropCommand;
import us.corenetwork.core.teleport.commands.warp.WarpCommand;
import us.corenetwork.core.util.RegulatedMessenger;

public class RespawnListener implements Listener {

	private RegulatedMessenger godModeAbuseMessenger;

	public RespawnListener()
	{
		godModeAbuseMessenger = new RegulatedMessenger(RespawnSettings.PROTECTION_ABUSE_SPAM_DELAY_SECONDS.integer() * 1000);
	}

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

					godModeAbuseMessenger.sendMessage(damager, RespawnSettings.MESSAGE_SPAWN_PROTECTION_DONT_ABUSE.string());

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
	
	@EventHandler(ignoreCancelled = true)
	public void onPLayerJoin(PlayerJoinEvent event)
	{
		if (!KnownPlayers.isKnownPlayer(event.getPlayer().getUniqueId()))
		{
			KnownPlayers.savePlayer(event.getPlayer().getUniqueId());
			
			String warpName = RespawnSettings.NEW_PLAYER_SPAWN_WARP_NAME.string();
			Location loc = WarpCommand.getWarpLocation(warpName);
			
			if (loc == null)
			{
				CLog.severe("Respawn warp for new players does not exist!");
				return;
			}
			
			event.getPlayer().teleport(loc);
			WarpCommand.runWarpCommands(warpName, event.getPlayer());
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerReSpawn(PlayerRespawnEvent event)
	{
		
		String warpName = RespawnSettings.EXISTING_PLAYER_SPAWN_WARP_NAME.string();
		Location loc = WarpCommand.getWarpLocation(warpName);
		
		if (loc == null)
		{
			CLog.severe("Respawn warp for existing players does not exist!");
			return;
		}
		
		event.setRespawnLocation(loc);
	}

}
