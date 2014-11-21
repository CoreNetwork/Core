package us.corenetwork.core.claims;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.corenetwork.core.GriefPreventionHandler;
import us.corenetwork.core.IO;
import us.corenetwork.core.PlayerUtils;

public class ClaimPacket {

	private static List<Integer> costList;
	private static List<PacketResource> resourceList;
	
	private static Map<Player, Integer> awaitingConfirmation = new HashMap<Player, Integer>();
	
	public static void reloadValues()
	{
		costList = ClaimsModule.instance.config.getIntegerList(ClaimsSettings.BUYING_PACKET_COST.string);
		resourceList = new ArrayList<PacketResource>();
		
		List<Map<?,?>> resourcesRaw = ClaimsModule.instance.config.getMapList(ClaimsSettings.BUYING_RESOURCES.string);
		for(Map<?, ?> resourceMap : resourcesRaw)
		{
			resourceList.add(new PacketResource(Material.getMaterial((Integer) resourceMap.get("id")), (String) resourceMap.get("name")));
		}
		
	}
	
	public static int getAmountOfPacketsBought(Player player)
	{
		int amount = 0;
		
		try
		{
			PreparedStatement statement = IO.getConnection().prepareStatement("SELECT BlocksPacketsBought FROM players WHERE PlayerUUID = ? LIMIT 1");
			
			statement.setString(1, player.getUniqueId().toString());
			
			ResultSet set = statement.executeQuery();
			if(set.next())
			{
				amount = set.getInt("BlocksPacketsBought");
			}
			else
			{
				PreparedStatement statement2 = IO.getConnection().prepareStatement("INSERT INTO players (PlayerUUID, BlocksPacketsBought) VALUES (?,?)");
				statement2.setString(1, player.getUniqueId().toString());
				statement2.setInt(2, 0);
				statement2.executeUpdate();
				statement2.close();
			}
			statement.close();
			IO.getConnection().commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return amount;
	}
	
	
	public static void trySchedulePurchase(Player player)
	{
		//already scheduled, remove last, do it again?
		awaitingConfirmation.remove(player);
		
		int packetsBoughtAlready = getAmountOfPacketsBought(player);
		
		if(packetsBoughtAlready >= costList.size() * resourceList.size())
		{
			PlayerUtils.Message(ClaimsSettings.BUYING_MESSAGE_BOUGHT_ALL.string(), player);
			return;
		}

		//No need to increase packetsBoughtAlready here, coz counting arrays from 0!
		
		int amount = costList.get(packetsBoughtAlready % costList.size());
		PacketResource resource = resourceList.get(packetsBoughtAlready / costList.size());
		
		awaitingConfirmation.put(player, packetsBoughtAlready);
		PlayerUtils.Message(ClaimsSettings.BUYING_MESSAGE_CONFIRM_INFO.string(), player);
		
		new FancyMessage(ChatColor.translateAlternateColorCodes('&', ClaimsSettings.BUYING_MESSAGE_CONFIRM_PREFIX.string().replace("<Resource>", resource.name)))
		                .then(ChatColor.translateAlternateColorCodes('&', ClaimsSettings.BUYING_MESSAGE_CONFIRM_BUTTON.string().replace("<Amount>", amount+"")))
		                	.command("/blocks confirm")
		                .then(ChatColor.translateAlternateColorCodes('&', ClaimsSettings.BUYING_MESSAGE_CONFIRM_SUFFIX.string()))
		                .send(player);
		
	}
	
	public static void confirmPurchase(Player player)
	{
		
		//Nothing to confirm from this player, quit silently.
		if(awaitingConfirmation.containsKey(player) == false)
		{
			return;
		}
		
		int packetNumber = awaitingConfirmation.get(player);
		
		if(hasResources(player, packetNumber))
		{
			removeResources(player, packetNumber);
			addClaimPacket(player);
			saveAmountBought(player, packetNumber);
			awaitingConfirmation.remove(player);
			PlayerUtils.Message(ClaimsSettings.BUYING_MESSAGE_BOUGHT.string(), player);
		}
		else
		{
			PlayerUtils.Message(ClaimsSettings.BUYING_MESSAGE_NOT_ENOUGH.string(), player);
		}
	}


	private static boolean hasResources(Player player, int packetNumber)
	{
		int amount = costList.get(packetNumber % costList.size());
		PacketResource resource = resourceList.get(packetNumber / costList.size());
		
		return player.getInventory().contains(resource.material, amount);
	}


	private static void removeResources(Player player, int packetNumber)
	{

		int amount = costList.get(packetNumber % costList.size());
		PacketResource resource = resourceList.get(packetNumber / costList.size());

		Inventory inventory = player.getInventory();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack stack = inventory.getItem(i);
			if (stack != null && stack.getType() == resource.material)
			{
				int newamount = stack.getAmount() - amount;
				if (newamount > 0)
				{
					stack.setAmount(newamount);
					break;
				} 
				else
				{
					inventory.setItem(i, null);
					amount = -newamount;
					if (amount == 0)
						break;
				}
			}
		}
	}


	private static void addClaimPacket(Player player)
	{
		GriefPreventionHandler.addBonusClaimBlocks(player, ClaimsSettings.BUYING_PACKET_SIZE.integer());
	}


	private static void saveAmountBought(Player player, int packetNumber)
	{
		try
		{
			PreparedStatement statement = IO.getConnection().prepareStatement("UPDATE players SET BlocksPacketsBought = ? WHERE PlayerUUID = ?");
			statement.setString(2, player.getUniqueId().toString());
			statement.setInt(1, packetNumber + 1);
			statement.executeUpdate();
			statement.close();
			IO.getConnection().commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
}
