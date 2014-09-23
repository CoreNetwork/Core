package us.corenetwork.core.corecommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand extends BaseCoreCommand {
	private static List<String> sudoPlayers = new ArrayList<String>();
	
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

		if (args[1].startsWith("/"))
			args[1] = args[1].substring(1);

		String playerName = args[0];

		String commandLine = "";
		for (int i = 1; i < args.length; i++)
			commandLine = commandLine.concat(args[i]).concat(" ");

		commandLine = commandLine.substring(0, commandLine.length() - 1);


		Player player = Bukkit.getPlayer(playerName);
		if (player == null)
			return;

		sudo(player, commandLine);

	}	
	
	public static void sudo(Player player, String command)
	{
		String playerName = player.getName();
		
		boolean isOp = player.isOp();
		try
		{
			sudoPlayers.add(playerName);
			
			if (!isOp)
				player.setOp(true);

			Bukkit.getServer().dispatchCommand(player, command);

		}
		finally
		{			
			if (!isOp)
				player.setOp(false);

			sudoPlayers.remove(playerName);
		}
	}
	
	public static boolean isUnderSudo(String player)
	{
		return sudoPlayers.contains(player);
	}

}
