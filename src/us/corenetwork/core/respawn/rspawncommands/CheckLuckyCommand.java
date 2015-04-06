package us.corenetwork.core.respawn.rspawncommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.respawn.RespawnModule;
import us.corenetwork.core.respawn.RespawnSettings;
import us.corenetwork.core.util.PlayerUtils;

public class CheckLuckyCommand extends BaseRSpawnCommand {

	public CheckLuckyCommand()
	{
		permission = "checklucky";
		desc = "Check amount of lucky boosters";
		needPlayer = true;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;

        int amount = RespawnModule.luckyBoosterManager.getAmount(player);
		if(RespawnModule.luckyBoosterManager.getAmount(player) <= 0)
		{
			PlayerUtils.Message(RespawnSettings.MESSAGE_CHECK_LUCKY_NO_BOOSTERS.string(), player);

		}
		else
		{
            String message = RespawnSettings.MESSAGE_CHECK_LUCKY.string();
            message = message.replace("<Amount>", Integer.toString(amount));
            if (amount == 1)
                message = message.replace("<S>", "");
            else
                message = message.replace("<S>", "s");

            PlayerUtils.Message(message, player);
		}
	}
}
