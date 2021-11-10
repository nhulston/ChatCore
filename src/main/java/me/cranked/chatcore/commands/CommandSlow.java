package me.cranked.chatcore.commands;

import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Set;

@CommandInfo(name = "slow", permission = "chatcore.slow")
public class CommandSlow extends ChatCommand {

    public CommandSlow() {
        setEnabled(ConfigManager.getEnabled("slow-chat"));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // Usage check
        if (args.length <= 1) {
            sender.sendMessage(ConfigManager.get("slow-usage"));
            return;
        }

        // Parse delay
        int delay;
        try {
            delay = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ConfigManager.get("slow-usage"));
            return;
        }

        // Set the delay
        ChatCore.setDelay(delay);

        // Args
        Set<String> arguments = Set.of(args);
        if (delay == 0) {
            if (arguments.contains("-s") && sender.hasPermission("chatcore.slow.silent")) {
                sender.sendMessage(ConfigManager.colorize(ConfigManager.get("unslow-silent").replace("%time%", Integer.toString(delay))));
            } else {
                Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("unslow").replace("%time%", Integer.toString(delay)).replace("%player%", sender.getName())));
            }
        } else if (arguments.contains("-s") && sender.hasPermission("chatcore.slow.silent")) {
            sender.sendMessage(ConfigManager.colorize(ConfigManager.get("slow-silent").replace("%time%", Integer.toString(delay))));
        } else if (arguments.contains("-a") && sender.hasPermission("chatcore.slow.anonymous")) {
            Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("slow-anon").replace("%time%", Integer.toString(delay))));
        } else {
            Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("slow").replace("%time%", Integer.toString(delay)).replace("%player%", sender.getName())));
        }

    }
}
