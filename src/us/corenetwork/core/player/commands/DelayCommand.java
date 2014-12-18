package us.corenetwork.core.player.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.player.PlayerModule;

public class DelayCommand extends BasePlayerCommand {

	private static HashMap<UUID, String> frozenPlayers = new HashMap<UUID, String>();

	public DelayCommand()
	{
		desc = "Direct message player";
		permission = "delay";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		if (args.length < 2 || !Util.isInteger(args[0]))
		{
			PlayerUtils.Message("Usage: /delay <seconds> [<frozen>] [<player>] <command> [<command's params>]", sender);
			return;
		}

		int seconds = Integer.parseInt(args[0]);
		final String messageNode = args[1];
		final boolean freeze = PlayerModule.instance.config.contains("Message.FrozenMessages." + messageNode + ".Start");

		if (freeze && args.length < 4)
		{
			PlayerUtils.Message("Usage: /delay <seconds> <frozen message node> <player> <command> [<command's params>]", sender);
		}

		int commandStart = freeze ? 3 : 1;

		if (args[commandStart].startsWith("/"))
			args[commandStart] = args[commandStart].substring(1);

		String commandToRun = "";
		for (int i = commandStart; i < args.length; i++)
			commandToRun = commandToRun.concat(args[i]).concat(" ");
		commandToRun = commandToRun.substring(0, commandToRun.length() - 1);

		final Player player;

		if (freeze)
		{
			String playerName = args[2];
			player = Bukkit.getPlayerExact(playerName);
			if (player == null)
				return;


			String message = (String) PlayerModule.instance.config.get("Message.FrozenMessages." + messageNode + ".Start");

			message = message.replace("<Time>", Integer.toString(seconds));
			if (seconds == 1)
				message = message.replace("<PluralS>", "");
			else
				message = message.replace("<PluralS>", "s");

			PlayerUtils.Message(message, player);
			frozenPlayers.put(player.getUniqueId(), (String) PlayerModule.instance.config.get("Message.FrozenMessages." + messageNode + ".Interrupt"));

		}
		else
			player = null;


		final String commandFinal = commandToRun;
		Bukkit.getScheduler().runTaskLater(CorePlugin.instance, new Runnable()
		{
			@Override
			public void run()
			{
				if (freeze && !frozenPlayers.containsKey(player.getUniqueId()))
					return;

				if (player != null)
					frozenPlayers.remove(player.getUniqueId());

				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandFinal);
			}
		}, seconds * 20);
	}

	public static void playerMoved(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		String interruptMessage = frozenPlayers.get(player.getUniqueId());
		if (interruptMessage != null)
		{
			boolean stayedStill = event.getFrom().getX() == event.getTo().getX() && event.getFrom().getY() == event.getTo().getY() && event.getFrom().getZ() == event.getTo().getZ();

			if (!stayedStill)
			{
				PlayerUtils.Message(interruptMessage, player);
				frozenPlayers.remove(player.getUniqueId());
			}
		}
	}
}
