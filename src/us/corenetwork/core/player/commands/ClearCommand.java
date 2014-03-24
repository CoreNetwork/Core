package us.corenetwork.core.player.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.player.PlayerSettings;

public class ClearCommand extends BasePlayerCommand {
	
	public ClearCommand()
	{
		desc = "Clear player inventory."; 
		permission = "clear";
		needPlayer = false;
	}


	public void run(final CommandSender sender, String[] args) 
	{
		Player p = null;
		if(args.length == 1)
		{
			p = CorePlugin.instance.getServer().getPlayerExact(args[0]);
		}
		else if (sender instanceof Player && args.length == 0)
		{
			p = (Player) sender;
		}
		else
		{
			PlayerUtils.Message("Usage: /clear <player>", sender);
			return;
		}
		
		if(p == null)
		{
			PlayerUtils.Message("Player not found", sender);
			return;
		}
		
		clearInventory(p);
		if(args.length == 0)
			PlayerUtils.Message(PlayerSettings.MESSAGE_SELF_CLEARED.string(), sender);
		else
			PlayerUtils.Message(PlayerSettings.MESSAGE_PLAYER_CLEARED.string().replace("<Player>", p.getName()), sender);
		
	}
	
	private void clearInventory(Player p)
	{
		p.setItemOnCursor(new ItemStack(Material.AIR));
        InventoryView openInventory = p.getOpenInventory();
        if(openInventory.getType().equals(InventoryType.CRAFTING)){
            openInventory.getTopInventory().clear();
        }
        
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
	}
}
