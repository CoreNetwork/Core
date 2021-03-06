package us.corenetwork.core.teleport.commands.warp;

import org.bukkit.command.CommandSender;

import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.teleport.TeleportModule;
import us.corenetwork.core.teleport.TeleportSettings;

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
			PlayerUtils.Message("Usage: /warp delete <name>", sender);
			return;
		}
		
		String name = args[0].toLowerCase();
		
		String existing = (String) TeleportModule.instance.storageConfig.get("Warps." + name + ".location");
		if (existing == null)
		{
			String message = TeleportSettings.MESSAGE_UNKNOWN_WARP.string();
			message = message.replace("<Name>", name);
			PlayerUtils.Message(message, sender);
			
			return;
		}
				
		TeleportModule.instance.storageConfig.set("Warps." + name, null);
		
		String message = TeleportSettings.MESSAGE_WARP_DELETED.string();
		message = message.replace("<Name>", name);
		
		TeleportModule.instance.saveStorageYaml();
		
		PlayerUtils.Message(message, sender);
		return;
	}	
}
