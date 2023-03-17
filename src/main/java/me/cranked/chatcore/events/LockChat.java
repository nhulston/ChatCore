package me.cranked.chatcore.events;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class LockChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        if (!ConfigManager.getEnabled("lock-chat"))
            return;
        Player player = e.getPlayer();
        if (ChatCore.getChatLocked() && !player.hasPermission("chatcore.lock.bypass")) {
            e.setCancelled(true);
            player.sendMessage(ConfigManager.get("locked"));
        }
    }
}
