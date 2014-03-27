package us.corenetwork.core.player.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.player.PlayerModule;
import us.corenetwork.core.player.PlayerSettings;

public class GodCommand extends BasePlayerCommand {

	public GodCommand()
	{
		desc = "Prevent any damage to the player"; 
		permission = "god";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		if (args.length == 0 || args.length > 2)
		{
			PlayerUtils.Message("Usage : /god <player> [silent]", sender);
			return;
		}

		
		Player player = CorePlugin.instance.getServer().getPlayerExact(args[0]);
		boolean silent = false;
		
		if(player == null)
		{
			PlayerUtils.Message("Cannot find " + args[0] + " player." , sender);
			return;
		}
		
		if (args.length == 2)
		{
			if (args[1].toLowerCase().equals("silent"))
			{
				silent = true;
			}
			else
			{
				PlayerUtils.Message("Usage : /god <player> [silent]", sender);
				return;	
			}
		}
		
		PlayerModule.gods.add(player.getName());
		
		if(silent == false)
		{
			PlayerUtils.Message(PlayerSettings.MESSAGE_GOD_APPLIED.string(), sender);
			PlayerUtils.Message(PlayerSettings.MESSAGE_GOT_GOD.string(), player);
		}
	}

}
