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
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.RedirectedVanillaCommand;
import us.corenetwork.core.player.commands.BasePlayerCommand;
import us.corenetwork.core.player.commands.ClearCommand;
import us.corenetwork.core.player.commands.DMCommand;
import us.corenetwork.core.player.commands.DelayCommand;
import us.corenetwork.core.player.commands.EffectCommand;
import us.corenetwork.core.player.commands.EnchantCommand;
import us.corenetwork.core.player.commands.GamemodeCommand;
import us.corenetwork.core.player.commands.GodCommand;
import us.corenetwork.core.player.commands.KitCommand;
import us.corenetwork.core.player.commands.RemindCommand;
import us.corenetwork.core.player.commands.UngodCommand;
import us.corenetwork.core.player.commands.UnvanishCommand;
import us.corenetwork.core.player.commands.VanishCommand;
import us.corenetwork.core.player.commands.XpCommand;

public class PlayerModule extends CoreModule {

	public static PlayerModule instance;
	public static VanishManager vanishManager;
	public static Set<String> gods = new HashSet<String>();
	public static Map<String, List<String>> kits;
	public static HashMap<String, BasePlayerCommand> commands;
	
	public PlayerModule()
	{
		super("Player", new String[] {"clear", "vanish", "unvanish", "god", "ungod", "kit", "dm", "delay"}, "player");
		
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
			return CorePlugin.coreCommands.get("effect").execute(sender, args, false);
		}
		if (command.getName().equals("enchant"))
		{
			return CorePlugin.coreCommands.get("enchant").execute(sender, args, false);
		}
		if (command.getName().equals("god"))
		{
			return commands.get("god").execute(sender, args, false);
		}
		if (command.getName().equals("ungod"))
		{
			return commands.get("ungod").execute(sender, args, false);
		}
		if (command.getName().equals("kit"))
		{
			return commands.get("kit").execute(sender, args, false);
		}
		if (command.getName().equals("dm"))
		{
			return commands.get("dm").execute(sender, args, false);
		}
		if (command.getName().equals("delay"))
		{
			return commands.get("delay").execute(sender, args, false);
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
		commands.put("kit", new KitCommand());
		commands.put("dm", new DMCommand());
		commands.put("delay", new DelayCommand());

		CorePlugin.coreCommands.put("effect", new EffectCommand());
		CorePlugin.coreCommands.put("enchant", new EnchantCommand());
		CorePlugin.coreCommands.put("remind", new RemindCommand());
		CorePlugin.coreCommands.put("xp", new XpCommand());
		CorePlugin.coreCommands.put("gamemode", new GamemodeCommand());

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
