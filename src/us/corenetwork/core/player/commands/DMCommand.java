package us.corenetwork.core.player.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.util.PlayerUtils;

public class DMCommand extends BasePlayerCommand {

	public DMCommand()
	{
		desc = "Direct message player";
		permission = "dm";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		if (args.length < 2)
		{
			PlayerUtils.Message("Usage : /dm <player> <message>", sender);
			return;
		}

		String playerName = args[0];

		String message = "";
		for (int i = 1; i < args.length; i++)
			message = message.concat(args[i]).concat(" ");

		message = message.substring(0, message.length() - 1);


		Player player = Bukkit.getPlayerExact(playerName);
		if (player == null)
			return;

		PlayerUtils.Message(message, player);
	}

}
