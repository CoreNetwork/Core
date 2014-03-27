package us.corenetwork.core.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.Inventory;

import us.corenetwork.core.PlayerUtils;

public class VanishListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		Player player = event.getPlayer();
		
		if (PlayerModule.vanishManager.canSeeAll(player))
		{
			PlayerModule.vanishManager.addToSeeAllGroup(player);
			return;
		}
		
		Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();		
		for(Player onlinePlayer : onlinePlayers) 
		{
			if(PlayerModule.vanishManager.isVanished(onlinePlayer)) 
			{
				player.hidePlayer(onlinePlayer);
			}
		}
	}
	

	@EventHandler(ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event) 
	{
		Player player = (event.getTarget() instanceof Player) ? (Player)event.getTarget() : null; 	
		if(player != null && PlayerModule.vanishManager.isVanished(player)) 
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) 
	{
		if(PlayerModule.vanishManager.isVanished(event.getPlayer())) 
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) 
	{
		if(event.getEntity() instanceof Player && PlayerModule.vanishManager.isVanished((Player)event.getEntity())) 
		{
			event.setCancelled(true);
		}
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) 
	{
		Player player = event.getPlayer();
		if(PlayerModule.vanishManager.isVanished(player) == false) 
		{
			return;
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			switch(event.getClickedBlock().getType()) {
			case CHEST:
				// cancel the opening thing
				event.setCancelled(true);
				
				// but actually open it
				final Chest chest = (Chest)event.getClickedBlock().getState();
				
				// create a copy of the chest
				final Inventory i = Bukkit.getServer().createInventory(event.getPlayer(), chest.getInventory().getSize());
				i.setContents(chest.getInventory().getContents());

				// and have the player open that
				event.getPlayer().openInventory(i);
				
				// alert them
				PlayerUtils.Message(PlayerSettings.MESSAGE_OPEN_CHEST_VANISHED.string(), player);
				break;
			case ENDER_CHEST:
				event.setCancelled(true);
				player.openInventory(player.getEnderChest());
				break;
			default:
				break;
			}
		}
		// prevent trampling
		else if(event.getAction() == Action.PHYSICAL && event.getMaterial() == Material.SOIL) 
		{
			event.setCancelled(true);
		}
	}
	
}
