package us.corenetwork.core;


public enum Setting {	
	
	DEBUG("Debug", false),
	
	MESSAGE_NO_PERMISSION("Messages.NoPermission", "No permission!"),
	MESSAGE_CONFIGURATION_RELOADED("Messages.ConfigurationReloaded", "Configuration reloaded successfully!"),
    MESSAGE_DEFAULT_KICK_ON_REBOOT("Messages.Reboot.Kick", "Server Reboot.");

	private String name;
	private Object def;
	
	private Setting(String Name, Object Def)
	{
		name = Name;
		def = Def;
	}
	
	public String getString()
	{
		return name;
	}
	
	public Object getDefault()
	{
		return def;
	}
}
