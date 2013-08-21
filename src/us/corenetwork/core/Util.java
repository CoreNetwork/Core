package us.corenetwork.core;
import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.server.v1_6_R2.ChunkProviderHell;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftFirework;
import org.bukkit.craftbukkit.v1_6_R2.generator.NormalChunkGenerator;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class Util {
	public static void Message(String message, CommandSender sender)
	{
		message = message.replaceAll("\\&([0-9abcdefklmnor])", ChatColor.COLOR_CHAR + "$1");

		final String newLine = "\\[NEWLINE\\]";
		String[] lines = message.split(newLine);

		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].trim();

			if (i == 0)
				continue;

			int lastColorChar = lines[i - 1].lastIndexOf(ChatColor.COLOR_CHAR);
			if (lastColorChar == -1 || lastColorChar >= lines[i - 1].length() - 1)
				continue;

			char lastColor = lines[i - 1].charAt(lastColorChar + 1);
			lines[i] = Character.toString(ChatColor.COLOR_CHAR).concat(Character.toString(lastColor)).concat(lines[i]);	
			System.out.println(lines[i]);
		}		

		for (int i = 0; i < lines.length; i++)
			sender.sendMessage(lines[i]);


	}

	public static Boolean isInteger(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static Boolean isDouble(String text) {
		try {
			Double.parseDouble(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}


	public static void safeTeleport(final Player player, final Location location)
	{
		Chunk c = location.getChunk();
		if (!c.isLoaded())
			location.getChunk().load();
		player.teleport(location);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CorePlugin.instance, new Runnable() {
			@Override
			public void run() {
				player.teleport(location);

			}
		}, 10);
	}

	public static int flatDistance(Location a, Location b)
	{
		return ((a.getBlockX() - b.getBlockX()) * (a.getBlockX() - b.getBlockX())) + ((a.getBlockZ() - b.getBlockZ()) * (a.getBlockZ() - b.getBlockZ()));
	}

	public static Location unserializeLocation(String text)
	{
		String[] split = text.split(";");

		World world = Bukkit.getWorld(split[0]);
		double x = Double.parseDouble(split[1]);
		double y = Double.parseDouble(split[2]);
		double z = Double.parseDouble(split[3]);
		float pitch = Float.parseFloat(split[4]);
		float yaw = Float.parseFloat(split[5]);

		return new Location(world, x, y, z, yaw, pitch);
	}

	public static String serializeLocation(Location location)
	{
		String locString = location.getWorld().getName().concat(";");
		locString = locString.concat(Double.toString(location.getX())).concat(";").concat(Double.toString(location.getY())).concat(";").concat(Double.toString(location.getZ())).concat(";");
		locString = locString.concat(Float.toString(location.getPitch())).concat(";").concat(Float.toString(location.getYaw()));

		return locString;
	}

	public static boolean hasPermission(CommandSender player, String permission)
	{
		while (true)
		{
			if (player.hasPermission(permission))
				return true;

			if (permission.length() < 2)
				return false;

			if (permission.endsWith("*"))
				permission = permission.substring(0, permission.length() - 2);

			int lastIndex = permission.lastIndexOf(".");
			if (lastIndex < 0)
				return false;

			permission = permission.substring(0, lastIndex).concat(".*");  
		}
	}
}
