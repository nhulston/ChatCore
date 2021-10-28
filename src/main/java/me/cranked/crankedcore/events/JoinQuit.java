package me.cranked.crankedcore.events;

import java.util.Objects;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.ChatColor;
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
            e.setJoinMessage(CrankedCore.placeholderColor(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("vip-join-message")).replace("%player%", player.getDisplayName())), player));
        } else {
            e.setJoinMessage(CrankedCore.placeholderColor(Objects.requireNonNull(this.plugin.getConfig().getString("join-message")).replace("%player%", player.getDisplayName()), player));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!this.plugin.getConfig().getBoolean("custom-join-quit-messages-enabled"))
            return;
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.vipquit")) {
            e.setQuitMessage(CrankedCore.placeholderColor(Objects.requireNonNull(this.plugin.getConfig().getString("vip-quit-message")).replace("%player%", player.getDisplayName()), player));
        } else {
            e.setQuitMessage(CrankedCore.placeholderColor(Objects.requireNonNull(this.plugin.getConfig().getString("quit-message")).replace("%player%", player.getDisplayName()), player));
        }
    }
}
