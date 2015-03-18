package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import org.bukkit.entity.Slime;
import us.corenetwork.core.respawn.ProtectTimer;
import us.corenetwork.core.respawn.RespawnModule;
import us.corenetwork.core.respawn.RespawnSettings;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;

import java.util.Collection;

public class SpawnCommand extends BaseRSpawnCommand {
	public SpawnCommand()
	{
		needPlayer = false;
		permission = "spawn";
	}

	public void run(final CommandSender sender, String[] args) 
	{
		if (args.length < 1)
			return;


		String playerName = args[0];


		Player player = Bukkit.getServer().getPlayerExact(playerName);
		if (player != null)
		{


			int startTime = player.getStatistic(Statistic.DEATHS) == 1 ? RespawnSettings.SPAWN_PROTECTION_LENGTH_FIRST.integer() : RespawnSettings.SPAWN_PROTECTION_LENGTH.integer();
			ProtectTimer.protectedPlayers.put(playerName, startTime);
			RespawnModule.manager.respawnPlayer(player);
			//Resetting player
			player.setHealth(20.0);
			player.setFoodLevel(20);
			player.setSaturation(20);
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
	}
}
