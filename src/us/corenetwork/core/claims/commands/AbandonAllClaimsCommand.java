package us.corenetwork.core.claims.commands;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.claims.ClaimsModule;

import java.util.Vector;

public class AbandonAllClaimsCommand extends BaseClaimsCommand {
    public AbandonAllClaimsCommand() {
        desc = "Abandons all claims";
        needPlayer = true;
        permission = "abandonall";
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Vector<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).getClaims();

        for (Claim claim : claims) {
            ClaimsModule.instance.claimFluids.onRemoveClaim(claim);
            ClaimsModule.instance.claimPerks.onRemoveClaim(claim);
        }

        Bukkit.dispatchCommand(player, "AbandonAllClaims");
    }
}
