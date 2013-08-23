package us.corenetwork.core.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;

public class ScoreboardModule extends CoreModule {
	public static ScoreboardModule instance;
	
	public ScoreboardModule() {
		super("Scoreboard manager", null, "scoreboard");
		
		instance = this;
	}

	@Override
	protected boolean loadModule() {
				
		Bukkit.getServer().getPluginManager().registerEvents(new ScoreboardListener(), CorePlugin.instance);
		
		return true;
	}
	
	@Override
	protected void unloadModule() {
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		return false;
	}
}
