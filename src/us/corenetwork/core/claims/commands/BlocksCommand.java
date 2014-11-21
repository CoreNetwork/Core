package us.corenetwork.core.claims.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CLog;
import us.corenetwork.core.ClaimBlocks;
import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.claims.ClaimPacket;
import us.corenetwork.core.claims.ClaimsSettings;

public class BlocksCommand extends BaseClaimsCommand{

	public BlocksCommand()
	{
		desc = "List your blocks";
		permission = "blocks.buy";
		needPlayer = true;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		List<String> msgs = new ArrayList<String>();
		String amount = "<Amount>";
		ClaimBlocks cBlocks = GriefPreventionHandler.getPlayerClaimBlocks((Player)sender);
		
		int gameplay = cBlocks.accrued;
		int ranks = cBlocks.rank;
		int bought = ClaimPacket.getAmountOfPacketsBought((Player)sender) * ClaimsSettings.BUYING_PACKET_SIZE.integer();
		int other = cBlocks.bonus - bought;
		int total = cBlocks.accrued + cBlocks.bonus + ranks;
		int available = cBlocks.remaining;
		int used = total - available;
		
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_HEADER.string());
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_LINE.string());
		
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_GAMEPLAY.string().replace(amount, gameplay+""));
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_RANKS.string().replace(amount, ranks+""));
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_BOUGHT.string().replace(amount, bought+""));
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_OTHER.string().replace(amount, other+""));
		
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_LINE.string());
		
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_TOTAL.string().replace(amount, total+""));
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_USED.string().replace(amount, used+""));
		msgs.add(ClaimsSettings.BLOCKS_MESSAGES_AVAILABLE.string().replace(amount, available+""));
		
		
		Player player = (Player) sender;
		
		for(String msg : msgs)
		{
			PlayerUtils.Message(msg, sender);
		}
	}

}
