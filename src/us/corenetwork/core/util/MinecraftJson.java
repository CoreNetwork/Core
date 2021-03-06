package us.corenetwork.core.util;

import org.json.simple.JSONObject;

public class MinecraftJson
{
    /**
     * Get colored JSON message
     *
     * @param message message to color.
     * @param color Color of the message. See Technical Name at <a href="http://minecraft.gamepedia.com/Format_codes#Color_codes">Minecraft Wiki</a>
     *
     */
    public static String getColorMessage(String message, String color)
    {
        JSONObject object = new JSONObject();
        object.put("text", message);
        object.put("color", color);
        return object.toJSONString();
    }

}
