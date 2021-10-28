package me.cranked.crankedcore.events;

import java.util.List;
import java.util.Objects;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class CommandSpy implements Listener {
    private final CrankedCore plugin;

    public CommandSpy(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        // Config check
        if (!plugin.getConfig().getBoolean("command-spy-enabled"))
            return;

        // Permission check
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.commandspy.bypass"))
            return;

        // Check ignored
        List<String> ignoredCommands = plugin.getConfig().getStringList("command-spy-ignored-commands");
        for (String ignoredCommand : ignoredCommands) {
            if (e.getMessage().length() >= ignoredCommand.length() && e.getMessage().substring(0, ignoredCommand.length()).equalsIgnoreCase(ignoredCommand))
                return;
        }

        // Broadcast to staff with enabled
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (me.cranked.crankedcore.commands.CommandSpy.commandSpyList.contains(onlinePlayer))
                onlinePlayer.sendMessage(PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("command-spy-format")).replace("%player%", player.getDisplayName()).replace("%command%", e.getMessage()))));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (plugin.getConfig().getBoolean("command-spy-enabled-on-join") && e.getPlayer().hasPermission("chat.commandspy"))
            me.cranked.crankedcore.commands.CommandSpy.commandSpyList.add(e.getPlayer());
    }
}
