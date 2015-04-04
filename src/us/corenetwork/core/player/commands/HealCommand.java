package us.corenetwork.core.player.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.player.PlayerSettings;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;

/**
 * Created by Ginaf on 2015-03-13.
 */
public class HealCommand extends BasePlayerCommand {

    public HealCommand()
    {
        desc = "Heal player.";
        permission = "heal";
        needPlayer = false;
    }

    public void run(final CommandSender sender, String[] args)
    {
        if (args.length < 1 || args.length > 3)
        {
            PlayerUtils.Message("Usage : /core heal [<player>] <value> [silent]", sender);
            return;
        }

        Player target;
        double healValue;
        boolean silent = false;

        //value
        //value + silent
        if(args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("silent")))
        {
            if (sender instanceof Player)
            {
                target = (Player) sender;
            }
            else
            {
                PlayerUtils.Message("You can only execute /core heal <value> [silent] as player.", sender);
                return;
            }

            if(Util.isDouble(args[0]))
            {
                healValue = Double.parseDouble(args[0]);
            }
            else
            {
                PlayerUtils.Message("Wrong <value> format in /core heal <value>.", sender);
                return;
            }

            silent = args.length == 2;
        }
        else if(args.length == 2 || (args.length == 3 && args[2].equalsIgnoreCase("silent")))
        {
            //player + value
            if(Util.isDouble(args[1]))
            {
                healValue = Double.parseDouble(args[1]);
            }
            else
            {
                PlayerUtils.Message("Wrong <value> format in /core heal <player> <value>.", sender);
                return;
            }

            target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
            if(target == null)
            {
                PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
                return;
            }

            silent = args.length == 3;
        }
        else
        {
            PlayerUtils.Message("Usage : /core heal [<player>] <value> [silent]", sender);
            return;
        }

        if (!silent)
        {
            if (sender instanceof Player && target.equals((Player)sender))
                PlayerUtils.Message(PlayerSettings.MESSAGE_SELF_HEALED.string(), sender);
            else
                PlayerUtils.Message(PlayerSettings.MESSAGE_PLAYER_HEALED.string().replace("<Player>", target.getName()), sender);
        }

        double newHealth = target.getHealth() + healValue;
        newHealth = newHealth > 20 ? 20 : newHealth;
        newHealth = newHealth < 0 ? 0 : newHealth;

        target.setHealth(newHealth);
    }

}
