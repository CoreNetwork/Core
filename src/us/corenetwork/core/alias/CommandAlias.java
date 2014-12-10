package us.corenetwork.core.alias;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandAlias {
    /**
     * List of regexp patterns. If one or more of those match, the actions will be executed.
     */
    private List<Pattern> matches = new LinkedList<Pattern>();

    /**
     * List of actions to execute when this matches.
     */
    private List<AliasAction> actions = new LinkedList<AliasAction>();

    /**
     * Temporary matcher instance cache.
     */
    private Matcher lastMatch = null;

    public CommandAlias() {
    }

    public CommandAlias(List<Pattern> matches, List<? extends AliasAction> actions) {
        this.matches.addAll(matches);
        this.actions.addAll(actions);
    }

    /**
     * Checks if the given message applies to this alias.
     * @param message the command entered.
     * @return if the given message applies to this alias.
     */
    public boolean matches(String message) {
        for (Pattern pattern : matches) {
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches()) {
                lastMatch = matcher;
                return true;
            }
        }
        lastMatch = null;
        return false;
    }

    /**
     * Executes the actions of this alias.
     * @param subject the subject that sent the command.
     * @param message the contents of the command.
     */
    public void execute(CommandSender subject, String message) {
        if (lastMatch == null) {
            if (!matches(message)) {
                return;
            }
        }

        for (AliasAction action : actions) {
            action.execute(subject, lastMatch);
        }
    }
}
