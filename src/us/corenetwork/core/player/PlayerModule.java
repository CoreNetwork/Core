package us.corenetwork.core.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.player.commands.BasePlayerCommand;
import us.corenetwork.core.player.commands.ClearCommand;
import us.corenetwork.core.player.commands.EffectCommand;
import us.corenetwork.core.player.commands.EnchantCommand;
import us.corenetwork.core.player.commands.GamemodeCommand;
import us.corenetwork.core.player.commands.GodCommand;
import us.corenetwork.core.player.commands.KitCommand;
import us.corenetwork.core.player.commands.UngodCommand;
import us.corenetwork.core.player.commands.UnvanishCommand;
import us.corenetwork.core.player.commands.VanishCommand;

public class PlayerModule extends CoreModule {

	public static PlayerModule instance;
	public static VanishManager vanishManager;
	public static Set<String> gods = new HashSet<String>();
	public static Map<String, List<String>> kits;
	public static HashMap<String, BasePlayerCommand> commands;
	
	public PlayerModule()
	{
		super("Player", new String[] {"clear", "vanish", "unvanish", "effect", "enchant", "god", "ungod", "gamemode", "kit"}, "player");
		
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
			if (sender instanceof BlockCommandSender || sender instanceof ConsoleCommandSender)
			{				
				return new org.bukkit.command.defaults.EffectCommand().execute(sender, "effect", args);
			}
			else
			{
				return CorePlugin.coreCommands.get("effect").execute(sender, args, false);
			}
		}
		if (command.getName().equals("enchant"))
		{
			if (sender instanceof BlockCommandSender || sender instanceof ConsoleCommandSender)
			{				
				return new org.bukkit.command.defaults.EnchantCommand().execute(sender, "enchant", args);
			}
			else
			{
				return CorePlugin.coreCommands.get("enchant").execute(sender, args, false);
			}
		}
		if (command.getName().equals("god"))
		{
			return commands.get("god").execute(sender, args, false);
		}
		if (command.getName().equals("ungod"))
		{
			return commands.get("ungod").execute(sender, args, false);
		}
		if (command.getName().equals("gamemode") || command.getName().equals("gm"))
		{
			return commands.get("gamemode").execute(sender, args, false);
		}
		if (command.getName().equals("kit"))
		{
			return commands.get("kit").execute(sender, args, false);
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
		commands.put("enchant", new EnchantCommand());
		commands.put("god", new GodCommand());
		commands.put("ungod", new UngodCommand());
		commands.put("gamemode", new GamemodeCommand());
		commands.put("kit", new KitCommand());

		CorePlugin.coreCommands.put("effect", new EffectCommand());
		CorePlugin.coreCommands.put("enchant", new EnchantCommand());
		
		vanishManager = new VanishManager();
		
		
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), CorePlugin.instance);
		Bukkit.getServer().getPluginManager().registerEvents(new VanishListener(), CorePlugin.instance);
		
		return true;
	}
	
	private void initializeKits()
	{
		ConfigurationSection chSection = config.getConfigurationSection(PlayerSettings.KITS.string);
		kits = new HashMap<String, List<String>>();
		if(chSection != null)
		{
			for(String key : chSection.getKeys(false))
			{
				kits.put(key.toLowerCase(), chSection.getStringList(key));
			}
		}
		
	}
	
	@Override
	protected void unloadModule()
	{
	}
	
	@Override
	public void loadConfig()
	{
		super.loadConfig();

		initializeKits();
	}
}
