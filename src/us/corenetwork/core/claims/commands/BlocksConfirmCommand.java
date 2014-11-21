package us.corenetwork.core.claims.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CLog;
import us.corenetwork.core.claims.ClaimPacket;

public class BlocksConfirmCommand extends BaseClaimsCommand{

	public BlocksConfirmCommand()
	{
		desc = "Confirm buying a packet of claim blocks";
		permission = "blocks.buy";
		needPlayer = true;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		ClaimPacket.confirmPurchase((Player) sender);
	}

}
