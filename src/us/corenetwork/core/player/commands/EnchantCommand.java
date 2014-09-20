package us.corenetwork.core.player.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.Util;
import us.corenetwork.core.player.PlayerSettings;

public class EnchantCommand extends BasePlayerCommand {

	private static HashMap<String, Enchantment> enchantments = new HashMap<String, Enchantment>();
	static
	{
		enchantments.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
		enchantments.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
		enchantments.put("fireprotection", Enchantment.PROTECTION_FIRE);
		enchantments.put("fireprot", Enchantment.PROTECTION_FIRE);
		enchantments.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
		enchantments.put("projprot", Enchantment.PROTECTION_PROJECTILE);
		enchantments.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
		enchantments.put("blastprot", Enchantment.PROTECTION_EXPLOSIONS);
		enchantments.put("featherfalling", Enchantment.PROTECTION_FALL);
		enchantments.put("featherfall", Enchantment.PROTECTION_FALL);
		enchantments.put("ff", Enchantment.PROTECTION_FALL);
		enchantments.put("respiration", Enchantment.OXYGEN);
		enchantments.put("aquaaffinity", Enchantment.WATER_WORKER);
		enchantments.put("aqua", Enchantment.WATER_WORKER);
		enchantments.put("thorns", Enchantment.THORNS);
		enchantments.put("sharpness", Enchantment.DAMAGE_ALL);
		enchantments.put("sharp", Enchantment.DAMAGE_ALL);
		enchantments.put("smite", Enchantment.DAMAGE_UNDEAD);
		enchantments.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
		enchantments.put("bane", Enchantment.DAMAGE_ARTHROPODS);
		enchantments.put("knockback", Enchantment.KNOCKBACK);
		enchantments.put("kb", Enchantment.KNOCKBACK);
		enchantments.put("fireaspect", Enchantment.FIRE_ASPECT);
		enchantments.put("fa", Enchantment.FIRE_ASPECT);
		enchantments.put("looting", Enchantment.LOOT_BONUS_MOBS);
		enchantments.put("loot", Enchantment.LOOT_BONUS_MOBS);
		enchantments.put("efficiency", Enchantment.DIG_SPEED);
		enchantments.put("eff", Enchantment.DIG_SPEED);
		enchantments.put("silktouch", Enchantment.SILK_TOUCH);
		enchantments.put("st", Enchantment.SILK_TOUCH);
		enchantments.put("unbreaking", Enchantment.DURABILITY);
		enchantments.put("unb", Enchantment.DURABILITY);
		enchantments.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
		enchantments.put("power", Enchantment.ARROW_DAMAGE);
		enchantments.put("punch", Enchantment.ARROW_KNOCKBACK);
		enchantments.put("flame", Enchantment.ARROW_FIRE);
		enchantments.put("infinity", Enchantment.ARROW_INFINITE);
		enchantments.put("luckofthesea", Enchantment.LUCK);
		enchantments.put("luck", Enchantment.LUCK);
		enchantments.put("lure", Enchantment.LURE);
	}
	
	public EnchantCommand()
	{
		desc = "Custom enchant command."; 
		permission = "enchant";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		String enchString;
		String levelString;
		Enchantment ench;
		int level;
		Map<Enchantment, Integer> enchMap = new HashMap<Enchantment, Integer>();
		
		Player target = null;
		boolean silent = false;
		int commandOffset = 0;
		int otherKeyWords = 0;
		
		if (args.length < 1)
		{
			PlayerUtils.Message("Usage : /enchant [<player>] <ench> <lvl> [<ench> <lvl> ...] [silent]   or   /enchant [<player>] clear [silent]", sender);
			return;
		}

		if (args.length == 1 && args[0].toLowerCase().equals("clear") == false)
		{
			PlayerUtils.Message("Usage : /enchant [<player>] <ench> <lvl> [<ench> <lvl> ...] [silent]"
					+ "  or   /enchant [<player>] clear [silent]", sender);
			return;
		}
		
		if (isEnch(args[0]) || args[0].toLowerCase().equals("clear"))
		{
			if (sender instanceof Player)
			{
				target = (Player) sender;
			}
			else
			{
				PlayerUtils.Message("You can only execute /enchant <ench> <lvl> [<ench> <lvl> ...] [silent] as player.", sender);
				return;
			}
		}
		else if(PlayerUtils.isPlayer(args[0]))
		{
			target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
			commandOffset++;
			otherKeyWords++;
		}
		else
		{
			PlayerUtils.Message("Cannot find " + args[0] + " player." , sender);
			return;
		}
		
		if (args[args.length-1].toLowerCase().equals("silent"))
		{
			silent = true;
			otherKeyWords++;
		}

		//player specified, check if second arg is 'clear', or if no player specified, and first is clear
		if((commandOffset == 1 && args[1].toLowerCase().equals("clear")) || (commandOffset == 0 && args[0].toLowerCase().equals("clear")))
		{
			for(Enchantment e : target.getItemInHand().getEnchantments().keySet())
			{
				target.getItemInHand().removeEnchantment(e);
			}
			
			if(silent == false)
			{
				if (sender.equals(target) == false)
					PlayerUtils.Message(PlayerSettings.MESSAGE_ENCHANT_CLEAR.string().replace("<Player>", target.getName()), sender);
				PlayerUtils.Message(PlayerSettings.MESSAGE_GOT_ENCHANT_CLEAR.string(), target);
			}
			return;
		}
		
		if((args.length - otherKeyWords) % 2 != 0 || args.length - otherKeyWords == 0) 
		{
			PlayerUtils.Message("Usage : /enchant [<player>] <ench> <lvl> [<ench> <lvl> ...] [silent]", sender);
			return;
		}
		
		//Here only checking if all params are ok
		for(int i = commandOffset; i < args.length - 1; i = i+2)
		{
			enchString = args[i];
			levelString = args[i+1];
			
			if (isEnch(enchString))
			{
				ench = enchantments.get(enchString);
			}
			else
			{
				PlayerUtils.Message("Enchantment not found - " + enchString, sender);
				return;
			}
			
			if (Util.isInteger(levelString))
			{
				level = Integer.parseInt(levelString);
			}
			else
			{
				PlayerUtils.Message("<level> must be a number - " + enchString + " " + levelString, sender);
				return;
			}
			
			enchMap.put(ench, level);
		}
		
		//All params ok, enchant!
		target.getItemInHand().addUnsafeEnchantments(enchMap);
		
		if(silent == false)
		{
			if (sender.equals(target) == false)
				PlayerUtils.Message(PlayerSettings.MESSAGE_ENCHANT_APPLIED.string().replace("<Player>", target.getName()), sender);
			PlayerUtils.Message(PlayerSettings.MESSAGE_GOT_ENCHANT.string(), target);
		}
	}
	
	private boolean isEnch(String value)
	{
		return enchantments.keySet().contains(value);
	}

}
