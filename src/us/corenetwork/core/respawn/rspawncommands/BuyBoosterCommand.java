package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.respawn.RespawnModule;
import us.corenetwork.core.respawn.RespawnSettings;
import us.corenetwork.core.util.Util;

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
		if(args.length != 1 && args.length != 2)
		{
			PlayerUtils.Message("Usage: /core buylucky <playerName> [<amount>]", sender);
			return;
		}

		int amount = 1;

		if(args.length == 2)
		{
			if(Util.isInteger(args[1]))
			{
				amount = Integer.parseInt(args[1]);
			}
			else
			{
				PlayerUtils.Message("[<amount>] must be an integer.", sender);
				return;
			}
		}

		String playerName = args[0];
		OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(playerName);
		RespawnModule.luckyBoosterManager.addPass(offlinePlayer, amount);
		if(offlinePlayer.isOnline())
		{
			PlayerUtils.Message(RespawnSettings.MESSAGE_LUCKY_BOOSTER_BOUGHT.string(), offlinePlayer.getPlayer());
		}
	}
}
