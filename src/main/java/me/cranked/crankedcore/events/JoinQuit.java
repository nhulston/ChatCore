package me.cranked.crankedcore.events;

import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {
    private final CrankedCore plugin;

    public JoinQuit(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!this.plugin.getConfig().getBoolean("custom-join-quit-messages-enabled"))
            return;
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.vipjoin")) {
            e.setJoinMessage(ConfigManager.colorize(ConfigManager.get("vip-join").replace("%player%", player.getDisplayName())));
        } else {
            e.setJoinMessage(ConfigManager.colorize(ConfigManager.get("join").replace("%player%", player.getDisplayName())));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!this.plugin.getConfig().getBoolean("custom-join-quit-messages-enabled"))
            return;
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.vipquit")) {
            e.setQuitMessage(ConfigManager.colorize(ConfigManager.get("vip-quit").replace("%player%", player.getDisplayName())));
        } else {
            e.setQuitMessage(ConfigManager.colorize(ConfigManager.get("quit").replace("%player%", player.getDisplayName())));
        }
    }
}
