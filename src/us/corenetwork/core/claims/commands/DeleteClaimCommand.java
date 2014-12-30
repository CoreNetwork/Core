package us.corenetwork.core.claims.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteClaimCommand extends BaseClaimsCommand {
    public DeleteClaimCommand() {
        desc = "Deletes the claim you're currently standing in";
        needPlayer = true;
        permission = "deleteclaim";
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        AbandonClaimCommand.removeClaim(player);
        Bukkit.dispatchCommand(player, "DeleteClaim");
    }
}
