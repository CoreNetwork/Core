package us.corenetwork.core.teleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.teleport.Coordinate;
import us.corenetwork.core.teleport.TeleportSettings;
import us.corenetwork.core.teleport.TeleportUtil;

public class TpCommand extends BaseWarpCommand {	
	private boolean silent;
	
	public TpCommand()
	{
		desc = "Upgrade of vanilla /tp";
		permission = "tp";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		int argsSize = args.length;
		silent = false;
		
		if (argsSize > 0 && args[argsSize - 1].equalsIgnoreCase("silent"))
		{
			argsSize--;
			
			if (Util.hasPermission(sender, "core.teleport.silent"))
				silent = true;
		}
		
		if (argsSize == 1)
		{
			// "/tp name"
			teleportTo(sender, sender, args[0]);
			return;
		}
		else if (argsSize == 2)
		{
			if (Coordinate.isCoordinate(args[0]) && Coordinate.isCoordinate(args[1]))
			{
				// "/tp x y"
				teleportTo(sender, sender, null, Coordinate.parseCoordinate(args[0]), null, Coordinate.parseCoordinate(args[1]));
				return;
			}
			else
			{
				// "/tp player player"
				teleportTo(sender, args[0], args[1]);
				return;
			}	
		}
		else if (argsSize == 3)
		{
			if (Coordinate.isCoordinate(args[0]) && Coordinate.isCoordinate(args[1]) && Coordinate.isCoordinate(args[2]))
			{
				// "/tp x y z"
				teleportTo(sender, sender, null, Coordinate.parseCoordinate(args[0]), Coordinate.parseCoordinate(args[1]), Coordinate.parseCoordinate(args[2]));
				return;
			}
			else if (Coordinate.isCoordinate(args[1]) && Coordinate.isCoordinate(args[2]))
			{
				if (Bukkit.getWorld(args[0]) == null)
				{
					// "/tp player x z"
					teleportTo(sender, args[0], null, Coordinate.parseCoordinate(args[1]), null, Coordinate.parseCoordinate(args[2]));
					return;
				}
				else
				{
					// "/tp world x z"
					teleportTo(sender, sender, args[0], Coordinate.parseCoordinate(args[1]), null, Coordinate.parseCoordinate(args[2]));
					return;
				}
			}
		}
		else if (argsSize == 4)
		{
			if (Bukkit.getWorld(args[0]) == null)
			{
				// "/tp player x y z"
				teleportTo(sender, args[0], null, Coordinate.parseCoordinate(args[1]), Coordinate.parseCoordinate(args[2]), Coordinate.parseCoordinate(args[3]));
				return;
			}
			else
			{
				// "/tp world x y z"
				teleportTo(sender, sender, args[0], Coordinate.parseCoordinate(args[1]), Coordinate.parseCoordinate(args[2]), Coordinate.parseCoordinate(args[3]));
				return;
			}
		}
		else if (argsSize == 5)
		{
			// "/tp player world x y z"
			teleportTo(sender, args[0], args[1], Coordinate.parseCoordinate(args[2]), Coordinate.parseCoordinate(args[3]), Coordinate.parseCoordinate(args[4]));
			return;
		}
		
		PlayerUtils.Message("Usage:", sender);
		PlayerUtils.Message("/tp [player]", sender);
		PlayerUtils.Message("/tp [player] [player]", sender);
		
		PlayerUtils.Message("/tp (player) (world) [x] (y) [z]", sender);
	}	
	
