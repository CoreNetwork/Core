package us.corenetwork.core.scoreboard;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class CoreScoreboardManager {
	protected static HashMap<String, PlayerData> scoreboards = new HashMap<String, PlayerData>();

	public static void registerScoreboard(Player player, int priority, Scoreboard scoreboard)
	{
		PlayerData playerData = getPlayerData(player.getName());
		playerData.registerScoreboard(player, priority, scoreboard);
	}
	
	public static void unregisterScoreboard(Player player, int priority)
	{
		PlayerData playerData = getPlayerData(player.getName());
		playerData.unregisterScoreboard(player, priority);
	}
	
	public static void setPlayerStat(String player, String stat, Integer value)
	{
		getPlayerData(player).setStat(stat, value);
	}
	
	public static boolean hasScoreboard(String player, int priority)
	{
		return getPlayerData(player).hasScoreboard(priority);
	}
	
	public static PlayerData getPlayerData(String player)
	{
		PlayerData playerData = scoreboards.get(player);
		if (playerData == null)
		{
			playerData = new PlayerData();
			scoreboards.put(player, playerData);
		}
		
		return playerData;
	}	
}


