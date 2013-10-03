package us.corenetwork.core.warps.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CLog;
import us.corenetwork.core.Util;
import us.corenetwork.core.corecommands.SudoCommand;
import us.corenetwork.core.warps.WarpsModule;
import us.corenetwork.core.warps.WarpsSettings;

public class TpCommand extends BaseWarpCommand {	
	public TpCommand()
	{
		desc = "Teleport";
		permission = "tp";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		if (args.length < 1)
		{
			Util.Message("Usage: /warp tp <name>", sender);
			return;
		}
		
		Player player = (Player) sender;
		
		String name = args[0].toLowerCase();
		
		String locationString = (String) WarpsModule.instance.config.get("Warps." + name);
		
		if (locationString == null)
		{
			String message = WarpsSettings.MESSAGE_UNKNOWN_WARP.string();
			message = message.replace("<Name>", name);
			Util.Message(message, sender);
			
			return;
		}
				
		Location location = Util.unserializeLocation(locationString);
		
		Util.safeTeleport(player, location);
		
		if (!SudoCommand.isUnderSudo(player.getName()))
		{
			String message = WarpsSettings.MESSAGE_TP_NOTICE.string();
			message = message.replace("<Player>", player.getName());
			message = message.replace("<Warp>", name);
			
			Util.MessagePermissions(message, "core.warps.notification");
			CLog.info(message);
		}
	}	
}
