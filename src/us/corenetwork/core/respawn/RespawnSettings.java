package us.corenetwork.core.respawn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum RespawnSettings {
	TELEPORT_Y("TeleportY", 14),
	RESPAWN_WORLD("World", "world"),
	RESPAWN_NO_BASE_MIN_X("Border.NoBase.MinX", -10000),
	RESPAWN_NO_BASE_MIN_Z("Border.NoBase.MinZ", -10000),
	RESPAWN_NO_BASE_MAX_X("Border.NoBase.MaxX", 10000),
	RESPAWN_NO_BASE_MAX_Z("Border.NoBase.MaxZ", 10000),
	RESPAWN_BASE_MIN_X("Border.Base.MinX", -16000),
	RESPAWN_BASE_MIN_Z("Border.Base.MinZ", -16000),
	RESPAWN_BASE_MAX_X("Border.Base.MaxX", 16000),
	RESPAWN_BASE_MAX_Z("Border.Base.MaxZ", 16000),
	SPAWN_PROTECTION_LENGTH("SpawnProtectionLength", 120),
	SPAWN_PROTECTION_NOTIFICATIONS("SpawnProtectionNotifications", Arrays.asList(new Integer[] {90, 60, 30, 10})),
	MOB_REMOVAL_RADIUS_SQUARED("MobRemovalRadiusSquared", 625),

	LUCKY100("Lucky100", new ArrayList<String>(){{
		add("SubUniv");
		add("SubSky");
	}}),
	LUCKY66("Lucky66", new ArrayList<String>(){{
		add("SubGrass");
	}}),
	
	NEW_PLAYER_SPAWN_WARP_NAME("SpawnWarps.NewPlayer", "endguide"),
	EXISTING_PLAYER_SPAWN_WARP_NAME("SpawnWarps.ExistingPlayer", "limbocrystal"),
	
	MESSAGE_SPAWN_IGNORED("Messages.SpawnIgnored", "Your home is now ignored when spawning"),
	MESSAGE_SPAWN_UNIGNORED("Messages.SpawnUnignored", "Your home is now unignored when spawning"),
	MESSAGE_SPAWN_PROTECTION_START("Messages.SpawnProtectionStart", "You are invincible for <Time> seconds!"),
	MESSAGE_SPAWN_PROTECTION_NOTIFICATION("Messages.SpawnProtectionNotification", "Invicibility will expire in <Time> seconds."),
	MESSAGE_SPAWN_PROTECTION_END_CLAIMS("Messages.SpawnProtectionEndClaims", "&eYou are no longer invincible! Find food and weapons before you try to reach your &6/base"),
	MESSAGE_SPAWN_PROTECTION_END_NO_CLAIMS("Messages.SpawnProtectionEndNoClaims", "&eYou are no longer invincible! Find food or shelter."),
	MESSAGE_SPAWN_UNPROTECT_NOT_PROTECTED("Messages.UnprotectNotProtected", "&eYou are already not protected."),
	MESSAGE_SPAWN_PROTECTION_DONT_ABUSE("Messages.SpawnProtectionDontAbuse", "Do not abuse spawn protection! Use /unprotect to disable it to start killing mobs.");
	
	protected String string;
	protected Object def;
	
	private RespawnSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public double doubleNumber()
	{
		return ((Number) RespawnModule.instance.config.get(string, def)).doubleValue();
	}
	
	public Integer integer()
	{
		return (Integer) RespawnModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) RespawnModule.instance.config.get(string, def);
	}
	
	public List<?> list()
	{
		return (List<?>) RespawnModule.instance.config.get(string, def);
	}
	
	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = RespawnModule.instance.config.get(path);
		if (descO == null)
		{
			RespawnModule.instance.config.set(path, "&a/chp " + cmd + " &8-&f " + def);
			RespawnModule.instance.saveConfig();
			descO = RespawnModule.instance.config.get(path);
		}
		
		return (String) descO;
		
	}
}
