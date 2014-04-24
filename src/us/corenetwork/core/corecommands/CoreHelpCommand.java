package us.corenetwork.core.corecommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.AbstractCoreCommand;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Settings;
import us.corenetwork.core.Util;

public class CoreHelpCommand extends BaseCoreCommand {
	
	public CoreHelpCommand()
	{
		desc = "List all possible commands";
		needPlayer = false;
		permission = "help";
	}


	public void run(CommandSender sender, String[] args) {		
		int page = 1;
		if (args.length > 0 && Util.isInteger(args[0])) page = Integer.parseInt(args[0]);
		List<String> komandes = new ArrayList<String>();

		for (Entry<String, AbstractCoreCommand> e : CorePlugin.coreCommands.entrySet())
		{
			komandes.add(Settings.getCommandDescription(e.getKey(), "core", e.getValue().desc));
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
