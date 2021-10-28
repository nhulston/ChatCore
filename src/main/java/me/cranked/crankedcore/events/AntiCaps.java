package me.cranked.crankedcore.events;

import me.cranked.crankedcore.CrankedCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AntiCaps implements Listener {
    private final CrankedCore plugin;

    public AntiCaps(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!plugin.getConfig().getBoolean("anti-caps-enabled"))
            return;

        // No bypass
        Player player = e.getPlayer();
        if (!player.hasPermission("crankedcore.caps.bypass") && e.getMessage().length() >= plugin.getConfig().getInt("anti-caps-min-length")) {
            int upperCaseCount = 0;
            for (int i = 0; i < e.getMessage().length(); i++) {
                if (e.getMessage().charAt(i) >= 'A' && e.getMessage().charAt(i) <= 'Z')
                    upperCaseCount++;
            }
            int percentCaps = upperCaseCount * 100 / e.getMessage().length();
            if (percentCaps >= plugin.getConfig().getInt("anti-caps-percentage")) {
                String msg = e.getMessage().charAt(0) + e.getMessage().substring(1).toLowerCase();
                e.setMessage(msg);
            }
        }
    }
}
