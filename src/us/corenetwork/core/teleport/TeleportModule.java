package us.corenetwork.core.teleport;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import us.corenetwork.core.AbstractCoreCommand;
import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.RedirectedVanillaCommand;
import us.corenetwork.core.teleport.commands.tp.TpCommand;
import us.corenetwork.core.teleport.commands.warp.DeleteCommand;
import us.corenetwork.core.teleport.commands.warp.SetCommand;
import us.corenetwork.core.teleport.commands.warp.WarpCommand;
import us.corenetwork.core.teleport.commands.warp.WarpsHelpCommand;

public class TeleportModule extends CoreModule {
	public static TeleportModule instance;

	public static HashMap<String, AbstractCoreCommand> commands;

	public TeleportModule() {
		super("Teleportation", new String[] {"warp", "bring", "swap"}, "teleportation");

		instance = this;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equals("tp"))
		{
			return commands.get("tp").execute(sender, args, false);
		}
		else if (command.getName().equals("bring"))
		{
			return TpCommand.subCommands.get("bring").execute(sender, args, false);
		}
		else if (command.getName().equals("swap"))
		{
			return TpCommand.subCommands.get("swap").execute(sender, args, false);
		}
		
		AbstractCoreCommand baseCommand = null;
		if (args.length > 0) 
			baseCommand = commands.get(args[0]);

		if (baseCommand != null)
			return baseCommand.execute(sender, args, true);

		commands.get("warp").execute(sender, args, false);

		return true;
	}



	@Override
	protected boolean loadModule() {

		for (TeleportSettings setting : TeleportSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();

		commands = new HashMap<String, AbstractCoreCommand>();
		
		commands.put("help", new WarpsHelpCommand());
		commands.put("set", new SetCommand());
		commands.put("warp", new WarpCommand());
		commands.put("delete", new DeleteCommand());
		commands.put("tp", new TpCommand());
		
		CorePlugin.coreCommands.put("tp", new TpCommand());

		Bukkit.getServer().getPluginManager().registerEvents(new TeleportListener(), CorePlugin.instance);

		return true;
	}

	@Override
	protected void unloadModule() {
	}

	@Override
	public void loadConfig()
	{
		super.loadConfig();
		loadStorageYaml();
	}
	
}
