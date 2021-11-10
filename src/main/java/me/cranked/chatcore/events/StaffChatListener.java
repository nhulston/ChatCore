package me.cranked.chatcore.events;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.CommandStaffChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class StaffChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!ConfigManager.getEnabled("staff-chat"))
            return;

//        if (me.cranked.chatcore.commands.StaffChat.staffChatList.contains(e.getPlayer())) {
//            Player player = e.getPlayer();
//            String msg = e.getMessage();
//            e.setCancelled(true);
//            Bukkit.getLogger().info("[ChatCore] " + ConfigManager.colorize(ConfigManager.get("staff-chat-format").replace("%message%", msg).replace("%player%", player.getDisplayName())));
//            me.cranked.chatcore.commands.StaffChat.sendMessage(msg, player);
//        }
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (CommandStaffChat.staffChatList.contains(uuid)) {
            String msg = e.getMessage();
            e.setCancelled(true);
            Bukkit.getLogger().info("[ChatCore] " + ConfigManager.colorize(ConfigManager.get("staff-chat-format").replace("%message%", msg).replace("%player%", player.getDisplayName())));
            CommandStaffChat.sendMessage(msg, player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        // Even if the player's uuid is not in this list
        // it will not throw any errors.
        CommandStaffChat.staffChatList.remove(uniqueId);
    }
}
