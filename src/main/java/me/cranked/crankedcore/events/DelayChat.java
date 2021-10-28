package me.cranked.crankedcore.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class DelayChat implements Listener {
    private final CrankedCore plugin;

    private final Map<UUID, Long> cooldown = new HashMap<>();

    public DelayChat(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Delay check
        if (this.plugin.getConfig().getInt("delay-in-millis") <= 0)
            return;

        // Bypass check
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.delay.bypass") || this.plugin.getDelay() != 0)
            return;

        // Cancel, send message
        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
            e.setCancelled(true);
            double remainingTime = Math.round((this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000.0D * 10.0D) / 10.0D;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("delay-msg")).replace("%time%", Double.toString(remainingTime))));
        }

        // Set cooldown
        else {
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + this.plugin.getConfig().getInt("delay-in-millis"));
        }
    }
}
