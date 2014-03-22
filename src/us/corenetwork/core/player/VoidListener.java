package us.corenetwork.core.player;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import us.corenetwork.core.PlayerUtils;

public class VoidListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.VOID && event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			World world = player.getWorld();
			
			int x = player.getLocation().getBlockX();
			int z = player.getLocation().getBlockZ();
			int y = getSafeY(world, x, z);
			
			if(isInBounds(player, x, z, player.getWorld().getName()))
			{
				Location tpLoc = new Location(world, x, y, z);
				tpLoc.setPitch(player.getLocation().getPitch());
				tpLoc.setYaw(player.getLocation().getYaw());
				
				PlayerUtils.safeTeleport(player, tpLoc);
				event.setCancelled(true);
			}
		}
	}
	
	private boolean isInBounds(CommandSender sender, int x, int z, String world)
	{
		Integer border = (Integer) PlayerModule.instance.config.get("Border." + world);
		if (border == null)
			border = (Integer) PlayerModule.instance.config.get("Border.Other");
		
		if (x > getLimit(world, "MaxX") - border)
		{
			return false;
		}
		if (x < getLimit(world, "MinX") + border)
		{
			return false;
		}
		
		if (z > getLimit(world, "MaxZ") - border)
		{
			return false;
		}
		if (z < getLimit(world, "MinZ") + border)
		{
			return false;
		}
		
		return true;
	}
	
	private int getLimit(String world_name, String limitType)
	{
		Integer limit = (Integer) PlayerModule.instance.config.get("Limits." + world_name + "." + limitType);
		if (limit == null)
			limit = (Integer) PlayerModule.instance.config.get("Limits.Other." + limitType);
		
		return limit ;
	}
	
	private int getSafeY(World world, int x, int z)
	{
		if (world.getEnvironment() != Environment.NETHER && world != null)
			return world.getHighestBlockYAt(x, z);
		else
			return PlayerSettings.NETHER_SURFACE_Y.integer();
	}
}