package me.cranked.chatcore.commands;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandInfo(name = "staffannounce", permission = "chatcore.announce.warning")
public class CommandStaffAnnounce extends ChatCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // Return if incorrect usage
        if (args.length == 0) {
            sender.sendMessage(ConfigManager.get("staff-announce-usage"));
            return;
        }

        // Calculate message of announcement
        args = Arrays.copyOfRange(args, 0, args.length);
        String msg = String.join(" ", args);

        String sound = ConfigManager.get("staff-announce-sound");
        String parsedMsg = ConfigManager.colorize(ConfigManager.get("staff-announce-format").replace("%message%", msg));
        Sound s = Sound.valueOf(sound);
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("chatcore.staffannounce.see")).forEach(player -> {
                    player.sendMessage(parsedMsg);
                    if (!sound.equalsIgnoreCase("none"))
                        player.playSound(player.getLocation(), s, 1.0F, 1.0F);
                });
    }
}
