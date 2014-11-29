package us.corenetwork.core.respawn;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import us.corenetwork.core.HoloDisplay;
import com.gmail.filoghost.holograms.api.Hologram;


public class CountdownDisplay extends HoloDisplay {

	RespawnCountdown countdown;
	
	public CountdownDisplay(RespawnCountdown countdown)
	{
		super(false);
		this.countdown = countdown;
		ArrayList<String> list = (ArrayList<String>) RespawnSettings.HOLOGRAMS_COUNTERS.list();
		initHolograms("", list);
		
	}
	
	public void display()
	{
		for (Hologram holo : holograms)
		{
			String msg = ChatColor.translateAlternateColorCodes('&', RespawnSettings.GROUP_RESPAWN_COUNTER_COLOR.string() + countdown.getTime());
			if(holo.getLinesLength() == 0)
				holo.addLine(msg);
			else
				holo.setLine(0, msg);
			holo.update();
		}
	}
}
