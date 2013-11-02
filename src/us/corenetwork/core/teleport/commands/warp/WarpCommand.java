package us.corenetwork.core.teleport.commands.warp;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.corecommands.SudoCommand;
import us.corenetwork.core.teleport.TeleportModule;
import us.corenetwork.core.teleport.TeleportSettings;
import us.corenetwork.core.teleport.TeleportUtil;

public class WarpCommand extends BaseWarpCommand {	
	public WarpCommand()
	{
		desc = "Teleport to warp";
		permission = "warp";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		if (args.length < 1)
		{
			PlayerUtils.Message("Usage: /warp warp <name>", sender);
			return;
		}
		
		Player player = (Player) sender;
		
		String name = args[0].toLowerCase();
		
		Location location = getWarpLocation(args[0]);
		
		if (location == null)
		{
			String message = TeleportSettings.MESSAGE_UNKNOWN_WARP.string();
			message = message.replace("<Name>", name);
			PlayerUtils.Message(message, sender);
			
			return;
		}
						
		PlayerUtils.safeTeleport(player, location);
		
		if (!SudoCommand.isUnderSudo(player.getName()))
		{
			String message = TeleportSettings.MESSAGE_TP_NOTICE.string();
			message = message.replace("<Player>", player.getName());
			message = message.replace("<Warp>", name);
			
			TeleportUtil.notifyModerators(sender, message);
		}
	}	
	
	public static Location getWarpLocation(String name)
	{
		String locationString = (String) TeleportModule.instance.storageConfig.get("Warps." + name);
		if (locationString == null)
			return null;
		
		return Util.unserializeLocation(locationString);
	}
}
