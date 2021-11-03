package me.cranked.crankedcore.events;

import me.cranked.crankedcore.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AntiCaps implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!ConfigManager.getEnabled("anti-caps"))
            return;

        // No bypass
        Player player = e.getPlayer();
        if (!player.hasPermission("crankedcore.caps.bypass") && e.getMessage().length() >= ConfigManager.getInt("anti-caps-min-length")) {
            int upperCaseCount = 0;
            for (int i = 0; i < e.getMessage().length(); i++) {
                if (e.getMessage().charAt(i) >= 'A' && e.getMessage().charAt(i) <= 'Z')
                    upperCaseCount++;
            }
            int percentCaps = upperCaseCount * 100 / e.getMessage().length();
            if (percentCaps >= ConfigManager.getInt("anti-caps-percentage")) {
                String msg = e.getMessage().charAt(0) + e.getMessage().substring(1).toLowerCase();
                e.setMessage(msg);
            }
        }
    }
}
