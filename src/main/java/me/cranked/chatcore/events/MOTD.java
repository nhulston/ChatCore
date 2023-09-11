package me.cranked.chatcore.events;

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.util.FormatText;
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
            player.sendMessage(FormatText.placeholderize(msg, player));
    }
}
