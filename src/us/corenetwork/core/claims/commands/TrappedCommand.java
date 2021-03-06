package us.corenetwork.core.claims.commands;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.LocationTuple;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.claims.ClaimsPlayers;
import us.corenetwork.core.claims.ClaimsSettings;
import us.corenetwork.core.claims.RescueTask;

public class TrappedCommand extends BaseClaimsCommand {
	
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

			if (ClaimsPlayers.trappedPlayers.contains(player))
			{
				return;
			}
			
			if (ClaimsSettings.ENABLED_WORLDS.list().contains(player.getWorld().getName()) == false)
			{
				PlayerUtils.Message(ClaimsSettings.MESSAGE_NOT_ENABLED_IN.string(), sender);
				return;
			}

			LocationTuple claimTuple = GriefPreventionHandler.getExactClaimAt(player.getLocation());

			//In gp claim
			if(claimTuple != null)
			{
				//gp admin claim
				if(GriefPreventionHandler.isAdminClaim(player.getLocation()))
				{
					PlayerUtils.Message(ClaimsSettings.MESSAGE_CANNOT_BE_RESCURED_FROM_HERE.string(), sender);
					return;
				}

				//trusted claim
				if (GriefPreventionHandler.canBuildAt(player, player.getLocation()))
				{
					//in WG region with build rights
					if(WorldGuardPlugin.inst().canBuild(player, player.getLocation()))
					{
						PlayerUtils.Message(ClaimsSettings.MESSAGE_CAN_BUILD.string(), sender);
						return;
					}
				}
			}
			//Not in gp claim
			else
			{
				//in WG region with build rights
				if(WorldGuardPlugin.inst().canBuild(player, player.getLocation()))
				{
					PlayerUtils.Message(ClaimsSettings.MESSAGE_CAN_BUILD.string(), sender);
					return;
				}
				else
				{
					PlayerUtils.Message(ClaimsSettings.MESSAGE_CANNOT_BE_RESCURED_FROM_HERE.string(), sender);
					return;
				}
			}

			if(GriefPreventionHandler.isAdminClaim(player.getLocation()) ||
			  (GriefPreventionHandler.getExactClaimAt(player.getLocation()) == null && !WorldGuardPlugin.inst().canBuild(player, player.getLocation())))
			{
				PlayerUtils.Message(ClaimsSettings.MESSAGE_CANNOT_BE_RESCURED_FROM_HERE.string(), sender);
				return;
			}

			if (GriefPreventionHandler.canBuildAt(player, player.getLocation()))
			{
				if(WorldGuardPlugin.inst().canBuild(player, player.getLocation()) && GriefPreventionHandler.getExactClaimAt(player.getLocation()) != null)
				{
					PlayerUtils.Message(ClaimsSettings.MESSAGE_CAN_BUILD.string(), sender);
					return;
				}
			}
			PlayerUtils.Message(ClaimsSettings.MESSAGE_RESCUE_IN_PROGRESS.string(), sender);
			RescueTask task = new RescueTask(player, player.getLocation(), claimLocationTuple);
			CorePlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(CorePlugin.instance, task, 100L);
		}		

}
