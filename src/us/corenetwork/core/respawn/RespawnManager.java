package us.corenetwork.core.respawn;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import us.corenetwork.core.CLog;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.respawn.rspawncommands.ToggleCommand;

public class RespawnManager {

	public void respawnPlayer(Player player)
	{
		Location location; 
		if(RespawnModule.teamManager.isInTeam(player))
		{
			location = RespawnModule.teamManager.getTeamRespawnLocation();
			RespawnModule.teamManager.playerSpawned(player);
		}
		else
		{
			location = getRespawnLocation(player);
		}
		PlayerUtils.safeTeleport(player, location);
	}
	
	public Location getRespawnLocation(Player player)
	{
		int minX, minZ, maxX, maxZ;
		
		if (isLucky(player))
		{
			Location biggestClaim = GriefPreventionHandler.findBiggestClaimInWorld(player.getName(), RespawnSettings.RESPAWN_WORLD.string());
			
			minX = Math.max(RespawnSettings.RESPAWN_BASE_MIN_X.integer(), biggestClaim.getBlockX() - 2500);
			maxX = Math.min(RespawnSettings.RESPAWN_BASE_MAX_X.integer(), biggestClaim.getBlockX() + 2500);
			minZ = Math.max(RespawnSettings.RESPAWN_BASE_MIN_Z.integer(), biggestClaim.getBlockZ() - 2500);
			maxZ = Math.min(RespawnSettings.RESPAWN_BASE_MAX_Z.integer(), biggestClaim.getBlockZ() + 2500);
		}
		else
		{
			minX = RespawnSettings.RESPAWN_NO_BASE_MIN_X.integer();
			minZ = RespawnSettings.RESPAWN_NO_BASE_MIN_Z.integer();
			maxX = RespawnSettings.RESPAWN_NO_BASE_MAX_X.integer();
			maxZ = RespawnSettings.RESPAWN_NO_BASE_MAX_Z.integer();
		}

		Location location = getRandomLocation(player, minX, maxX, minZ, maxZ);
		
		return location;
	}
	
	
	private boolean isLucky(Player player)
	{
		if(ToggleCommand.ignoredPlayers.contains(player.getName()))
		{
			ToggleCommand.ignoredPlayers.remove(player.getName());
			return false;
		}
		else
		{
			return throwDice(player);
		}
	}
	
	
	private boolean throwDice(Player player)
	{
		ArrayList<String> groupList = (ArrayList<String>) RespawnSettings.LUCKY100.list();
		for(String permGroup : groupList)
			if (CorePlugin.permission.playerInGroup(player, permGroup))
				return true;
		
		groupList = (ArrayList<String>) RespawnSettings.LUCKY66.list();
		for(String permGroup : groupList)
			if (CorePlugin.permission.playerInGroup(player, permGroup))
				return CorePlugin.random.nextInt(100) < 66;
		
		return CorePlugin.random.nextInt(100) < 33;
	}

	private Location getRandomLocation(Player player, int minX, int maxX, int minZ, int maxZ)
	{
		World overworld = Bukkit.getWorlds().get(0);

		Deque<Location> claims = GriefPreventionHandler.getAllClaimsInWorld(RespawnSettings.RESPAWN_WORLD.string());	
		
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

			Location location = new Location(overworld, x + 0.5, y, z + 0.5);
			Block block = location.getBlock();

			if (!block.getChunk().isLoaded())
				block.getChunk().load();
			
			if (!isEmpty(block))
				continue;
			
			Block belowBlock = block.getRelative(BlockFace.DOWN);
			if (belowBlock == null || belowBlock.getType() != Material.GRASS)
				continue;
			
			Block aboveBlock = block.getRelative(BlockFace.UP);
			if (aboveBlock == null || !aboveBlock.isEmpty())
				continue;
			
			int smallestDist = getSmallestDistance(claims, location);	
			if (smallestDist < range)
				continue;
			
			return location;
		}
	}

	private boolean isEmpty(Block block)
	{
		for (int i = 1; i < 13; i++)
		{
			Block aboveBlock = block.getRelative(BlockFace.UP, i);
			if (aboveBlock != null && !aboveBlock.isEmpty())
				return false;
		}
		return true;
	}

	private static int getSmallestDistance(Deque<Location> checkLocations, Location point)
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
