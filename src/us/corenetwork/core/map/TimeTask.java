package us.corenetwork.core.map;

import java.util.Set;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import us.corenetwork.core.CorePlugin;

public class TimeTask implements Runnable {

	private final String OBJECTIVE_PREFIX = "time";
	private final String OBJECTIVE_CRITERIA = "time_crit";
	private Scoreboard scoreboard = null;
	private String lastObjective = null;
	private String newObjective = null;
	
	public TimeTask()
	{
		scoreboard = CorePlugin.instance.getServer().getScoreboardManager().getMainScoreboard();
		for(Objective obj : scoreboard.getObjectives())
		{
			obj.unregister();
		}
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
		scoreboard.registerNewObjective(newObjective, OBJECTIVE_CRITERIA);
		lastObjective = newObjective;
	}

}
