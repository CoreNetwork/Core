package us.corenetwork.core.claims.commands;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.claims.ClaimsSettings;
import us.corenetwork.core.util.PlayerUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ginaf on 2015-03-20.
 */
public class IgnoreClaimsCommand extends BaseClaimsCommand{

    public static Set<UUID> ignoringPlayers = new HashSet<UUID>();

    public IgnoreClaimsCommand()
    {
        desc = "Ignore claims";
        permission = "ignore";
        needPlayer = true;
    }

    @Override
    public void run(CommandSender sender, String[] args)
    {
        if(args.length > 1)
        {
            PlayerUtils.Message("Usage : /core ignoreclaims [silent]", sender);
            return;
        }

        boolean silent = false;

        if(args.length == 1 && args[0].equalsIgnoreCase("silent"))
        {
            silent = true;
        }

        if(!silent)
        {
            PlayerUtils.Message(ClaimsSettings.MESSAGE_IGNORE_CLAIMS.string(), sender);
        }

        Player player = (Player)sender;

        if(!ignoringPlayers.contains(player.getUniqueId()))
        {
            ignoringPlayers.add(player.getUniqueId());
            GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).ignoreClaims = true;
        }
    }
}
