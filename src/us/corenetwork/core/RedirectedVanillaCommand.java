package us.corenetwork.core;

import com.google.common.base.Joiner;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import net.minecraft.server.v1_8_R1.CommandAbstract;
import net.minecraft.server.v1_8_R1.EntityMinecartCommandBlock;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.ICommandListener;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.TileEntityCommand;
import net.minecraft.server.v1_8_R1.TileEntityCommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_8_R1.command.CraftRemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftMinecartCommand;

/**
 * Created by Matej on 28.12.2014.
 */
public class RedirectedVanillaCommand extends CommandAbstract
{
    private static Joiner joiner = Joiner.on(' ');
    private Command originalCommand;
    private VanillaCommandWrapper vanillaCommand;

    public RedirectedVanillaCommand(Command originalCommand, VanillaCommandWrapper vanillaCommand)
    {
        this.originalCommand = originalCommand;
        this.vanillaCommand = vanillaCommand;
    }

    @Override
    public String getCommand()
    {
        return originalCommand.getName();
    }

    @Override
    public String getUsage(ICommandListener iCommandListener)
    {
        return vanillaCommand.getUsage();
    }

    @Override
    public void execute(ICommandListener iCommandListener, String[] strings)
    {
        if (!(iCommandListener instanceof EntityPlayer))
        {
            vanillaCommand.dispatchVanillaCommand(null, iCommandListener, strings);
            //vanillaCommand.dispatchVanillaCommand(iCommandListener, strings);
            return;
        }

        CommandSender sender = getSender(iCommandListener);
        if (sender == null)
        {
            CLog.severe("Error while executing command! Can't find sender: " + iCommandListener.getClass().toString());
            return;
        }

        originalCommand.execute(sender, originalCommand.getName(), strings);
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }

    @Override
    public boolean canUse(ICommandListener iCommandListener)
    {
        return true;
    }

    private static CommandSender getSender(ICommandListener listener) {
        if (listener instanceof EntityPlayer) {
            return ((EntityPlayer) listener).getBukkitEntity();
        }
        if (listener instanceof TileEntityCommandListener) {
            return new CraftBlockCommandSender((TileEntityCommandListener) listener);
        }
        if (listener instanceof EntityMinecartCommandBlock) {
            return new CraftMinecartCommand((CraftServer) Bukkit.getServer(), (EntityMinecartCommandBlock) listener);
        }
        if (listener instanceof RemoteConsoleCommandSender) {
            return new CraftRemoteConsoleCommandSender();
        }
        if (listener instanceof MinecraftServer) {
            return ((MinecraftServer) listener).console;
        }

        return null;
    }

    public static void redirectVanillaCommand(String command)
    {
        CommandMap map = ((CraftServer) Bukkit.getServer()).getCommandMap();

        Command originalCommand = map.getCommand(command);
        VanillaCommandWrapper vanillaCommand = (VanillaCommandWrapper) map.getCommand("minecraft:".concat(command));

        RedirectedVanillaCommand redirectedVanillaCommand = new RedirectedVanillaCommand(originalCommand, vanillaCommand);
        VanillaCommandWrapper redirectedCommandWrapper = new VanillaCommandWrapper(redirectedVanillaCommand, null);

        try
        {
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);

            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(map);
            knownCommands.put(command, redirectedCommandWrapper);

        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
