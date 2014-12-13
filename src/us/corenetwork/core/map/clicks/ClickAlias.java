package us.corenetwork.core.map.clicks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

/**
 * suck it @ridddle, it's an alias now
 */
public class ClickAlias {
    private List<String> commands = new LinkedList<String>();


    public ClickAlias(List<String> commands) {
        this.commands.addAll(commands);
    }

    public void execute(CommandSender subject) {
        for (String command : commands) {
            Bukkit.dispatchCommand(subject, command);
        }
    }
}
