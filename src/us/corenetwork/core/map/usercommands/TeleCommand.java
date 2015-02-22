package us.corenetwork.core.map.usercommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.map.MapModule;
import us.corenetwork.core.map.CheckpointsSettings;
import us.corenetwork.core.map.SavedCheckpoint;
import us.corenetwork.core.map.ScheduledTeleport;

public class TeleCommand extends BaseCheckpointUserCommand {	
	public TeleCommand()
	{
		permission = "tele";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {		
		final Player player = (Player) sender;
		SavedCheckpoint lastCheckpoint = MapModule.savedCheckpoints.get(player.getName());
		if (lastCheckpoint == null)
		{
			String message = CheckpointsSettings.MESSAGE_NOTHING_SAVED.string();
			message = message.replace("<Player>", player.getName());
			
			PlayerUtils.Message(message, sender);
			return;
		}
		
		int delay = CheckpointsSettings.TELEPORT_DELAY.integer();
		
		String message = CheckpointsSettings.MESSAGE_TELEPORT_SCHEDULED.string();
		message = message.replace("<Player>", player.getName());
		
		PlayerUtils.Message(message, sender);

		final ScheduledTeleport schedule = new ScheduledTeleport();
		schedule.player = player;
		schedule.location = lastCheckpoint.location;
		schedule.time = System.currentTimeMillis() + delay * 1000;
		MapModule.scheduledTeleports.put(player.getName(), schedule);

		return;
	}	
}
