package me.cranked.crankedcore.events;

import me.cranked.crankedcore.CrankedCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AutoCaps implements Listener {
    private final CrankedCore plugin;

    public AutoCaps(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!plugin.getConfig().getBoolean("auto-caps-enabled"))
            return;

        // Set first char
        if (!e.getPlayer().hasPermission("crankedcore.autocaps.bypass") && e.getMessage().length() >= plugin.getConfig().getInt("auto-caps-min-length"))
            e.setMessage(Character.toUpperCase(e.getMessage().charAt(0)) + e.getMessage().substring(1));
    }
}
