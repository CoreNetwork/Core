package us.corenetwork.core.calculator;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.calculator.commands.BaseCalcCommand;
import us.corenetwork.core.calculator.commands.CalcCommand;

public class CalculatorModule extends CoreModule {

	public static CalculatorModule instance;
	
	public static HashMap<String, BaseCalcCommand> commands;
	
	public CalculatorModule()
	{
		super("Calculator", new String[] {"calc"}, "calculator");
		
		instance = this;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if (command.getName().equals("calc"))
		{
			return commands.get("calc").execute(sender, args, false);
		}
		else
		{
			BaseCalcCommand cmd = commands.get(args[0]);
			if (cmd != null)
				return cmd.execute(sender, args, false);
			else
				return false;
		}
	}
	@Override
	protected boolean loadModule()
	{
		for (CalculatorSettings setting : CalculatorSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();
		
		commands = new HashMap<String, BaseCalcCommand>();
		
		commands.put("calc", new CalcCommand());
		
		return true;
	}
	@Override
	protected void unloadModule()
	{
	}
	
	
}
