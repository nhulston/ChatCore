package me.cranked.crankedcore.events;

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MOTD implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // Config check
        if (!ConfigManager.getEnabled("motd"))
            return;

        // Print list of MOTD
        List<String> motd = ConfigManager.getList("motd");
        Player player = e.getPlayer();
        for (String msg : motd)
            player.sendMessage(CrankedCore.placeholderColor(PlaceholderAPI.setPlaceholders(player, msg), player));
    }
}
