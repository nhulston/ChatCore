package me.cranked.crankedcore.events;

import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class LockChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!ConfigManager.getEnabled("lock-chat"))
            return;
        Player player = e.getPlayer();
        if (CrankedCore.getChatLocked() && !player.hasPermission("crankedcore.lock.bypass")) {
            e.setCancelled(true);
            player.sendMessage(ConfigManager.get("locked"));
        }
    }
}
