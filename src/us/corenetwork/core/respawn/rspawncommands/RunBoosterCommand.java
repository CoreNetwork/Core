package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.respawn.RespawnModule;
import us.corenetwork.core.respawn.RespawnSettings;

public class RunBoosterCommand extends BaseRSpawnCommand {

	public RunBoosterCommand()
	{
		permission = "runboost";
		desc = "Adds a lucky booster time";
		needPlayer = true;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		
		String path = "Amount."+player.getName().toLowerCase();
		int amountLeft = RespawnModule.instance.storageConfig.getInt(path);
		
		if(amountLeft > 0)
		{
			RespawnModule.instance.storageConfig.set(path, amountLeft - 1);
			
			long luckyTimer = RespawnModule.instance.storageConfig.getLong("luckyTimer", 0);
			luckyTimer += RespawnSettings.LUCKYBOOSTER.integer() * 60 * 20;
			RespawnModule.instance.storageConfig.set("luckyTimer", luckyTimer);
			
			RespawnModule.instance.saveStorageYaml();

			PlayerUtils.Message(RespawnSettings.MESSAGE_ACTIVATED_LUCKY_BOOSTER.string(), player);
		}
		else
		{
			PlayerUtils.Message(RespawnSettings.MESSAGE_NO_LUCKY_BOOSTER.string(), player);
		}
	}
}
