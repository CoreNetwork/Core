package us.corenetwork.core.map.usercommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.map.CheckpointsSettings;
import us.corenetwork.core.map.MapModule;
import us.corenetwork.core.map.SavedCheckpoint;
import us.corenetwork.core.map.ScheduledTeleport;

public class TeleInstantCommand extends BaseCheckpointUserCommand {
	
	public TeleInstantCommand()
	{
		permission = "teleinstant";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {		
		final Player player = (Player) sender;
		SavedCheckpoint lastCheckpoint = MapModule.savedCheckpoints.get(player.getName());
		if (lastCheckpoint == null)
		{
			return;
		}
		
		String message = CheckpointsSettings.MESSAGE_TELEPORT_INSTANT.string();
		message = message.replace("<Player>", player.getName()).replace("<Position>", lastCheckpoint.position+"");
		PlayerUtils.Message(message, sender);
		player.teleport(lastCheckpoint.location);
		
		return;
	}	

}
