package us.corenetwork.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimArray;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class GriefPreventionHandler {	
	public static Deque<Location> getAllClaimsInWorld(String worldName)
	{
		ArrayDeque<Location> list = new ArrayDeque<Location>();
		ClaimArray ca = GriefPrevention.instance.dataStore.getClaimArray();
		for (int i = 0; i < ca.size(); i++)
		{
			Claim claim = ca.get(i);
			
			if(claim.getClaimWorldName() != worldName)
				continue;
			
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
	
	public static Location findBiggestClaimInWorld(String player, String worldName)
	{
		ClaimArray ca = GriefPrevention.instance.dataStore.getClaimArray();
		
		Claim biggest = null;
		int biggestSize = 0;
		for (int i = 0; i < ca.size(); i++)
		{
			Claim claim = ca.get(i);
			
			if (!claim.getOwnerName().equals(player))
				continue;
			
			if (claim.getClaimWorldName().equals(worldName) == false)
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

	public static LocationTuple getExactClaimAt(Location location)
	{
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false);
		if(claim == null)
			return null;
		else
		{
			Location locLow = claim.getLesserBoundaryCorner();
			Location locGr = claim.getGreaterBoundaryCorner();
			return new LocationTuple(locLow, locGr);
			
		}
		
	}
	
	public static boolean canBuildAt(Player player, Location location)
	{
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false);
		return claim == null || claim.allowBuild(player) == null; 
	}
	
	public static List<ClaimSimple> getPlayerClaimsSimple(Player player, String worldName)
	{
		List<ClaimSimple> listOfClaimSimple = new ArrayList<ClaimSimple>();
				
		//TODO UUID Switch!
		for(Claim c : GriefPrevention.instance.dataStore.getPlayerData(player.getName()).claims)
		{
			if(c.getClaimWorldName().equalsIgnoreCase(worldName))
			{
				int width = c.getWidth();
				width = width*width;
				int area = c.getArea();
				listOfClaimSimple.add(new ClaimSimple(c.getGreaterBoundaryCorner(), area, area==width));
			}
		}

		return listOfClaimSimple;
	}
	
	public static ClaimBlocks getPlayerClaimBlocks(Player player)
	{
		PlayerData pd = GriefPrevention.instance.dataStore.getPlayerData(player.getName());
		int accrued = pd.accruedClaimBlocks;
		int bonus = pd.bonusClaimBlocks;
		int rank = GriefPrevention.instance.dataStore.getGroupBonusBlocks(player.getName());
		int remaining = pd.getRemainingClaimBlocks();
		
		return new ClaimBlocks(accrued, bonus, rank, remaining);
	}

	public static void addBonusClaimBlocks(Player player, Integer toAdd)
	{
		PlayerData pd = GriefPrevention.instance.dataStore.getPlayerData(player.getName());
		pd.bonusClaimBlocks += toAdd;
		GriefPrevention.instance.dataStore.savePlayerData(player.getName(), pd);
	}
}
