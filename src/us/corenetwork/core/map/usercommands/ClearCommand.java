package us.corenetwork.core.map.usercommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.map.MapModule;

public class ClearCommand extends BaseCheckpointUserCommand {	
	public ClearCommand()
	{
		permission = "clear";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {		
		final Player player = (Player) sender;
		MapModule.savedCheckpoints.remove(player.getName());
		MapModule.scheduledTeleports.remove(player.getName());
		return;
	}	
}
