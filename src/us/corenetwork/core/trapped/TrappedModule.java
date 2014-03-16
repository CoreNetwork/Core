package us.corenetwork.core.trapped;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.corecommands.BaseCoreCommand;
import us.corenetwork.core.trapped.commands.TrappedCommand;

public class TrappedModule extends CoreModule {

	public static TrappedModule instance;
	
	public static HashMap<String, BaseCoreCommand> commands;
	
	public TrappedModule()
	{
		super("Trapped", new String[] {"trapped"}, "trapped");
		
		instance = this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if (command.getName().equals("trapped"))
		{
			return commands.get("trapped").execute(sender, args, false);
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
		for (TrappedSettings setting : TrappedSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();
		
		commands = new HashMap<String, BaseCoreCommand>();
		
		commands.put("trapped", new TrappedCommand());
		
		Bukkit.getServer().getPluginManager().registerEvents(new TrappedListener(), CorePlugin.instance);
		
		return true;
	}
	
	@Override
	protected void unloadModule()
	{
	}
	
	
}
