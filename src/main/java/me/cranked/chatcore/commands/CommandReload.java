package me.cranked.chatcore.commands;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.DeathMessagesConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "reload", permission = "chatcore.reload")
public class CommandReload extends ChatCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        ConfigManager.reload();
        DeathMessagesConfigManager.reload();
        sender.sendMessage(ConfigManager.get("reload"));
    }
}
