package me.cranked.chatcore.events;

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class CommandSpy implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        // Config check
        if (!ConfigManager.getEnabled("command-spy"))
            return;

        // Permission check
        Player player = e.getPlayer();
        if (player.hasPermission("chatcore.commandspy.bypass"))
            return;

        // Check ignored
        List<String> ignoredCommands = ConfigManager.getList("command-spy-ignored-commands");
        for (String ignoredCommand : ignoredCommands) {
            if (e.getMessage().length() >= ignoredCommand.length() && e.getMessage().substring(0, ignoredCommand.length()).equalsIgnoreCase(ignoredCommand))
                return;
        }

        // Broadcast to staff with enabled
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (me.cranked.chatcore.commands.CommandSpy.commandSpyList.contains(onlinePlayer))
                onlinePlayer.sendMessage(PlaceholderAPI.setPlaceholders(player, ConfigManager.colorize(ConfigManager.get("command-spy-format").replace("%player%", player.getDisplayName()).replace("%command%", e.getMessage()))));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (ConfigManager.getEnabled("command-spy-enabled-on-join") && e.getPlayer().hasPermission("chat.commandspy"))
            me.cranked.chatcore.commands.CommandSpy.commandSpyList.add(e.getPlayer());
    }
}
