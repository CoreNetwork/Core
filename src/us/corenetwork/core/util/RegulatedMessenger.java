package us.corenetwork.core.util;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Spam control class that makes sure minimum amount of time passes between messages routed through this class.
 */
public class RegulatedMessenger
{
    private int minimumDelay;
    private HashMap<UUID, Long> lastMessageTimes;

    /**
     * @param minimumDelay Minimum delay between messages to one player in miliseconds
     */
    public RegulatedMessenger(int minimumDelay)
    {
        this.minimumDelay = minimumDelay;
        lastMessageTimes = new HashMap<UUID, Long>();
    }

    /**
     * Send regulated message
     * @param player Player that will receive the message
     * @param message The message
     * @return <b>true</b> if message was sent, <b>false</b> if message was dropped
     */
    public boolean sendMessage(Player player, String message)
    {
        Long lastMessage = lastMessageTimes.get(player.getUniqueId());
        if (lastMessage != null && (System.currentTimeMillis() - lastMessage) < minimumDelay)
            return false;

        PlayerUtils.Message(message, player);
        lastMessageTimes.put(player.getUniqueId(), System.currentTimeMillis());
        return true;
    }
}
