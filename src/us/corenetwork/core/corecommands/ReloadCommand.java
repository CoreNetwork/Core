package us.corenetwork.core.corecommands;

import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.IO;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.Setting;
import us.corenetwork.core.Settings;

public class ReloadCommand extends BaseCoreCommand {	
	public ReloadCommand()
	{
		desc = "Reload config";
		needPlayer = false;
		permission = "reload";
	}


	public void run(final CommandSender sender, String[] args) 
	{
		IO.LoadSettings();
		CoreModule.reloadConfigs();
		PlayerUtils.Message(Settings.getString(Setting.MESSAGE_CONFIGURATION_RELOADED), sender);
	}	
}
