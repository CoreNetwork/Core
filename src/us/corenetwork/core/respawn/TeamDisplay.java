package us.corenetwork.core.respawn;

import java.util.ArrayList;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.HoloDisplay;

public class TeamDisplay extends HoloDisplay {

	RespawnTeamManager teamManager;
	
	public TeamDisplay(RespawnTeamManager teamManager)
	{
		super(true);
		this.teamManager = teamManager;
		ArrayList<String> list = (ArrayList<String>) RespawnSettings.HOLOGRAMS_LISTS.list();
		initHolograms(list);
	}

	public void addLine(int line, String name)
	{
		line += hasHeader ? 2 : 1;
		for(String holoName : holograms)
		{
			String command = RespawnSettings.HOLOGRAMS_COMM_LISTLINE.string();
			command = command.replace("<holoName>", holoName).replace("<text>", name);
			CorePlugin.instance.getServer().dispatchCommand(CorePlugin.instance.getServer().getConsoleSender(), command);
		}
	}

	public void removeLine(int line)
	{
		String command;
		line += hasHeader ? 2 : 1;
		
		for(String holoName : holograms)
		{
			command = RespawnSettings.HOLOGRAMS_COMM_DELLINE.string();
			command = command.replace("<holoName>", holoName).replace("<lineNumber>", line+"");
			CorePlugin.instance.getServer().dispatchCommand(CorePlugin.instance.getServer().getConsoleSender(), command);
		}
	}

	public void clear()
	{
		for(int i = 0; i<teamManager.getTeam().size();i++)
		{
			removeLine(0);
		}
	}
	
}
