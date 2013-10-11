package us.corenetwork.core.teleport.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.Util;
import us.corenetwork.core.teleport.WarpsModule;
import us.corenetwork.core.teleport.WarpsSettings;

public class DeleteCommand extends BaseWarpCommand {	
	public DeleteCommand()
	{
		desc = "Delete warp";
		permission = "delete";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		if (args.length < 1)
		{
			Util.Message("Usage: /warp delete <name>", sender);
			return;
		}
		
		String name = args[0].toLowerCase();
		
		String existing = (String) WarpsModule.instance.config.get("Warps." + name);
		if (existing == null)
		{
			String message = WarpsSettings.MESSAGE_UNKNOWN_WARP.string();
			message = message.replace("<Name>", name);
			Util.Message(message, sender);
			
			return;
		}
				
		WarpsModule.instance.config.set("Warps." + name, null);
		
		String message = WarpsSettings.MESSAGE_WARP_DELETED.string();
		message = message.replace("<Name>", name);
		
		WarpsModule.instance.saveConfig();
		
		Util.Message(message, sender);
		return;
	}	
}
