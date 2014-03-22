package us.corenetwork.core.scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		CoreScoreboardManager.getPlayerData(event.getPlayer().getName()).init(event.getPlayer());
	}
	
	@EventHandler()
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		CoreScoreboardManager.scoreboards.remove(event.getPlayer().getName());
	}
}
