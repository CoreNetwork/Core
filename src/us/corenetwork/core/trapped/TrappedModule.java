package us.corenetwork.core.trapped;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.trapped.commands.BaseTrappedCommand;
import us.corenetwork.core.trapped.commands.TrappedCommand;

public class TrappedModule extends CoreModule {

	public static TrappedModule instance;
	
	public static HashMap<String, BaseTrappedCommand> commands;
	
	public TrappedModule()
	{
		super("Trapped", new String[] {"trapped"}, "trapped");
		
		instance = this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		//Trapped moved to /core trapped, nothing happens here
		return false;
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
		
		commands = new HashMap<String, BaseTrappedCommand>();
		
		CorePlugin.coreCommands.put("trapped", new TrappedCommand());
		
		return true;
	}
	
	@Override
	protected void unloadModule()
	{
	}
	
}
