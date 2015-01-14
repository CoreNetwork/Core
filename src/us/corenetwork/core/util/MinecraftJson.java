package us.corenetwork.core.util;

import net.minecraft.server.v1_8_R1.EnumChatFormat;

public class MinecraftJson
{
    /**
     * Get colored JSON message
     *
     * @param message message to color.
     * @param color Color of the message. Do not use formatting options here, only colors
     *
     */
    public static String getColorMessage(String message, EnumChatFormat color)
    {
        return "{text: '" + message + "', color: " + color.name().toLowerCase() + "}";
    }

}
