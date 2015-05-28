package us.corenetwork.core.player.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.corenetwork.core.player.PlayerSettings;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;

public class XpCommand extends BasePlayerCommand {


	public XpCommand()
	{
		desc = "Give XP";
		permission = "xp";
		needPlayer = false;
	}

	// /core xp [<player>] (<experience> | <levels> | clear) [silent]

	@Override
	public void run(CommandSender sender, String[] args)
	{
		int levelsPosition = 0;

		try
		{
			boolean silent = args[args.length - 1].equals("silent");

			Player targetPlayer = null;
			targetPlayer = Bukkit.getPlayerExact(args[0]);
			if (targetPlayer != null)
			{
				levelsPosition = 1;
			}
			else
			{
				if (!(sender instanceof Player))
				{
					sender.sendMessage("You need to be player to message yourself!");
					return;
				}

				targetPlayer = (Player) sender;
			}

			String action = args[levelsPosition];
			if (action.equals("clear"))
			{
				targetPlayer.setExp(0);
				targetPlayer.setLevel(0);
				if (!silent)
				{
					PlayerUtils.Message(PlayerSettings.MESSAGE_EXPERIENCE_CLEARED.string(), sender);
				}

				return;
			}

			if (action.endsWith("L"))
			{
				String levelsString = action.substring(0, action.length() - 1);
				if (!Util.isInteger(levelsString))
				{
					displayUsage(sender);
					return;
				}

				int levels = Integer.parseInt(levelsString);
				targetPlayer.setLevel(targetPlayer.getLevel() + levels);

				if (!silent)
				{
					String message = PlayerSettings.MESSAGE_LEVELS_ADDED.string();
					message = message.replace("<Amount>", Integer.toString(levels));
					PlayerUtils.Message(message, sender);
				}

				return;
			}

			if (!Util.isInteger(action))
			{
				displayUsage(sender);
				return;
			}

			int exp = Integer.parseInt(action);

			((CraftPlayer) targetPlayer).giveExp(exp);

			if (!silent)
			{
				String message = PlayerSettings.MESSAGE_EXPERIENCE_ADDED.string();
				message = message.replace("<Amount>", Integer.toString(exp));
				PlayerUtils.Message(message, sender);
			}
		}
		catch (IndexOutOfBoundsException e) //Number of elements is highly variable so lets just catch invalid number by using try-catch.
		{
			displayUsage(sender);
			return;
		}
	}

	private void displayUsage(CommandSender sender)
	{
		PlayerUtils.Message(PlayerSettings.MESSAGE_XP_SYNTAX.string(), sender);
	}
}
