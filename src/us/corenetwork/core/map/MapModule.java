package us.corenetwork.core.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.map.admincommands.BaseCheckpointCommand;
import us.corenetwork.core.map.admincommands.CheckpointHelpCommand;
import us.corenetwork.core.map.admincommands.CreateCommand;
import us.corenetwork.core.map.admincommands.DeleteListCommand;
import us.corenetwork.core.map.admincommands.MoveCommand;
import us.corenetwork.core.map.usercommands.BaseCheckpointUserCommand;
import us.corenetwork.core.map.usercommands.ClearCommand;
import us.corenetwork.core.map.usercommands.SaveCommand;
import us.corenetwork.core.map.usercommands.TeleCommand;
import us.corenetwork.core.map.usercommands.TeleInstantCommand;

public class MapModule extends CoreModule {
	public static MapModule instance;
	
	public static HashMap<String, BaseCheckpointCommand> admincommands;
	public static HashMap<String, BaseCheckpointUserCommand> usercommands;

	public static HashMap<String, SavedCheckpoint> savedCheckpoints;
	public static HashMap<String, ScheduledTeleport> scheduledTeleports;

	public MapModule() {
		super("Map", new String[] {"chp", "checkpoint"}, "map");
		
		instance = this;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

		if (command.getName().equals("checkpoint"))
		{
			BaseCheckpointUserCommand baseCommand = null;
			if (args.length > 0) 
				baseCommand = usercommands.get(args[0]);
			
			if (baseCommand != null)
				return baseCommand.execute(sender, args, true);
			
			usercommands.get("tele").execute(sender, args, true);
		}
		else
		{
			
			BaseCheckpointCommand baseCommand = null;
			if (args.length > 0) 
				baseCommand = admincommands.get(args[0]);
			
			if (baseCommand != null)
				return baseCommand.execute(sender, args, true);
			
			admincommands.get("help").execute(sender, args, true);
		}
		
		return false;
	}

	@Override
	protected boolean loadModule() {

		for (CheckpointsSettings setting : CheckpointsSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();
		
		admincommands = new HashMap<String, BaseCheckpointCommand>();
		admincommands.put("create", new CreateCommand());
		admincommands.put("help", new CheckpointHelpCommand());
		admincommands.put("deletelist", new DeleteListCommand());
		admincommands.put("move", new MoveCommand());

		usercommands = new HashMap<String, BaseCheckpointUserCommand>();
		usercommands.put("save", new SaveCommand());
		usercommands.put("tele", new TeleCommand());
		usercommands.put("instant", new TeleInstantCommand());
		usercommands.put("clear", new ClearCommand());

		savedCheckpoints = new HashMap<String, SavedCheckpoint>();
		scheduledTeleports = new HashMap<String, ScheduledTeleport>();
		
		Bukkit.getScheduler().runTaskTimer(CorePlugin.instance, new Teleporter(), 20, 20);

		Bukkit.getScheduler().runTaskTimer(CorePlugin.instance, new TimeTask(), 20, 400);
		
		Bukkit.getScheduler().runTaskTimer(CorePlugin.instance, new ClearOrbTask(), 20, CheckpointsSettings.ORB_CLEARING_INTERVAL.integer() * 20);
		
		Bukkit.getServer().getPluginManager().registerEvents(new CheckpointsListener(), CorePlugin.instance);

        ClickRegionListener clickRegionListener = new ClickRegionListener();
        clickRegionListener.load(config);
        Bukkit.getServer().getPluginManager().registerEvents(clickRegionListener, CorePlugin.instance);
		
		return true;
	}

	@Override
	protected void unloadModule() {
	}

	private static class Teleporter implements Runnable
	{
		@Override
		public void run() {
			if (scheduledTeleports.size() == 0)
				return;
			
			Iterator<Entry<String, ScheduledTeleport>> i = scheduledTeleports.entrySet().iterator();
			
			while (i.hasNext())
			{
				ScheduledTeleport teleport = i.next().getValue();
				if (System.currentTimeMillis() >= teleport.time)
				{
					teleport.player.teleport(teleport.location);
					i.remove();
				}
			}
		}
		
	}
}
