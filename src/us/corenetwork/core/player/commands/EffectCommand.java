package us.corenetwork.core.player.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.player.PlayerSettings;

public class EffectCommand extends BasePlayerCommand {

	private final static char SECOND = 's';
	private final static char MINUTE = 'm';
	private final static char HOUR = 'h';
	private static HashMap<String, PotionEffectType> effects = new HashMap<String, PotionEffectType>();
	static
	{
		effects.put("speed", PotionEffectType.SPEED);
		effects.put("slowness", PotionEffectType.SLOW);
		effects.put("haste", PotionEffectType.FAST_DIGGING);
		effects.put("miningfatigue", PotionEffectType.SLOW_DIGGING);
		effects.put("strength", PotionEffectType.INCREASE_DAMAGE);
		effects.put("instanthealth", PotionEffectType.HEAL);
		effects.put("instantdamage", PotionEffectType.HARM);
		effects.put("jumpboost", PotionEffectType.JUMP);
		effects.put("nausea", PotionEffectType.CONFUSION);
		effects.put("regeneration", PotionEffectType.REGENERATION);
		effects.put("resistance", PotionEffectType.DAMAGE_RESISTANCE);
		effects.put("fireresistance", PotionEffectType.FIRE_RESISTANCE);
		effects.put("waterbreathing", PotionEffectType.WATER_BREATHING);
		effects.put("invisibility", PotionEffectType.INVISIBILITY);
		effects.put("blindness", PotionEffectType.BLINDNESS);
		effects.put("nightvision", PotionEffectType.NIGHT_VISION);
		effects.put("hunger", PotionEffectType.HUNGER);
		effects.put("weakness", PotionEffectType.WEAKNESS);
		effects.put("poison", PotionEffectType.POISON);
		effects.put("wither", PotionEffectType.WITHER);
		effects.put("healthboost", PotionEffectType.HEALTH_BOOST);
		effects.put("absorption", PotionEffectType.ABSORPTION);
		effects.put("saturation", PotionEffectType.SATURATION);
	}
	public EffectCommand()
	{
		desc = "Custom effect command."; 
		permission = "effect";
		needPlayer = false;
	}
	
	///effect [<player>] <effect> [<level>] [<time>] [ambient] [silent]
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		Player target = null;
		PotionEffectType effect = null;
		String effectString = null;
		
		int level = 0;
		int duration = 20*60;
		boolean ambient = false;
		boolean silent = false;
		
		int levelCount = 0;
		int durationCount = 0;
		int ambientCount = 0;
		int silentCount = 0;
		
		if (args.length == 0 || args.length > 6)
		{
			PlayerUtils.Message("Usage : /effect [<player>] <effect> [<level>] [<time>] [ambient] [silent]", sender);
			return;
		}
	
