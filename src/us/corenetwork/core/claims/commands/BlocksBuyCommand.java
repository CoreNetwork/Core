package us.corenetwork.core.claims.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CLog;
import us.corenetwork.core.claims.ClaimPacket;

public class BlocksBuyCommand extends BaseClaimsCommand{

	public BlocksBuyCommand()
	{
		desc = "Schedule buying a packet of claim blocks";
		permission = "blocks.buy";
		needPlayer = true;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		ClaimPacket.trySchedulePurchase((Player) sender);
	}

}
