package us.corenetwork.core.util;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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
}
