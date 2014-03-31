package us.corenetwork.core.player.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.player.PlayerModule;
import us.corenetwork.core.player.PlayerSettings;

public class UngodCommand extends BasePlayerCommand {

	public UngodCommand()
	{
		desc = "Revoke the god mode."; 
		permission = "ungod";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		if (args.length > 2)
		{
			PlayerUtils.Message("Usage : /ungod <player> [silent]", sender);
			return;
		}

		
		Player player = null;
		boolean silent = false;
		
		if (args.length == 0)
		{
			if (sender instanceof Player)
			{
				player = (Player) sender;
			}
			else
			{
				PlayerUtils.Message("You can only execute /god [silent] as player.", sender);
				return;
			}
		}
		if (args.length == 1)
		{
			player = CorePlugin.instance.getServer().getPlayerExact(args[0]);
			if (player == null)
			{
				if (args[0].toLowerCase().equals("silent"))
				{
					if (sender instanceof Player)
					{
						player = (Player) sender;
						silent = true;
					}
					else
					{
						PlayerUtils.Message("You can only execute /ungod [silent] as player.", sender);
						return;
					}
				}
				else
				{
					PlayerUtils.Message("Cannot find " + args[0] + " player." , sender);
					return;					
				}
			}
		}
		else if (args.length == 2)
		{
			player = CorePlugin.instance.getServer().getPlayerExact(args[0]);
			if (player == null)
			{
				PlayerUtils.Message("Cannot find " + args[0] + " player." , sender);
				return;					
			}
			
			if (args[1].toLowerCase().equals("silent"))
			{
				silent = true;
			}
			else
			{
				PlayerUtils.Message("Usage : /ungod <player> [silent]", sender);
				return;	
			}
		}
		
		PlayerModule.gods.remove(player.getName());
		
		if(silent == false)
		{
			PlayerUtils.Message(PlayerSettings.MESSAGE_LOST_GOD.string(), player);
		}
	}

}
