package us.corenetwork.core.teleport.commands.tp;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.PlayerUtils.PickPlayerResult;
import us.corenetwork.core.Util;
import us.corenetwork.core.corecommands.SudoCommand;
import us.corenetwork.core.teleport.TeleportSettings;
import us.corenetwork.core.teleport.TeleportUtil;

public class SwapCommand extends BaseTpCommand {		
	public SwapCommand()
	{
		desc = "Swap two players";
		permission = "swap";
		needPlayer = true;
	}


	public void run(final CommandSender sender, String[] args) {
		int argsSize = args.length;
		boolean silent = false;
		
		if (argsSize > 0 && args[argsSize - 1].equalsIgnoreCase("silent"))
		{
			argsSize--;
			
			if (Util.hasPermission(sender, "core.teleport.silent"))
				silent = true;
		}
		
		if (argsSize == 2)
		{
			
			// "/swap name name"
			swap(sender, args[0], args[1], silent);
			return;
		}
		
		PlayerUtils.Message("Usage: /swap [player] [player]", sender);
	}		
	
	public void swap(CommandSender sender, String playerName1, String playerName2, boolean silent)
	{
		Player player1 = null;
		Player player2 = null;
		
		PickPlayerResult searchResult = PlayerUtils.pickPlayer(playerName1);
		switch (searchResult.result)
		{
		case OK:
			player1 = (Player) searchResult.player;
			break;
		case NOT_FOUND:
			String message = TeleportSettings.MESSAGE_UNKNOWN_PLAYER.string();
			message = message.replace("<Name>", playerName1);

			PlayerUtils.Message(message, sender);
			return;
		case AMBIGUOUS:
			message = TeleportSettings.MESSAGE_AMBIGUOUS_MATCH.string();
			message = message.replace("<Name>", playerName1);

			PlayerUtils.Message(message, sender);
			return;
		}
		
		searchResult = PlayerUtils.pickPlayer(playerName2);
		switch (searchResult.result)
		{
		case OK:
			player2 = (Player) searchResult.player;
			break;
		case NOT_FOUND:
			String message = TeleportSettings.MESSAGE_UNKNOWN_PLAYER.string();
			message = message.replace("<Name>", playerName1);

			PlayerUtils.Message(message, sender);
			return;
		case AMBIGUOUS:
			message = TeleportSettings.MESSAGE_AMBIGUOUS_MATCH.string();
			message = message.replace("<Name>", playerName1);

			PlayerUtils.Message(message, sender);
			return;
		}
		
		Location destinationPlayer1 = player2.getLocation();
		Location destinationPlayer2 = player1.getLocation();
		
		if (!silent && !(sender instanceof Player && SudoCommand.isUnderSudo(sender.getName())))
		{
			String message = TeleportSettings.MESSAGE_SWAPPED_WITH_PLAYER.string();
			message = message.replace("<Player>", player2.getName());
			PlayerUtils.Message(message, player1);
			
			message = TeleportSettings.MESSAGE_SWAPPED_WITH_PLAYER.string();
			message = message.replace("<Player>", player1.getName());
			PlayerUtils.Message(message, player2);
			
			String notice = TeleportSettings.MESSAGE_PLAYER_SWAPPED_WITH_PLAYER.string();
			notice = notice.replace("<Player1>", player1.getName());
			notice = notice.replace("<Player2>", player2.getName());
			TeleportUtil.notifyModerators(sender, notice, player1, player2);
		}

		player1.teleport(destinationPlayer1);
        player2.teleport(destinationPlayer2);

	}
}
