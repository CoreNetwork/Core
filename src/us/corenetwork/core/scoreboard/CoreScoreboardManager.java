package us.corenetwork.core.scoreboard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class CoreScoreboardManager {
	protected static HashMap<String, PlayerData> scoreboards = new HashMap<String, PlayerData>();
	private static CraftScoreboard teamStorage;
	protected static TeamStorageScoreboard nmsTeamStorage;

	static
	{
		try
		{
			Constructor craftScoreboardConstructor = CraftScoreboard.class.getDeclaredConstructor(net.minecraft.server.v1_8_R3.Scoreboard.class);
			craftScoreboardConstructor.setAccessible(true);

			nmsTeamStorage = new TeamStorageScoreboard();
			teamStorage = (CraftScoreboard) craftScoreboardConstructor.newInstance(nmsTeamStorage);

		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

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

	/**
	 * Use this method to get scoreboard that stores global teams. These teams apply to all players through all scoreboards.
	 * <p />
	 * Do not use any non-team-related functions on this scoreboard or you will get exceptions.
	 *
	 * @return Global teams scoreboard.
	 */
	public static Scoreboard getTeamsScoreboard()
	{
		return teamStorage;
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


