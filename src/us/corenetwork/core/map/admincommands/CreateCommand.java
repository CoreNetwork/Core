package us.corenetwork.core.map.admincommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;
import us.corenetwork.core.map.MapModule;
import us.corenetwork.core.map.CheckpointsSettings;

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
		
		List<String> stringList = MapModule.instance.config.getStringList(node);
		if (stringList == null)
			stringList = new ArrayList<String>();
		
		int position = stringList.size() + 1;
		
		Player player = (Player) sender;
		
		stringList.add(Util.serializeLocation(player.getLocation()));
		MapModule.instance.config.set(node, stringList);
		
		String message = CheckpointsSettings.MESSAGE_CHECKPOINT_CREATED.string();
		message = message.replace("<Position>", Integer.toString(position));
		message = message.replace("<List>", checkpointList);
		
		MapModule.instance.saveConfig();
		
		PlayerUtils.Message(message, sender);
		return;
	}	
}
