package us.corenetwork.core.player.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.v1_8_R1.EnumChatFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.Setting;
import us.corenetwork.core.Settings;
import us.corenetwork.core.player.PlayerModule;
import us.corenetwork.core.respawn.ProtectTimer;
import us.corenetwork.core.util.MinecraftJson;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.player.PlayerSettings;
import us.corenetwork.core.util.TimeUtils;
import us.corenetwork.core.util.TitleSender;

public class RemindCommand extends BasePlayerCommand {

	private static List<PendingReminder> pendingReminders = new LinkedList<PendingReminder>();

	public RemindCommand()
	{
		desc = "Remind me";
		permission = "remind";
		needPlayer = false;

		Bukkit.getScheduler().runTaskTimer(CorePlugin.instance, new RemindTimer(), 20, 20);
	}

	// /remind [me | <player>] [in] <time> [to] <message>

	@Override
	public void run(CommandSender sender, String[] args)
	{
		int pointer = 0;

		try
		{
			String author = null;
			Player targetPlayer = null;
			if (args[pointer].equalsIgnoreCase("me"))
			{
				pointer++; //It is dummy "me", lets just move forward to next one
			}
			else
			{
				targetPlayer = Bukkit.getPlayerExact(args[pointer]);
				if (targetPlayer != null)
				{
					pointer++; //First argument is player name, lets move forward to next argument

					if (!sender.hasPermission("core.player.remind.player"))
					{
						PlayerUtils.Message(Settings.getString(Setting.MESSAGE_NO_PERMISSION), sender);
						return;
					}

					author = sender.getName();
				}

				// We don't move forward here as first argument is neither "me" or valid online player name.
			}

			if (targetPlayer == null)
			{
				if (!(sender instanceof Player))
				{
					sender.sendMessage("You need to be player to message yourself!");
					return;
				}

				targetPlayer = (Player) sender;
			}

			if (args[pointer].equalsIgnoreCase("in")) //ignore "in" and just move forward to next argument
				pointer++;

			if (args[pointer].equals("clear"))
			{
				clearRemindersForPlayer(targetPlayer);
				PlayerUtils.Message(PlayerSettings.MESSAGE_REMINDERS_CLEARED.string(), sender);
				return;
			}

			if (targetPlayer == sender && getPendingRemindersForPlayer(targetPlayer) > PlayerSettings.REMINDER_MAXIMUM_PENDING.integer())
			{
				PlayerUtils.Message(PlayerSettings.MESSAGE_TOO_MANY_REMINDERS.string(), sender);
				return;
			}

			int time = TimeUtils.getTimeFromString(args[pointer]);
			if (time < 0)
			{
				displayUsage(sender);
				return;
			}
			if (time > PlayerSettings.REMINDER_MAXIMUM_TIME.integer())
			{
				PlayerUtils.Message(PlayerSettings.MESSAGE_REMINDER_TOO_LONG.string(), sender);
				return;
			}
			pointer++; //We got time

			if (args[pointer].equalsIgnoreCase("to")) //ignore "to" and just move forward to next argument
				pointer++;

			String message = "";
			for (int i = pointer; i < args.length; i++)
				message = message.concat(args[i]).concat(" ");

			message = message.substring(0, message.length() - 1);

			PendingReminder pendingReminder = new PendingReminder();
			pendingReminder.playerUUID = targetPlayer.getUniqueId();
			pendingReminder.author = author;
			pendingReminder.message = message;
			pendingReminder.time = System.currentTimeMillis() + time * 1000;
			pendingReminders.add(pendingReminder);

			PlayerUtils.Message(PlayerSettings.MESSAGE_REMINDER_ADDED.string(), sender);
		}
		catch (IndexOutOfBoundsException e) //Number of elements is highly variable so lets just catch invalid number by using try-catch.
		{
			displayUsage(sender);
			return;
		}
	}

	private void displayUsage(CommandSender sender)
	{
		PlayerUtils.Message(PlayerSettings.MESSAGE_REMINDER_SYNTAX.string(), sender);
	}

	private static class RemindTimer implements Runnable
	{

		@Override
		public void run()
		{
			Iterator<PendingReminder> iterator = pendingReminders.iterator();
			while (iterator.hasNext())
			{
				PendingReminder pendingReminder = iterator.next();
				if (pendingReminder.time <= System.currentTimeMillis())
				{
					iterator.remove();

					Player player = Bukkit.getPlayer(pendingReminder.playerUUID);
					if (player == null || !player.isOnline())
						return; //Quote from riddle: "You’re not online, tough nuggies. "

					TitleSender.times(player, 10, PlayerSettings.REMINDER_DISPLAYED_TIME_TICKS.integer() ,10);

					if (pendingReminder.author != null)
						TitleSender.subtitle(player, MinecraftJson.getColorMessage("set by " + pendingReminder.author, PlayerSettings.REMINDER_SUBTITLE_COLOR.string()));

					TitleSender.title(player, MinecraftJson.getColorMessage(pendingReminder.message, PlayerSettings.REMINDER_MAIN_COLOR.string()));
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				}
			}
		}
	}

	private int getPendingRemindersForPlayer(Player player)
	{
		int amount = 0;
		for (PendingReminder reminder : pendingReminders)
		{
			if (reminder.author == null && reminder.playerUUID.equals(player.getUniqueId()))
				amount++;
		}

		return amount;
	}

	private void clearRemindersForPlayer(Player player)
	{
		Iterator<PendingReminder> iterator = pendingReminders.iterator();
		while (iterator.hasNext())
		{
			PendingReminder pendingReminder = iterator.next();
			if (pendingReminder.playerUUID == player.getUniqueId())
			{
				iterator.remove();
			}
		}

	}

	private static class PendingReminder
	{
		public UUID playerUUID;
		public long time;
		public String message;
		public String author;
	}
}
