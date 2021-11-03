package me.cranked.crankedcore;

import java.util.List;
import me.cranked.crankedcore.commands.*;
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
    /**
     * Manages all commands that start with "chat"
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
        }

        switch (args[0].toLowerCase()) {
            case "help":
                sendHelpMessage(sender);
                break;
            case "reload":
                if (sender.hasPermission("crankedcore.reload") || !(sender instanceof Player)) {
                    ConfigManager.reload();
                    DeathMessagesConfigManager.reload();
                    sender.sendMessage(ConfigManager.get("reload"));
                } else {
                    sender.sendMessage(ConfigManager.get("no-permission"));
                }
                break;
            case "clear":
                Clear.command(sender, args);
                break;
            case "slow":
                Slow.command(sender, args);
                break;
            case "lock":
            case "mute":
                Lock.command(sender, args);
                break;
            case "staff-chat":
            case "staff":
                StaffChat.command(sender, args);
                break;
            case "spy":
                CommandSpy.command(sender);
                break;
            case "announce":
            case "shout":
            case "broadcast":
                Announce.command(sender, args, "announce");
                break;
            case "warning":
            case "warn":
                Announce.command(sender, args, "warning");
                break;
            case "staffannounce":
                Announce.command(sender, args, "staff-announce");
                break;
        }

        return true;
    }

    /**
     * Helper method for onCommand
     * Called when user runs "/chat" or "/chat help"
     * @param sender The user who is trying to get help
     */
    private void sendHelpMessage(CommandSender sender) {
        if (sender.hasPermission("crankedcore.help")) {
            List<String> messages = ConfigManager.getList("help-msg");
            for (String msg : messages)
                sender.sendMessage(ConfigManager.colorize(msg));
        } else {
            sender.sendMessage(ConfigManager.colorize("&aServer is running CrankedCore " + CrankedCore.plugin.getDescription().getVersion() + " by &bteeditator&a.")); // TODO update
        }
    }
}
