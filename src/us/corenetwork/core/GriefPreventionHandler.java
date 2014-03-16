package us.corenetwork.core;

import java.util.ArrayDeque;
import java.util.Deque;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimArray;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.minecraft.server.v1_7_R1.Tuple;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class GriefPreventionHandler {	
	public static Deque<Location> getAllClaims()
	{
		ArrayDeque<Location> list = new ArrayDeque<Location>();
		ClaimArray ca = GriefPrevention.instance.dataStore.getClaimArray();
		for (int i = 0; i < ca.size(); i++)
		{
			Claim claim = ca.get(i);
			
			int claimMinX = Math.min(claim.getLesserBoundaryCorner().getBlockX(), claim.getGreaterBoundaryCorner().getBlockX());
			int claimMinZ = Math.min(claim.getLesserBoundaryCorner().getBlockZ(), claim.getGreaterBoundaryCorner().getBlockZ());
			int claimSizeX = Math.abs(claim.getLesserBoundaryCorner().getBlockX() - claim.getGreaterBoundaryCorner().getBlockX());
			int claimSizeZ = Math.abs(claim.getLesserBoundaryCorner().getBlockZ() - claim.getGreaterBoundaryCorner().getBlockZ());

			int claimCenterX = claimMinX + claimSizeX / 2;
			int claimCenterZ = claimMinZ + claimSizeZ / 2;
			
			Location center = new Location(claim.getLesserBoundaryCorner().getWorld(), claimCenterX, 0, claimCenterZ);
			
			list.addLast(center);
		}
		
		return list;
	}
		
	public static boolean playerHasClaim(String player)
	{
		ClaimArray ca = GriefPrevention.instance.dataStore.getClaimArray();
		for (int i = 0; i < ca.size(); i++)
		{
			Claim claim = ca.get(i);

			if (claim.getOwnerName().equals(player))
					return true;			
		}
		
		return false;
	}
	
	public static Location findBiggestClaim(String player)
	{
		ClaimArray ca = GriefPrevention.instance.dataStore.getClaimArray();
		
		Claim biggest = null;
		int biggestSize = 0;
		for (int i = 0; i < ca.size(); i++)
		{
			Claim claim = ca.get(i);
			
			if (!claim.getOwnerName().equals(player))
					continue;
			
			if (biggestSize >= claim.getArea())
				continue;
			
			biggest = claim;
			biggestSize = claim.getArea();
			
		}
				
		if (biggest == null)
			return null;
		
		int claimMinX = Math.min(biggest.getLesserBoundaryCorner().getBlockX(), biggest.getGreaterBoundaryCorner().getBlockX());
		int claimMinZ = Math.min(biggest.getLesserBoundaryCorner().getBlockZ(), biggest.getGreaterBoundaryCorner().getBlockZ());
		int claimSizeX = Math.abs(biggest.getLesserBoundaryCorner().getBlockX() - biggest.getGreaterBoundaryCorner().getBlockX());
		int claimSizeZ = Math.abs(biggest.getLesserBoundaryCorner().getBlockZ() - biggest.getGreaterBoundaryCorner().getBlockZ());

		int claimCenterX = claimMinX + claimSizeX / 2;
		int claimCenterZ = claimMinZ + claimSizeZ / 2;
		
		Location center = new Location(biggest.getLesserBoundaryCorner().getWorld(), claimCenterX, 0, claimCenterZ);
		
		return center;
	}

	public static Tuple getExactClaimAt(Location location)
	{
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false);
		if(claim == null)
			return null;
		else
		{
			Location locLow = claim.getLesserBoundaryCorner();
			Location locGr = claim.getGreaterBoundaryCorner();
			return new Tuple(locLow, locGr);
		}
		
	}
	
	public static boolean canBuildAt(Player player, Location location)
	{
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false);
		return claim == null || claim.allowBuild(player) == null; 
	}
}
