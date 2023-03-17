package me.cranked.chatcore.events;

import me.cranked.chatcore.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AutoCaps implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        // Config check
        if (!ConfigManager.getEnabled("auto-caps"))
            return;

        // Set first char
        if (!e.getPlayer().hasPermission("chatcore.autocaps.bypass") && e.getMessage().length() >= ConfigManager.getInt("auto-caps-min-length"))
            e.setMessage(Character.toUpperCase(e.getMessage().charAt(0)) + e.getMessage().substring(1));
    }
}
