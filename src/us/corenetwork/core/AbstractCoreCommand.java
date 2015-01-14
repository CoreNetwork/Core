package us.corenetwork.core;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;

public abstract class AbstractCoreCommand {

	public String desc;
	public Boolean needPlayer;

	public String permissionNode;
	public String permission;
	
	public abstract void run(CommandSender sender, String[] args);
	
	public boolean execute(CommandSender sender, String[] args, boolean stripArgs)
	{
		if (!(sender instanceof Player) && needPlayer) 
		{
			PlayerUtils.Message("Sorry, but you need to execute this command as player.", sender);
			return true;
		}
		
		if (sender instanceof Player && !Util.hasPermission(sender, permissionNode + permission))
		{
			PlayerUtils.Message(Settings.getString(Setting.MESSAGE_NO_PERMISSION), sender);
			return true;
		}
		
		if (stripArgs && args.length > 0 && !Util.isInteger(args[0]))
		{
			args = Arrays.copyOfRange(args, 1, args.length);
		}

		run(sender, args);
		return true;
	}
}
