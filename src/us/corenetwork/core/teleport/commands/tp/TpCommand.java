package us.corenetwork.core.teleport.commands.tp;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.PlayerUtils.PickPlayerResult;
import us.corenetwork.core.Util;
import us.corenetwork.core.corecommands.BaseCoreCommand;
import us.corenetwork.core.corecommands.SudoCommand;
import us.corenetwork.core.teleport.Coordinate;
import us.corenetwork.core.teleport.OfflineTeleportation;
import us.corenetwork.core.teleport.TeleportSettings;
import us.corenetwork.core.teleport.TeleportUtil;

public class TpCommand extends BaseCoreCommand {	
	public static HashMap<String, BaseCoreCommand> subCommands = new HashMap<String, BaseCoreCommand>();
	static
	{
		subCommands.put("bring", new BringCommand());
		subCommands.put("swap", new SwapCommand());

	}


	public TpCommand()
	{
		desc = "Upgrade of vanilla /tp";
		permission = "tp";
		needPlayer = false;
	}


	public void run(final CommandSender sender, String[] args) {
		int argsSize = args.length;
		boolean silent = false;

		if (argsSize > 0)
		{
			if (args[argsSize - 1].equalsIgnoreCase("silent"))
			{
				argsSize--;

				if (Util.hasPermission(sender, "core.teleport.silent"))
					silent = true;
			}
			else if (args[argsSize - 1].equalsIgnoreCase("confirm"))
			{
				// "/<anything> confirm"
				OfflineTeleportation.confirmTeleport(sender);
				return;
			}
			else if (subCommands.containsKey(args[0]))
			{
				subCommands.get(args[0]).execute(sender, args, true);
				return;
			}

		}


		if (argsSize == 1)
		{
			// "/tp name"
			teleportTo("tp", sender, sender, args[0], silent);
			return;
		}
		else if (argsSize == 2)
		{
			if (Coordinate.isCoordinate(args[0]) && Coordinate.isCoordinate(args[1]))
			{
				// "/tp x y"
				teleportTo(sender, sender, null, Coordinate.parseCoordinate(args[0]), null, Coordinate.parseCoordinate(args[1]), silent);
				return;
			}
			else
			{
				// "/tp player player"
				teleportTo("tp", sender, args[0], args[1], silent);
				return;
			}	
		}
		else if (argsSize == 3)
		{
			if (Coordinate.isCoordinate(args[0]) && Coordinate.isCoordinate(args[1]) && Coordinate.isCoordinate(args[2]))
			{
				// "/tp x y z"
				teleportTo(sender, sender, null, Coordinate.parseCoordinate(args[0]), Coordinate.parseCoordinate(args[1]), Coordinate.parseCoordinate(args[2]), silent);
				return;
			}
			else if (Coordinate.isCoordinate(args[1]) && Coordinate.isCoordinate(args[2]))
			{
				if (Bukkit.getWorld(args[0]) == null)
				{
					// "/tp player x z"
					teleportTo(sender, args[0], null, Coordinate.parseCoordinate(args[1]), null, Coordinate.parseCoordinate(args[2]), silent);
					return;
				}
				else
				{
					// "/tp world x z"
					teleportTo(sender, sender, args[0], Coordinate.parseCoordinate(args[1]), null, Coordinate.parseCoordinate(args[2]), silent);
					return;
				}
			}
		}
		else if (argsSize == 4)
		{
			if (Bukkit.getWorld(args[0]) == null)
			{
				// "/tp player x y z"
				teleportTo(sender, args[0], null, Coordinate.parseCoordinate(args[1]), Coordinate.parseCoordinate(args[2]), Coordinate.parseCoordinate(args[3]), silent);
				return;
			}
			else
			{
				// "/tp world x y z"
				teleportTo(sender, sender, args[0], Coordinate.parseCoordinate(args[1]), Coordinate.parseCoordinate(args[2]), Coordinate.parseCoordinate(args[3]), silent);
				return;
			}
		}
		else if (argsSize == 5)
		{
			// "/tp player world x y z"
			teleportTo(sender, args[0], args[1], Coordinate.parseCoordinate(args[2]), Coordinate.parseCoordinate(args[3]), Coordinate.parseCoordinate(args[4]), silent);
			return;
		}

		PlayerUtils.Message("Usage:", sender);
		PlayerUtils.Message("/tp [player]", sender);
		PlayerUtils.Message("/tp [player] [player]", sender);

		PlayerUtils.Message("/tp (player) (world) [x] (y) [z]", sender);
	}	

