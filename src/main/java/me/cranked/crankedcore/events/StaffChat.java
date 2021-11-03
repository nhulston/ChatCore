package me.cranked.crankedcore.events;

import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChat implements Listener {
    private final CrankedCore plugin;

    public StaffChat(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!ConfigManager.getEnabled("staff-chat"))
            return;
        if (me.cranked.crankedcore.commands.StaffChat.staffChatList.contains(e.getPlayer())) {
            Player player = e.getPlayer();
            String msg = e.getMessage();
            e.setCancelled(true);
            System.out.println(ConfigManager.colorize(ConfigManager.get("staff-chat-format").replace("%message%", msg).replace("%player%", player.getDisplayName())));
            me.cranked.crankedcore.commands.StaffChat sendMessage = new me.cranked.crankedcore.commands.StaffChat(this.plugin);
            sendMessage.sendMessage(msg, player);
        }
    }
}
