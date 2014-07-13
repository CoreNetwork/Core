package us.corenetwork.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;

public abstract class HoloDisplay {
	
	private boolean hasHeader;
	protected List<Hologram> holograms = new ArrayList<Hologram>();
	
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
	
	public abstract void display();
	
	public void clear()
	{
		int start = 0;
		if(hasHeader)
		{
			start = 1;
		}
		
		for(Hologram holo : holograms)
		{
			for(int i = holo.getLinesLength() - 1; i>=start; i--)
			{
				holo.removeLine(i);
			}
			holo.update();
		}
	}

}
