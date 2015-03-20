package us.corenetwork.core.claims.commands;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.claims.ClaimsModule;
import us.corenetwork.core.claims.ClearFluidsInClaimWorker;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class AbandonAllClaimsCommand extends BaseClaimsCommand {
    public AbandonAllClaimsCommand() {
        desc = "Abandons all claims";
        needPlayer = true;
        permission = "abandonall";
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        final Vector<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).getClaims();

        final Set<Claim> claimsDone = new HashSet<Claim>();

        for (final Claim claim : claims) {
            ClaimsModule.instance.pool.addWorker(new ClearFluidsInClaimWorker(claim, new Runnable() {
                @Override
                public void run() {
                    claimsDone.add(claim);
                    if (claimsDone.size() == claims.size()) {
                        Bukkit.dispatchCommand(player, "AbandonAllClaims");
                    }
                }
            }));
            ClaimsModule.instance.claimPerks.onRemoveClaim(claim);
        }

    }
}
