package us.corenetwork.core.player.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.player.PlayerModule;
import us.corenetwork.core.player.PlayerSettings;

public class UnvanishCommand extends BasePlayerCommand {

	public UnvanishCommand()
	{
		desc = "Unvanish player. [silently]"; 
		permission = "unvanish";
		needPlayer = false;
	}
	
	public void run(CommandSender sender, String[] args) 
	{
		
		if (args.length == 0 || args.length > 2 || (args.length == 2 && !args[1].equals("silent")))
		{
			PlayerUtils.Message("Usage : /unvanish <player> [silent]", sender);
			return;
		}
		
		boolean silent = false;
		if (args.length == 2)
			silent = true;

		Player player = CorePlugin.instance.getServer().getPlayerExact(args[0]);
		if(player == null)
		{
			PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
			return;
		}

		if (silent == false)
		{
			PlayerUtils.Message(PlayerSettings.MESSAGE_PLAYER_UNVANISHED.string().replace("<Player>", player.getName()), sender);
			PlayerUtils.Message(PlayerSettings.MESSAGE_UNVANISHED.string(), player);
		}
		
		PlayerModule.vanishManager.unvanish(player);
	}

}
