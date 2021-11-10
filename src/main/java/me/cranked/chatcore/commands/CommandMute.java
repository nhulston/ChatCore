package me.cranked.chatcore.commands;

import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Set;

@CommandInfo(name = "mute", aliases = {"lock"}, permission = "chatcore.lock")
public class CommandMute extends ChatCommand {

    public CommandMute() {
        setEnabled(ConfigManager.getEnabled("lock-chat"));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // Lock chat
        ChatCore.toggleChatLocked();

        // Broadcasting
        Set<String> arguments = Set.of(args);
        if (ChatCore.getChatLocked()) {
            if (arguments.contains("-s") && sender.hasPermission("chatcore.lock.silent")) {
                sender.sendMessage(ConfigManager.get("lock-silent"));
            } else if (arguments.contains("-a") && sender.hasPermission("chatcore.lock.anonymous")) {
                Bukkit.broadcastMessage(ConfigManager.get("lock-anon"));
            } else {
                Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("lock").replace("%player%", sender.getName())));
            }
        } else if (arguments.contains("-s") && sender.hasPermission("chatcore.lock.silent")) {
            sender.sendMessage(ConfigManager.get("unlock-silent"));
        } else if (arguments.contains("-a") && sender.hasPermission("chatcore.lock.anonymous")) {
            Bukkit.broadcastMessage(ConfigManager.get("unlock-anon"));
        } else {
            Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("unlock").replace("%player%", sender.getName())));
        }
    }
}
