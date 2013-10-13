package us.corenetwork.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils {
	public static Player pickPlayer(String partialName)
	{
		return (Player) pickPlayer(partialName, false);
	}
	
	public static OfflinePlayer pickPlayer(String partialName, boolean includeOffline)
	{
		partialName = partialName.toLowerCase();
		
		Player pickedPlayer = null;
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getName().toLowerCase().startsWith(partialName))
			{
				if (pickedPlayer != null)
					return null; //Ambiguity detected
				
				pickedPlayer = player;
			}
		}
		
		if (pickedPlayer == null && includeOffline)
		{
			for (OfflinePlayer player : Bukkit.getOfflinePlayers())
			{
				if (player.getName().equalsIgnoreCase(partialName))
				{
					return player;
				}
			}
		}
		
		return pickedPlayer;
	}

	public static void safeTeleport(final Player player, final Location location)
	{
		Chunk c = location.getChunk();
		if (!c.isLoaded())
			location.getChunk().load();
		player.teleport(location);
	
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CorePlugin.instance, new Runnable() {
			@Override
			public void run() {
				player.teleport(location);
	
			}
		}, 10);
	}

	public static void Message(String message, CommandSender sender)
	{
		message = message.replaceAll("\\&([0-9abcdefklmnor])", ChatColor.COLOR_CHAR + "$1");
	
		final String newLine = "\\[NEWLINE\\]";
		String[] lines = message.split(newLine);
	
		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].trim();
	
			if (i == 0)
				continue;
	
			int lastColorChar = lines[i - 1].lastIndexOf(ChatColor.COLOR_CHAR);
			if (lastColorChar == -1 || lastColorChar >= lines[i - 1].length() - 1)
				continue;
	
			char lastColor = lines[i - 1].charAt(lastColorChar + 1);
			lines[i] = Character.toString(ChatColor.COLOR_CHAR).concat(Character.toString(lastColor)).concat(lines[i]);	
		}		
	
		for (int i = 0; i < lines.length; i++)
			sender.sendMessage(lines[i]);
	}

	public static void MessagePermissions(String message, String permission)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (Util.hasPermission(p,permission))
				Message(message, p);
		}
	}
}
