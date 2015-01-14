package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.util.PlayerUtils;
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
		OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(playerName);
		RespawnModule.luckyBoosterManager.addPass(offlinePlayer);
		if(offlinePlayer.isOnline())
		{
			PlayerUtils.Message(RespawnSettings.MESSAGE_LUCKY_BOOSTER_BOUGHT.string(), offlinePlayer.getPlayer());
		}
	}
}
