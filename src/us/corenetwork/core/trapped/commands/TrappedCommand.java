package us.corenetwork.core.trapped.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.LocationTuple;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.trapped.RescueTask;
import us.corenetwork.core.trapped.TrappedPlayers;
import us.corenetwork.core.trapped.TrappedSettings;

public class TrappedCommand extends BaseTrappedCommand {
	
		public TrappedCommand()
		{
			desc = "Escape if you are stuck";
			permission = "trapped";
			needPlayer = true;
		}


		public void run(final CommandSender sender, String[] args) 
		{
			Player player = (sender instanceof Player) ? (Player) sender : null;
			LocationTuple claimLocationTuple = GriefPreventionHandler.getExactClaimAt(player.getLocation());

			if (TrappedPlayers.trappedPlayers.contains(player))
			{
				return;
			}
			
			if (TrappedSettings.ENABLED_WORLDS.list().contains(player.getWorld().getName()) == false)
			{
				PlayerUtils.Message(TrappedSettings.MESSAGE_NOT_ENABLED_IN.string(), sender);
				return;
			}
			
			if (GriefPreventionHandler.canBuildAt(player, player.getLocation()))
			{
				PlayerUtils.Message(TrappedSettings.MESSAGE_CAN_BUILD.string(), sender);
				return;
			}
			
			PlayerUtils.Message(TrappedSettings.MESSAGE_RESCUE_IN_PROGRESS.string(), sender);
			RescueTask task = new RescueTask(player, player.getLocation(), claimLocationTuple);
			CorePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(CorePlugin.instance, task, 100L);
		}		

}
