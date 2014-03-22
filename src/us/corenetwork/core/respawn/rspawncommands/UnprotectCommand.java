package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.respawn.ProtectTimer;
import us.corenetwork.core.respawn.RespawnSettings;

public class UnprotectCommand extends BaseRSpawnCommand {	

	public UnprotectCommand()
	{
		needPlayer = true;
		permission = "unprotect";
	}

	public Boolean run(final CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		boolean silent = args.length > 0 && args[0].equals("silent");
		
		boolean hasProtection = ProtectTimer.protectedPlayers.containsKey(player.getName());
		if (!hasProtection)
		{
			if (!silent) PlayerUtils.Message(RespawnSettings.MESSAGE_SPAWN_UNPROTECT_NOT_PROTECTED.string(), sender);
			return true;
		}
		
		ProtectTimer.protectedPlayers.remove(player.getName());
		if (!silent) ProtectTimer.endProtectionMessage(player);
		return true;
	}	
	
}
