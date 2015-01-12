package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.respawn.RespawnModule;
import us.corenetwork.core.respawn.RespawnSettings;

public class RunBoosterCommand extends BaseRSpawnCommand {

	public RunBoosterCommand()
	{
		permission = "runlucky";
		desc = "Adds a lucky booster time";
		needPlayer = true;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;

		if(RespawnModule.luckyBoosterManager.getAmount(player) <= 0)
		{
			PlayerUtils.Message(RespawnSettings.MESSAGE_NO_LUCKY_BOOSTER.string(), player);

		}
		else
		{
			RespawnModule.luckyBoosterManager.removePass(player);
			RespawnModule.luckyBoosterManager.runPass(player);

			PlayerUtils.Broadcast(RespawnSettings.MESSAGE_ACTIVATED_LUCKY_BOOSTER_BROADCAST.string().replace("<Player>", player.getName()), player.getName());
			PlayerUtils.Message(RespawnSettings.MESSAGE_ACTIVATED_LUCKY_BOOSTER.string(), player);
		}
	}
}
