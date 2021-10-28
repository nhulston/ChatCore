package me.cranked.crankedcore.events;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SlowChat implements Listener {
    private final CrankedCore plugin;

    private final HashMap<UUID, Long> cooldown;

    public SlowChat(CrankedCore plugin) {
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!this.plugin.getConfig().getBoolean("slow-enabled"))
            return;

        // Bypass check
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.slow.bypass"))
            return;

        // Cancel, send message
        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
            e.setCancelled(true);
            double remainingTime = Math.round((this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000.0D * 10.0D) / 10.0D;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("slow-delay-msg")).replace("%time%", Double.toString(remainingTime))));
        }
        // Set cooldown
        else {
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (this.plugin.getDelay() * 1000L));
        }
    }
}
