package us.corenetwork.core.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils.PickPlayerResult.PickPlayerResultState;

import java.util.HashMap;
import java.util.UUID;

public class PlayerUtils {
    private static HashMap<UUID, HashMap<String, Long>> antiSpamMap = new HashMap<UUID, HashMap<String, Long>>();

	public static PickPlayerResult pickPlayer(String partialName)
	{
		return pickPlayer(partialName, false);
	}
	
	public static PickPlayerResult pickPlayer(String partialName, boolean includeOffline)
	{
		partialName = partialName.toLowerCase();
		
		Player pickedPlayer = null;
		
		pickedPlayer = Bukkit.getPlayerExact(partialName);
		if (pickedPlayer != null)
		{
			return new PickPlayerResult(pickedPlayer);
		}
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getName().toLowerCase().startsWith(partialName))
			{
				if (pickedPlayer != null)
					return new PickPlayerResult(PickPlayerResultState.AMBIGUOUS);
				
				pickedPlayer = player;
			}
		}
		
		if (pickedPlayer == null && includeOffline)
		{
			for (OfflinePlayer player : Bukkit.getOfflinePlayers())
			{
				if (player.getName().equalsIgnoreCase(partialName))
				{
					return new PickPlayerResult(player);
				}
			}
		}
		
		return new PickPlayerResult(pickedPlayer);
	}

	public static class PickPlayerResult
	{
		public PickPlayerResult(PickPlayerResultState result)
		{
			this.result = result;
		}
		
		public PickPlayerResult(OfflinePlayer player)
		{
			this.player = player;
			this.result = player == null ? PickPlayerResultState.NOT_FOUND : PickPlayerResultState.OK;
		}
		
		public OfflinePlayer player;
		public PickPlayerResultState result;
		
		public static enum PickPlayerResultState
		{
			OK,
			NOT_FOUND,
			AMBIGUOUS;
		}
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

	public static void Broadcast(String message, String exclusion)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (!p.getName().equals(exclusion))
				PlayerUtils.Message(message, p);
		}

	}

	public static void MessagePermissions(String message, String permission)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (Util.hasPermission(p, permission))
				Message(message, p);
		}
	}
	
	public static boolean isPlayer(String arg)
	{
		Player player = CorePlugin.instance.getServer().getPlayerExact(arg);
		return player != null;
	}

    /**
     * Checks if the given message has been sent to the player in the last 10 seconds. If not, sends the message.<br>
     * @param player the recipient of the message
     * @param message the message
     * @return wether the message actually has been sent
     */
    public static boolean sendSpammyMessage(Player player, String message) {
        HashMap<String, Long> playerMap = antiSpamMap.get(player.getUniqueId());
        boolean send = true;
        if (playerMap == null) {
            playerMap = new HashMap<String, Long>();
            antiSpamMap.put(player.getUniqueId(), playerMap);
        }
        Long lastMessage = playerMap.get(message);
        if (lastMessage != null && System.currentTimeMillis() - lastMessage < 10000) {
            send = false;
        }
        playerMap.put(message, System.currentTimeMillis());
        if (send) {
            Message(message, player);
        }
        return send;
    }

}
