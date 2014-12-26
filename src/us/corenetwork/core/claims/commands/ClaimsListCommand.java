package us.corenetwork.core.claims.commands;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CLog;
import us.corenetwork.core.ClaimSimple;
import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.claims.ClaimsModule;
import us.corenetwork.core.claims.ClaimsSettings;

public class ClaimsListCommand extends BaseClaimsCommand{

	private static Comparator<ClaimSimple> comp = new Comparator<ClaimSimple>() {
		@Override
		public int compare(ClaimSimple o1, ClaimSimple o2)
		{
			return Integer.compare(o1.area, o2.area);
		}
	};
	
	public ClaimsListCommand()
	{
		desc = "List your claims";
		permission = "list";
		needPlayer = true;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		String world = "world";
		String nether = "world_nether";
		Player player = (Player)sender;
		
		List<ClaimSimple> worldClaims = GriefPreventionHandler.getPlayerClaimsSimple(player, world);
		List<ClaimSimple> netherClaims = GriefPreventionHandler.getPlayerClaimsSimple(player, nether);
		
		if(worldClaims.size() == 0 && netherClaims.size() == 0)
		{
			PlayerUtils.Message(ClaimsSettings.CLAIMSLIST_MESSAGES_NO_CLAIMS.string(), player);
		}
		else
		{
			printClaimsIfAny(worldClaims, player, world);
			printClaimsIfAny(netherClaims, player, nether);
		}
		
		
	}

	
	private void printClaimsIfAny(List<ClaimSimple> worldClaims, Player player, String worldName)
	{
        World world = Bukkit.getWorld(worldName);
        if(worldClaims.size() == 0)
		{
			return;
		}
		Collections.sort(worldClaims);
		Collections.reverse(worldClaims);
		
		String prettyName = "";
		String claimsMax = "";
		String claimsNow = worldClaims.size() + "";
		String claimMessage = ClaimsSettings.CLAIMSLIST_MESSAGES_CLAIM.string();
		String scClaimMessage = ClaimsSettings.CLAIMSLIST_MESSAGES_SC_CLAIM.string();
		if(worldName.equalsIgnoreCase("world"))
		{
			prettyName = ClaimsSettings.CLAIMSLIST_WORLDS_OW_NAME.string();
		}
		else if(worldName.equalsIgnoreCase("world_nether"))
		{
			prettyName = ClaimsSettings.CLAIMSLIST_WORLDS_NETHER_NAME.string();
		}

        claimsMax = "" + ClaimsModule.instance.claimsAreaProxy.getMaxClaims(player);
		
		String header = ClaimsSettings.CLAIMSLIST_MESSAGES_HEADER.string().replace("<WorldName>", prettyName).replace("<ClaimsNow>", claimsNow).replace("<ClaimsMax>", claimsMax);
		PlayerUtils.Message("", player);
		PlayerUtils.Message(header, player);
		PlayerUtils.Message(ClaimsSettings.CLAIMSLIST_MESSAGES_LINE.string(), player);
		
		for(ClaimSimple cs : worldClaims)
		{
			String readyMessage;
			if(cs.is9by9)
			{
				readyMessage = scClaimMessage.replace("<X>", cs.location.getBlockX()+"").replace("<Z>", cs.location.getBlockZ()+"").replace("<Area>", cs.area+"");
			}
			else
			{
				readyMessage = claimMessage.replace("<X>", cs.location.getBlockX()+"").replace("<Z>", cs.location.getBlockZ()+"").replace("<Area>", cs.area+"");
			}
			
			PlayerUtils.Message(readyMessage, player);
		}
	}
}
