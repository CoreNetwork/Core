package us.corenetwork.core.teleport.commands.warp;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.teleport.TeleportModule;
import us.corenetwork.core.teleport.TeleportSettings;

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
				
		TeleportModule.instance.storageConfig.set("Warps." + name + ".location", locationString);
		TeleportModule.instance.storageConfig.set("Warps." + name + ".RunCommands", new ArrayList<String>());
		
		String message = TeleportSettings.MESSAGE_WARP_SET.string();
		message = message.replace("<Name>", name);
		
		TeleportModule.instance.saveStorageYaml();
		
		PlayerUtils.Message(message, sender);
		return;
	}	
}
