package us.corenetwork.core.player;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import us.corenetwork.core.util.PlayerUtils;

public class VanishListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		Player player = event.getPlayer();
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		
		for(Player onlinePlayer : onlinePlayers) 
		{
			if(PlayerModule.vanishManager.isVanished(onlinePlayer)) 
			{
				player.hidePlayer(onlinePlayer);
				
				//((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) onlinePlayer).getHandle()));
			}
		}
	}
	
	//Due to some weird bug in #36 disallow drinking milk while vanished
	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event)
	{
		if( PlayerModule.vanishManager.isVanished(event.getPlayer()) && event.getItem().getType().equals(Material.MILK_BUCKET))
		{
			PlayerUtils.Message(PlayerSettings.MESSAGE_DRINK_MILK_VANISHED.string(), event.getPlayer());
			event.setCancelled(true);
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
		final Player player = event.getPlayer();
		if(PlayerModule.vanishManager.isVanished(player) == false) 
		{
			return;
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			switch(event.getClickedBlock().getType()) {
			case TRAPPED_CHEST:
			case CHEST:
				if(player.isSneaking())
					return;
				
				event.setCancelled(true);
				final Chest chest = (Chest)event.getClickedBlock().getState();

				InventoryView iv = new InventoryView() {
					
					@Override
					public InventoryType getType()
					{
						return InventoryType.CHEST;
					}
					
					@Override
					public Inventory getTopInventory()
					{
						return chest.getInventory();
					}
					
					@Override
					public HumanEntity getPlayer()
					{
						return player;
					}
					
					@Override
					public Inventory getBottomInventory()
					{
						return player.getInventory();
					}
				};
				
				player.openInventory(iv);
				
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClickEvent(InventoryClickEvent event) 
	{
		final Player player = (Player) event.getWhoClicked();
		if(PlayerModule.vanishManager.isVanished(player) == false) 
		{
			return;
		}
		if(event.isShiftClick())
		{
			event.setCancelled(true);
		}
	}
}
