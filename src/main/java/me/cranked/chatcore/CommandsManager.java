package me.cranked.chatcore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.cranked.chatcore.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Manages all commands that start with "chat"
 * @author Nick
 * @since 1.0
 */
public class CommandsManager implements CommandExecutor {

    private static final Map<String, CustomRunnable<CommandSender, String[]>> map = new HashMap<>();

    private interface CustomRunnable<CommandSender, T> {
        void run(CommandSender sender, T args);
    }

    public CommandsManager() {
        map.put("reload", (sender, args) -> {
            if (sender.hasPermission("chatcore.reload") || !(sender instanceof Player)) {
                ConfigManager.reload();
                sender.sendMessage(ConfigManager.get("reload"));
            } else {
                sender.sendMessage(ConfigManager.get("no-permission"));
            }
        });
        map.put("clear", Clear::command);
        map.put("slow", Slow::command);
        map.put("lock", Lock::command);
        map.put("mute", Lock::command);
        map.put("staff", StaffChat::command);
        map.put("staff-chat", StaffChat::command);
        map.put("donator", DonatorChat::command);
        map.put("donator-chat", DonatorChat::command);
        map.put("spy", (sender, args) -> CommandSpy.command(sender));
        map.put("announce", (sender, args) -> Announce.command(sender, args, "announce"));
        map.put("shout", (sender, args) -> Announce.command(sender, args, "announce"));
        map.put("broadcast", (sender, args) -> Announce.command(sender, args, "announce"));
        map.put("warn", (sender, args) -> Announce.command(sender, args, "warning"));
        map.put("warning", (sender, args) -> Announce.command(sender, args, "warning"));
        map.put("staffannounce", (sender, args) -> Announce.command(sender, args, "staff-announce"));
    }

    /**
     * Manages all commands that start with "chat"
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        CustomRunnable<CommandSender, String[]> runnable = map.getOrDefault(args[0].toLowerCase(), null);
        if (runnable == null) {
            sendHelpMessage(sender);
        } else {
            runnable.run(sender, args);
        }

        return true;
    }

    /**
     * Helper method for onCommand
     * Called when user runs "/chat" or "/chat help"
     * @param sender The user who is trying to get help
     */
    private void sendHelpMessage(CommandSender sender) {
        if (sender.hasPermission("chatcore.help")) {
            List<String> messages = ConfigManager.getList("help-msg");
            for (String msg : messages)
                sender.sendMessage(ConfigManager.colorize(msg));
        } else {
            // TextComponent formatComponent = new TextComponent(ConfigManager.colorize("&aServer is running &b&nChatCore " + ChatCore.plugin.getDescription().getVersion() + "&a."));
            // formatComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.mc-market.org/resources/13998/"));
            // sender.spigot().sendMessage(formatComponent);
            sender.sendMessage(ConfigManager.get("no-permission"));
        }
    }
}
