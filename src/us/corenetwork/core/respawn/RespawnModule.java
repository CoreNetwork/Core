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
import us.corenetwork.core.respawn.rspawncommands.RSpawnTeamCommand;
import us.corenetwork.core.respawn.rspawncommands.ToggleCommand;
import us.corenetwork.core.respawn.rspawncommands.UnprotectCommand;

public class RespawnModule extends CoreModule {
	public static RespawnModule instance;
	public static RespawnTeamManager teamManager;
	public static RespawnManager manager;
	public static HashMap<String, BaseRSpawnCommand> rspawnCommands = new HashMap<String, BaseRSpawnCommand>();
	
	public RespawnModule() {
		super("Random respawn", new String[] { "rspawn", "togglespawn", "unprotect", "rspawnteam"}, "respawn");
		
		instance = this;
	}

	@Override
	protected boolean loadModule() {

		teamManager = new RespawnTeamManager();
		manager = new RespawnManager();
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
		rspawnCommands.put("rspawnteam", new RSpawnTeamCommand());

		Bukkit.getServer().getScheduler().runTaskTimer(CorePlugin.instance, new ProtectTimer(), 20, 20);
		
		KnownPlayers.load();
		
		return true;
	}
	@Override
	protected void unloadModule() {
		KnownPlayers.save();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equals("togglespawn"))
		{
			return rspawnCommands.get("toggle").execute(sender, args, true);
		}
		else if (command.getName().equals("unprotect"))
		{
			return rspawnCommands.get("unprotect").execute(sender, args, true);
		}
		else if (command.getName().equals("rspawnteam"))
		{
			return rspawnCommands.get("rspawnteam").execute(sender, args, false);
		}
		else
		{
			if (args.length < 1 || Util.isInteger(args[0]))
				return rspawnCommands.get("rspawn").execute(sender, args, true);

			BaseRSpawnCommand cmd = rspawnCommands.get(args[0]);
			if (cmd != null)
				return cmd.execute(sender, args, true);
			else
				return false;
		}
	}

}
