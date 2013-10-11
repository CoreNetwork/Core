package us.corenetwork.core.checkpoints.admincommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.checkpoints.CheckpointsModule;
import us.corenetwork.core.checkpoints.CheckpointsSettings;

public class CreateCommand extends BaseCheckpointCommand {	
	public CreateCommand()
	{
		desc = "Create checkpoint";
		permission = "create";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		if (args.length < 1)
		{
			PlayerUtils.Message("Usage: /chp create <list name>", sender);
			return;
		}
		
		String checkpointList = args[0];
		String node = "checkpoints."  + checkpointList.toLowerCase();
		
		List<String> stringList = CheckpointsModule.instance.config.getStringList(node);
		if (stringList == null)
			stringList = new ArrayList<String>();
		
		int position = stringList.size() + 1;
		
		Player player = (Player) sender;
		
		stringList.add(Util.serializeLocation(player.getLocation()));
		CheckpointsModule.instance.config.set(node, stringList);
		
		String message = CheckpointsSettings.MESSAGE_CHECKPOINT_CREATED.string();
		message = message.replace("<Position>", Integer.toString(position));
		message = message.replace("<List>", checkpointList);
		
		CheckpointsModule.instance.saveConfig();
		
		PlayerUtils.Message(message, sender);
		return;
	}	
}
