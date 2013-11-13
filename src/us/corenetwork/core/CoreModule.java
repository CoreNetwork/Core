package us.corenetwork.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import us.corenetwork.core.calculator.CalculatorModule;
import us.corenetwork.core.checkpoints.CheckpointsModule;
import us.corenetwork.core.respawn.RespawnModule;
import us.corenetwork.core.scoreboard.ScoreboardModule;
import us.corenetwork.core.teleport.TeleportModule;

public abstract class CoreModule implements CommandExecutor {
	private String moduleName;

	private String configName;
	private String[] commands;

	public boolean active = false;

	public YamlConfiguration config;
	public YamlConfiguration storageConfig;

	protected CoreModule(String name, String[] commands, String configName)
	{
		this.moduleName = name;
		this.configName = configName;
		this.commands = commands;
	}

	protected abstract boolean loadModule();
	protected abstract void unloadModule();

	private boolean loadModuleInternal()
	{
		CLog.info("Loading module " + moduleName + "....");

		if (configName != null)
		{
			loadConfig();

			Boolean enabled = (Boolean) config.get("Enabled");
			if (enabled == null)
			{
				config.set("Enabled", true);
				saveConfig();

				enabled = true;
			}

			if (!enabled)
			{
				CLog.info("Module disabled. Skipping.");
				return false;
			}

		}

		if (commands != null)
		{
			for (String command : commands)
			{
				CorePlugin.instance.getCommand(command).setExecutor(this);
			}
		}

		return loadModule();
	}

	public void loadConfig()
	{
		File configFile = new File(CorePlugin.instance.getDataFolder(), configName.concat(".yml"));


		config = new YamlConfiguration();

		if (configFile.exists())
		{
			try {
				config.load(configFile);
			} catch (FileNotFoundException e) {
				CLog.severe("Error while loading conifg for module " + moduleName + ".");

				e.printStackTrace();
				return;
			} catch (IOException e) {
				CLog.severe("Error while loading conifg for module " + moduleName + ".");

				e.printStackTrace();
				return;
			} catch (InvalidConfigurationException e) {
				CLog.severe("Error while loading conifg for module " + moduleName + ".");

				e.printStackTrace();
				return;
			}
		}
	}

	public void saveConfig()
	{
		if (config == null)
			return;

		try
		{
			File configFile = new File(CorePlugin.instance.getDataFolder(), configName.concat(".yml"));

			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadStorageYaml()
	{
		File storageFolder = new File(CorePlugin.instance.getDataFolder(), "storage");
		if (!storageFolder.exists())
			storageFolder.mkdir();
		
		File configFile = new File(storageFolder, configName.concat(".yml"));

		storageConfig = new YamlConfiguration();

		if (configFile.exists())
		{
			try {
				storageConfig.load(configFile);
			} catch (FileNotFoundException e) {
				CLog.severe("Error while loading storage for module " + moduleName + ".");

				e.printStackTrace();
				return;
			} catch (IOException e) {
				CLog.severe("Error while loading storage for module " + moduleName + ".");

				e.printStackTrace();
				return;
			} catch (InvalidConfigurationException e) {
				CLog.severe("Error while loading storage for module " + moduleName + ".");

				e.printStackTrace();
				return;
			}
		}
	}

	public void saveStorageYaml()
	{
		if (storageConfig == null)
			return;

		try
		{
			File storageFolder = new File(CorePlugin.instance.getDataFolder(), "storage");			
			File configFile = new File(storageFolder, configName.concat(".yml"));

			storageConfig.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}



	//Module manager

	private static List<CoreModule> modules = new ArrayList<CoreModule>();

	public static void unloadAll()
	{
		for (CoreModule module : modules)
		{
			module.saveConfig();
			module.unloadModule();
			module.active = false;
		}
	}

	public static void loadModules()
	{
		CoreModule module;

		//Checkpoints
		module = new CheckpointsModule();
		if (module.loadModuleInternal())
		{
			module.active = true;
			modules.add(module);
		}

		//Respawn
		module = new RespawnModule();
		if (module.loadModuleInternal())
		{
			module.active = true;
			modules.add(module);
		}

		//Scoreboard
		module = new ScoreboardModule();
		if (module.loadModuleInternal())
		{
			module.active = true;
			modules.add(module);
		}
		
		//Warps
		module = new TeleportModule();
		if (module.loadModuleInternal())
		{
			module.active = true;
			modules.add(module);
		}

		//Calculator
		module = new CalculatorModule();
		if (module.loadModuleInternal())
		{
			module.active = true;
			modules.add(module);
		}
	}

	public static void reloadConfigs()
	{
		for (CoreModule module : modules)
		{
			module.loadConfig();
		}
	}
}
