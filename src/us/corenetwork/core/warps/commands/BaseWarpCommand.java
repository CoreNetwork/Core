package us.corenetwork.core.warps.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.Setting;
import us.corenetwork.core.Settings;
import us.corenetwork.core.Util;

public abstract class BaseWarpCommand {
	public Boolean needPlayer;
	public String permission;
	public String desc;
	
	public abstract void run(CommandSender sender, String[] args);
	
	public boolean execute(CommandSender sender, String[] args, boolean stripArgs)
	{
		if (stripArgs)
		{
			if (args.length > 0 && !Util.isInteger(args[0]))
			{
				String[] newargs = new String[args.length - 1];
				for (int i = 1; i < args.length; i++)
				{
					newargs[i - 1] = args[i];
				}
				args = newargs;			
			}
		}

		if (!(sender instanceof Player) && needPlayer) 
		{
		Util.Message("Sorry, but you need to execute this command as player.", sender);
			return true;
		}
		if (sender instanceof Player && !Util.hasPermission(sender, "core.warps.commands." + permission)) 
		{
			Util.Message(Settings.getString(Setting.MESSAGE_NO_PERMISSION), sender);
			return true;
		}
		
		run(sender, args);
		return true;
	}

}