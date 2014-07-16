package us.corenetwork.core;

import java.util.ArrayList;
import java.util.List;

public abstract class HoloDisplay {

	protected List<String> holograms = new ArrayList<String>();
	protected boolean hasHeader;
	
	public HoloDisplay(boolean hasHeader)
	{
		this.hasHeader = hasHeader;
	}
	
	protected void initHolograms(ArrayList<String> list)
	{
		for(String hol : list)
		{
			holograms.add(hol);
		}
	}
	

}
