package us.corenetwork.core.respawn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class KnownPlayers {
	private static HashSet<String> playerList = new HashSet<String>();
	
	public static void load()
	{
		RespawnModule.instance.loadStorageYaml();
		playerList.clear();
		
		List<String> savedPlayers = RespawnModule.instance.storageConfig.getStringList("knownPlayers");
		if (savedPlayers == null)
			return;
		
		for (String player : savedPlayers)
			playerList.add(player);
	}
	
	public static void save()
	{
		List<String> playersToSave = new ArrayList<String>(playerList.size());
		for (String player : playerList)
			playersToSave.add(player);
		
		RespawnModule.instance.storageConfig.set("knownPlayers", playersToSave);
		RespawnModule.instance.saveStorageYaml();
	}
	
	public static boolean isKnownPlayer(String player)
	{
		return playerList.contains(player.toLowerCase());
	}
	
	public static void savePlayer(String player)
	{
		playerList.add(player.toLowerCase());
		save();
	}
}
