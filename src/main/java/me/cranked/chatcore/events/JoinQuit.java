package me.cranked.chatcore.events;

import me.cranked.chatcore.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!ConfigManager.getEnabled("custom-join-quit-messages"))
            return;
        Player player = e.getPlayer();
        if (player.isOp()) {
            e.setJoinMessage(null);
        } else if (player.hasPermission("chatcore.vipjoin")) {
            e.setJoinMessage(ConfigManager.colorize(ConfigManager.get("vip-join").replace("%player%", player.getDisplayName())));
        } else {
            e.setJoinMessage(ConfigManager.colorize(ConfigManager.get("join").replace("%player%", player.getDisplayName())));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!ConfigManager.getEnabled("custom-join-quit-messages"))
            return;
        Player player = e.getPlayer();
        if (player.hasPermission("chatcore.vipquit")) {
            e.setQuitMessage(ConfigManager.colorize(ConfigManager.get("vip-quit").replace("%player%", player.getDisplayName())));
        } else {
            e.setQuitMessage(ConfigManager.colorize(ConfigManager.get("quit").replace("%player%", player.getDisplayName())));
        }
    }
}
