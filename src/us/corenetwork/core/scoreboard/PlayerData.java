package us.corenetwork.core.scoreboard;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerData {
	private int curScoreboard;
	protected HashMap<Integer, Scoreboard> scoreboards;
	
	private Scoreboard playerStatsScoreboard;
	private Objective playerStatsObjective;
	
	public PlayerData()
	{		
		curScoreboard = -1;
		scoreboards = new HashMap<Integer, Scoreboard>();		
	}
	
	public void setStat(String name, Integer value)
	{
		if (value == null)
			playerStatsScoreboard.resetScores(name);
		else
		{
			Score score = playerStatsObjective.getScore(name);
			if (value == 0)
				score.setScore(1);
			score.setScore(value);
		}
	}	
	
	protected void init(Player player)
	{
		playerStatsScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		playerStatsObjective = playerStatsScoreboard.registerNewObjective(ScoreboardSettings.STATS_PANEL_HEADER.string(), "dummy");
		playerStatsObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		registerScoreboard(player, 0, playerStatsScoreboard);
	}
	
	public boolean hasScoreboard(int priority)
	{
		return scoreboards.containsKey(priority);
	}
	
	private void setHighestScoreboard(Player player)
	{
		Scoreboard winner = null;
		int bestPriority = Integer.MIN_VALUE;
		
		for (Entry<Integer, Scoreboard> e : scoreboards.entrySet())
		{
			if (e.getKey() > bestPriority)
			{
				bestPriority = e.getKey();
				winner = e.getValue();
			}
		}
		
		if (winner == null)
			return;
		
		curScoreboard = bestPriority;
		player.setScoreboard(winner);
	}
	
	public void registerScoreboard(Player player, int priority, Scoreboard scoreboard)
	{
		scoreboards.put(priority, scoreboard);
		
		if (curScoreboard <= priority)
		{
			curScoreboard = priority;
			player.setScoreboard(scoreboard);
		}
	}
	
	public void unregisterScoreboard(Player player, int priority)
	{		
		scoreboards.remove(priority);
		
		if (curScoreboard == priority)
		{
			setHighestScoreboard(player);
			return;
		}
	}

}
