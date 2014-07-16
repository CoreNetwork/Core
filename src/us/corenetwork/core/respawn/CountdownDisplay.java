package us.corenetwork.core.respawn;

import java.util.ArrayList;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.HoloDisplay;


public class CountdownDisplay extends HoloDisplay {

	RespawnCountdown countdown;
	
	public CountdownDisplay(RespawnCountdown countdown)
	{
		super(false);
		this.countdown = countdown;
		ArrayList<String> list = (ArrayList<String>) RespawnSettings.HOLOGRAMS_COUNTERS.list();
		initHolograms(list);
	}
	
	public void refresh()
	{
		setLine(countdown.getTime()+"");
	}
	
	public void clear()
	{
		setLine("");
	}
	
	private void setLine(String msg)
	{
		for(String holoName : holograms)
		{
			String command = RespawnSettings.HOLOGRAMS_COMM_COULINE.string();
			command = command.replace("<holoName>", holoName).replace("<text>", msg);
			CorePlugin.instance.getServer().dispatchCommand(CorePlugin.instance.getServer().getConsoleSender(), command);
		}
	}
}
