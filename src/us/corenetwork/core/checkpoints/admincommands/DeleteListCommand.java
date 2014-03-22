package us.corenetwork.core.checkpoints.admincommands;

import java.util.List;

import org.bukkit.command.CommandSender;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.checkpoints.CheckpointsModule;
import us.corenetwork.core.checkpoints.CheckpointsSettings;

public class DeleteListCommand extends BaseCheckpointCommand {	
	public DeleteListCommand()
	{
		desc = "Delete list of checkpoints";
		permission = "deleteist";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		if (args.length < 1)
		{
			PlayerUtils.Message("Usage: /chp deleteList <list name>", sender);
			return;
		}
		
		String checkpointList = args[0];
		String node = "checkpoints."  + checkpointList.toLowerCase();
		
		List<String> stringList = CheckpointsModule.instance.config.getStringList(node);
		if (stringList == null || stringList.size() == 0)
		{
			String message = CheckpointsSettings.MESSAGE_LIST_NOT_EXIST.string();
			message = message.replace("<List>", checkpointList);
			
			PlayerUtils.Message(message, sender);
			return;
		}
		
		CheckpointsModule.instance.config.set(node, null);
		
		String message = CheckpointsSettings.MESSAGE_LIST_DELETED.string();
		message = message.replace("<List>", checkpointList);
		
		CheckpointsModule.instance.saveConfig();
		
		PlayerUtils.Message(message, sender);
		return;
	}	
}
