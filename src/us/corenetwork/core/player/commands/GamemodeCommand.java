package us.corenetwork.core.player.commands;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.player.PlayerSettings;

public class GamemodeCommand extends BasePlayerCommand {


	public GamemodeCommand()
	{
		desc = "Change player gamemode";
		permission = "gamemode";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		Player target = null;
		GameMode gameMode = null;
		boolean self = false;
		boolean silent = false;
		
		if (args.length == 0 || args.length > 3)
		{
			PlayerUtils.Message("Usage : /gamemode <mode> [<player>] [silent]", sender);
			return;
		}
		
		gameMode = getGameMode(args[0]);
		if (gameMode == null)
		{
			PlayerUtils.Message("Wrong mode, use 0/s/survival or 1/c/creative or 2/a/adventure", sender);
			return;
		}
		
		if (args.length == 1)
		{
			if (sender instanceof Player)
			{
				target = (Player)sender;
				self = true;
			}
			else
			{
				PlayerUtils.Message("You have to use /gamemode <mode> [silent] as player." , sender);
				return;
			}
		}
		else if (args.length == 2)
		{
			target = CorePlugin.instance.getServer().getPlayerExact(args[1]);
			if(target == null)
			{
				if (args[1].toLowerCase().equals("silent"))
				{
					silent = true;
					if (sender instanceof Player)
					{
						target = (Player)sender;
						self = true;
					}
					else
					{
						PlayerUtils.Message("You have to use /gamemode <mode> [silent] as player." , sender);
						return;
					}
				}
				else
				{
					PlayerUtils.Message("Cannot find " + args[1] + " player." , sender);
					return;
				}
			}
		}
		else if (args.length == 3)
		{
			target = CorePlugin.instance.getServer().getPlayerExact(args[1]);
			if(target == null)
			{
				PlayerUtils.Message("Cannot find " + args[1] + " player." , sender);
				return;
			}
			else
			{
				if (args[2].toLowerCase().equals("silent"))
				{
					silent = true;
				}
				else
				{
					PlayerUtils.Message("Usage : /gamemode <mode> [<player>] [silent]", sender);
					return;	
				}
			}
		}
		
		
		target.setGameMode(gameMode);
		
		if(silent == false)
		{
			String gameModeString = gameMode.toString().toLowerCase();
			if (self)
			{
				PlayerUtils.Message(PlayerSettings.MESSAGE_SELF_GAMEMODE.string().replace("<Gamemode>", gameModeString), target);
			}
			else
			{
				PlayerUtils.Message(PlayerSettings.MESSAGE_SET_GAMEMODE.string().replace("<Gamemode>", gameModeString).replace("<Player>", target.getName()), sender);
				PlayerUtils.Message(PlayerSettings.MESSAGE_GAMEMODE_CHANGED.string().replace("<Gamemode>", gameModeString), target);
			}
		}
	}

	public GameMode getGameMode(String arg)
	{
		if (arg.equals("0") || arg.equals("s") || arg.equals("survival"))
		{
			return GameMode.SURVIVAL;
		}
		else if (arg.equals("1") || arg.equals("c") || arg.equals("creative"))
		{
			return GameMode.CREATIVE;
		}
		else if (arg.equals("2") || arg.equals("a") || arg.equals("adventrue"))
		{
			return GameMode.ADVENTURE;
		}
		else
		{
			return null;
		}
	}
}
