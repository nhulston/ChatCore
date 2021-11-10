package me.cranked.chatcore.commands;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandInfo(name = "warn", aliases = {"warning"}, permission = "chatcore.announce.warning")
public class CommandWarn extends ChatCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // Return if incorrect usage
        if (args.length == 0) {
            sender.sendMessage(ConfigManager.get("warning-usage"));
            return;
        }

        // Calculate message of announcement
        args = Arrays.copyOfRange(args, 0, args.length);
        String msg = String.join(" ", args);

        broadcast(ConfigManager.get("warning-format").replace("%message%", msg));

        String sound = ConfigManager.get("warning-sound");
        if (!sound.equalsIgnoreCase("none")) {
            Sound s = Sound.valueOf(sound);
            Bukkit.getOnlinePlayers()
                    .forEach(player -> player.playSound(player.getLocation(), s, 1f, 1f));
        }
    }
}
