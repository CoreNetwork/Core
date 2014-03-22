package us.corenetwork.core.player;


public enum PlayerSettings {

	ENABLED("Enabled", true),
	
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
	
	MESSAGE_SELF_CLEARED("Message.InventorySelfCleared", "Inventory cleared."),
	MESSAGE_PLAYER_CLEARED("Message.InventoryPlayerCleared", "<Player>'s inventory cleared.");
	
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
