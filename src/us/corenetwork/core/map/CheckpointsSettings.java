package us.corenetwork.core.map;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CheckpointsSettings {
	ENABLED("Enabled", true),

	CLOCK_UDPATE_FREQUENCY("Clock.UpdateFrequency", 200),
	CLOCK_HOLO_LIST("Clock.HoloList", new ArrayList<String>(){{add("clock-spawn");add("clock-respawn");}}),
	CLOCK_TIME_RANGE_MESSAGES("Clock.TimeRanges",new ArrayList<Map>(){{
		add(new HashMap<String, Object>(){{
			put("beginning", 0);
			put("end", 12000);
			put("message", "&eDay ");
		}});
		add(new HashMap<String, Object>(){{
			put("beginning", 12001);
			put("end", 24000);
			put("message", "&9Night ");
		}});
	}}),


	TELEPORT_DELAY("TeleportDelay", 5),
	ORB_CLEARING_BOX_SIZE("OrbClearBoxSize", 10),
	ORB_CLEARING_INTERVAL("OrbClearInterval", 60),
	MESSAGE_CHECKPOINT_CREATED("Messages.PointCreated", "Checkpoint <Position> in list <List> created!"),
	MESSAGE_LIST_NOT_EXIST("Messages.ListNotExist", "List <List> does not exist!"),
	MESSAGE_LIST_DELETED("Messages.ListDeleted", "List <List> deleted."),
	MESSAGE_CHECKPOINT_NOT_EXIST("Messages.CheckpointNotExist", "Checkpoint <Position> in <List> does not exist!"),
	MESSAGE_CHECKPOINT_MOVED("Messages.CheckpointMoved", "Checkpoint <Position> in <List> moved to your location."),
	MESSAGE_CHECKPOINT_SAVED("Messages.CheckpointSaved", "<Player>, your progress is now saved at checkpoint <Position> in <List>!"),
	MESSAGE_CHECKPOINT_NO_BACKWARDS("Messages.CheckpointNoBackwards", "<Player>, Checkpoint <Position> at <List> is behind your latest checkpoint! Nothing has been saved."),
	MESSAGE_NOTHING_SAVED("Messages.NothingSaved", "<Player>, You need to save checkpoint first before teleporting!"),
	MESSAGE_TELEPORT_SCHEDULED("Messages.TeleportScheduled", "<Player>, You will be teleported in few seconds to your checkpoint! Be completely still until then."),
	MESSAGE_TELEPORT_CANCELLED("Messages.TeleportCancelled", "<Player>, Your teleport was cancelled! Try again and don't move."),
	MESSAGE_TELEPORT_INSTANT("Messages.TeleportInstant", "aReturned you to Checkpoint #<Position>");
	protected String string;
	protected Object def;
	
	private CheckpointsSettings(String string, Object def)
	{
		this.string = string;
		this.def = def;
	}

	public String getString()
	{
		return string;
	}

	public Integer integer()
	{
		return (Integer) MapModule.instance.config.get(string, def);
	}
	
	public String string()
	{
		return (String) MapModule.instance.config.get(string, def);
	}

	public List<String> stringList()
	{
		return MapModule.instance.config.getStringList(string);
	}

	public static String getCommandDescription(String cmd, String def)
	{
		String path = "CommandDescriptions." + cmd;
		
		Object descO = MapModule.instance.config.get(path);
		if (descO == null)
		{
			MapModule.instance.config.set(path, "&a/chp " + cmd + " &8-&f " + def);
			MapModule.instance.saveConfig();
			descO = MapModule.instance.config.get(path);
		}
		
		return (String) descO;
		
	}
}
