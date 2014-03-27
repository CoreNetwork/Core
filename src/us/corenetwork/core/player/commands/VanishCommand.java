package us.corenetwork.core.player.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.player.PlayerModule;
import us.corenetwork.core.player.PlayerSettings;
import us.corenetwork.core.player.VanishManager;

public class VanishCommand extends BasePlayerCommand  {

	public VanishCommand()
	{
		desc = "Vanish player. [silently]"; 
		permission = "vanish";
		needPlayer = false;
	}
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		
		if (args.length == 0 || args.length > 2 || (args.length == 2 && !args[1].equals("silent")))
		{
			PlayerUtils.Message("Usage : /vanish <player> [silent]", sender);
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
			PlayerUtils.Message(PlayerSettings.MESSAGE_PLAYER_VANISHED.string().replace("<Player>", player.getName()), sender);
			PlayerUtils.Message(PlayerSettings.MESSAGE_VANISHED.string(), player);
		}
		
		PlayerModule.vanishManager.vanish(player);
	}

}
