package me.cranked.crankedcore.events;

import java.util.HashMap;
import java.util.UUID;
import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class DelayCommand implements Listener {
    private final CrankedCore plugin;

    private final HashMap<UUID, Long> cooldown;

    public DelayCommand(CrankedCore plugin) {
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        // Config check
        if (this.plugin.getConfig().getInt("command-delay-in-millis") <= 0)
            return;

        // Bypass check
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.commanddelay.bypass"))
            return;

        // Cancel, send message
        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
            e.setCancelled(true);
            double remainingTime = Math.round((this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000.0D * 10.0D) / 10.0D;
            player.sendMessage(ConfigManager.colorize(ConfigManager.get("command-delay").replace("%time%", Double.toString(remainingTime))));
        }

        // Set cooldown
        else {
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + this.plugin.getConfig().getInt("command-delay-in-millis"));
        }
    }
}
