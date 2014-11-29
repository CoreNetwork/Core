package us.corenetwork.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class GriefPreventionHandler {	
	public static Deque<Location> getAllClaimsInWorld(String worldName)
	{
		ArrayDeque<Location> list = new ArrayDeque<Location>();
		

		
		ArrayList<Claim> ca = new ArrayList<Claim>();
		try
		{
			Field privateField = DataStore.class.getDeclaredField("claims");
			privateField.setAccessible(true);
			ca = (ArrayList<Claim>) privateField.get(GriefPrevention.instance.dataStore);
		} catch (Exception e)
		{
			CLog.severe("Reflection error, blah.");
			e.printStackTrace();
		}
		
		for (int i = 0; i < ca.size(); i++)
		{
			Claim claim = ca.get(i);
			
			if(claim.getLesserBoundaryCorner().getWorld().getName().equalsIgnoreCase(worldName) == false)
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
		
	public static boolean playerHasClaim(Player player)
	{
		return GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).getClaims().size() != 0;
	}
	
	public static Location findBiggestClaimInWorld(Player player, String worldName)
	{
		Vector<Claim> playerClaims = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).getClaims();
		
		Claim biggest = null;
		int biggestSize = 0;
		for (int i = 0; i < playerClaims.size(); i++)
		{
			Claim claim = playerClaims.get(i);
			
			if (!claim.getOwnerName().equals(player))
				continue;
			
			if (claim.getLesserBoundaryCorner().getWorld().getName().equals(worldName) == false)
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
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
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
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
		return claim == null || claim.allowBuild(player, Material.STONE) == null; 
	}
	
	public static List<ClaimSimple> getPlayerClaimsSimple(Player player, String worldName)
	{
		List<ClaimSimple> listOfClaimSimple = new ArrayList<ClaimSimple>();
				
		for(Claim c : GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).getClaims())
		{
			if(c.getLesserBoundaryCorner().getWorld().getName().equalsIgnoreCase(worldName))
			{
				int width = c.getWidth();
				width = width*width;
				int area = c.getArea();
				listOfClaimSimple.add(new ClaimSimple(c.getGreaterBoundaryCorner(), area, area==width && area == 81));
			}
		}

		return listOfClaimSimple;
	}
	
	public static ClaimBlocks getPlayerClaimBlocks(Player player)
	{
		PlayerData pd = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());
		int accrued = pd.getAccruedClaimBlocks();
		int bonus = pd.getBonusClaimBlocks();
		
		Integer returnValue = new Integer(0);
		
		Method privateStringMethod = null;
		try
		{
			privateStringMethod = DataStore.class.getDeclaredMethod("getGroupBonusBlocks", UUID.class);
			privateStringMethod.setAccessible(true);
			returnValue = (Integer) privateStringMethod.invoke(GriefPrevention.instance.dataStore, player.getUniqueId());
		} catch (Exception e)
		{
			CLog.severe("Reflection error, blah.");
			e.printStackTrace();
		}
		int rank = returnValue;
		int remaining = pd.getRemainingClaimBlocks();
		
		return new ClaimBlocks(accrued, bonus, rank, remaining);
	}

	public static void addBonusClaimBlocks(Player player, Integer toAdd)
	{
		PlayerData pd = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());
		pd.setBonusClaimBlocks(pd.getBonusClaimBlocks() + toAdd);
		GriefPrevention.instance.dataStore.savePlayerData(player.getUniqueId(), pd);
	}
}
