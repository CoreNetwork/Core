package us.corenetwork.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;

public abstract class HoloDisplay {

	protected List<Hologram> holograms = new ArrayList<Hologram>();
	protected boolean hasHeader;
	
	public HoloDisplay(boolean hasHeader)
	{
		this.hasHeader = hasHeader;
	}
	
	protected void initHolograms(String msg, ArrayList<String> list)
	{
		for(String loc : list)
		{
			Location location = Util.unserializeLocation(loc);
			holograms.add(HolographicDisplaysAPI.createHologram(CorePlugin.instance, location,	msg));
		}
		clear();
	}
	
	public void clear()
	{
		int firstLine = hasHeader ? 1 : 0;
		
		for(Hologram holo : holograms)
		{
			int linesToDelete = holo.getLinesLength() - (hasHeader ? 1 : 0);
			for(int i = 0; i<linesToDelete;i++)
			{
				holo.removeLine(firstLine);
			}
			holo.update();
		}
	}
}
