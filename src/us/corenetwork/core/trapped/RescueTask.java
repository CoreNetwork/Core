package us.corenetwork.core.trapped;

import java.util.Collection;
import java.util.List;

import net.minecraft.server.v1_7_R1.Tuple;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import us.corenetwork.core.CLog;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;

public class RescueTask implements Runnable {

	private Player player;
	private Location originalLocation;
	private Tuple claimLocation;
	
	private Material safeMaterial;
	private int maxX;
	private int maxZ;
	private int minX;
	private int minZ;
	private int y;
	
	private int claimLowerX;
	private int claimGreaterX;
	private int claimLowerZ;
	private int claimGreaterZ;
	
	public RescueTask(Player player, Location originalLocation, Tuple claimLocation)
	{
		this.player = player;
		this.originalLocation = originalLocation;
		this.claimLocation = claimLocation;
	}
	
	@Override
	public void run() 
	{
		if (!player.isOnline())
			return;
		
		TrappedPlayers.trappedPlayers.remove(player);
		
		if (player.getLocation().distance(originalLocation) > 1)
		{
			PlayerUtils.Message(TrappedSettings.MESSAGE_RESCUE_PLAYER_MOVED.string(), player);	
			return;
		}
		
		commenceRescue();
	}
	
	private void commenceRescue()
	{
		CLog.debug("[RescueTask] Commencing Rescue!");
		getSafeMaterial(player.getWorld());	
		CLog.debug("[RescueTask] getSafeNaterial complete");
		getWorldBounds(player.getWorld().getName());
		CLog.debug("[RescueTask] getWorldBounds complete");
		normalizeClaimCorners();
		CLog.debug("[RescueTask] normalizingClaimCorners complete");		
		Location targetLocation = getRandomSafeLocation();
		CLog.debug("[RescueTask] getRandomSafeLocation complete");
		clearTargetLocation(targetLocation);
		CLog.debug("[RescueTask] clearingTargetLocation complete");
		teleportTo(targetLocation);
		CLog.debug("[RescueTask] teleportTo complete");
	}
	

	private void getSafeMaterial(World world)
	{
		if (world.getEnvironment() == Environment.NETHER)
			safeMaterial = Material.NETHERRACK;
		else 
			safeMaterial = Material.GRASS;
	}
	private void getWorldBounds(String worldName)
	{
		maxX = getLimit(worldName, "MaxX");
		minX = getLimit(worldName, "MinX");
		maxZ = getLimit(worldName, "MaxZ");
		minZ = getLimit(worldName, "MinZ");
		y = (Integer) TrappedModule.instance.config.get("Trapped.Surface." + worldName);
	}
	
	private int getLimit(String worldName, String limitType)
	{
		Integer limit = (Integer) TrappedModule.instance.config.get("Trapped.Limits." + worldName + "." + limitType);
		if (limit == null)
			limit = (Integer) TrappedModule.instance.config.get("Trapped.Limits.Other." + limitType);
		
		return limit;
	}
	
	private void normalizeClaimCorners()
	{
		int clX = ((Location)claimLocation.a()).getBlockX();
		int clZ = ((Location)claimLocation.a()).getBlockZ();
		int cgX = ((Location)claimLocation.b()).getBlockX();
		int cgZ = ((Location)claimLocation.b()).getBlockZ();
		
		CLog.debug(clX + " " + clZ + " " + cgX + " " + cgZ);
		
		claimLowerX = (clX > maxX) ? maxX : clX;
		claimLowerZ = (clZ > maxZ) ? maxZ : clZ;
		claimGreaterX = (cgX > maxX) ? maxX : cgX;
		claimGreaterZ = (cgZ > maxZ) ? maxZ : cgZ;
		
		claimLowerX = (clX < minX) ? minX : claimLowerX;
		claimLowerZ = (clZ < minZ) ? minZ : claimLowerZ;
		claimGreaterX = (cgX < minX) ? minX : claimGreaterX;
		claimGreaterZ = (cgZ < minZ) ? minZ : claimGreaterZ;
		
		CLog.debug(claimLowerX + " " + claimLowerZ + " " + claimGreaterX + " " + claimGreaterZ);
	}
	
