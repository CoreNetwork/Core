package us.corenetwork.core.alias.actions;

import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.corenetwork.core.alias.AliasAction;
import us.corenetwork.core.alias.AliasModule;

import java.util.regex.Matcher;

public class RunAliasAction extends AliasAction {
    private String alias;

    public RunAliasAction(String alias) {
        this.alias = alias;
    }

    @Override
    public void execute(CommandSender subject, Matcher matcher) {
        String e = matcher.replaceAll(alias);

        AliasModule.instance.onCommand(e, subject);
    }
}
