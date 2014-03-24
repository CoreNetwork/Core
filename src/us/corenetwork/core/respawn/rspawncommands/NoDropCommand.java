package us.corenetwork.core.respawn.rspawncommands;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.Util;

public class NoDropCommand extends BaseRSpawnCommand {	
	public static HashSet<String> blockedPlayers = new HashSet<String>();

	public NoDropCommand()
	{
		needPlayer = false;
		permission = "nodrop";
	}

	public void run(final CommandSender sender, String[] args) {
		if (args.length < 2 || !Util.isInteger(args[1]))
			return;

		final String playerName = args[0];
		int protectionLength = Integer.parseInt(args[1]);

		blockedPlayers.add(playerName);

		Bukkit.getScheduler().runTaskLater(CorePlugin.instance, new Runnable() {

			@Override
			public void run() {
				blockedPlayers.remove(playerName);
			}

		}, protectionLength);

		return;
	}	

}
