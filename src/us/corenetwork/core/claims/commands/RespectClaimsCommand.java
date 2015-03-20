package us.corenetwork.core.claims.commands;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.claims.ClaimsSettings;
import us.corenetwork.core.util.PlayerUtils;

import java.util.Comparator;

/**
 * Created by Ginaf on 2015-03-20.
 */
public class RespectClaimsCommand extends BaseClaimsCommand{

    public RespectClaimsCommand()
    {
        desc = "Respect claims";
        permission = "respect";
        needPlayer = true;
    }

    @Override
    public void run(CommandSender sender, String[] args)
    {
        if(args.length > 1)
        {
            PlayerUtils.Message("Usage : /core respectclaims [silent]", sender);
            return;
        }

        boolean silent = false;

        if(args.length == 1 && args[0].equalsIgnoreCase("silent"))
        {
            silent = true;
        }

        if(!silent)
        {
            PlayerUtils.Message(ClaimsSettings.MESSAGE_RESPECT_CLAIMS.string(), sender);
        }

        Player player = (Player)sender;

        if(IgnoreClaimsCommand.ignoringPlayers.contains(player.getUniqueId()))
        {
            IgnoreClaimsCommand.ignoringPlayers.remove(player.getUniqueId());
            GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).ignoreClaims = false;
        }
    }
}
