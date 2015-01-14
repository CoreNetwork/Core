package us.corenetwork.core.teleport;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CLog;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;

public class TeleportUtil {
	public static void notifyModerators(CommandSender sender, String message, Player... ignored)
	{
		boolean alreadySent = false;
		List<Player> ignoredPlayers = Arrays.asList(ignored);
		
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (sender == player)
				alreadySent = true;
			
			if (ignoredPlayers.contains(player))
				continue;
			
			if (Util.hasPermission(player, "core.teleport.notify"))
				PlayerUtils.Message(message, player);
		}
		
		if (!alreadySent && sender != null && sender instanceof Player)
			PlayerUtils.Message(message, sender);

			CLog.info(message);
	}
	
	public static boolean isInBounds(CommandSender sender, int x, int z, String world)
	{
		if (x > getLimit(world, "MaxX"))
		{
			return false;
		}
		if (x < getLimit(world, "MinX"))
		{
			return false;
		}
		
		if (z > getLimit(world, "MaxZ"))
		{
			return false;
		}
		if (z < getLimit(world, "MinZ"))
		{
			return false;
		}
		
		return true;
	}
	
	public static int getLimit(String world_name, String limitType)
	{
		Integer limit = (Integer) TeleportModule.instance.config.get("Teleport.Limits." + world_name + "." + limitType);
		if (limit == null)
			limit = (Integer) TeleportModule.instance.config.get("Teleport.Limits.Other." + limitType);
		
		return limit;
	}

	public static Location getPossiblePlayerLocation(OfflinePlayer player)
	{
		if (player.isOnline())
			return player.getPlayer().getLocation();

		return null;
	}
}
