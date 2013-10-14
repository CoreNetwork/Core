package us.corenetwork.core.teleport;



public enum TeleportSettings {
	ENABLED("Enabled", true),
	
	PLAYER_FREEZE_DURATION("PlayerFreezeDurationMS", 2000),
	NETHER_SURFACE_Y("NetherSurfaceY", 64),
	OFFLINE_TELEPORT_CONFIRM_TIMEOUT_SECONDS("OfflineTeleportConfirmTimeoutSeconds", 10),
	
	MESSAGE_WARP_SET("Messages.WarpSet", "Warp <Name> set!"),
	MESSAGE_UNKNOWN_WARP("Messages.UnknownWarp", "Unknown warp <Name>! Please contact administrator."),
	MESSAGE_TP_NOTICE("Messages.TpNotice", "&7<Player> was just teleported to <Warp>!"),
	MESSAGE_WARP_DELETED("Messages.WarpDeleted", "Warp <Name> deleted!"),
	MESSAGE_UNKNOWN_PLAYER("Messages.UnknownPlayer", "Unknown player <Name>!"),
	MESSAGE_ONLY_PLAYER("Messages.OnlyPlayer", "You can only execute that as player!"),
	MESSAGE_RELATIVE_NO_CONSOLE("Messages.RelativeNoConsole", "You can't use relative coordinates from console!"),
	MESSAGE_YOU_TELEPORTED_TO_PLAYER("Messages.YouTeleportedToPlayer", "You were teleported to <Player>."),
	MESSAGE_YOU_TELEPORTED_TO_COORDINATES("Messages.YouTeleportedToCoordinates", "You were teleported to <X> <Y> <Z>"),
	MESSAGE_PLAYER_TELEPORTED_TO_PLAYER("Messages.PlayerTeleportedToPlayer", "<From> was teleported to <To>!"),
	MESSAGE_PLAYER_TELEPORTED_TO_COORDINATES("Messages.PlayerTeleportedToCoordinates", "<Player> was teleported to <World> <X> <Y> <Z>!"),
	MESSAGE_UNKNOWN_WORLD("Messages.UnknownWorld", "Unknown world <World>!"),
	MESSAGE_CONFIRM_OFFLINE_TELEPORT("Messages.ConfirmOfflineTeleport", "Player <Player> will be teleported after he logs in. Confirm your decision with &a/<Command> confirm&7"),
	MESSAGE_OFFLINE_TELEPORT_CONFIRMED("Messages.OfflineTeleportConfirmed", "Offline teleport confirmed."),
	MESSAGE_NO_OFFLINE_TELEPORTS("Messages.NoOfflineTeleports", "You have no pending teleports to confirm.");
	
	protected String string;
	protected Object def;
	
	private TeleportSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public double doubleNumber()
	{
		return ((Number) TeleportModule.instance.config.get(string, def)).doubleValue();
	}
	
	public Integer integer()
	{
		return (Integer) TeleportModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) TeleportModule.instance.config.get(string, def);
	}
	
	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = TeleportModule.instance.config.get(path);
		if (descO == null)
		{
			TeleportModule.instance.config.set(path, "&a/warp " + cmd + " &8-&f " + def);
			TeleportModule.instance.saveConfig();
			descO = TeleportModule.instance.config.get(path);
		}
		
		return (String) descO;
		
	}
}