	private void teleportTo(CommandSender sender, Object teleported, String worldName, Coordinate x, Coordinate y, Coordinate z)
	{
		
		Player player;
		
		if (teleported instanceof String)
		{
			player = PlayerUtils.pickPlayer((String) teleported);
			if (player == null)
			{
				String message = TeleportSettings.MESSAGE_UNKNOWN_PLAYER.string();
				message = message.replace("<Name>", (String) teleported);
				
				PlayerUtils.Message(message, sender);
				return;
			}
		}
		else if (!(teleported instanceof Player))
		{
			PlayerUtils.Message(TeleportSettings.MESSAGE_ONLY_PLAYER.string(), sender);
			return;
		}
		else
			player = (Player) teleported;
		
		World world;
		if (worldName == null)
			world = player.getWorld();
		else
		{
			world = Bukkit.getWorld(worldName);
			if (world == null)
			{
				String message = TeleportSettings.MESSAGE_UNKNOWN_WORLD.string();
				message = message.replace("<World>", worldName);

				PlayerUtils.Message(message, sender);
				return;
			}
		}
		
		if (world == null)
			world = player.getWorld();
		
		Location senderLocation = TeleportUtil.getSenderLocation(sender);
		if ((x.isRelative() || z.isRelative() || (y != null && y.isRelative())) && senderLocation == null)
		{
			PlayerUtils.Message(TeleportSettings.MESSAGE_RELATIVE_NO_CONSOLE.string(), sender);
			return;
		}
		
		double xNumber = x.getNumber();
		if (x.isRelative()) xNumber += senderLocation.getX();
		
		double zNumber = z.getNumber();
		if (z.isRelative()) zNumber += senderLocation.getZ();
		
		double yNumber;
		
		if (y == null)
		{
			if (world.getEnvironment() != Environment.NETHER)
				yNumber = world.getHighestBlockYAt((int) xNumber, (int) zNumber);
			else
				yNumber = TeleportSettings.NETHER_SURFACE_Y.doubleNumber();
		}
		else
		{
			yNumber = y.getNumber();
			if (y.isRelative()) yNumber += senderLocation.getY();
		}
		
		if (!silent)
		{
			String message = TeleportSettings.MESSAGE_YOU_TELEPORTED_TO_COORDINATES.string();
			message = message.replace("<Player>", player.getName());
			message = message.replace("<X>", Double.toString(xNumber));
			message = message.replace("<Y>", Double.toString(yNumber));
			message = message.replace("<Z>", Double.toString(zNumber));

			PlayerUtils.Message(message, player);
		}
		
		String notice = TeleportSettings.MESSAGE_PLAYER_TELEPORTED_TO_COORDINATES.string();
		notice = notice.replace("<World>", world.getName());
		notice = notice.replace("<X>", Double.toString(xNumber));
		notice = notice.replace("<Y>", Double.toString(yNumber));
		notice = notice.replace("<Z>", Double.toString(zNumber));

		TeleportUtil.notifyModerators(sender, notice, player);
		
		TeleportUtil.freezePlayer(player.getName());
		PlayerUtils.safeTeleport(player, new Location(world, xNumber, yNumber, zNumber));
	}
	
	private void teleportTo(CommandSender sender, Object from, String to)
	{
		Player playerFrom = null;
		
		if (from instanceof String)
		{
			playerFrom = PlayerUtils.pickPlayer((String) from);
			if (playerFrom == null)
			{
				String message = TeleportSettings.MESSAGE_UNKNOWN_PLAYER.string();
				message = message.replace("<Name>", (String) from);
				
				PlayerUtils.Message(message, sender);
				return;
			}
		}
		else if (!(from instanceof Player))
		{
			PlayerUtils.Message(TeleportSettings.MESSAGE_ONLY_PLAYER.string(), sender);
			return;
		}
		else
			playerFrom = (Player) from;
		
		Player playerTo = PlayerUtils.pickPlayer(to);
		
		if (playerTo == null)
		{
			String message = TeleportSettings.MESSAGE_UNKNOWN_PLAYER.string();
			message = message.replace("<Name>", to);
			
			PlayerUtils.Message(message, sender);
			return;
		}

		if (!silent)
		{
			String message = TeleportSettings.MESSAGE_YOU_TELEPORTED_TO_PLAYER.string();
			message = message.replace("<Player>", playerTo.getName());
			PlayerUtils.Message(message, playerFrom);
		}
		
		String notice = TeleportSettings.MESSAGE_PLAYER_TELEPORTED_TO_PLAYER.string();
		notice = notice.replace("<From>", playerFrom.getName());
		notice = notice.replace("<To>", playerTo.getName());
		TeleportUtil.notifyModerators(sender, notice, playerTo);
		
		TeleportUtil.freezePlayer(playerFrom.getName());
		PlayerUtils.safeTeleport(playerFrom, playerTo.getLocation());
	}
}
