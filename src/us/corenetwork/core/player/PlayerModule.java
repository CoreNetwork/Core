package us.corenetwork.core.player;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.corecommands.BaseCoreCommand;
import us.corenetwork.core.player.commands.ClearCommand;

public class PlayerModule extends CoreModule {

	public static PlayerModule instance;
	
	public static HashMap<String, BaseCoreCommand> commands;
	
	public PlayerModule()
	{
		super("Player", new String[] {"clear"}, "player");
		
		instance = this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if (command.getName().equals("clear"))
		{
			return commands.get("clear").execute(sender, args, false);
		}
		else
		{
			BaseCoreCommand cmd = commands.get(args[0]);
			if (cmd != null)
				return cmd.execute(sender, args, false);
			else
				return false;
		}
	}
	
	@Override
	protected boolean loadModule()
	{
		for (PlayerSettings setting : PlayerSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();
		
		commands = new HashMap<String, BaseCoreCommand>();
		
		commands.put("clear", new ClearCommand());

		Bukkit.getServer().getPluginManager().registerEvents(new VoidListener(), CorePlugin.instance);
		return true;
	}
	
	@Override
	protected void unloadModule()
	{
	}
}
