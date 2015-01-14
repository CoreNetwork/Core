package us.corenetwork.core.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import us.corenetwork.core.CLog;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;
import us.corenetwork.core.scoreboard.CoreScoreboardManager;

public class VanishManager {

	private final String VANISH_TEAM = "vanish";
	private final String MOD_GROUP = "Overseer";
	
	private Set<Player> vanishedPlayers;
	private Scoreboard scoreboard;
	
	public VanishManager()
	{
		vanishedPlayers = new HashSet<Player>();
		initializeScoreboard();
	}
	
	
	private void initializeScoreboard()
	{
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboard.registerNewTeam(VANISH_TEAM);
		
		for(Player onlinePlayer: Bukkit.getServer().getOnlinePlayers()) 
		{	
			if (canSeeAll(onlinePlayer))
				addToSeeAllGroup(onlinePlayer);
		}
	}
	
	public void addToSeeAllGroup(Player player) 
	{
		scoreboard.getTeam(VANISH_TEAM).addPlayer(player);
		CoreScoreboardManager.registerScoreboard(player, 3, scoreboard);
	}
	
	public void removeFromSeeAllGroup(Player player)
	{
		scoreboard.getTeam(VANISH_TEAM).removePlayer(player);
		CoreScoreboardManager.unregisterScoreboard(player, 3);
	}
	
	public boolean canSeeAll(Player player)
	{
		return CorePlugin.permission.playerInGroup(player, MOD_GROUP);
	}
	
	public boolean isVanished(Player player)
	{
		return vanishedPlayers.contains(player);
	}
	
	public void vanish(Player player)
	{
		vanishedPlayers.add(player);
        
		for(Player onlinePlayer: Bukkit.getServer().getOnlinePlayers()) 
		{
			if(onlinePlayer.equals(player))
				continue;
			
			if (canSeeAll(onlinePlayer) == false)
			{
				onlinePlayer.hidePlayer(player);
				
				//((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo());
				//((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
			}
		}
		addToSeeAllGroup(player);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 0));		
	}
	
	public void unvanish(Player player)
	{
		vanishedPlayers.remove(player);
		
		for(Player onlinePlayer: Bukkit.getServer().getOnlinePlayers()) 
		{
			if(onlinePlayer.equals(player))
				continue;
			
			if (canSeeAll(onlinePlayer) == false)
			{
				onlinePlayer.showPlayer(player);
				//((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo());
				//((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) onlinePlayer).getHandle()));
			}
		}
		removeFromSeeAllGroup(player);
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
	}
	
	public void notifyModerators(CommandSender sender, String message, Player... ignored)
	{
		boolean alreadySent = false;
		List<Player> ignoredPlayers = Arrays.asList(ignored);
		
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (sender == player)
				alreadySent = true;
			
			if (ignoredPlayers.contains(player))
				continue;
			
			if (Util.hasPermission(player, "core.player.notify"))
				PlayerUtils.Message(message, player);
		}
		
		if (!alreadySent && sender != null && sender instanceof Player)
			PlayerUtils.Message(message, sender);

			CLog.info(message);
	}
}
