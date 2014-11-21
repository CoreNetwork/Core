package us.corenetwork.core.claims;

import org.bukkit.Material;

public class PacketResource {

	public Material material;
	public String name;
	
	public PacketResource(Material material, String name)
	{
		this.material = material;
		this.name = name;
	}
	
	
}
