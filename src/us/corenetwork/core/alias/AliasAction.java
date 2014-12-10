package us.corenetwork.core.alias;

import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;

public abstract class AliasAction {
    /**
     * Execute the action.
     * @param subject the subject that this should execute an action for.
     * @param matcher the parsed regular expression.
     */
    public abstract void execute(CommandSender subject, Matcher matcher);
}
