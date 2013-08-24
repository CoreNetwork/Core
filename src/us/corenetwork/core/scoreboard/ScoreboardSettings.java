package us.corenetwork.core.scoreboard;


public enum ScoreboardSettings {
	ENABLED("Enabled", true),

	STATS_PANEL_HEADER("StatsPanelHeader", "Hydration");
	
	protected String string;
	protected Object def;
	
	private ScoreboardSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public Integer integer()
	{
		return (Integer) ScoreboardModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) ScoreboardModule.instance.config.get(string, def);
	}
	
	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = ScoreboardModule.instance.config.get(path);
		if (descO == null)
		{
			ScoreboardModule.instance.config.set(path, "&a/chp " + cmd + " &8-&f " + def);
			ScoreboardModule.instance.saveConfig();
			descO = ScoreboardModule.instance.config.get(path);
		}
		
		return (String) descO;
		
	}
}
