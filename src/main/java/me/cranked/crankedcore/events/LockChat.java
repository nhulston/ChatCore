package me.cranked.crankedcore.events;

import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
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
            player.sendMessage(ConfigManager.get("locked"));
        }
    }
}
