package us.corenetwork.core.map;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Range;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.respawn.RespawnSettings;

public class TimeTask implements Runnable {

	private static Map<Range<Integer>, String> ranges = new HashMap<Range<Integer>, String>();

	private World overworld;

	public TimeTask()
	{
		overworld = Bukkit.getWorld("world");
	}

	public static void load(YamlConfiguration config)
	{
		ranges.clear();
		List<Map<?,?>> listOfMaps = config.getMapList(CheckpointsSettings.CLOCK_TIME_RANGE_MESSAGES.getString());

		for(Map<?,?> map : listOfMaps)
		{
			Integer beg = (Integer) map.get("beginning");
			Integer end = (Integer) map.get("end");
			String msg = (String) map.get("message");

			msg = ChatColor.translateAlternateColorCodes('&', msg);

			ranges.put(Range.closed(beg, end), msg);
		}
	}

	@Override
	public void run() 
	{
		int time = (int) overworld.getTime();

		String msg = "";

		for(Range<Integer> rr : ranges.keySet())
		{
			if(rr.contains(time))
			{
				msg = ranges.get(rr);
				break;
			}
		}

		for(String holo : CheckpointsSettings.CLOCK_HOLO_LIST.stringList())
		{
			CorePlugin.instance.getServer().dispatchCommand(CorePlugin.instance.getServer().getConsoleSender(),
					"holo update " + holo + " \"" + msg + "\"");
		}
	}

}
