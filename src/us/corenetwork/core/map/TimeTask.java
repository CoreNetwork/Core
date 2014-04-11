package us.corenetwork.core.map;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import us.corenetwork.core.CorePlugin;

public class TimeTask implements Runnable {

	private final String OBJECTIVE_PREFIX = "time";
	private Scoreboard scoreboard = null;
	private String lastObjective = null;
	private String newObjective = null;
	
	public TimeTask()
	{
		scoreboard = CorePlugin.instance.getServer().getScoreboardManager().getMainScoreboard();
	}
	
	@Override
	public void run() 
	{
		long time = CorePlugin.instance.getServer().getWorld("world").getTime();
		double hour = time / 1000.0;

		if(lastObjective != null)
		{
			scoreboard.getObjective(lastObjective).unregister();
		}
		newObjective = OBJECTIVE_PREFIX + Math.round(hour); 
		scoreboard.registerNewObjective(newObjective, "dummy");
		lastObjective = newObjective;
	}

}
