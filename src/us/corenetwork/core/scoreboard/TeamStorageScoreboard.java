package us.corenetwork.core.scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import net.minecraft.server.v1_8_R3.ScoreboardServer;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import us.corenetwork.core.util.ReflectionUtils;

/**
 * Dummy scoreboard that serves as parent to all global teams. All teams are sent to all players
 */
public class TeamStorageScoreboard extends Scoreboard
{
    private static final UnsupportedOperationException unsopportedException = new UnsupportedOperationException("This team is global. You cannot use per-scoreboard methods.");

    @Override
    public boolean addPlayerToTeam(String s, String s1)
    {
        boolean success = super.addPlayerToTeam(s, s1);
        if (success)
        {
            ScoreboardTeam scoreboardTeam = this.getTeam(s1);
            MinecraftServer.getServer().getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(scoreboardTeam, Arrays.asList(new String[]{s}), 3));
        }

        return success;
    }

    @Override
    public void removePlayerFromTeam(String s, ScoreboardTeam scoreboardTeam)
    {
        super.removePlayerFromTeam(s, scoreboardTeam);
        MinecraftServer.getServer().getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(scoreboardTeam, Arrays.asList(new String[]{s}), 3));
    }

    @Override
    public Collection getTeamNames()
    {
        return super.getTeamNames();
    }

    @Override
    public Collection getTeams()
    {
        return super.getTeams();
    }

    @Override
    public ScoreboardTeam getPlayerTeam(String s)
    {
        return super.getPlayerTeam(s);
    }

    @Override
    public void handleTeamAdded(ScoreboardTeam scoreboardTeam)
    {
        MinecraftServer.getServer().getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(scoreboardTeam, 0));
    }

    @Override
    public void handleTeamChanged(ScoreboardTeam scoreboardTeam)
    {
        MinecraftServer.getServer().getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(scoreboardTeam, 2));
    }

    @Override
    public void handleTeamRemoved(ScoreboardTeam scoreboardTeam)
    {
        MinecraftServer.getServer().getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(scoreboardTeam, 1));
    }

    public void sendAllTeams(Player player)
    {
        EntityPlayer nmsPLayer = ((CraftPlayer) player).getHandle();
    }




    //Unused stuff below...

    @Override
    public ScoreboardObjective getObjective(String s)
    {
        throw unsopportedException;
    }

    @Override
    public ScoreboardObjective registerObjective(String s, IScoreboardCriteria iScoreboardCriteria)
    {
        throw unsopportedException;
    }

    @Override
    public Collection getObjectivesForCriteria(IScoreboardCriteria iScoreboardCriteria)
    {
        throw unsopportedException;
    }

    @Override
    public boolean b(String s, ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public ScoreboardScore getPlayerScoreForObjective(String s, ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public Collection getScoresForObjective(ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public Collection getObjectives()
    {
        //Cannot throw exception here, method is automatcally called from some CB stuff.
        return new ArrayList();
    }

    @Override
    public Collection getPlayers()
    {
        throw unsopportedException;
    }

    @Override
    public void resetPlayerScores(String s, ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public Collection getScores()
    {
        throw unsopportedException;
    }

    @Override
    public Map getPlayerObjectives(String s)
    {
        throw unsopportedException;
    }

    @Override
    public void unregisterObjective(ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public void setDisplaySlot(int i, ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public ScoreboardObjective getObjectiveForSlot(int i)
    {
        throw unsopportedException;
    }

    @Override
    public void handleObjectiveAdded(ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public void handleObjectiveChanged(ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public void handleObjectiveRemoved(ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    @Override
    public void handleScoreChanged(ScoreboardScore scoreboardScore)
    {
        throw unsopportedException;
    }

    @Override
    public void handlePlayerRemoved(String s)
    {
        throw unsopportedException;
    }

    @Override
    public void a(String s, ScoreboardObjective scoreboardObjective)
    {
        throw unsopportedException;
    }

    public static ScoreboardServer getInternalNMSScoreboard(CraftScoreboard scoreboard)
    {
        return (ScoreboardServer) ReflectionUtils.get(scoreboard, "board");
    }
}
