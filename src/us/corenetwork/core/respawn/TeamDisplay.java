package us.corenetwork.core.respawn;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.gmail.filoghost.holograms.api.Hologram;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.HoloDisplay;

public class TeamDisplay extends HoloDisplay {

	RespawnTeamManager teamManager;
	
	public TeamDisplay(RespawnTeamManager teamManager)
	{
		super(true);
		this.teamManager = teamManager;
		String msg = ChatColor.translateAlternateColorCodes('&', RespawnSettings.GROUP_RESPAWN_HEADER.string());
		ArrayList<String> list = (ArrayList<String>) RespawnSettings.HOLOGRAMS_LISTS.list();
		initHolograms(msg, list);
	}

	public void addLine(String name)
	{
		for(Hologram holo : holograms)
		{
			String msg = ChatColor.translateAlternateColorCodes('&', RespawnSettings.GROUP_RESPAWN_NAME_COLOR.string() + name);
			holo.addLine(msg);
			holo.update();
		}
	}

	public void removeLine(int line)
	{
		line += hasHeader ? 1 : 0;
		
		for(Hologram holo : holograms)
		{
			holo.removeLine(line);
			holo.update();
		}
	}
	
}
