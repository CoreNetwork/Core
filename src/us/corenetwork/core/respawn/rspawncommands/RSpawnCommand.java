package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.respawn.RespawnModule;

public class RSpawnCommand extends BaseRSpawnCommand {	
	public RSpawnCommand()
	{
		needPlayer = true;
		permission = "rspawn";
	}

	public void run(final CommandSender sender, String[] args) 
	{
		Player player = (Player) sender;
		RespawnModule.manager.respawnPlayer(player);
	}
}