	private Location getRandomSafeLocation()
	{
		boolean found = false;
		int border = 20;
		int counter = 0;
		Location newLocation = null;
		
		while(found == false)
		{
			counter++;
			
			if(counter == 50)
			{
				border += (border < 8000) ? border : 0;
				counter = 0;
			}
			
			//We choose which rectangle player will spawn
			// -------------
			// |____1____|  |
			// |  |CLAIM | 2|
			// | 0|______|__|
			// |__|____3____|
			//
			int xx = 0;
			int zz = 0;
			
			int whichOne = CorePlugin.random.nextInt(4);
			switch (whichOne) {
			case 0:
				xx = claimLowerX - (CorePlugin.random.nextInt(border) + 1);
				zz = claimLowerZ - border + CorePlugin.random.nextInt(claimGreaterZ - claimLowerZ + border);
				break;
			case 1:
				xx = claimLowerX - border + CorePlugin.random.nextInt(claimGreaterX - claimLowerX + border);
				zz = claimGreaterZ + (CorePlugin.random.nextInt(border) + 1);
				break;
			case 2:
				xx = claimGreaterX + (CorePlugin.random.nextInt(border) + 1);
				zz = claimLowerZ + CorePlugin.random.nextInt(claimGreaterZ - claimLowerZ + border);
				break;
			case 3:
				xx = claimLowerX + CorePlugin.random.nextInt(claimGreaterX - claimLowerX + border);
				zz = claimLowerZ - (CorePlugin.random.nextInt(border) + 1);
				break;
			default:
				break;
			}
			
			newLocation = new Location(player.getWorld(), xx + 0.5, y, zz + 0.5);
			
			if(isAcceptable(newLocation))
			{
				found = true;
			}
		}
		
		CLog.debug(newLocation.getBlockX() + " " + newLocation.getBlockY() + " " + newLocation.getBlockZ());
		return newLocation;
	}
	
	private boolean isAcceptable(Location location)
	{
		if (GriefPreventionHandler.canBuildAt(player, location) == false)			
			return false;
		
		Block block = location.getBlock();

		if (!block.getChunk().isLoaded())
			block.getChunk().load();
		
		if (!isEmpty(block))
			return false;
		
		Block belowBlock = block.getRelative(BlockFace.DOWN);
		if (belowBlock == null || belowBlock.getType() != safeMaterial)
			return false;
		
		Block aboveBlock = block.getRelative(BlockFace.UP);
		if (aboveBlock == null || !aboveBlock.isEmpty())
			return false;
		
		return true;
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
	
	private void clearTargetLocation(Location location)
	{
		int radiusSquared = TrappedSettings.CLEAR_RADIUS.integer()*TrappedSettings.CLEAR_RADIUS.integer();
	
		Collection<LivingEntity> monsters = player.getWorld().getEntitiesByClass(LivingEntity.class);
		for (LivingEntity entity : monsters)
		{
			if (entity instanceof Monster || entity instanceof Slime)
			{
				int distance = Util.flatDistance(player.getLocation(), entity.getLocation());
				if (distance < radiusSquared)
				{
					entity.remove();
				}
			}	
		}
	}
	
	private void teleportTo(Location location)
	{
		
		Chunk c = location.getChunk();
		if (!c.isLoaded())
		{
			location.getChunk().load();
		}
		Entity vehicle = player.getVehicle();
		
		if (vehicle == null)
		{
			player.teleport(location);
		}
		else
		{
			vehicle.eject();
			vehicle.teleport(location);
			player.teleport(location);
			vehicle.setPassenger(player);
		}
	}
}


