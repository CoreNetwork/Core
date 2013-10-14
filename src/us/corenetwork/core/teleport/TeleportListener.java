package us.corenetwork.core.teleport;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TeleportListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		OfflineTeleportation.playerLoggedIn(event.getPlayer());
	}
}
