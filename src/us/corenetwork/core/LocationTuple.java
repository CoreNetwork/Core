package us.corenetwork.core;

import org.bukkit.Location;

public class LocationTuple {

	private Location a;
	private Location b;

	public LocationTuple(Location a, Location b) {
		this.a = a;
		this.b = b;
	}

	public Location a() {
		return a;
	}

	public Location b() {
		return b;
	}
}
