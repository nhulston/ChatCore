package me.cranked.chatcore.events;

import me.cranked.chatcore.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Death implements Listener {

    /**
     * Changes the death message colors to a format
     * Works on versions 1.7-1.19
     * @param e PlayerDeathEvent, used to calculate what death message to use
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent e) {
        String playerColor = ConfigManager.get("death-messages-player-color");
        String color = ConfigManager.colorize(ConfigManager.get("death-messages-color"));
        String message = e.getDeathMessage();
        if (message == null) return;
        String player = e.getEntity().getName();
        message = color + message.replace(player, playerColor + player + color) + ".";

        Player killer = e.getEntity().getKiller();
        if (killer != null) {
            String killerName = killer.getName();
            message = message.replace(killerName, playerColor + killerName + color);
        }

        e.setDeathMessage(message);
    }
}
