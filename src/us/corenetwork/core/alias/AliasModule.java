package us.corenetwork.core.alias;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.corenetwork.core.CLog;
import us.corenetwork.core.CoreModule;
import us.corenetwork.core.CorePlugin;
import us.corenetwork.core.alias.actions.RunAliasAction;
import us.corenetwork.core.alias.actions.RunCommandAction;
import us.corenetwork.core.alias.actions.SendMessageAction;

import java.util.*;
import java.util.regex.Pattern;

public class AliasModule extends CoreModule implements Listener {
    public AliasModule() {
        super("Alias", null, "alias");
    }

    public static AliasModule instance;

    {
        instance = this;
    }

    private List<CommandAlias> aliases = new LinkedList<CommandAlias>();

    @Override
    protected boolean loadModule() {
        Bukkit.getPluginManager().registerEvents(this, CorePlugin.instance);

        CLog.info("Alias module loaded");
        return true;
    }

    @Override
    protected void unloadModule() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCommandEntered(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        CommandSender sender = event.getPlayer();
        event.setCancelled(onCommand(message, sender));
    }

    public boolean onCommand(String message, CommandSender sender) {
        for (CommandAlias alias : aliases) {
            if (alias.matches(message)) {
                alias.execute(sender, message);
                return true;
            }
        }
        return false;
    }

    public void loadSettings(YamlConfiguration config) {
        List<?> items = config.getList("Aliases", new ArrayList());

        if(items.size() == 0)
        {
            CLog.info("No aliases found in config.");
        }
        else
        {
            CLog.info("Loading " + items.size() + " aliases.");
        }


        for (Object item : items) {
            List<Pattern> patterns = new ArrayList<Pattern>();
            List<AliasAction> actions = new ArrayList<AliasAction>();
            Map<String, Object> itemMap = (Map<String, Object>) item;

            // matches
            boolean raw = itemMap.get("raw") == (Boolean) true;
            boolean sudo = itemMap.get("sudo") == (Boolean) true;
            if (itemMap.containsKey("match")) {
                Object matchObj = itemMap.get("match");
                if (matchObj instanceof String) {
                    patterns.add(makePattern((String) matchObj, raw));
                } else if (matchObj instanceof List) {
                    for (String pattern : (List<String>) matchObj) {
                        patterns.add(makePattern(pattern, raw));
                    }
                }
            }

            // actions
            if (itemMap.containsKey("route")) {
                Object routeObj = itemMap.get("route");
                if (routeObj instanceof String) {
                    actions.add(new RunCommandAction(((String) routeObj).replaceFirst("\\/", ""), sudo));
                } else if (routeObj instanceof List) {
                    for (String route : (List<String>) routeObj) {
                        actions.add(new RunCommandAction(route.replaceFirst("\\/", ""), sudo));
                    }
                }
            }
            if (itemMap.containsKey("message")) {
                Object msgObj = itemMap.get("message");
                if (msgObj instanceof String) {
                    actions.add(new SendMessageAction((String) msgObj));
                } else if (msgObj instanceof List) {
                    for (String route : (List<String>) msgObj) {
                        actions.add(new SendMessageAction(route));
                    }
                }
            }
            if (itemMap.containsKey("forward")) {
                Object msgObj = itemMap.get("forward");
                if (msgObj instanceof String) {
                    actions.add(new RunAliasAction((String) msgObj));
                } else if (msgObj instanceof List) {
                    for (String route : (List<String>) msgObj) {
                        actions.add(new RunAliasAction(route));
                    }
                }
            }


            // construct alias
            CommandAlias alias = new CommandAlias(patterns, actions);
            aliases.add(alias);
        }
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        aliases.clear();
        loadSettings(config);
    }

    public Pattern makePattern(String pre, boolean raw) {
        if (!raw) {
            pre = pre.replaceAll("/", "\\/");
            pre = '^' + pre + '$';
        }
        return Pattern.compile(pre, Pattern.CASE_INSENSITIVE);
    }
}
