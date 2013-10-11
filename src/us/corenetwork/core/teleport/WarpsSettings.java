package us.corenetwork.core.teleport;


public enum WarpsSettings {
	ENABLED("Enabled", true),
	
	MESSAGE_WARP_SET("Messages.WarpSet", "Warp <Name> set!"),
	MESSAGE_UNKNOWN_WARP("Messages.UnknownWarp", "Unknown warp <Name>! Please contact administrator."),
	MESSAGE_TP_NOTICE("Messages.TpNotice", "&7<Player> was just teleported to <Warp>!"),
	MESSAGE_WARP_DELETED("Messages.WarpDeleted", "Warp <Name> deleted!");

	protected String string;
	protected Object def;
	
	private WarpsSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public Integer integer()
	{
		return (Integer) WarpsModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) WarpsModule.instance.config.get(string, def);
	}
	
	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = WarpsModule.instance.config.get(path);
		if (descO == null)
		{
			WarpsModule.instance.config.set(path, "&a/warp " + cmd + " &8-&f " + def);
			WarpsModule.instance.saveConfig();
			descO = WarpsModule.instance.config.get(path);
		}
		
		return (String) descO;
		
	}
}
