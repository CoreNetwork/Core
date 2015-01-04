package us.corenetwork.core.claims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import us.corenetwork.core.respawn.RespawnModule;

public enum ClaimsSettings {

	ENABLED("Enabled", true),

	ENABLED_WORLDS("EnabledWorlds", new ArrayList<String>(){{
		add("world");
		add("world_nether");
	}}),
	
	CLEAR_RADIUS("Trapped.ClearRadius", 7),
	NETHER_SURFACE_Y("Trapped.Surface.world_nether", 64),
	OVERWORLD_SURFACE_Y("Trapped.Surface.world", 4),

	OVERWORLD_MAX_X("Trapped.Limits.world.MaxX", 8000),
	OVERWORLD_MAX_Z("Trapped.Limits.world.MaxZ", 8000),
	OVERWORLD_MIN_X("Trapped.Limits.world.MinX", -8000),
	OVERWORLD_MIN_Z("Trapped.Limits.world.MinZ", -8000),

	NETHER_MAX_X("Trapped.Limits.world_nether.MaxX", 2000),
	NETHER_MAX_Z("Trapped.Limits.world_nether.MaxZ", 2000),
	NETHER_MIN_X("Trapped.Limits.world_nether.MinX", -2000),
	NETHER_MIN_Z("Trapped.Limits.world_nether.MinZ", -2000),
	
	MAX_X("Trapped.Limits.Other.MaxX", 8000),
	MAX_Z("Trapped.Limits.Other.MaxZ", 8000),
	MIN_X("Trapped.Limits.Other.MinX", -8000),
	MIN_Z("Trapped.Limits.Other.MinZ", -8000),
	
	BUYING_PACKET_SIZE("Buying.PacketSize", 1000),
	BUYING_PACKET_COST("Buying.PacketCost", new ArrayList<Integer>(){{add(64);add(79);add(98);
	add(123);add(156);add(201);add(262);add(346);add(463);
	add(629);add(868);add(1217);add(1736);}}),
	BUYING_RESOURCES("Buying.Resources", new ArrayList<HashMap<String, Object>>(){{
		add(new HashMap<String, Object>(){{
			put("id", new Integer(265));
			put("name", "Iron ingot");
		}});
		add(new HashMap<String, Object>(){{
			put("id", new Integer(388));
			put("name", "Emerald");
		}});
		add(new HashMap<String, Object>(){{
			put("id", new Integer(266));
			put("name", "Gold ingot");
		}});
	}}), 
	
	BUYING_MESSAGE_BOUGHT_ALL("Buying.Messages.BoughtAll", "&cYou you can't buy more claim blocks."),
	BUYING_MESSAGE_CONFIRM_INFO("Buying.Messages.ConfirmInfo", "&aBuying 1,000 claim blocks."),
	BUYING_MESSAGE_CONFIRM_PREFIX("Buying.Messages.ConfirmPrefix", "&a<Resource>: ["),
	BUYING_MESSAGE_CONFIRM_BUTTON("Buying.Messages.ConfirmButton", "&6Buy for <Amount>"),
	BUYING_MESSAGE_CONFIRM_SUFFIX("Buying.Messages.ConfirmSuffix", "&a]"),
	
	BUYING_MESSAGE_CONFIRM_SUCCESS_PREFIX("Buying.Messages.Confirm.SuccessPrefix", "&aSuccess! Buy another 1,000 for ["),
	BUYING_MESSAGE_CONFIRM_SUCCESS_BUTTON("Buying.Messages.Confirm.SuccessButton", "&6<Resource>, <Amount>"),
	BUYING_MESSAGE_CONFIRM_SUCCESS_SUFFIX("Buying.Messages.Confirm.SuccessSuffix", "&a]"),
	
	BUYING_MESSAGE_CONFIRM_NO_NEXT("Buying.Messages.Confirm.SuccessNoNext", "&aSuccess!"),
	