		if (args.length == 1)
		{
			if(isEffect(args[0]))
			{
				if (sender instanceof Player)
				{
					target = (Player) sender;
					effect = getEffect(args[0]);
					effectString = args[0];
				}
				else
				{
					PlayerUtils.Message("You can only execute /effect <effect> as player.", sender);
					return;
				}
					
			}
			else if (isPlayer(args[0]))
			{
				PlayerUtils.Message("Usage : /effect [<player>] <effect> [<level>] [<time>] [ambient] [silent]", sender);
				return;
			}
			else
			{
				PlayerUtils.Message("Cannot find " + args[0] + " effect." , sender);
				return;
			}
		}
		else if (args.length >= 2)
		{
			int normalLength;
			
			if (isPlayer(args[0]))
			{
				if (isEffect(args[1]))
				{
					target = getPlayer(args[0]);
					effect = getEffect(args[1]);
					effectString = args[1];
					normalLength = 2;
				}
				else
				{
					PlayerUtils.Message("Cannot find " + args[1] + " effect." , sender);
					return;
				}
			}
			else if (isEffect(args[1]))
			{
				PlayerUtils.Message("Cannot find " + args[0] + " player." , sender);
				return;
			}
			else if (isEffect(args[0]))
			{
				if (sender instanceof Player)
				{
					target = (Player) sender;
					effect = getEffect(args[0]);
					effectString = args[0];
					normalLength = 1;
				}
				else
				{
					PlayerUtils.Message("You can only execute /effect <effect> as player.", sender);
					return;
				}
			}
			else 
			{
				PlayerUtils.Message("Cannot find " + args[0] + " effect." , sender);
				return;
			}

			for (int i = normalLength; i<args.length; i++ )
			{
				String arg = args[i];
				if (isInteger(arg))
				{
					level = getLevel(arg) - 1;
					levelCount++;
				}
				else if (isTime(arg))
				{
					duration = getDuration(arg);
					durationCount++;
				}
				else if (isAmbient(arg))
				{
					ambient = true;
					ambientCount++;
				}
				else if (isSilent(arg))
				{
					silent = true;
					silentCount++;
				}
				else
				{
					//arg found after the normal length is something we really didnt expect, return.
					PlayerUtils.Message("Usage : /effect [<player>] <effect> [<level>] [<time>] [ambient] [silent]", sender);
					return;
				}
			}
			
			if(silentCount > 1 || ambientCount > 1 || durationCount > 1 || levelCount > 1)
			{
				PlayerUtils.Message("Usage : /effect [<player>] <effect> [<level>] [<time>] [ambient] [silent]", sender);
				return;
			}

		}
		
		applyEffect(target, effect, level, duration, ambient);
		
		effectString = effectString.substring(0, 1).toUpperCase() + effectString.substring(1);
		
		if(silent == false)
		{
			PlayerUtils.Message(PlayerSettings.MESSAGE_GOT_EFFECT.string().replace("<Effect>", effectString).replace("<Level>", ""+(level+1)), target);
		}
		
		return;
	}
	
	private void applyEffect(Player player, PotionEffectType potionEffectType, int level, int duration, boolean ambient)
	{
		player.removePotionEffect(potionEffectType);
		player.addPotionEffect(new PotionEffect(potionEffectType, duration, level, ambient));
	}
	
	private boolean isPlayer(String arg)
	{
		Player player = CorePlugin.instance.getServer().getPlayerExact(arg);
		return player != null;
	}
	
	private Player getPlayer(String arg)
	{
		return CorePlugin.instance.getServer().getPlayerExact(arg);
	}
	
	private boolean isEffect(String arg)
	{
		return effects.keySet().contains(arg); 
	}
	
	private PotionEffectType getEffect(String arg)
	{
		return effects.get(arg);
	}

	private boolean isInteger(String arg)
	{
		try {
			Integer.parseInt(arg);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private int getLevel(String arg)
	{
		return Integer.parseInt(arg);
	}
	
	private boolean isTime(String arg)
	{
		char c = arg.toLowerCase().charAt(arg.length() - 1);
		String number = arg.substring(0, arg.length()-1);
		
		if ((c == SECOND || c == MINUTE || c == HOUR) && isInteger(number))
			return true;
		else
			return false;
	}
	
	private int getDuration(String arg)
	{
		final int TICKS_PER_SECOND = 20;
		final int SECONDS_PER_MINUTE = 60;
		final int MINUTES_PER_HOUR = 60;
		
		char c = arg.toLowerCase().charAt(arg.length() - 1);
		int duration = Integer.parseInt(arg.substring(0, arg.length()-1));
		
		switch (c) {
		case SECOND:
			duration = duration * TICKS_PER_SECOND;
			break;
		case MINUTE:
			duration = duration * TICKS_PER_SECOND * SECONDS_PER_MINUTE;
			break;
		case HOUR:
			duration = duration * TICKS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
			break;
		default:
			break;
		}
		
		return duration;
	}
	
	private boolean isAmbient(String arg)
	{
		return arg.toLowerCase().equals("ambient");
	}
	
	
	private boolean isSilent(String arg)
	{
		return arg.toLowerCase().equals("silent");
	}
}
