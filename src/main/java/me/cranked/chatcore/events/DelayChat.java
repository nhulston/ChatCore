package me.cranked.chatcore.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.util.FormatText;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class DelayChat implements Listener {
    private final Map<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        // Delay check
        if (ConfigManager.getInt("delay-in-millis") <= 0)
            return;

        // Bypass check
        Player player = e.getPlayer();
        if (player.hasPermission("chatcore.delay.bypass") || ChatCore.getDelay() != 0)
            return;

        // Cancel, send message
        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
            e.setCancelled(true);
            double remainingTime = Math.round((this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000.0D * 10.0D) / 10.0D;
            player.sendMessage(FormatText.formatText(ConfigManager.get("delay").replace("%time%", Double.toString(remainingTime))));
        }

        // Set cooldown
        else {
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + ConfigManager.getInt("delay-in-millis"));
        }
    }
}
