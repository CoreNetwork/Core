package us.corenetwork.core.corecommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.Setting;
import us.corenetwork.core.Settings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import us.corenetwork.core.util.TitleSender;
import us.corenetwork.core.util.Util;

public class ShutdownCommand extends BaseCoreCommand {
    protected long shutdownTime = 0;
    private int requestedTime = 0;
    private List<Integer> tasks = new ArrayList<Integer>();
    private String kickMessage;
    private static final int INTERVAL[] = {
            1, 2, 3, 4, 5, 10, 30, 60, 120, 180, 240, 300, 600, 900, 1800, 3600
    };
    private static boolean[] messagesSent = new boolean[INTERVAL.length];
    private final ShutdownTimer TIMER_INSTANCE = new ShutdownTimer();

    public ShutdownCommand() {
        desc = "Shutdown with grace period";
        needPlayer = false;
        permission = "shutdown";
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args[0].equals("cancel")) {
                cancelAllTasks();
                shutdownTime = 0;
                sender.sendMessage("Scheduled shutdown cancelled.");
            } else {
                if (!tasks.isEmpty()) {
                    sender.sendMessage("Another shutdown was scheduled for " + formatTime(shutdownTime, false) + " and has been cancelled.");
                    cancelAllTasks();
                }
                int time;
                try {
                    time = Integer.valueOf(args[0]);
                    shutdownTime = System.currentTimeMillis() + time * 1000;
                    requestedTime = time;
                } catch (NumberFormatException e) {
                    sender.sendMessage("Invalid number " + args[0]);
                    return;
                }
                kickMessage = Settings.getString(Setting.MESSAGE_DEFAULT_KICK_ON_REBOOT);

                if (args.length > 1) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        builder.append(args[i]).append(' ');
                    }
                    kickMessage = builder.toString();
                }

                sender.sendMessage("Shutdown scheduled in " + formatTime(shutdownTime, true) + ".");

                for (int i = 0; i < messagesSent.length; i++)
                    messagesSent[i] = false;

                sendNotifications();
                scheduleNext();
            }
        } else {
            sender.sendMessage("No shutdown time specified.");
        }
    }

    private class ShutdownTimer implements Runnable {
        @Override
        public void run() {
            int remaining = (int) Math.ceil((shutdownTime - System.currentTimeMillis()) / 1000d);

            if (remaining <= 0)
            {
                shutdown();
                return;

            }

            for (int i = INTERVAL.length - 1; i >= 0; i--)
            {
                int curInterval = INTERVAL[i];
                if (curInterval >= remaining && curInterval <= requestedTime && !messagesSent[i])
                {
                    sendNotifications();
                    messagesSent[i] = true;
                    break;
                }
            }

            scheduleNext();
        }
    }

    private void scheduleNext()
    {
        tasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(CorePlugin.instance, TIMER_INSTANCE, 20));
    }

    private void shutdown()
    {
        Bukkit.broadcastMessage("Reboot.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(kickMessage);
        }
        Bukkit.shutdown();
    }

    private void sendNotifications()
    {
        String text = "Reboot in " + formatTime(shutdownTime, true);
        String json = "{text: '" + text + "', color: gold}";
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (shutdownTime - System.currentTimeMillis() < 10000) {
                TitleSender.times(player, 5, 10, 5);
                TitleSender.title(player, json);
            } else {
                TitleSender.times(player, 5, 50, 5);
                TitleSender.subtitle(player, json);
                TitleSender.title(player, "{text: ''}");
            }
        }
        CorePlugin.instance.getLogger().info("Shutdown in " + formatTime(shutdownTime, true));

    }


    private static String formatTime(long shutdownTime, boolean relative) {
        if (relative) {
            long remaining = (long) Math.ceil((shutdownTime - System.currentTimeMillis()) / 1000d);
            if (remaining < 60) {
                return remaining + "s";
            } else if (remaining < 60 * 60) {
                return remaining / 60 + "m";
            } else {
                return remaining / (60 * 60) + "h";
            }
        } else {
            return new Date(shutdownTime).toString();
        }
    }

    private void cancelAllTasks() {
        for (Integer taskId : tasks) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        tasks.clear();
    }

    private void cancelTask(int id) {
        Bukkit.getScheduler().cancelTask(id);
        tasks.remove(id);
    }

    private Timer timer = new Timer();
}
