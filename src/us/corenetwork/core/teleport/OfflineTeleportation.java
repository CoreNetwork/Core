package us.corenetwork.core.teleport;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.util.PlayerUtils;

public class OfflineTeleportation {
	public static Map<String, PendingTeleport> awaitingConfirmation = new HashMap<String, PendingTeleport>();
	public static Map<String, Location> waitingLogon = new HashMap<String, Location>();

	public static void registerTeleport(String command, CommandSender moderator, String teleportedPlayer, Location location)
	{
		PendingTeleport teleport = new PendingTeleport();
		
		teleport.player = teleportedPlayer;
		teleport.location = location;
		teleport.startTiming = System.currentTimeMillis();
		
		awaitingConfirmation.put(moderator.getName(), teleport);
		
		String message = TeleportSettings.MESSAGE_CONFIRM_OFFLINE_TELEPORT.string();
		message = message.replace("<Command>", command);
		message = message.replace("<Player>", command);
		PlayerUtils.Message(message, moderator);
	}
	
	public static void confirmTeleport(CommandSender moderator)
	{
		PendingTeleport teleport = awaitingConfirmation.get(moderator.getName());
		if (teleport == null || (System.currentTimeMillis() - teleport.startTiming > TeleportSettings.OFFLINE_TELEPORT_CONFIRM_TIMEOUT_SECONDS.integer() * 1000))
		{
			PlayerUtils.Message(TeleportSettings.MESSAGE_NO_OFFLINE_TELEPORTS.string(), moderator);
			return;
		}
		
		waitingLogon.put(teleport.player, teleport.location);
		
		PlayerUtils.Message(TeleportSettings.MESSAGE_OFFLINE_TELEPORT_CONFIRMED.string(), moderator);
	}
	
	public static void playerLoggedIn(Player player)
	{
		Location location = waitingLogon.get(player.getName());
		waitingLogon.remove(player.getName());
		
		if (location != null)
		{
			if (location.getWorld() == null)
			{
				location.setWorld(player.getWorld());

				if (!TeleportUtil.isInBounds(player, location.getBlockX(), location.getBlockZ(), location.getWorld().getName()))
				{
					return;
				}
			}
			
			location.setPitch(player.getLocation().getPitch());
			location.setYaw(player.getLocation().getYaw());
			
			String message = TeleportSettings.MESSAGE_YOU_TELEPORTED_TO_COORDINATES.string();
			message = message.replace("<Player>", player.getName());
			message = message.replace("<X>", Integer.toString((int) location.getX()));
			message = message.replace("<Y>", Integer.toString((int) location.getY()));
			message = message.replace("<Z>", Integer.toString((int) location.getZ()));

			PlayerUtils.Message(message, player);
			
			String notice = TeleportSettings.MESSAGE_PLAYER_TELEPORTED_TO_COORDINATES.string();
			notice = notice.replace("<Player>", player.getName());
			notice = notice.replace("<World>", location.getWorld().getName());
			notice = notice.replace("<X>", Integer.toString((int) location.getX()));
			notice = notice.replace("<Y>", Integer.toString((int) location.getY()));
			notice = notice.replace("<Z>", Integer.toString((int) location.getZ()));

			TeleportUtil.notifyModerators(null, notice, player);
			
			player.teleport(location);
		}
	}
	
	public static class PendingTeleport
	{
		String player;
		Location location;
		long startTiming;
	}
}
