package us.corenetwork.core.player.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.player.PlayerSettings;
import us.corenetwork.core.util.PlayerUtils;
import us.corenetwork.core.util.Util;

/**
 * Created by Ginaf on 2015-03-17.
 */
public class FeedCommand extends BasePlayerCommand {

    public FeedCommand()
    {
        desc = "Feed player.";
        permission = "feed";
        needPlayer = false;
    }

    public void run(final CommandSender sender, String[] args)
    {
        if (args.length > 4)
        {
            PlayerUtils.Message("Usage : /core feed [<player>] [<hunger>] [<saturation>] [silent]", sender);
            return;
        }

        Player target;
        int feedValue = 20;
        int saturationValue = 20;
        boolean silent = false;

        if(args.length == 0)
        {
            if (sender instanceof Player)
            {
                target = (Player) sender;
            }
            else
            {
                PlayerUtils.Message("You can only execute /core feed [<hunger>] [<saturation>] [silent] as player.", sender);
                return;
            }
        }
        else if(args.length == 4)
        {
            if(args[3].equalsIgnoreCase("silent"))
            {
                silent = true;
            }
            else
            {
                PlayerUtils.Message("Usage : /core feed [<player>] [<hunger>] [<saturation>] [silent]", sender);
                return;
            }

            //Saturation
            if(Util.isInteger(args[2]))
            {
                saturationValue = Integer.parseInt(args[2]);
            }
            else
            {
                PlayerUtils.Message("Usage : /core feed [<player>] [<hunger>] [<saturation>] [silent]", sender);
                return;
            }

            //Hunger
            if(Util.isInteger(args[1]))
            {
                feedValue = Integer.parseInt(args[1]);
            }
            else
            {
                PlayerUtils.Message("Usage : /core feed [<player>] [<hunger>] [<saturation>] [silent]", sender);
                return;
            }

            target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
            if(target == null)
            {
                PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
                return;
            }

        }
        else if (args.length == 3)
        {
            //player hunger saturation
            //player hunger silent
            //hunger satiration silent

            boolean selfCast = false;

            target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
            if(target == null)
            {
                if(Util.isInteger(args[0]))
                {
                    if (sender instanceof Player)
                    {
                        target = (Player) sender;
                        selfCast = true;
                    }
                    else
                    {
                        PlayerUtils.Message("You can only execute /core feed [<hunger>] [<saturation>] [silent] as player.", sender);
                        return;
                    }

                    feedValue = Integer.parseInt(args[0]);
                }
                else
                {
                    PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
                    return;
                }
            }

            if(Util.isInteger(args[1]))
            {
                if(selfCast)
                    saturationValue = Integer.parseInt(args[1]);
                else
                    feedValue = Integer.parseInt(args[1]);
            }

            if(args[2].equalsIgnoreCase("silent"))
            {
                silent = true;
            }
            else if (Util.isInteger(args[2]))
            {
                saturationValue = Integer.parseInt(args[2]);
            }
            else
            {
                PlayerUtils.Message("Usage : /core feed [<player>] [<hunger>] [<saturation>] [silent]", sender);
                return;
            }
        }
        else if(args.length == 2)
        {
            //player hunger
            //player silent
            //hunger silent

            target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
            if(target == null)
            {
                if(Util.isInteger(args[0]))
                {
                    if (sender instanceof Player)
                    {
                        target = (Player) sender;
                    }
                    else
                    {
                        PlayerUtils.Message("You can only execute /core feed [<hunger>] [<saturation>] [silent] as player.", sender);
                        return;
                    }

                    feedValue = Integer.parseInt(args[0]);
                }
                else
                {
                    PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
                    return;
                }
            }

            if(args[1].equalsIgnoreCase("silent"))
            {
                silent = true;
            }
            else if (Util.isInteger(args[1]))
            {
                feedValue = Integer.parseInt(args[1]);
            }
            else
            {
                PlayerUtils.Message("Usage : /core feed [<player>] [<hunger>] [<saturation>] [silent]", sender);
                return;
            }
        }
        else
        {
            target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
            if(target == null)
            {
                if(Util.isInteger(args[0]))
                {
                    if (sender instanceof Player)
                    {
                        target = (Player) sender;
                    }
                    else
                    {
                        PlayerUtils.Message("You can only execute /core feed [<hunger>] [<saturation>] [silent] as player.", sender);
                        return;
                    }

                    feedValue = Integer.parseInt(args[0]);
                }
                else if(args[0].equalsIgnoreCase("silent"))
                {
                    if (sender instanceof Player)
                    {
                        target = (Player) sender;
                    }
                    else
                    {
                        PlayerUtils.Message("You can only execute /core feed [<hunger>] [<saturation>] [silent] as player.", sender);
                        return;
                    }
                    
                    silent = true;
                }
                else
                {
                    PlayerUtils.Message("Usage : /core feed [<player>] [<hunger>] [<saturation>] [silent]", sender);
                    return;
                }
            }
        }



        if (!silent)
        {
            if (sender instanceof Player && target.equals((Player)sender))
                PlayerUtils.Message(PlayerSettings.MESSAGE_SELF_FEED.string(), sender);
            else
                PlayerUtils.Message(PlayerSettings.MESSAGE_PLAYER_FEED.string().replace("<Player>", target.getName()), sender);
        }

        int newFood = target.getFoodLevel() + feedValue;
        newFood = newFood > 20 ? 20 : newFood;
        newFood = newFood < 0 ? 0 : newFood;

        double newSaturation = target.getSaturation() + saturationValue;
        newSaturation = newSaturation > 20 ? 20 : newSaturation;
        newSaturation = newSaturation < 0 ? 0 : newSaturation;

        target.setFoodLevel(newFood);
        target.setSaturation((float) newSaturation);



    }

}
