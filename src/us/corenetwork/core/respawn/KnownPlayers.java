package us.corenetwork.core.respawn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class KnownPlayers {
	private static HashSet<UUID> playerList = new HashSet<UUID>();
	
	public static void load()
	{
		RespawnModule.instance.loadStorageYaml();
		playerList.clear();
		
		List<String> savedPlayers = RespawnModule.instance.storageConfig.getStringList("knownPlayers");
		if (savedPlayers == null)
			return;
		
		for (String player : savedPlayers)
			playerList.add(UUID.fromString(player));
	}
	
	public static void save()
	{
		List<String> playersToSave = new ArrayList<String>(playerList.size());
		for (UUID player : playerList)
			playersToSave.add(player.toString());
		
		RespawnModule.instance.storageConfig.set("knownPlayers", playersToSave);
		RespawnModule.instance.saveStorageYaml();
	}
	
	public static boolean isKnownPlayer(UUID uuid)
	{
		return playerList.contains(uuid);
	}
	
	public static void savePlayer(UUID uuid)
	{
		playerList.add(uuid);
		save();
	}
}
