package me.cranked.chatcore.commands;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Set;

@CommandInfo(name = "clear", permission = "chatcore.clear")
public class CommandClear extends ChatCommand {

    public CommandClear() {
        // If the "clear-chat" config value is "false"
        // this command will not be available
        setEnabled(ConfigManager.getEnabled("clear-chat"));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        // Using .filter will only allow players through if the given condition is met.
        // In this, it will only allow player who do not have the permission provided.
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.hasPermission("chatcore.clear.bypass"))
                .forEach(player -> {
                    for (int i = 0; i < 100; i++) {
                        player.sendMessage(" ");
                    }
                });

        Set<String> arguments = Set.of(args);
        // Announce anonymous message
        if ((!arguments.contains("-s") || !sender.hasPermission("chatcore.clear.silent")) && arguments.contains("-a") && sender.hasPermission("chatcore.clear.anonymous")) {
            Bukkit.broadcastMessage(ConfigManager.get("clear-anon"));
        }
        // Announce normal message if not silent
        else if (!arguments.contains("-s") || !sender.hasPermission("chatcore.clear.silent")) {
            Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("clear").replace("%player%", sender.getName())));
        }
    }

}
