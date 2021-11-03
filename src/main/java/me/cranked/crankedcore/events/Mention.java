package me.cranked.crankedcore.events;

import java.util.Objects;
import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Mention implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!ConfigManager.getEnabled("mention"))
            return;

        // Permission check
        if (!e.getPlayer().hasPermission("crankedcore.mention"))
            return;

        String message = e.getMessage();
        String messageLower = message.toLowerCase();
        String sound = ConfigManager.get("mention-sound");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String playerName = onlinePlayer.getName();
            String playerNameLower = playerName.toLowerCase();
            int loc = messageLower.indexOf(playerNameLower);
            if ((loc != -1 && (loc + playerName.length() == message.length() || (loc == 0 && messageLower.contains(playerNameLower + " ")) || messageLower.contains(" " + playerNameLower + " ") || messageLower.contains(" " + playerNameLower + ".") || messageLower.contains(" " + playerNameLower + "?") || messageLower.contains(" " + playerNameLower + "!"))) || (message.length() == playerName.length() + 1 && (messageLower.contains(playerNameLower + ".") || messageLower.contains(playerNameLower + "!") || messageLower.contains(playerNameLower + "?")))) {
                e.setMessage(ConfigManager.colorize(message.substring(0, loc) + ConfigManager.get("mention-color") + playerName + getChatColor(e.getPlayer()) + message.substring(loc + playerName.length())));
                if (!sound.equalsIgnoreCase("none"))
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
            }
        }
    }

    private String getChatColor(Player player) {
        String format;
        try {
            format = Objects.requireNonNull(CrankedCore.plugin.getConfig().getString("rank-formats." + CrankedCore.vaultChat.getPrimaryGroup(player))).replace("%message%", "");
        } catch (NullPointerException e2) {
            format = Objects.requireNonNull(ConfigManager.get("default-format")).replace("%message%", "");
        }
        if (format.length() > 3 && format.charAt(format.length() - 4) == '&')
            return format.substring(format.length() - 4);
        return format.substring(format.length() - 2);
    }
}
