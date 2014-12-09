package us.corenetwork.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CLog {
	public static void debug(String text)
	{
		if (Settings.getBoolean(Setting.DEBUG))
			sendLog("&f[&3Core&f]&f " + text);
	}

	public static void info(String text)
	{
		sendLog("&f[&fCore&f]&f " + text);
	}

	public static void warning(String text)
	{
		sendLog("&f[&eCore&f]&f " + text);
	}

	public static void severe(String text)
	{
		sendLog("&f[&cCore&f]&f " + text);
	}

	public static void sendLog(String text)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
	}
}
