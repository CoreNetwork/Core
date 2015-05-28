package us.corenetwork.core.scoreboard;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		CoreScoreboardManager.getPlayerData(event.getPlayer().getName()).init(event.getPlayer());

		EntityPlayer nmsPlayer = ((CraftPlayer) event.getPlayer()).getHandle();

		//Send all teams to the player
		for (Object team : CoreScoreboardManager.nmsTeamStorage.getTeams())
			nmsPlayer.playerConnection.sendPacket(new PacketPlayOutScoreboardTeam((ScoreboardTeam) team, 0));
	}
	
	@EventHandler()
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		CoreScoreboardManager.scoreboards.remove(event.getPlayer().getName());
	}
}
