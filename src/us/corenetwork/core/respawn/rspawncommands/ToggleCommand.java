package us.corenetwork.core.respawn.rspawncommands;

import java.util.HashSet;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.respawn.RespawnSettings;

public class ToggleCommand extends BaseRSpawnCommand {	
	public static HashSet<String> ignoredPlayers = new HashSet<String>();
	
	public ToggleCommand()
	{
		needPlayer = true;
		permission = "togglespawn";
	}

	public void run(final CommandSender sender, String[] args) {
		String playerName = ((Player) sender).getName();
		
		if (ignoredPlayers.contains(playerName))
		{
			ignoredPlayers.remove(playerName);
			PlayerUtils.Message(RespawnSettings.MESSAGE_SPAWN_UNIGNORED.string(), sender);
		}
		else
		{
			ignoredPlayers.add(playerName);
			PlayerUtils.Message(RespawnSettings.MESSAGE_SPAWN_IGNORED.string(), sender);
		}
		
		return;
	}	
}
