package me.cranked.crankedcore.events;

import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ColoredChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!ConfigManager.getEnabled("colored-chat"))
            return;

        // Permission check
        if (e.getPlayer().hasPermission("crankedcore.coloredtext"))
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
    }
}
