package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.respawn.RespawnModule;
import us.corenetwork.core.respawn.RespawnSettings;

public class RSpawnTeamCommand extends BaseRSpawnCommand {

	
	public RSpawnTeamCommand()
	{
		needPlayer = true;
		permission = "rspawnteam";
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;

		if(RespawnModule.teamManager.isInTeam(player))
		{
			RespawnModule.teamManager.removeFromTeam(player);
		}
		else
		{
			RespawnModule.teamManager.addToTeam(player);
			PlayerUtils.Message(RespawnSettings.MESSAGE_GROUP_JOIN.string(), sender);
		}
	}
	
	
}
