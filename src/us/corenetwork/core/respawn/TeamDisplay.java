package us.corenetwork.core.respawn;

import java.util.ArrayList;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.HoloDisplay;
import us.corenetwork.core.Util;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;

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

	

	@Override
	public void display()
	{
		clear();
		for(Hologram holo : holograms)
		{
			for(Player player : teamManager.getTeam())
			{
				String msg = ChatColor.translateAlternateColorCodes('&', RespawnSettings.GROUP_RESPAWN_NAME_COLOR.string() + player.getName());
				holo.addLine(msg);
			}
			holo.update();
		}
	}
	
	
}
