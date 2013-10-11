package us.corenetwork.core.teleport.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.teleport.WarpsModule;
import us.corenetwork.core.teleport.WarpsSettings;

public class SetCommand extends BaseWarpCommand {	
	public SetCommand()
	{
		desc = "Set warp here";
		permission = "set";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		if (args.length < 1)
		{
			PlayerUtils.Message("Usage: /warp set <name>", sender);
			return;
		}
		
		String name = args[0].toLowerCase();
		
		Location location = ((Player) sender).getLocation();
		String locationString = Util.serializeLocation(location);
				
		WarpsModule.instance.config.set("Warps." + name, locationString);
		
		String message = WarpsSettings.MESSAGE_WARP_SET.string();
		message = message.replace("<Name>", name);
		
		WarpsModule.instance.saveConfig();
		
		PlayerUtils.Message(message, sender);
		return;
	}	
}
