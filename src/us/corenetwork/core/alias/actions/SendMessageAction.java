package us.corenetwork.core.alias.actions;

import org.bukkit.command.CommandSender;
import us.corenetwork.core.PlayerUtils;
import us.corenetwork.core.alias.AliasAction;

import java.util.regex.Matcher;

public class SendMessageAction extends AliasAction {
    private String message;

    public SendMessageAction() {
    }

    public SendMessageAction(String message) {
        this.message = message;
    }

    @Override
    public void execute(CommandSender subject, Matcher matcher) {
        PlayerUtils.Message(matcher.replaceAll(message), subject);
    }
}
