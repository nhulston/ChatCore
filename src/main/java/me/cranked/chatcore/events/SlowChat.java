package me.cranked.chatcore.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SlowChat implements Listener {
    private final Map<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!ConfigManager.getEnabled("slow-chat"))
            return;

        // Bypass check
        Player player = e.getPlayer();
        if (player.hasPermission("chatcore.slow.bypass"))
            return;

        // Cancel, send message
        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
            e.setCancelled(true);
            double remainingTime = Math.round((this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000.0D * 10.0D) / 10.0D;
            player.sendMessage(ConfigManager.colorize(ConfigManager.get("slow-delay").replace("%time%", Double.toString(remainingTime))));
        }
        // Set cooldown
        else {
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (ChatCore.getDelay() * 1000L));
        }
    }
}