	public static void teleportTo(CommandSender sender, Object teleported, String worldName, Coordinate x, Coordinate y, Coordinate z, boolean silent)
	{

		OfflinePlayer player = null;

		if (teleported instanceof String)
		{
			PickPlayerResult searchResult = PlayerUtils.pickPlayer((String) teleported, true);

			switch (searchResult.result)
			{
			case OK:
				player = searchResult.player;
				break;
			case NOT_FOUND:
				String message = TeleportSettings.MESSAGE_UNKNOWN_PLAYER.string();
				message = message.replace("<Name>", (String) teleported);

				PlayerUtils.Message(message, sender);
				return;
			case AMBIGUOUS:
				message = TeleportSettings.MESSAGE_AMBIGUOUS_MATCH.string();
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

		World world = null;
		if (worldName == null)
		{
			if (player.isOnline())
			{
				world = ((Player) player).getWorld();
			}
		}
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
			if (world.getEnvironment() != Environment.NETHER && world != null)
				yNumber = world.getHighestBlockYAt((int) xNumber, (int) zNumber);
			else
				yNumber = TeleportSettings.NETHER_SURFACE_Y.doubleNumber();
		}
		else
		{
			yNumber = y.getNumber();
			if (y.isRelative()) yNumber += senderLocation.getY();
		}

		Location tpLoc = new Location(world, xNumber, yNumber, zNumber);

		if (world != null && !TeleportUtil.isInBounds(sender, (int) xNumber, (int) zNumber, world.getName()))
		{
			PlayerUtils.Message(TeleportSettings.MESSAGE_OUT_OF_BOUNDS.string(), sender);
			return;
		}
		
		if (player.isOnline())
		{
			Player onlinePlayer = (Player) player;
			
			tpLoc.setPitch(onlinePlayer.getLocation().getPitch());
			tpLoc.setYaw(onlinePlayer.getLocation().getYaw());
			
			if (!silent  && !(sender instanceof Player && SudoCommand.isUnderSudo(sender.getName())))
			{
				String message = TeleportSettings.MESSAGE_YOU_TELEPORTED_TO_COORDINATES.string();
				message = message.replace("<Player>", player.getName());
				message = message.replace("<X>", Integer.toString((int) xNumber));
				message = message.replace("<Y>", Integer.toString((int) yNumber));
				message = message.replace("<Z>", Integer.toString((int) zNumber));

				PlayerUtils.Message(message, onlinePlayer);
				
				String notice = TeleportSettings.MESSAGE_PLAYER_TELEPORTED_TO_COORDINATES.string();
				notice = notice.replace("<Player>", player.getName());
				notice = notice.replace("<World>", world.getName());
				notice = notice.replace("<X>", Integer.toString((int) xNumber));
				notice = notice.replace("<Y>", Integer.toString((int) yNumber));
				notice = notice.replace("<Z>", Integer.toString((int) zNumber));
				
				TeleportUtil.notifyModerators(sender, notice, onlinePlayer);
			}

			PlayerUtils.safeTeleport(onlinePlayer, tpLoc);
		}
		else
		{
			OfflineTeleportation.registerTeleport("tp", sender, player.getName(), tpLoc);
		}
	}

	public static void teleportTo(String command, CommandSender sender, Object from, Object to, boolean silent)
	{
		OfflinePlayer playerFrom = null;

		if (from instanceof String)
		{
			PickPlayerResult searchResult = PlayerUtils.pickPlayer((String) from, true);
			switch (searchResult.result)
			{
			case OK:
				playerFrom = searchResult.player;
				break;
			case NOT_FOUND:
				String message = TeleportSettings.MESSAGE_UNKNOWN_PLAYER.string();
				message = message.replace("<Name>", (String) from);

				PlayerUtils.Message(message, sender);
				return;
			case AMBIGUOUS:
				message = TeleportSettings.MESSAGE_AMBIGUOUS_MATCH.string();
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

		Player playerTo = null;

		if (to instanceof String)
		{
			PickPlayerResult searchResult = PlayerUtils.pickPlayer((String) to);
			switch (searchResult.result)
			{
			case OK:
				playerTo = (Player) searchResult.player;
				break;
			case NOT_FOUND:
				String message = TeleportSettings.MESSAGE_UNKNOWN_PLAYER.string();
				message = message.replace("<Name>", (String) to);

				PlayerUtils.Message(message, sender);
				return;
			case AMBIGUOUS:
				message = TeleportSettings.MESSAGE_AMBIGUOUS_MATCH.string();
				message = message.replace("<Name>", (String) to);

				PlayerUtils.Message(message, sender);
				return;
			}
		}
		else if (!(to instanceof Player))
		{
			PlayerUtils.Message(TeleportSettings.MESSAGE_ONLY_PLAYER.string(), sender);
			return;
		}
		else
			playerTo = (Player) to;

		if (playerFrom.isOnline())
		{
			Player onlinePlayer = (Player) playerFrom;
			
			if (!silent && !(sender instanceof Player && SudoCommand.isUnderSudo(sender.getName())))
			{
				String message = TeleportSettings.MESSAGE_YOU_TELEPORTED_TO_PLAYER.string();
				message = message.replace("<Player>", playerTo.getName());
				PlayerUtils.Message(message, onlinePlayer);
				
				String notice = TeleportSettings.MESSAGE_PLAYER_TELEPORTED_TO_PLAYER.string();
				notice = notice.replace("<From>", playerFrom.getName());
				notice = notice.replace("<To>", playerTo.getName());
				TeleportUtil.notifyModerators(sender, notice, onlinePlayer);
			}

			PlayerUtils.safeTeleport(onlinePlayer, playerTo.getLocation());
		}
		else
		{
			OfflineTeleportation.registerTeleport(command, sender, playerFrom.getName(), playerTo.getLocation());
		}

	}
}
