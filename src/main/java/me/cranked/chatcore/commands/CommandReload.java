package me.cranked.chatcore.commands;

import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.DeathMessagesConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

@CommandInfo(name = "reload", permission = "chatcore.reload")
public class CommandReload extends ChatCommand {

    public CommandReload() {
        setEnabled(true);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        ConfigManager.reload();
        DeathMessagesConfigManager.reload();
        sender.sendMessage(ConfigManager.get("reload"));
        if (!(sender instanceof ConsoleCommandSender)) {
            ChatCore.plugin.getLogger().info(ConfigManager.get("reload"));
        }
    }
}
