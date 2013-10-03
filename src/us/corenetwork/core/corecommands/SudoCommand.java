package us.corenetwork.core.corecommands;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CLog;
import us.corenetwork.core.Util;

public class SudoCommand extends BaseCoreCommand {
	public SudoCommand()
	{
		desc = "Run any command as op";
		needPlayer = false;
		permission = "sudo";
	}


	public void run(final CommandSender sender, String[] args) {

		if (args.length < 2)
		{
			return;
		}

		if (!args[1].startsWith("/"))
			args[1] = "/" + args[1];

		String playerName = args[0];

		String commandLine = "";
		for (int i = 1; i < args.length; i++)
			commandLine = commandLine.concat(args[i]).concat(" ");

		commandLine = commandLine.substring(0, commandLine.length() - 1);


		Player player = Bukkit.getPlayer(playerName);
		if (player == null)
			return;

		boolean isOp = player.isOp();
		try
		{
			if (!isOp)
				player.setOp(true);

			player.chat(commandLine);

		}
		finally
		{
			if (!isOp)
				player.setOp(false);

		}

	}	

}
