package me.cranked.crankedcore.events;

import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AntiAscii implements Listener {
    private final CrankedCore plugin;

    public AntiAscii(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!plugin.getConfig().getBoolean("disable-ascii"))
            return;

        // Ascii detected
        if (!e.getPlayer().hasPermission("crankedcore.ascii.bypass") && containsAscii(e.getMessage().toCharArray()))
            // Cancel message
            if (plugin.getConfig().getBoolean("ascii-cancel")) {
                e.getPlayer().sendMessage(ConfigManager.get("ascii-cancel"));
                e.setCancelled(true);
            }

            // Replace ascii characters
            else {
                char[] message = e.getMessage().toCharArray();
                for (int i = 0; i < message.length; i++) {
                    if (message[i] >= 128)
                        message[i] = ConfigManager.get("ascii-replace-character").charAt(0);
                }
                e.setMessage(new String(message));
            }
    }

    public static boolean containsAscii(char[] msg) {
        for (char ch : msg) {
            if (ch >= 128)
            return true;
        }
        return false;
    }
}
