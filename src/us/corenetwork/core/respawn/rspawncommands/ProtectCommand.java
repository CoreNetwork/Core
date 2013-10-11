package us.corenetwork.core.respawn.rspawncommands;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.respawn.ProtectTimer;
import us.corenetwork.core.respawn.RespawnSettings;

public class ProtectCommand extends BaseRSpawnCommand {	

	public ProtectCommand()
	{
		needPlayer = false;
		permission = "protect";
	}

	public Boolean run(final CommandSender sender, String[] args) {
		if (args.length < 1)
			return true;
		
		int startTime = RespawnSettings.SPAWN_PROTECTION_LENGTH.integer();
		String playerName = args[0];
		ProtectTimer.protectedPlayers.put(playerName, startTime);
		
		Player player = Bukkit.getServer().getPlayerExact(playerName);
		if (player != null)
		{
			//Resetting player
			player.setHealth(20);
			player.setFoodLevel(20);
			player.setSaturation(20);
			player.setExp(0);
			player.setLevel(0);
			for (int i =0; i < player.getInventory().getSize() + 4; i++)
				player.getInventory().setItem(i, null);
			
			//Mob removal
			int removalRadiusSquared = RespawnSettings.MOB_REMOVAL_RADIUS_SQUARED.integer();
			
			Collection<LivingEntity> monsters = player.getWorld().getEntitiesByClass(LivingEntity.class);
			for (LivingEntity entity : monsters)
			{
				if (entity instanceof Monster || entity instanceof Slime)
				{
					int distance = Util.flatDistance(player.getLocation(), entity.getLocation());
					if (distance < removalRadiusSquared)
					{
						entity.remove();
					}
				}
				
			}
			
			//Announcing to player
			String message = RespawnSettings.MESSAGE_SPAWN_PROTECTION_START.string();
			message = message.replace("<Time>", Integer.toString(startTime));
			PlayerUtils.Message(message, player);
		}
		
		return true;
	}	
	
	
}
