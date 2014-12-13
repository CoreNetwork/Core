package us.corenetwork.core.claims;

import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import us.corenetwork.core.CLog;
import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.claims.commands.BaseClaimsCommand;
import us.corenetwork.core.claims.commands.BlocksBuyCommand;
import us.corenetwork.core.claims.commands.BlocksCommand;
import us.corenetwork.core.claims.commands.BlocksConfirmCommand;
import us.corenetwork.core.claims.commands.ClaimsListCommand;
import us.corenetwork.core.claims.commands.TrappedCommand;
import us.corenetwork.core.map.admincommands.BaseCheckpointCommand;
import us.corenetwork.core.map.usercommands.BaseCheckpointUserCommand;

public class ClaimsModule extends CoreModule {

	public static ClaimsModule instance;
	
	public static HashMap<String, BaseClaimsCommand> commands;
	
	public ClaimsModule()
	{
		super("Claims", new String[] {"trapped", "blocks", "claimslist"}, "claims");
		
		instance = this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if (command.getName().equals("blocks") && args.length > 0)
		{
			BaseClaimsCommand baseCommand = commands.get(args[0]);
			if (baseCommand != null)
				return baseCommand.execute(sender, args, true);
		}
		
		BaseClaimsCommand baseCommand = commands.get(command.getName());
		if (baseCommand != null)
			return baseCommand.execute(sender, args, true);
		
		
		return false;
	}
	
	@Override
	protected boolean loadModule()
	{
		if(CorePlugin.instance.getServer().getPluginManager().getPlugin("GriefPrevention") == null)
		{
			CLog.info("Claims module requires GriefPrevention. Skipping.");
			return false;
		}
		
		for (ClaimsSettings setting : ClaimsSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();

		ClaimPacket.reloadValues();
		
		commands = new HashMap<String, BaseClaimsCommand>();
		commands.put("blocks", new BlocksCommand());
		commands.put("buy", new BlocksBuyCommand());
		commands.put("confirm", new BlocksConfirmCommand());
		
		CorePlugin.coreCommands.put("trapped", new TrappedCommand());
		CorePlugin.coreCommands.put("claimslist", new ClaimsListCommand());
		
		return true;
	}
	
	@Override
	protected void unloadModule()
	{
	}
	
	
	
	@Override
	public void loadConfig()
	{
		super.loadConfig();
		ClaimPacket.reloadValues();
	}
}
