package me.cranked.chatcore.events;

import java.util.Objects;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.VersionManager;
import me.cranked.chatcore.util.FormatText;
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
        if (e.isCancelled()) return;
        // Config check
        if (!ConfigManager.getEnabled("mention"))
            return;

        // Permission check
        if (!e.getPlayer().hasPermission("chatcore.mention"))
            return;

        String senderNameLower = e.getPlayer().getName().toLowerCase();
        String message = e.getMessage();
        String messageLower = message.toLowerCase();
        String sound = ConfigManager.get("mention-sound");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (VersionManager.isV16() && onlinePlayer.isInvisible()) continue;
            String playerName = onlinePlayer.getName();
            String playerNameLower = playerName.toLowerCase();
            // Write regex that checks if message contains the word playerName
            if (!senderNameLower.equals(playerNameLower) && messageLower.matches(".*\\b" + playerNameLower + "\\b.*")) {
                int loc = messageLower.indexOf(playerNameLower);
                if (loc != -1) {
                    e.setMessage(FormatText.formatText(message.substring(0, loc) + ConfigManager.get("mention-color") + playerName + getChatColor(e.getPlayer()) + message.substring(loc + playerName.length())));
                }
                if (!sound.equalsIgnoreCase("none"))
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
            }
        }
    }

    private String getChatColor(Player player) {
        String format;
        try {
            format = Objects.requireNonNull(ChatCore.plugin.getConfig().getString("rank-formats." + ChatCore.vaultChat.getPrimaryGroup(player))).replace("%message%", "");
        } catch (NullPointerException e2) {
            format = Objects.requireNonNull(ConfigManager.get("default-format")).replace("%message%", "");
        }
        if (format.length() > 3 && format.charAt(format.length() - 4) == '&')
            return format.substring(format.length() - 4);
        return format.substring(format.length() - 2);
    }
}
