package us.corenetwork.core.player.commands;

import net.minecraft.server.v1_8_R1.NBTBase;
import net.minecraft.server.v1_8_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagList;
import net.minecraft.server.v1_8_R1.WorldNBTStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import us.corenetwork.core.CLog;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.util.PlayerUtils;

import java.util.List;

/**
 * Created by Ginaf on 2015-03-17.
 */
public class InfoCommand extends BasePlayerCommand {

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

        OfflinePlayer target;
        boolean verbose = false;

        target = CorePlugin.instance.getServer().getPlayerExact(args[0]);
        if(target == null)
        {
            PlayerUtils.Message("Cannot find player called " + ChatColor.stripColor(args[0]), sender);
            return;
        }

        if(args.length == 2 && args[1].equalsIgnoreCase("verbose"))
        {
            verbose = true;
        }

        double posX,posY,posZ;
        String worldString;
        String vehicleString = "NONE";

        if(target.isOnline())
        {
            Player player = target.getPlayer();
            posX = player.getLocation().getX();
            posY = player.getLocation().getY();
            posZ = player.getLocation().getZ();

            worldString = player.getWorld().getName();

            Entity vehicle = player.getVehicle();
            if(vehicle != null)
            {
                vehicleString = vehicle.getType().name();
            }
        }
        else
        {
            CraftWorld craftWorld = (CraftWorld)Bukkit.getWorld("world");
            WorldNBTStorage worldNBTStorage = (WorldNBTStorage) craftWorld.getHandle().getDataManager();
            NBTTagCompound nbtTagCompound = worldNBTStorage.getPlayerData(target.getUniqueId().toString());

            NBTTagList list = (NBTTagList) nbtTagCompound.get("Pos");

            posX = list.d(0);
            posY = list.d(1);
            posZ = list.d(2);
        }
        if(verbose)
        {

        }
    }
}
