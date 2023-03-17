package me.cranked.chatcore.events;

import me.cranked.chatcore.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AddPeriod implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        if (!ConfigManager.getEnabled("add-period"))
            return;
        char lastChar = e.getMessage().charAt(e.getMessage().length() - 1);
        if (!e.getPlayer().hasPermission("chatcore.addperiod.bypass") &&
                e.getMessage().length() >= ConfigManager.getInt("add-period-min-length") &&
                lastChar != '.' && lastChar != '!' && lastChar != '?')
            e.setMessage(e.getMessage() + ".");
    }
}
