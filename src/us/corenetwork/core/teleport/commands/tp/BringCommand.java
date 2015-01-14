package us.corenetwork.core.teleport.commands.tp;

import org.bukkit.command.CommandSender;

import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;
import us.corenetwork.core.teleport.OfflineTeleportation;

public class BringCommand extends BaseTpCommand {		
	public BringCommand()
	{
		desc = "Bring someone to you";
		permission = "bring";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		int argsSize = args.length;
		boolean silent = false;
		
		if (argsSize > 0 && args[argsSize - 1].equalsIgnoreCase("silent"))
		{
			argsSize--;
			
			if (Util.hasPermission(sender, "core.teleport.silent"))
				silent = true;
		}
		
		if (argsSize == 1)
		{
			// "/bring confirm"
			if (args[0].equals("confirm"))
			{
				OfflineTeleportation.confirmTeleport(sender);
				return;
			}
			
			// "/bring name"
			TpCommand.teleportTo("bring", sender, args[0], sender, silent);
			return;
		}
		
		PlayerUtils.Message("Usage: /bring [player]", sender);
	}		
}
