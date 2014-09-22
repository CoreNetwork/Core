package us.corenetwork.core.respawn;

import java.util.ArrayList;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;

public class CommandCountdownDisplay {

	private ArrayList<String> holoNames;
	
	
	public CommandCountdownDisplay()
	{
		holoNames = (ArrayList<String>) RespawnSettings.HOLOGRAMS_COUNTERS_18.list();
	}

	public void clear()
	{
		for(String holo : holoNames)
		{
			CorePlugin.instance.getServer().dispatchCommand(CorePlugin.instance.getServer().getConsoleSender(), 
					"holo update "+ holo+ " \"\"");
		}
	}
	
	public void set(int value)
	{
		for(String holo : holoNames)
		{
			String msg = ChatColor.translateAlternateColorCodes('&', RespawnSettings.GROUP_RESPAWN_COUNTER_COLOR.string() + value);
			CorePlugin.instance.getServer().dispatchCommand(CorePlugin.instance.getServer().getConsoleSender(), 
					"holo update "+ holo+ " \"" + msg +"\"");
		}
	}
}
