package us.corenetwork.core.player;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.player.commands.BasePlayerCommand;
import us.corenetwork.core.player.commands.ClearCommand;
import us.corenetwork.core.player.commands.EffectCommand;
import us.corenetwork.core.player.commands.UnvanishCommand;
import us.corenetwork.core.player.commands.VanishCommand;

public class PlayerModule extends CoreModule {

	public static PlayerModule instance;
	public static VanishManager vanishManager;
	
	public static HashMap<String, BasePlayerCommand> commands;
	
	public PlayerModule()
	{
		super("Player", new String[] {"clear", "vanish", "unvanish", "effect"}, "player");
		
		instance = this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if (command.getName().equals("clear"))
		{
			return commands.get("clear").execute(sender, args, false);
		}
		if (command.getName().equals("vanish"))
		{
			return commands.get("vanish").execute(sender, args, false);
		}
		if (command.getName().equals("unvanish"))
		{
			return commands.get("unvanish").execute(sender, args, false);
		}
		if (command.getName().equals("effect"))
		{
			return commands.get("effect").execute(sender, args, false);
		}
		else
		{
			BasePlayerCommand cmd = commands.get(args[0]);
			if (cmd != null)
				return cmd.execute(sender, args, false);
			else
				return false;
		}
	}
	
	@Override
	protected boolean loadModule()
	{
		for (PlayerSettings setting : PlayerSettings.values())
		{
			if (config.get(setting.string) == null)
				config.set(setting.string, setting.def);
		}
		saveConfig();
		
		commands = new HashMap<String, BasePlayerCommand>();
		
		commands.put("clear", new ClearCommand());
		commands.put("vanish", new VanishCommand());
		commands.put("unvanish", new UnvanishCommand());
		commands.put("effect", new EffectCommand());

		vanishManager = new VanishManager();
		
		Bukkit.getServer().getPluginManager().registerEvents(new VoidListener(), CorePlugin.instance);
		Bukkit.getServer().getPluginManager().registerEvents(new VanishListener(), CorePlugin.instance);
		
		return true;
	}
	
	@Override
	protected void unloadModule()
	{
	}
}
