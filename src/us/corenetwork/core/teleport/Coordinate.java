package us.corenetwork.core.teleport;

import us.corenetwork.core.Util;

public class Coordinate {
	double number;
	boolean relative;
	
	private Coordinate(double number, boolean relative) {
		this.number = number;
		this.relative = relative;
	}
	
	public double getNumber() {
		return number;
	}
	public boolean isRelative() {
		return relative;
	}	
	
	public static Coordinate parseCoordinate(String input)
	{
		if (Util.isDouble(input))
			return new Coordinate(Double.parseDouble(input), false);
		else if (input.startsWith("~"))
		{
			double coord = 0;
			if(input.length() != 1)
			{
				input = input.substring(1);
				if (Util.isDouble(input))
					coord = Double.parseDouble(input);
				else
					return null;
			}
			return new Coordinate(coord, true);
		}
		else
			return null;
	}
	
	public static boolean isCoordinate(String input)
	{
		return parseCoordinate(input) != null;
	}


}
