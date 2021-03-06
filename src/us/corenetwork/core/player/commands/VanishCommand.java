package us.corenetwork.core.player.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.Setting;
import us.corenetwork.core.Settings;
import us.corenetwork.core.util.Util;
import us.corenetwork.core.player.PlayerModule;
import us.corenetwork.core.player.PlayerSettings;

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
		if(PlayerSettings.VANISH_ENABLED.bool() == false)
		{
			PlayerUtils.Message(PlayerSettings.MESSAGE_VANISH_DISABLED.string(), sender);
			return;
		}
		
		if (args.length > 2 || (args.length == 2 && !args[1].equals("silent")))
		{
			PlayerUtils.Message("Usage : /vanish [<player>] [silent]", sender);
			return;
		}
		
		boolean silent = false;
		Player player = null;
		
		if (args.length == 0)
		{
			if (sender instanceof Player)
			{
				player = (Player) sender;
			}
			else
			{
				PlayerUtils.Message("You can only execute /vanish [silent] as player.", sender);
				return;
			}
		}
		else if (args.length == 1)
		{
			player = CorePlugin.instance.getServer().getPlayerExact(args[0]);
			if(player == null)
			{
				if (args[0].toLowerCase().equals("silent"))
				{
					silent = true;
					if (sender instanceof Player)
					{
						player = (Player) sender;
					}
					else
					{
						PlayerUtils.Message("You can only execute /vanish [silent] as player.", sender);
						return;
					}
				}
				else
				{
					PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
					return;
				}
			}
		}
		else if (args.length == 2)
		{
			player = CorePlugin.instance.getServer().getPlayerExact(args[0]);
			if(player == null)
			{
				PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
				return;
			}
			if (args[1].toLowerCase().equals("silent"))
			{
				silent = true;
			}
			else
			{
				PlayerUtils.Message("Usage : /vanish [<player>] [silent]", sender);
				return;
			}
		}
			

		
		if (silent == false)
		{
			String message = PlayerSettings.MESSAGE_PLAYER_VANISHED.string().replace("<Player>", player.getName());
			if (sender.equals(player) == false)
				PlayerUtils.Message(message, sender);
			PlayerUtils.Message(PlayerSettings.MESSAGE_VANISHED.string(), player);
			
			PlayerModule.vanishManager.notifyModerators(sender, message, player);
		}
		else
		{
			if(sender instanceof Player && !Util.hasPermission(sender, permissionNode + permission + ".silent"))
			{
				PlayerUtils.Message(Settings.getString(Setting.MESSAGE_NO_PERMISSION), sender);
				return;
			}
		}
		
		PlayerModule.vanishManager.vanish(player);
	}

}
