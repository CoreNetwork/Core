package us.corenetwork.core.player.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.player.PlayerSettings;
import us.corenetwork.core.util.PlayerUtils;

/**
 * Created by Ginaf on 2015-03-20.
 */
public class UnstuckCommand extends BasePlayerCommand {

    public UnstuckCommand()
    {
        desc = "Unstuck player from solid blocks";
        permission = "unstuck";
        needPlayer = false;
    }

    @Override
    public void run(CommandSender sender, String[] args)
    {
        if (args.length > 2)
        {
            PlayerUtils.Message("Usage : /core unstuck [<player>] [silent]", sender);
            return;
        }

        Player target;
        boolean silent = false;

        if(args.length == 0)
        {
            if (sender instanceof Player)
            {
                target = (Player) sender;
            }
            else
            {
                PlayerUtils.Message("You can only execute /core unstuck [silent] as player.", sender);
                return;
            }
        }
        else if (args.length == 2)
        {
            target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
            if(target == null)
            {
                PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
                return;
            }

            if(args[1].equalsIgnoreCase("silent"))
            {
                silent = true;
            }
            else
            {
                PlayerUtils.Message("Usage : /core unstuck [<player>] [silent]", sender);
                return;
            }
        }
        else
        {
            target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
            if(target == null)
            {
                if(args[0].equalsIgnoreCase("silent"))
                {
                    silent = true;
                    if (sender instanceof Player)
                    {
                        target = (Player) sender;
                    }
                    else
                    {
                        PlayerUtils.Message("You can only execute /core unstuck [silent] as player.", sender);
                        return;
                    }
                }
                else
                {
                    PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
                    return;
                }
            }
        }

        if (!silent)
        {
            if (sender instanceof Player && target.equals((Player)sender))
                PlayerUtils.Message(PlayerSettings.MESSAGE_SELF_UNSTUCK.string(), sender);
            else
                PlayerUtils.Message(PlayerSettings.MESSAGE_PLAYER_UNSTUCK.string().replace("<Player>", target.getName()), sender);
        }


        target.setVelocity(new Vector(0,0,0));
        target.
        if(target.getWorld().getBlockAt(target.getLocation()).getType().isSolid())
        {
            target.teleport(target.getLocation().add(0,1,0));
        }

    }

}
