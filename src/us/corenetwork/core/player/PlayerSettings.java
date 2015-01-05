package us.corenetwork.core.player;

import java.util.ArrayList;


public enum PlayerSettings {

	ENABLED("Enabled", true),
	VANISH_ENABLED("VanishEnabled", true),
	
	NETHER_SURFACE_Y("Trapped.Surface.world_nether", 64),
	
	OVERWORLD_MAX_X("Limits.world.MaxX", 8000),
	OVERWORLD_MAX_Z("Limits.world.MaxZ", 8000),
	OVERWORLD_MIN_X("Limits.world.MinX", -8000),
	OVERWORLD_MIN_Z("Limits.world.MinZ", -8000),

	NETHER_MAX_X("Limits.world_nether.MaxX", 2000),
	NETHER_MAX_Z("Limits.world_nether.MaxZ", 2000),
	NETHER_MIN_X("Limits.world_nether.MinX", -2000),
	NETHER_MIN_Z("Limits.world_nether.MinZ", -2000),
	
	MAX_X("Limits.Other.MaxX", 8000),
	MAX_Z("Limits.Other.MaxZ", 8000),
	MIN_X("Limits.Other.MinX", -8000),
	MIN_Z("Limits.Other.MinZ", -8000),
	
	OVERWORLD_IGNORE_BORDER("Border.world", 10),
	IGNORE_BORDER("Border.Other", 0),
	
	KITS("Kits", new ArrayList<String>()),
	
	MESSAGE_SELF_CLEARED("Message.InventorySelfCleared", "Inventory cleared."),
	MESSAGE_PLAYER_CLEARED("Message.InventoryPlayerCleared", "<Player>'s inventory cleared."),
	
	MESSAGE_DRINK_MILK_VANISHED("Message.DrinkMilkWhileVanished", "You can't drink milk while vanished."),
	//MESSAGE_OPEN_CHEST_VANISHED("Message.OpenChestWhileVanished", "You are opening a chest while vanished."),
	MESSAGE_VANISHED("Message.Vanished", "You have been vanished."),
	MESSAGE_UNVANISHED("Message.Unvanished", "You have been unvanished."),
	
	MESSAGE_PLAYER_VANISHED("Message.PlayerVanished", "<Player> vanished."),
	MESSAGE_PLAYER_UNVANISHED("Message.PlayerUnvanished", "<Player> unvanished."),

	MESSAGE_EFFECT_APPLIED("Message.EffectApplied", "Effect applied."),
	MESSAGE_GOT_EFFECT("Message.GotEffect", "You have been granted <Effect> level <Level> effect."),

	MESSAGE_EFFECT_CLEARED("Message.EffectCleared", "Effect cleared."),
	MESSAGE_LOST_EFFECT("Message.LostEffect", "You have lost <Effect> effect."),
	MESSAGE_CLEARED_ALL("Message.EffectsCleared", "All effects cleared."),
	MESSAGE_LOST_ALL("Message.LostAllEffects", "You have lost all effects!"),


	MESSAGE_ENCHANT_APPLIED("Message.EnchantApplied", "Item enchanted."),
	MESSAGE_GOT_ENCHANT("Message.GotEnchant", "Your item has been enchanted."),
	
	MESSAGE_ENCHANT_CLEAR("Message.EnchantClear", "Item enchantments cleared."),
	MESSAGE_GOT_ENCHANT_CLEAR("Message.GotEnchantClear", "Your item has been cleared."),
	
	
	MESSAGE_GOD_APPLIED("Message.GodApplied", "God mode applied."),
	MESSAGE_GOD_REVOKED("Message.GodRevoked", "God mode revoked."),
	MESSAGE_GOT_GOD("Message.GotGod", "You have been granted god mode."),
	MESSAGE_LOST_GOD("Message.LostGod", "You have lost god mode."),
	
	
	MESSAGE_SELF_GAMEMODE("Message.SelfGamemode", "You changed your gamemode to <Gamemode>."),
	MESSAGE_SET_GAMEMODE("Message.SetGamemode", "You changed <Player>'s gamemode to <Gamemode>."),
	MESSAGE_GAMEMODE_CHANGED("Message.GamemodeChanged", "Your gamemode was changed to <Gamemode>."),
	
	MESSAGE_VANISH_DISABLED("Message.VanishDisabled", "Vanish is disabled due to some bugs :["),
	
	MESSAGE_KIT_RECEIVED("Message.KitReceived", "You have been granted a <Kit> kit."),
	MESSAGE_KIT_GRANTED("Message.KitGranted", "<Kit> kit granted to a <Player>."),

	DELAY_GRACE_PERIOD_TICKS("DelayGracePeriodInTicks", 20),
	;
	protected String string;
	protected Object def;
	
	private PlayerSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public Integer integer()
	{
		return (Integer) PlayerModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) PlayerModule.instance.config.get(string, def);
	}
	
	public Boolean bool()
	{
		return (Boolean) PlayerModule.instance.config.get(string, def);
	}
	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = PlayerModule.instance.config.get(path);
		if (descO == null)
		{
			PlayerModule.instance.config.set(path, "&a/chp " + cmd + " &8-&f " + def);
			PlayerModule.instance.saveConfig();
			descO = PlayerModule.instance.config.get(path);
		}
		
		return (String) descO;
	}

}
