package me.cranked.chatcore.events;

import me.cranked.chatcore.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class DonatorChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;
        if (!ConfigManager.getEnabled("donator-chat"))
            return;

        if (me.cranked.chatcore.commands.DonatorChat.donatorChatList.contains(e.getPlayer())) {
            Player player = e.getPlayer();
            String msg = e.getMessage();
            e.setCancelled(true);
            Bukkit.getLogger().info("[ChatCore] " + ConfigManager.get("donator-chat-format").replace("%message%", msg).replace("%player%", player.getDisplayName()));
            me.cranked.chatcore.commands.DonatorChat.sendMessage(msg, player);
        }
    }
}
