package us.corenetwork.core.trapped;

import java.util.ArrayList;
import java.util.List;

import us.corenetwork.core.respawn.RespawnModule;

public enum TrappedSettings {

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
	
	MESSAGE_NOT_ENABLED_IN("Messages.NotEnabledIn", "&4You cannot use /trapped in this world."),
	MESSAGE_CAN_BUILD("Messages.CanBuild", "&4You can build here. Save yourelf."),
	MESSAGE_RESCUE_IN_PROGRESS("Messages.RescueInProgress", "Please wait still for 5 seconds."),
	MESSAGE_RESCUE_PLAYER_MOVED("Messages.RescurePlayerMoved", "Rescue cancelled, you moved!");
	
	protected String string;
	protected Object def;
	
	private TrappedSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public Integer integer()
	{
		return (Integer) TrappedModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) TrappedModule.instance.config.get(string, def);
	}
	
	public List<?> list()
	{
		return (List<?>) RespawnModule.instance.config.get(string, def);
	}
	
	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = TrappedModule.instance.config.get(path);
		if (descO == null)
		{
			TrappedModule.instance.config.set(path, "&a/chp " + cmd + " &8-&f " + def);
			TrappedModule.instance.saveConfig();
			descO = TrappedModule.instance.config.get(path);
		}
		
		return (String) descO;
	}

}
