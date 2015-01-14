package us.corenetwork.core.util;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;


/**
    Bunch of convenience commands that execute minecraft title functions. They are also silent unlike /title command itself which sends spam to moderators.
 */
public class TitleSender {

    /**
      *  Display text in the middle of the player text. This should be send last in the chain of the title commands.
      *  <p>
      *  Equal to /title &lt;player&gt; title &lt;title&gt;
      *
      * @param player Affected player
      * @param title text of the subtitle, can be JSON.
     */
    public static void title(Player player, String title)
    {
        try
        {
            IChatBaseComponent component = ChatSerializer.a(title);

            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, component);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
        }
        catch (JsonParseException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *  Set text that will display below title.
     *  <p>
     *  Equal to /title &lt;player&gt; subtitle &lt;subtitle&gt;
     *
     * @param player Affected player
     * @param subtitle text of the subtitle, can be JSON.
     */
    public static void subtitle(Player player, String subtitle)
    {
        try
        {
            IChatBaseComponent component = ChatSerializer.a(subtitle);

            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, component);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
        }
        catch (JsonParseException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *  Reset all title options to default values
     *  <p>
     *  Equal to /title &lt;player&gt; reset
     *
     * @param player Affected player
     */
    public static void reset(Player player)
    {
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.RESET, null);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
    }

    /**
     *  Reset all title options to default values
     *  <p>
     *  Equal to /title &lt;player&gt; reset
     *
     * @param player Affected player
     */
    public static void clear(Player player)
    {
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.CLEAR, null);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
    }

    /**
     *  Set time-related options for title
     *  <p>
     *  Equal to /title &lt;player&gt; times &lt;fadeIn&gt; &lt;stay&gt; &lt;fadeOut&gt;
     *
     * @param player Affected player
     * @param fadeIn fade-in length in ticks
     * @param stay how long should title stay displayed in ticks
     * @param fadeOut fade-out length in ticks
     */
    public static void times(Player player, int fadeIn, int stay, int fadeOut)
    {
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
    }
}
