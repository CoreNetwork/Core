package us.corenetwork.core.player.commands;

import net.minecraft.server.v1_8_R2.NBTBase;
import net.minecraft.server.v1_8_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.NBTTagList;
import net.minecraft.server.v1_8_R2.WorldNBTStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.corenetwork.core.CLog;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.player.PlayerSettings;
import us.corenetwork.core.util.PlayerUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ginaf on 2015-03-17.
 */
public class InfoCommand extends BasePlayerCommand {

    private final String PH_PLAYER = "<Player>";
    private final String PH_X = "<X>";
    private final String PH_Y = "<Y>";
    private final String PH_Z = "<Z>";
    private final String PH_WORLD = "<World>";
    private final String PH_VEHICLE = "<Vehicle>";
    private final String PH_HEALTH = "<Health>";
    private final String PH_HUNGER = "<Hunger>";
    private final String PH_SATURATION = "<Saturation>";
    private final String PH_AIR = "<Air>";
    private final String PH_LEVEL = "<Level>";
    private final String PH_EFFECTS = "<Effects>";

    private Map<String, String> replacements = new HashMap<String, String>();


    public InfoCommand()
    {
        desc = "Info about a player.";
        permission = "info";
        needPlayer = false;
    }

    public void run(final CommandSender sender, String[] args)
    {

        if (args.length < 1 || args.length > 2)
        {
            PlayerUtils.Message("Usage : /core info <player> [verbose]", sender);
            return;
        }

        replacements.clear();

        OfflinePlayer target;
        boolean verbose = false;

        target = CorePlugin.instance.getServer().getOfflinePlayer(args[0]);
        if(target == null)
        {
            PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
            return;
        }

        replacements.put(PH_PLAYER, target.getName());
        if(args.length == 2)
        {
            if(args[1].equalsIgnoreCase("verbose"))
            {
                verbose = true;
            }
            else
            {
                PlayerUtils.Message("Usage : /core info <player> [verbose]", sender);
                return;
            }
        }

        if(target.isOnline())
        {
            Player player = target.getPlayer();
            replacements.put(PH_X, String.format("%.2f", player.getLocation().getX()));
            replacements.put(PH_Y, String.format("%.2f", player.getLocation().getY()));
            replacements.put(PH_Z, String.format("%.2f", player.getLocation().getZ()));

            replacements.put(PH_WORLD, player.getWorld().getName());

            Entity vehicle = player.getVehicle();
            if(vehicle != null)
            {
                replacements.put(PH_VEHICLE, vehicle.getType().name());
            }
            else
            {
                replacements.put(PH_VEHICLE, "");
            }

            replacements.put(PH_HEALTH, String.format("%.2f", player.getHealth()));
            replacements.put(PH_HUNGER, player.getFoodLevel()+"");
            replacements.put(PH_SATURATION, String.format("%.2f", player.getSaturation()));
            replacements.put(PH_AIR, player.getRemainingAir()+"");
            replacements.put(PH_LEVEL, player.getLevel()+"");

            StringBuilder effectsString = new StringBuilder();
            for(PotionEffect effect : player.getActivePotionEffects())
            {
                effectsString.append(effect.getType().getName());
                effectsString.append(" ");
                effectsString.append(effect.getAmplifier() + 1);
                effectsString.append("; ");
            }
            replacements.put(PH_EFFECTS, effectsString.toString());
        }
        else
        {
            CraftWorld craftWorld = (CraftWorld)Bukkit.getWorld("world");
            WorldNBTStorage worldNBTStorage = (WorldNBTStorage) craftWorld.getHandle().getDataManager();
            NBTTagCompound nbtTagCompound = worldNBTStorage.getPlayerData(target.getUniqueId().toString());

            if(nbtTagCompound == null)
            {
                PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
                return;
            }

            NBTTagList list = (NBTTagList) nbtTagCompound.get("Pos");

            replacements.put(PH_X, String.format("%.2f", list.d(0)));
            replacements.put(PH_Y, String.format("%.2f", list.d(1)));
            replacements.put(PH_Z, String.format("%.2f", list.d(2)));

            int dimension = nbtTagCompound.getInt("Dimension");
            String worldString = dimension == 0 ? "world" : dimension == -1 ? "world_nether" : "world_the_end";

            replacements.put(PH_WORLD, worldString);

            NBTTagCompound ridingCompound = nbtTagCompound.getCompound("Riding");
            if(ridingCompound != null)
            {
                replacements.put(PH_VEHICLE, ridingCompound.getString("id"));
            }
            else
            {
                replacements.put(PH_VEHICLE, "");
            }

            replacements.put(PH_HEALTH, String.format("%.2f", nbtTagCompound.getDouble("Health")));
            replacements.put(PH_HUNGER, nbtTagCompound.getInt("foodLevel")+"");
            replacements.put(PH_SATURATION, String.format("%.2f", nbtTagCompound.getDouble("foodSaturationLevel")));
            replacements.put(PH_AIR, nbtTagCompound.getInt("Air")+"");
            replacements.put(PH_LEVEL, nbtTagCompound.getInt("XpLevel")+"");

            list = (NBTTagList) nbtTagCompound.get("ActiveEffects");

            StringBuilder effectsString = new StringBuilder();
            if(list != null && list.size() != 0)
            {
                for(int i = 0;i<list.size();i++)
                {
                    NBTTagCompound effectCompund = list.get(i);

                    effectsString.append(PotionEffectType.getById(effectCompund.getInt("Id")).getName());
                    effectsString.append(" ");
                    effectsString.append(effectCompund.getInt("Amplifier")+1);
                    effectsString.append("; ");
                }
            }
            replacements.put(PH_EFFECTS, effectsString.toString());
        }


        if(verbose)
        {
            PlayerUtils.Message(replacePlaceholders(PlayerSettings.INFO_VERBOSE.string()), sender);

            for(String comm : PlayerSettings.INFO_COMMANDS.stringList())
            {
                Bukkit.getServer().dispatchCommand(sender, comm.replace(PH_PLAYER, replacements.get(PH_PLAYER)));
            }
        }
        else
        {
            PlayerUtils.Message(replacePlaceholders(PlayerSettings.INFO_SIMPLE.string()), sender);
        }
    }

    private String replacePlaceholders(String message)
    {
        for(String key : replacements.keySet())
        {
            message = message.replace(key, replacements.get(key));
        }

        return message;
    }
}
