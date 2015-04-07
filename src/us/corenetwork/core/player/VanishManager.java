package us.corenetwork.core.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import us.corenetwork.core.CLog;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.ScoreboardUtils;
import us.corenetwork.core.util.Util;
import us.corenetwork.core.scoreboard.CoreScoreboardManager;

public class VanishManager {

	private final String MOD_GROUP_DUTY = "Overseer";
	private final String MOD_GROUP_OFF_DUTY = "Guardian";
	private final String MOD_SCOREBOARD_TEAM = "rankTeamA"; //Teams only change on login so guardians stay in guardian team even after duty mode switch.
															//This is hardcoded bfytw
	private Set<UUID> vanishedPlayers;

	public VanishManager()
	{
		vanishedPlayers = new HashSet<UUID>();
		initializeScoreboard();
	}
	
	private void initializeScoreboard()
	{
		ScoreboardUtils.getOrCreateTeam(CoreScoreboardManager.getTeamsScoreboard(), MOD_SCOREBOARD_TEAM).setCanSeeFriendlyInvisibles(true);
	}

	public boolean canSeeAll(Player player)
	{
		return CorePlugin.permission.playerInGroup(player, MOD_GROUP_DUTY) || CorePlugin.permission.playerInGroup(player, MOD_GROUP_OFF_DUTY);
	}
	
	public boolean isVanished(Player player)
	{
		return vanishedPlayers.contains(player.getUniqueId());
	}
	
	public void vanish(Player player)
	{
		vanishedPlayers.add(player.getUniqueId());
        
		for(Player onlinePlayer: Bukkit.getServer().getOnlinePlayers()) 
		{
			if(onlinePlayer.equals(player))
				continue;
			
			if (!canSeeAll(onlinePlayer))
			{
				onlinePlayer.hidePlayer(player);
				
				//((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo());
				//((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
			}
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 0));
	}
	
	public void unvanish(Player player)
	{
		vanishedPlayers.remove(player.getUniqueId());
		
		for(Player onlinePlayer: Bukkit.getServer().getOnlinePlayers()) 
		{
			if(onlinePlayer.equals(player))
				continue;
			
			if (!canSeeAll(onlinePlayer))
			{
				onlinePlayer.showPlayer(player);
				//((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo());
				//((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) onlinePlayer).getHandle()));
			}
		}
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
