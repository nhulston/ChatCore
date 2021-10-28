package me.cranked.crankedcore.events;

import me.cranked.crankedcore.CrankedCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ColoredChat implements Listener {
    private final CrankedCore plugin;

    public ColoredChat(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!this.plugin.getConfig().getBoolean("colored-chat-enabled"))
            return;

        // Permission check
        if (e.getPlayer().hasPermission("crankedcore.coloredtext"))
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
    }
}
