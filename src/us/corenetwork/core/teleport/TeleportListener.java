package us.corenetwork.core.teleport;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportListener implements Listener {
	protected static HashMap<String, Long> frozenPlayers = new HashMap<String, Long>();
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		//Freeze
		Long frozenUntil = frozenPlayers.get(event.getPlayer().getName());
		if (frozenUntil != null)
		{
			if (frozenUntil > System.currentTimeMillis())
			{
				event.setCancelled(true);
				return;
			}
			else
				frozenPlayers.remove(event.getPlayer().getName());
		}
	}
}
