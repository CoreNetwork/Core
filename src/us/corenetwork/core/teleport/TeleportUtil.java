package us.corenetwork.core.teleport;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CLog;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;

public class TeleportUtil {	
	public static Location getSenderLocation(CommandSender sender)
	{
		if (sender instanceof Player)
			return ((Player) sender).getLocation();
		else if (sender instanceof BlockCommandSender)
			return ((BlockCommandSender) sender).getBlock().getLocation();
		else
			return null;
	}
	
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
}
