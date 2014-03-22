package us.corenetwork.core.respawn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.PlayerUtils;

public class ProtectTimer implements Runnable
{
	public static Map<String, Integer> protectedPlayers = new HashMap<String, Integer>();

	public static void endProtectionMessage(Player player)
	{
		String message;
		if (GriefPreventionHandler.playerHasClaim(player.getName()))
			message = RespawnSettings.MESSAGE_SPAWN_PROTECTION_END_CLAIMS.string();
		else
			message = RespawnSettings.MESSAGE_SPAWN_PROTECTION_END_NO_CLAIMS.string();
	
		PlayerUtils.Message(message, player);
	}
	
	@Override
	public void run() {
		Iterator<Entry<String, Integer>> i = protectedPlayers.entrySet().iterator();
		while (i.hasNext())
		{
			Entry<String, Integer> e = i.next();
			
			Player player = Bukkit.getServer().getPlayerExact(e.getKey());
			if (player == null)
				continue;
			
			int timeLeft = e.getValue();
			timeLeft--;
			if (timeLeft <= 0)
			{
				i.remove();
				endProtectionMessage(player);
			}
			else
			{
				e.setValue(timeLeft);
				
				@SuppressWarnings("unchecked")
				List<Integer> notifications = (List<Integer>) RespawnSettings.SPAWN_PROTECTION_NOTIFICATIONS.list();
				for (Integer nTime : notifications)
				{
					if (nTime == timeLeft)
					{
						String message = RespawnSettings.MESSAGE_SPAWN_PROTECTION_NOTIFICATION.string();
						message = message.replace("<Time>", Integer.toString(timeLeft));
						PlayerUtils.Message(message, player);

						break;
					}
				}
			}
		}
		
	}
	
}
