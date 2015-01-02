package us.corenetwork.core.respawn;

import java.util.ArrayList;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;

public class CommandTeamDisplay {

	private ArrayList<String> holoNames;
	
	
	public CommandTeamDisplay()
	{
		holoNames = (ArrayList<String>) RespawnSettings.HOLOGRAMS_LISTS_18.list();
	}
	
	public void clear()
	{
		for(String holo : holoNames)
		{
			CorePlugin.instance.getServer().dispatchCommand(CorePlugin.instance.getServer().getConsoleSender(), 
					"holo set "+ holo+ " \""+RespawnSettings.GROUP_RESPAWN_HEADER.string()+"\"");
		}
	}
	
	public void set(LinkedList<Player> respawnTeam)
	{
		for(String holo : holoNames)
		{
			StringBuilder sb = new StringBuilder();
			for(Player p : respawnTeam)
			{
				String msg = ChatColor.translateAlternateColorCodes('&', RespawnSettings.GROUP_RESPAWN_NAME_COLOR.string() + p.getName());
				sb.append(msg+"<N>");
			}
			CorePlugin.instance.getServer().dispatchCommand(CorePlugin.instance.getServer().getConsoleSender(), 
					"holo set "+ holo+ " \"" + RespawnSettings.GROUP_RESPAWN_HEADER.string()+"<N>"+sb.toString()+"\"");
		}
	}
}
