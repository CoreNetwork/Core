package us.corenetwork.core.warps;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.warps.commands.BaseWarpCommand;
import us.corenetwork.core.warps.commands.DeleteCommand;
import us.corenetwork.core.warps.commands.SetCommand;
import us.corenetwork.core.warps.commands.TpCommand;
import us.corenetwork.core.warps.commands.WarpsHelpCommand;

public class WarpsModule extends CoreModule {
	public static WarpsModule instance;

	public static HashMap<String, BaseWarpCommand> commands;

	public WarpsModule() {
		super("Warps", new String[] {"warp"}, "warps");
		
		instance = this;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {


		BaseWarpCommand baseCommand = null;
		if (args.length > 0) 
			baseCommand = commands.get(args[0]);

		if (baseCommand != null)
			return baseCommand.execute(sender, args, true);

		commands.get("tp").execute(sender, args, false);

		return true;
	}



	@Override
	protected boolean loadModule() {

		for (WarpsSettings setting : WarpsSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();

		commands = new HashMap<String, BaseWarpCommand>();
		
		commands.put("help", new WarpsHelpCommand());
		commands.put("set", new SetCommand());
		commands.put("tp", new TpCommand());
		commands.put("delete", new DeleteCommand());

		Bukkit.getServer().getPluginManager().registerEvents(new WarpsListener(), CorePlugin.instance);

		return true;
	}

	@Override
	protected void unloadModule() {
	}


}
