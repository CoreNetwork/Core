package us.corenetwork.core.player.commands;

import org.bukkit.ChatColor;
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
		if (args.length > 2 || (args.length == 2 && !args[1].toLowerCase().equals("silent")))
		{
			PlayerUtils.Message("Usage : /clear [<player>] [silent]", sender);
			return;
		}
		
		boolean silent = false;
		Player player = null;
		
		if (args.length == 0)
		{
			if (sender instanceof Player)
			{
				player = (Player) sender;
			}
			else
			{
				PlayerUtils.Message("You can only execute /clear [silent] as player.", sender);
				return;
			}
		}
		else if (args.length == 1)
		{
			player = CorePlugin.instance.getServer().getPlayerExact(args[0]);
			if(player == null)
			{
				if (args[0].toLowerCase().equals("silent"))
				{
					silent = true;
					if (sender instanceof Player)
					{
						player = (Player) sender;
					}
					else
					{
						PlayerUtils.Message("You can only execute /clear [silent] as player.", sender);
						return;
					}
				}
				else
				{
					PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
					return;
				}
			}
		}
		else if (args.length == 2)
		{
			player = CorePlugin.instance.getServer().getPlayerExact(args[0]);
			if(player == null)
			{
				PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
				return;
			}
			if (args[1].toLowerCase().equals("silent"))
			{
				silent = true;
			}
			else
			{
				PlayerUtils.Message("Usage : /clear [<player>] [silent]", sender);
				return;
			}
		}
		
		
		
		if (silent == false)
		{
			if (sender instanceof Player && player.equals((Player)sender))
				PlayerUtils.Message(PlayerSettings.MESSAGE_SELF_CLEARED.string(), sender);
			else
				PlayerUtils.Message(PlayerSettings.MESSAGE_PLAYER_CLEARED.string().replace("<Player>", player.getName()), sender);
		}
		clearInventory(player);
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
		
		p.updateInventory();
	}
}
