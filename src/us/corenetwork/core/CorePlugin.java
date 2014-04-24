package us.corenetwork.core;

import java.util.HashMap;
import java.util.Random;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import us.corenetwork.core.corecommands.BaseCoreCommand;
import us.corenetwork.core.corecommands.CoreHelpCommand;
import us.corenetwork.core.corecommands.ReloadCommand;
import us.corenetwork.core.corecommands.SudoCommand;
public class CorePlugin extends JavaPlugin {
	public static CorePlugin instance;
	
	public static Permission permission;
	
	public static Random random;
	
	public static HashMap<String, AbstractCoreCommand> coreCommands = new HashMap<String, AbstractCoreCommand>();
	
	@Override
	public void onEnable() {
		instance = this;
		random = new Random();
		
		coreCommands.put("help", new CoreHelpCommand());
		coreCommands.put("reload", new ReloadCommand());
		coreCommands.put("sudo", new SudoCommand());
		
		IO.LoadSettings();
		
		if (!setupPermissions())
		{
			getLogger().warning("could not load Vault permissions - did you forget to install Vault?");
		}
		
		CoreModule.loadModules();
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}
	
	@Override
	public void onDisable() {
		CoreModule.unloadAll();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if (command.getName().equals("sudo"))
		{
			return coreCommands.get("sudo").execute(sender, args, false);
		}
		
		if (args.length < 1 || Util.isInteger(args[0]))
			return coreCommands.get("help").execute(sender, args, true);

		AbstractCoreCommand cmd = coreCommands.get(args[0]);
		if (cmd != null)
			return cmd.execute(sender, args, true);
		else
			return coreCommands.get("help").execute(sender, args, true);
	}
}
