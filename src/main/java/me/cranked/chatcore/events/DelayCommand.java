package me.cranked.chatcore.events;

import java.util.HashMap;
import java.util.UUID;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.util.FormatText;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class DelayCommand implements Listener {
    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        // Config check
        if (ConfigManager.getInt("command-delay-in-millis") <= 0)
            return;

        // Bypass check
        Player player = e.getPlayer();
        if (player.hasPermission("chatcore.commanddelay.bypass"))
            return;

        // Cancel, send message
        if (this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
            e.setCancelled(true);
            double remainingTime = Math.round((this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000.0D * 10.0D) / 10.0D;
            player.sendMessage(FormatText.formatText(ConfigManager.get("command-delay").replace("%time%", Double.toString(remainingTime))));
        }

        // Set cooldown
        else {
            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + ConfigManager.getInt("command-delay-in-millis"));
        }
    }
}
