package us.corenetwork.core.player.commands;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;
import us.corenetwork.core.player.PlayerModule;
import us.corenetwork.core.player.PlayerSettings;

public class DelayCommand extends BasePlayerCommand {

	private static HashMap<UUID, InterHelper> frozenPlayers = new HashMap<UUID, InterHelper>();

	public DelayCommand()
	{
		desc = "Direct message player";
		permission = "delay";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		if (args.length < 2 || !Util.isInteger(args[0]))
		{
			PlayerUtils.Message("Usage: /delay <seconds> [<frozen>] [<player>] <command> [<command's params>]", sender);
			return;
		}

		int seconds = Integer.parseInt(args[0]);
		final String messageNode = args[1];
		final boolean freeze = PlayerModule.instance.config.contains("Message.FrozenMessages." + messageNode + ".Start");

		if (freeze && args.length < 4)
		{
			PlayerUtils.Message("Usage: /delay <seconds> <frozen message node> <player> <command> [<command's params>]", sender);
		}

		int commandStart = freeze ? 3 : 1;

		if (args[commandStart].startsWith("/"))
			args[commandStart] = args[commandStart].substring(1);

		String commandToRun = "";
		for (int i = commandStart; i < args.length; i++)
			commandToRun = commandToRun.concat(args[i]).concat(" ");
		commandToRun = commandToRun.substring(0, commandToRun.length() - 1);

		final Player player;

		if (freeze)
		{
			String playerName = args[2];
			player = Bukkit.getPlayerExact(playerName);
			if (player == null)
				return;


			String startMessage = PlayerModule.instance.config.getString("Message.FrozenMessages." + messageNode + ".Start");
			startMessage = startMessage.replace("<Time>", Integer.toString(seconds));
			if (seconds == 1)
				startMessage = startMessage.replace("<PluralS>", "");
			else
				startMessage = startMessage.replace("<PluralS>", "s");

			PlayerUtils.Message(startMessage, player);
		}
		else
		{
			player = null;
		}

		final String commandFinal = commandToRun;
		BukkitTask task = Bukkit.getScheduler().runTaskLater(CorePlugin.instance, new Runnable()
		{
			@Override
			public void run()
			{
				if (freeze)
				{
					if (!frozenPlayers.containsKey(player.getUniqueId()))
						return;

					if (player != null)
						frozenPlayers.remove(player.getUniqueId());
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandFinal);
			}
		}, seconds * 20);

		if(freeze)
		{
			String interruptMessage = PlayerModule.instance.config.getString("Message.FrozenMessages." + messageNode + ".Interrupt");
			frozenPlayers.put(player.getUniqueId(), new InterHelper(task, interruptMessage));
		}
	}

    class InterHelper
    {

        String interruptMessage;
        long time;
		BukkitTask task;
        public InterHelper(BukkitTask task, String msg)
        {
            interruptMessage = msg;
            time = System.currentTimeMillis();
			this.task = task;
        }
    }

	public static void playerMoved(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
        InterHelper interHelper = frozenPlayers.get(player.getUniqueId());
        if(interHelper != null)
        {
            if (interHelper.interruptMessage != null && (System.currentTimeMillis() - interHelper.time)/50 > PlayerSettings.DELAY_GRACE_PERIOD_TICKS.integer())
            {
                boolean stayedStill =  event.getFrom().getBlockX() == event.getTo().getBlockX()
									&& event.getFrom().getBlockY() == event.getTo().getBlockY()
									&& event.getFrom().getBlockZ() == event.getTo().getBlockZ();

                if (!stayedStill)
                {
                    PlayerUtils.Message(interHelper.interruptMessage, player);
                    frozenPlayers.remove(player.getUniqueId());
					interHelper.task.cancel();
                }
            }
        }
	}
}
