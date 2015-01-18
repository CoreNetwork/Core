package us.corenetwork.core.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.corenetwork.core.scoreboard.CoreScoreboardManager;

public class ScoreboardUtils
{
    /**
     * Convenience method to get team from scoreboard or automatically create one if it does not exist already
     */
    public static Team getOrCreateTeam(Scoreboard scoreboard, String teamName)
    {
        Team team = scoreboard.getTeam(teamName);
        if (team == null)
            team = scoreboard.registerNewTeam(teamName);

        return team;
    }

    /**
     * Move player to specific global team (automatically removes them from previous team)
     * @param player Player to move
     * @param team Name of the team
     * @return true if player was moved, false if player was already in that team
     */
    public static boolean movePlayerToTeam(OfflinePlayer player, String team)
    {
        Scoreboard teamScoreboard = CoreScoreboardManager.getTeamsScoreboard();
        Team newTeam = ScoreboardUtils.getOrCreateTeam(teamScoreboard, team);

        if (!newTeam.hasPlayer(player))
        {
            Team oldPlayerTeam = teamScoreboard.getPlayerTeam(player);
            if (oldPlayerTeam != null)
                oldPlayerTeam.removePlayer(player);

            newTeam.addPlayer(player);


            return true;
        }

        return false;
    }
}
