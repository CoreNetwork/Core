package us.corenetwork.core;

import org.bukkit.Bukkit;

public class CLog {
	public static void debug(String text)
	{
		if (Settings.getBoolean(Setting.DEBUG))
			info(text);
	}
	
	public static void info(String text)
	{
		Bukkit.getLogger().info("[Core] " + text);
	}
	
	public static void warning(String text)
	{
		Bukkit.getLogger().warning("[Core] " + text);
	}
	
	public static void severe(String text)
	{
		Bukkit.getLogger().severe("[Core] " + text);
	}
}
