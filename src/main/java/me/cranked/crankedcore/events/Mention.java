package me.cranked.crankedcore.events;

import java.util.Objects;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Mention implements Listener {
    private final CrankedCore plugin;

    public Mention(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!this.plugin.getConfig().getBoolean("mention-enabled"))
            return;

        // Permission check
        if (!e.getPlayer().hasPermission("crankedcore.mention"))
            return;

        String message = e.getMessage();
        String messageLower = message.toLowerCase();
        boolean playSound = !Objects.requireNonNull(this.plugin.getConfig().getString("mention-sound")).equalsIgnoreCase("none");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String playerName = onlinePlayer.getName();
            String playerNameLower = playerName.toLowerCase();
            int loc = messageLower.indexOf(playerNameLower);
            // TODO why is this big if statement necessary
            if ((loc != -1 && (loc + playerName.length() == message.length() || (loc == 0 && messageLower.contains(playerNameLower + " ")) || messageLower.contains(" " + playerNameLower + " ") || messageLower.contains(" " + playerNameLower + ".") || messageLower.contains(" " + playerNameLower + "?") || messageLower.contains(" " + playerNameLower + "!"))) || (message.length() == playerName.length() + 1 && (messageLower.contains(playerNameLower + ".") || messageLower.contains(playerNameLower + "!") || messageLower.contains(playerNameLower + "?")))) {
                e.setMessage(ChatColor.translateAlternateColorCodes('&', message.substring(0, loc) + this.plugin.getConfig().getString("mention-color") + playerName + getChatColor(e.getPlayer()) + message.substring(loc + playerName.length())));
                if (playSound)
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(this.plugin.getConfig().getString("mention-sound")), 1.0F, 1.0F);
            }
        }
    }

    private String getChatColor(Player player) {
        String format;
        try {
            format = Objects.requireNonNull(this.plugin.getConfig().getString("rank-formats." + CrankedCore.vaultChat.getPrimaryGroup(player))).replace("%message%", "");
        } catch (NullPointerException e2) {
            format = Objects.requireNonNull(this.plugin.getConfig().getString("default-format")).replace("%message%", "");
        }
        if (format.length() > 3 && format.charAt(format.length() - 4) == '&')
            return format.substring(format.length() - 4);
        return format.substring(format.length() - 2);
    }
}
