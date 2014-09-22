package us.corenetwork.core.respawn;

import java.util.ArrayList;
import java.util.List;

import us.corenetwork.core.CorePlugin;

public class RespawnCountdown {

	private boolean running;
	private int length;
	private int currentTime;
	
	private List<Integer> countdownTasks;
	
	CountdownDisplay display;
	CommandCountdownDisplay commDisplay;
	
	public RespawnCountdown()
	{
		running = false;
		length = RespawnSettings.GROUP_RESPAWN_TIME_LIMIT.integer();
		
		countdownTasks = new ArrayList<Integer>();
		
		display = new CountdownDisplay(this);
		commDisplay = new CommandCountdownDisplay();
	}
	
	/**
	 * Stards the countdown. Only one countdown can be active at the same time.
	 */
	public void start()
	{
		if(running == false)
		{
			running = true;
			
			for(int i = length; i>=0; i--)
			{
				int taskId = CorePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(CorePlugin.instance, new CountdownRunnable(this, i), (length - i)*20);
				countdownTasks.add(taskId);
			}
		}
		else
		{
			stop();
			start();
		}
	}
	
	public void stop()
	{
		for(Integer taskId : countdownTasks)
		{
			CorePlugin.instance.getServer().getScheduler().cancelTask(taskId);
		}
		
		clear();
	}
	
	public void finish()
	{
		clear();
		RespawnModule.teamManager.clear();
	}
	
	private void clear()
	{
		running = false;
		countdownTasks.clear();
		display.clear();
		commDisplay.clear();
	}
	
	public boolean isRunning()
	{
		return running;
	}

	public int getTime()
	{	
		return currentTime;
	}
	
	public void setTime(int value)
	{
		currentTime = value;
		if(running)
		{
			display.display();
			commDisplay.set(value);
			if(value == 0)
			{
				finish();
			}
		}
	}
}
