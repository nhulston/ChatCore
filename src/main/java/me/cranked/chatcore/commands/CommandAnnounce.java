package me.cranked.chatcore.commands;

import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "announce", aliases = {"shout", "broadcast"}, permission = "chatcore.announce")
public class CommandAnnounce extends ChatCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {

    }
}
