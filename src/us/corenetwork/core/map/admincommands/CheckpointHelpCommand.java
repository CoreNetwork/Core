package us.corenetwork.core.map.admincommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;
import us.corenetwork.core.map.MapModule;
import us.corenetwork.core.map.CheckpointsSettings;

public class CheckpointHelpCommand extends BaseCheckpointCommand {
	
	public CheckpointHelpCommand()
	{
		permission = "help";
		desc = "List all possible commands";
		needPlayer = false;
	}


	public void run(CommandSender sender, String[] args) {		
		int page = 1;
		if (args.length > 0 && Util.isInteger(args[0])) page = Integer.parseInt(args[0]);
		List<String> komandes = new ArrayList<String>();

		for (Entry<String, BaseCheckpointCommand> e : MapModule.admincommands.entrySet())
		{
			komandes.add(CheckpointsSettings.getCommandDescription(e.getKey(),  e.getValue().desc));
		}  		
		String[] komande = komandes.toArray(new String[0]);
		Arrays.sort(komande);
		
		int maxpage = (int) Math.ceil((double) komande.length / (sender instanceof Player ? 15.0 : 30.0));
		
		if (page > maxpage)
			page = maxpage;
		
		PlayerUtils.Message("List of all commands:", sender);
		PlayerUtils.Message("&8Page " + String.valueOf(page) + " of " + String.valueOf(maxpage), sender);

		for (int i = (page - 1) * 15; i < page * 15; i++)
		{
			if (komande.length < i + 1 || i < 0) break;	
			PlayerUtils.Message(komande[i], sender);
		}   		
	}
	

}
