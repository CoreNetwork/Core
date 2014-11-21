package us.corenetwork.core;

import org.bukkit.Location;

public class ClaimSimple implements Comparable<ClaimSimple>{
		public Location location;
		public int area;
		public boolean is9by9;
		
		public ClaimSimple(Location location, int area,boolean is9by9)
		{
			this.location = location;
			this.area = area;
			this.is9by9 = is9by9;
		}

		@Override
		public int compareTo(ClaimSimple o)
		{
			return Integer.compare(area, o.area);
		}
	}