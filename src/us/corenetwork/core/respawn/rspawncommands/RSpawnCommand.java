package us.corenetwork.core.respawn.rspawncommands;

import java.util.Deque;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CLog;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.Util;
import us.corenetwork.core.respawn.RespawnSettings;

public class RSpawnCommand extends BaseRSpawnCommand {	
	public RSpawnCommand()
	{
		needPlayer = true;
		permission = "rspawn";
	}

	public Boolean run(final CommandSender sender, String[] args) {

		Player player = (Player) sender;

		int minX = RespawnSettings.RESPAWN_MIN_X.integer();
		int minZ = RespawnSettings.RESPAWN_MIN_Z.integer();
		int maxX = RespawnSettings.RESPAWN_MAX_X.integer();
		int maxZ = RespawnSettings.RESPAWN_MAX_Z.integer();

		Location biggestClaim = null;
		if (!ToggleCommand.ignoredPlayers.contains(player.getName()) && throwDice(player))
			biggestClaim = GriefPreventionHandler.findBiggestClaim(player.getName());

		ToggleCommand.ignoredPlayers.remove(player.getName());

		if (biggestClaim != null)
		{
			minX = Math.max(minX, biggestClaim.getBlockX() - 2500);
			maxX = Math.min(maxX, biggestClaim.getBlockX() + 2500);
			minZ = Math.max(minZ, biggestClaim.getBlockZ() - 2500);
			maxZ = Math.min(maxZ, biggestClaim.getBlockZ() + 2500);
		}

		teleport((Player) sender, minX, maxX, minZ, maxZ);		
		return true;
	}	

	public boolean throwDice(Player player)
	{
		if (Util.hasPermission(player,"mcnsaflatcore.donorsky"))
			return true;
		else if (Util.hasPermission(player,"mcnsaflatcore.donorgrass"))
			return CorePlugin.random.nextInt(100) < 66;
		else
			return CorePlugin.random.nextInt(100) < 33;
	}

	public void teleport(Player player, int minX, int maxX, int minZ, int maxZ)
	{
		World overworld = Bukkit.getWorlds().get(0);

		Deque<Location> claims = GriefPreventionHandler.getAllClaims();	
		
		int counter = 0;
		int range = 40000;

		int xDiff = maxX - minX;
		int zDiff = maxZ - minZ;

		int y = RespawnSettings.TELEPORT_Y.integer();

		while (true)
		{
			counter++;

			if (counter > 100)
			{
				counter = 0;
				range = Math.max(0, range - 3000);

				CLog.warning("Could not found teleport location after 100 tries. Is map overcrowded?s Current range:" + range);

			}

			int x = CorePlugin.random.nextInt(xDiff) + minX;
			int z = CorePlugin.random.nextInt(zDiff) + minZ;

			Location location = new Location(overworld, x, y, z);
			Block block = location.getBlock();

			if (!block.getChunk().isLoaded())
				block.getChunk().load();
			
			Block belowBlock = block.getRelative(BlockFace.DOWN);
			if (belowBlock == null || belowBlock.getType() != Material.GRASS)
				continue;
			
			if (!isEmpty(block))
				continue;

			int smallestDist = getSmallestDistance(claims, location);	
			if (smallestDist < range)
				continue;

			CLog.info("dist " + smallestDist);

			
			Util.safeTeleport(player, location);
			break;
		}
	}

	public boolean isEmpty(Block block)
	{
		for (int i = 1; i < 13; i++)
		{
			Block aboveBlock = block.getRelative(BlockFace.UP, i);
			if (aboveBlock != null && !aboveBlock.isEmpty())
				return false;
		}
		return true;
	}

	public static int getSmallestDistance(Deque<Location> checkLocations, Location point)
	{
		int smallestDist = Integer.MAX_VALUE;

		Iterator<Location> i = checkLocations.iterator();
		while (i.hasNext())
		{
			Location check = i.next();
			int dist = Util.flatDistance(check, point);
			if (dist < smallestDist)
				smallestDist = dist;
		}

		return smallestDist;
	}

}
