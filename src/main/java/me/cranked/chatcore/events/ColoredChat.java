package me.cranked.chatcore.events;

import me.cranked.chatcore.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ColoredChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        // Config check
        if (!ConfigManager.getEnabled("colored-chat"))
            return;

        // Permission check
        if (e.getPlayer().hasPermission("chatcore.coloredtext"))
            e.setMessage(ConfigManager.colorize(e.getMessage()));
    }
}
