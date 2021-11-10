package me.cranked.chatcore.events;

import java.util.List;
import java.util.UUID;

import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.CommandCommandSpy;
import me.cranked.chatcore.commands.CommandSpy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CommandSpyListener implements Listener {
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
        // This will loop through all the ignored messages and return true if the provided
        // conditions are met.
        // #JavaStreams!!
        boolean isIgnored = ignoredCommands.stream()
                .anyMatch(ignoredCommand -> e.getMessage().length() >= ignoredCommand.length()
                        && e.getMessage().substring(0, ignoredCommand.length()).equalsIgnoreCase(ignoredCommand));

        if (isIgnored)
            return;
//        for (String ignoredCommand : ignoredCommands) {
//            if (e.getMessage().length() >= ignoredCommand.length() && e.getMessage().substring(0, ignoredCommand.length()).equalsIgnoreCase(ignoredCommand))
//                return;
//        }

        // Broadcast to staff with enabled
        Bukkit.getOnlinePlayers().stream()
                .filter(onlinePlayer -> CommandCommandSpy.commandSpyList.contains(onlinePlayer.getUniqueId())).forEach(onlinePlayer ->
                        onlinePlayer.sendMessage(PlaceholderAPI.setPlaceholders(player,
                                ConfigManager.colorize(ConfigManager.get("command-spy-format")
                                        .replace("%player%", player.getDisplayName())
                                        .replace("%command%", e.getMessage())))));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
//        if (ConfigManager.getEnabled("command-spy-enabled-on-join") && e.getPlayer().hasPermission("chat.commandspy"))
//            me.cranked.chatcore.commands.CommandSpy.commandSpyList.add(e.getPlayer());
        if (ConfigManager.getEnabled("command-spy-enabled-on-join") && player.hasPermission("chat.commandspy"))
            CommandCommandSpy.commandSpyList.add(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // Even if the player's uuid is not in
        // the list, it will remove it without
        // throwing any errors.
        CommandCommandSpy.commandSpyList.remove(uuid);
    }
}
