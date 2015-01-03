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
import us.corenetwork.core.player.commands.DelayCommand;

public class PlayerListener implements Listener {



	// listener for cancelling damage on God players and rescuing lagged players from void.
	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			if (PlayerModule.gods.contains(player.getName()))
			{
				event.setCancelled(true);
				return;
			}

			//Make sure players stay frozen
			DelayCommand.playerGotDamaged(player);

			if (event.getCause() == DamageCause.VOID)
			{
				World world = player.getWorld();
				
				int x = player.getLocation().getBlockX();
				int z = player.getLocation().getBlockZ();
				int y = getSafeY(world, x, z);
				
				if(isInBounds(player, x, z, player.getWorld().getName()))
				{
					Location tpLoc = new Location(world, x, y, z);
					tpLoc.setPitch(player.getLocation().getPitch());
					tpLoc.setYaw(player.getLocation().getYaw());
					
					player.teleport(tpLoc);
					event.setCancelled(true);
					return;
				}
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