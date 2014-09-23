package us.corenetwork.core.player.commands;

import java.util.List;
import net.minecraft.server.v1_7_R4.PlayerSelector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.corecommands.SudoCommand;
import us.corenetwork.core.player.PlayerModule;
import us.corenetwork.core.player.PlayerSettings;
import us.corenetwork.core.respawn.RespawnSettings;

public class KitCommand extends BasePlayerCommand {

	public KitCommand()
	{
		desc = "Gives a player a kit of items."; 
		permission = "kit";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		if(args.length < 1 || args.length > 3)
		{
			PlayerUtils.Message("Usage: /kit [<player>] <kit> [silent]", sender);
			return;
		}
		
		String kit = "";
		Player player = null;
		boolean silent = false;
		boolean selfCast = false;
		
		if(args.length == 1)
		{
			kit = args[0];
			
			if(sender instanceof Player)
			{
				player = (Player) sender;
				selfCast = true;
			}
			else
			{
				PlayerUtils.Message("You have to execute /kit <kit> as a player.", sender);
				return;
			}
		}
		else if(args.length == 3)
		{
			player = CorePlugin.instance.getServer().getPlayer(args[0]);
			if(player == null)
			{
				PlayerUtils.Message("Could not find player called " + args[0], sender);
				return;
			}
			
			kit = args[1];
			
			if(args[2].toLowerCase().equals("silent"))
			{
				silent = true;
			}
			else
			{
				PlayerUtils.Message("Usage: /kit [<player>] <kit> [silent]", sender);
				return;
			}
		}
		else if(args.length == 2)
		{
			if(args[1].toLowerCase().equals("silent"))
			{
				silent = true;
				kit = args[0];
				
				if(sender instanceof Player)
				{
					player = (Player) sender;
					selfCast = true;
				}
				else
				{
					PlayerUtils.Message("You have to execute /kit <kit> as a player.", sender);
					return;
				}
			}
			else
			{
				player = CorePlugin.instance.getServer().getPlayer(args[0]);
				if(player == null)
				{
					PlayerUtils.Message("Could not find player called " + args[0], sender);
					return;
				}
				
				kit = args[1];
			}
		}
		
		if(selfCast == false && sender instanceof Player)
		{
			if(((Player) sender).getName().equals(player.getName()))
			{
				selfCast = true;
			}
		}
		
		kit = kit.toLowerCase();
		if(kitExists(kit) == false)
		{
			PlayerUtils.Message("Could not find " + kit + " kit.", sender);
			return;
		}
		
		if(silent == false)
		{
			PlayerUtils.Message(PlayerSettings.MESSAGE_KIT_RECEIVED.string().replace("<Kit>", kit), player);
			if(selfCast == false)
			{
				PlayerUtils.Message(PlayerSettings.MESSAGE_KIT_GRANTED.string().replace("<Kit>", kit).replace("<Player>", player.getName()), sender);
			}
		}
		
		spawnKit(player, kit);
	}

	
	private boolean kitExists(String kit)
	{
		return PlayerModule.kits.containsKey(kit);
	}
	
	private void spawnKit(Player player, String kit)
	{
		List<String> commList = PlayerModule.kits.get(kit);
		for(String comm : commList)
		{
			SudoCommand.sudo(player, comm);
		}
	}
}
