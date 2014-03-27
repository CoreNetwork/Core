package us.corenetwork.core.player;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.scoreboard.CoreScoreboardManager;

public class VanishManager {

	private final String VANISH_TEAM = "vanish";
	private final String MOD_GROUP = "Guardian";
	
	private ArrayList<Player> vanishedPlayers;
	private Scoreboard scoreboard;
	
	public VanishManager()
	{
		vanishedPlayers = new ArrayList<Player>();
		initializeScoreboard();	
	}
	
	
	private void initializeScoreboard()
	{
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboard.registerNewTeam(VANISH_TEAM);
		
		Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for(Player onlinePlayer: onlinePlayers) 
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
		
		Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for(Player onlinePlayer: onlinePlayers) 
		{
			if(onlinePlayer.equals(player))
				continue;
			
			if (CorePlugin.permission.playerInGroup(onlinePlayer, MOD_GROUP) == false)
				onlinePlayer.hidePlayer(player);

		}
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 0));		
	}
	
	public void unvanish(Player player)
	{
		vanishedPlayers.remove(player);
		
		Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for(Player onlinePlayer: onlinePlayers) 
		{
			if(onlinePlayer.equals(player))
				continue;
			
			if (CorePlugin.permission.playerInGroup(onlinePlayer, MOD_GROUP) == false)
				onlinePlayer.showPlayer(player);
		}

		player.removePotionEffect(PotionEffectType.INVISIBILITY);
	}


	
		
	
}