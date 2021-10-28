package me.cranked.crankedcore.events;

import me.cranked.crankedcore.CrankedCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AddPeriod implements Listener {
    private final CrankedCore plugin;

    public AddPeriod(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!plugin.getConfig().getBoolean("add-period-enabled"))
            return;
        char lastChar = e.getMessage().charAt(e.getMessage().length() - 1);
        if (!e.getPlayer().hasPermission("crankedcore.addperiod.bypass") &&
                e.getMessage().length() >= this.plugin.getConfig().getInt("add-period-min-length") &&
                lastChar != '.' && lastChar != '!' && lastChar != '?')
            e.setMessage(e.getMessage() + ".");
    }
}
