package me.cranked.crankedcore.events;

import java.util.List;
import java.util.Objects;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockedCommands implements Listener {
    // TODO config tips section. Like how to change unknown command message
    // TODO better plugin hiding see https://www.spigotmc.org/resources/pluginhider-pluginhiderplus-hide-your-plugins-anti-tab-complete-all-message-replace.51583/
    // TODO auto respawn
    private final CrankedCore plugin;

    public BlockedCommands(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        // Config check
        if (plugin.getConfig().getStringList("blocked-commands").size() == 0)
            return;

        // Bypass check
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.blockedcommands.bypass"))
            return;

        // Colon check
        if (plugin.getConfig().getBoolean("block-all-commands-containing-colon") && e.getMessage().contains(":")) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("block-all-commands-containing-colon-message"))));
        }
        
        // Check if contains blocked commands
        List<String> blockedCommands = plugin.getConfig().getStringList("blocked-commands");
        String msg = e.getMessage();
        for (String blockedCommand : blockedCommands) {
            String originalCommand = blockedCommand;
            // Commas act as a tag to punish for specific commands
            int commaLoc = blockedCommand.indexOf(",");
            if (commaLoc != -1)
                blockedCommand = blockedCommand.substring(0, commaLoc);
            if (msg.equalsIgnoreCase(blockedCommand) || (msg.length() > blockedCommand.length() && msg.substring(0, blockedCommand.length() + 1).equalsIgnoreCase(blockedCommand + " "))) {
                // A comma exists, punish
                if (commaLoc != -1) {
                    // Find punishment
                    String punishmentCategory = originalCommand.substring(commaLoc + 2);
                    List<String> punishments = plugin.getConfig().getStringList("blocked-commands-punishments." + punishmentCategory);
                    for (String punishment : punishments) {
                        if (e.isAsynchronous()) {
                            (new BukkitRunnable() {
                                public void run() {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), punishment.replaceAll("%player%", e.getPlayer().getName()).replaceAll("%command%", e.getMessage()));
                                }
                            }).runTask(plugin);
                            continue;
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), punishment.replaceAll("%player%", e.getPlayer().getName()).replaceAll("%command%", e.getMessage()));
                    }
                }

                // Cancel command
                e.setCancelled(true);

                // Send message
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("blocked-commands-msg"))));

                // Warn staff
                if (plugin.getConfig().getBoolean("blocked-commands-warn-staff"))
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission("crankedcore.blockedcommands.warn"))
                            onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("blocked-commands-warn-staff-msg")).replace("%player%", player.getDisplayName()).replace("%command%", e.getMessage())));
                    }
                return;
            }
        }
    }
}
