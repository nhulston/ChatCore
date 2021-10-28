package me.cranked.crankedcore.events;

import java.util.Objects;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class LockChat implements Listener {
    private final CrankedCore plugin;

    public LockChat(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!plugin.getConfig().getBoolean("lock-enabled"))
            return;
        Player player = e.getPlayer();
        if (plugin.getChatLocked() && !player.hasPermission("crankedcore.lock.bypass")) {
            e.setCancelled(true);
            player.sendMessage(PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', Objects.<String>requireNonNull(plugin.getConfig().getString("locked-msg")))));
        }
    }
}
