package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.respawn.RespawnModule;
import us.corenetwork.core.respawn.RespawnSettings;

public class BuyBoosterCommand extends BaseRSpawnCommand{

	public BuyBoosterCommand()
	{
		permission = "buylucky";
		desc = "Adds one lucky booster to callers pool, run later with /core runlucky";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		if(args.length != 1)
		{
			PlayerUtils.Message("Usage: /core buylucky <playerName>", sender);
			return;
		}
		String playerName = args[0]; 
		
		String path = "Amount."+playerName.toLowerCase();
		int amountLeft = RespawnModule.instance.storageConfig.getInt(path);
		
		RespawnModule.instance.storageConfig.set(path, amountLeft + 1);
		RespawnModule.instance.saveStorageYaml();
		
		Player player = CorePlugin.instance.getServer().getPlayer(playerName);
		if(player != null)
		{
			PlayerUtils.Message(RespawnSettings.MESSAGE_LUCKY_BOOSTER_BOUGHT.string(), player);
		}
	}
}