	BUYING_MESSAGE_NOT_ENOUGH ("Buying.Messages.NotEnough", "&cYou don't have enough resources in your inventory."),
	
	CLAIMSLIST_MESSAGES_HEADER("Claimslist.Messages.Header", "<WorldName> (<ClaimsNow>/<ClaimsMax>)"),
	CLAIMSLIST_MESSAGES_LINE("Claimslist.Messages.Line", "------------"),
	CLAIMSLIST_MESSAGES_CLAIM("Claimslist.Messages.Claim", "<X>, <Z> (area: <Area>)"),
	CLAIMSLIST_MESSAGES_SC_CLAIM("Claimslist.Messages.SpecialCaseClaim", "&c<X>, <Z> (area: <Area>)"),
	CLAIMSLIST_MESSAGES_NO_CLAIMS("Claimslist.Messages.NoClaims", "&cYou don't have claims yet! Learn how to do it: &7/help Claims"),
	CLAIMSLIST_WORLDS_OW_NAME("Claimslist.Worlds.World.Name", "Overworld"),
	CLAIMSLIST_WORLDS_OW_CLAIMS_MAX("Claimslist.Worlds.World.ClaimsMax", 5),
	CLAIMSLIST_WORLDS_NETHER_NAME("Claimslist.Worlds.Nether.Name", "Nether"),
	CLAIMSLIST_WORLDS_NETHER_CLAIMS_MAX("Claimslist.Worlds.Nether.ClaimsMax", 5),
	
	BLOCKS_MESSAGES_HEADER("Blocks.Messages.Header", "Claim blocks:"),
	BLOCKS_MESSAGES_LINE("Blocks.Messages.Line", "------------"),
	BLOCKS_MESSAGES_GAMEPLAY("Blocks.Messages.Gameplay", "Normal gameplay: <Amount>"),
	BLOCKS_MESSAGES_RANKS("Blocks.Messages.Ranks", "Bonus, ranks: <Amount>"),
	BLOCKS_MESSAGES_BOUGHT("Blocks.Messages.Bought", "Bonus, bought: <Amount>"),
	BLOCKS_MESSAGES_OTHER("Blocks.Messages.Other", "Bonus, other: <Amount>"),
	BLOCKS_MESSAGES_TOTAL("Blocks.Messages.Total", "Total: <Amount>"),
	BLOCKS_MESSAGES_USED("Blocks.Messages.Used", "Used: <Amount>"),
	BLOCKS_MESSAGES_AVAILABLE("Blocks.Messages.Available", "Available: <Amount>"),

	MESSAGE_NOT_ENABLED_IN("Messages.NotEnabledIn", "&4You cannot use /trapped in this world."),
	MESSAGE_CAN_BUILD("Messages.CanBuild", "&4You can build here. Save yourelf."),
	MESSAGE_RESCUE_IN_PROGRESS("Messages.RescueInProgress", "Please wait still for 5 seconds."),
	MESSAGE_RESCUE_PLAYER_MOVED("Messages.RescurePlayerMoved", "Rescue cancelled, you moved!"),

	CLAIMSLIMIT_NO_CLAIMS_LEFT("ClaimsLimits.Messages.NoClaimsLeft", "You used all of your <Claims> claims in this world.");
	
	protected String string;
	protected Object def;
	
	private ClaimsSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public Integer integer()
	{
		return (Integer) ClaimsModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) ClaimsModule.instance.config.get(string, def);
	}
	
	public List<?> list()
	{
		return (List<?>) RespawnModule.instance.config.get(string, def);
	}
	
	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = ClaimsModule.instance.config.get(path);
		if (descO == null)
		{
			ClaimsModule.instance.config.set(path, "&a/chp " + cmd + " &8-&f " + def);
			ClaimsModule.instance.saveConfig();
			descO = ClaimsModule.instance.config.get(path);
		}
		
		return (String) descO;
	}

}
