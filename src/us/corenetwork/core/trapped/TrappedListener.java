package us.corenetwork.core.trapped;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import us.corenetwork.core.CLog;

public class TrappedListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent  event)
	{
		if (event.getMessage().equals("/trapped"))
		{
			event.setCancelled(true);
			TrappedModule.commands.get("trapped").execute((CommandSender) event.getPlayer(), new String[0], false);
		}
	}
}
