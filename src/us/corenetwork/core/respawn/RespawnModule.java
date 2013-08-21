package us.corenetwork.core.respawn;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.Util;
import us.corenetwork.core.respawn.rspawncommands.BaseRSpawnCommand;
import us.corenetwork.core.respawn.rspawncommands.NoDropCommand;
import us.corenetwork.core.respawn.rspawncommands.ProtectCommand;
import us.corenetwork.core.respawn.rspawncommands.RSpawnCommand;
import us.corenetwork.core.respawn.rspawncommands.ToggleCommand;
import us.corenetwork.core.respawn.rspawncommands.UnprotectCommand;

public class RespawnModule extends CoreModule {
	public static RespawnModule instance;

	public static HashMap<String, BaseRSpawnCommand> rspawnCommands = new HashMap<String, BaseRSpawnCommand>();
	
	public RespawnModule() {
		super("Random respawn", new String[] { "rspawn", "togglespawn", "unprotect" }, "respawn");
		
		instance = this;
	}

	@Override
	protected boolean loadModule() {

		for (RespawnSettings setting : RespawnSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();
				
		Bukkit.getServer().getPluginManager().registerEvents(new RespawnListener(), CorePlugin.instance);
		
		rspawnCommands.put("rspawn", new RSpawnCommand());
		rspawnCommands.put("toggle", new ToggleCommand());
		rspawnCommands.put("protect", new ProtectCommand());
		rspawnCommands.put("unprotect", new UnprotectCommand());
		rspawnCommands.put("nodrop", new NoDropCommand());

		Bukkit.getServer().getScheduler().runTaskTimer(CorePlugin.instance, new ProtectTimer(), 20, 20);
		
		return true;
	}
	@Override
	protected void unloadModule() {
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equals("togglespawn"))
		{
			return rspawnCommands.get("toggle").execute(sender, args);
		}
		else if (command.getName().equals("unprotect"))
		{
			return rspawnCommands.get("unprotect").execute(sender, args);
		}
		else
		{
			if (args.length < 1 || Util.isInteger(args[0]))
				return rspawnCommands.get("rspawn").execute(sender, args);

			BaseRSpawnCommand cmd = rspawnCommands.get(args[0]);
			if (cmd != null)
				return cmd.execute(sender, args);
			else
				return false;
		}
	}

}
