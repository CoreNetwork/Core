package us.corenetwork.core.claims.commands;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.claims.ClaimsModule;
import us.corenetwork.core.claims.ClearFluidsInClaimWorker;

public class AbandonClaimCommand extends BaseClaimsCommand {
    public AbandonClaimCommand() {
        desc = "Abandons the claim you're currently in.";
        needPlayer = true;
        permission = "abandon";
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        removeClaim(player);
        Bukkit.dispatchCommand(player, "AbandonClaim");
    }

    public static void removeClaim(final Player player) {
        Location location = player.getLocation();
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        if (claim != null && claim.parent == null && claim.allowEdit(player) == null) {
            ClaimsModule.instance.pool.addWorker(new ClearFluidsInClaimWorker(claim, new Runnable() {
                @Override
                public void run() {
                }
            }));

            ClaimsModule.instance.claimPerks.onRemoveClaim(claim);
        }
    }
}
