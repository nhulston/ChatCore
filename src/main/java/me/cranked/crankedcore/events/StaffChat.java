package me.cranked.crankedcore.events;

import me.cranked.crankedcore.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!ConfigManager.getEnabled("staff-chat"))
            return;

        if (me.cranked.crankedcore.commands.StaffChat.staffChatList.contains(e.getPlayer())) {
            Player player = e.getPlayer();
            String msg = e.getMessage();
            e.setCancelled(true);
            Bukkit.getLogger().info("[CrankedCore] " + ConfigManager.colorize(ConfigManager.get("staff-chat-format").replace("%message%", msg).replace("%player%", player.getDisplayName())));
            me.cranked.crankedcore.commands.StaffChat.sendMessage(msg, player);
        }
    }
}
