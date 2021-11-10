package me.cranked.chatcore.commands;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@CommandInfo(name = "spy", permission = "chat.commandspy", playerOnly = true)
public class CommandCommandSpy extends ChatCommand {
    // Having UUIDs instead of players is better practice for Spigot.
    public static Set<UUID> commandSpyList = new HashSet<>();
    public CommandCommandSpy() {
        setEnabled(ConfigManager.getEnabled("command-spy"));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        if (commandSpyList.contains(uuid)) {
            commandSpyList.remove(uuid);
            sender.sendMessage(ConfigManager.get("command-spy-off"));
        } else {
            commandSpyList.add(uuid);
            sender.sendMessage(ConfigManager.get("command-spy-on"));
        }
    }
}
