package us.corenetwork.core.alias.actions;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.core.alias.AliasAction;
import us.corenetwork.core.corecommands.SudoCommand;

import java.util.regex.Matcher;

public class RunCommandAction extends AliasAction {
    private String command;
    private boolean sudo;

    public RunCommandAction(String command, boolean sudo) {
        this.command = command;
        this.sudo = sudo;
    }

    @Override
    public void execute(CommandSender subject, Matcher matcher) {
        String commandLine = matcher.replaceAll(command);
        if (sudo && subject instanceof Player) {
            SudoCommand.sudo((Player) subject, commandLine);
        } else {
            Bukkit.dispatchCommand(subject, commandLine);
        }
    }
}
