package me.cranked.crankedcore.events;

import java.util.List;
import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockedCommands implements Listener {
    // TODO config tips section. Like how to change unknown command message
    // TODO better plugin hiding see https://www.spigotmc.org/resources/pluginhider-pluginhiderplus-hide-your-plugins-anti-tab-complete-all-message-replace.51583/
    // TODO auto respawn
    // TODO console filtering
    private final CrankedCore plugin;

    public BlockedCommands(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        // Config check
        if (ConfigManager.getList("blocked-commands").size() == 0)
            return;

        // Bypass check
        Player player = e.getPlayer();
        if (player.hasPermission("crankedcore.blockedcommands.bypass"))
            return;

        // Colon check
        if (ConfigManager.getEnabled("block-all-commands-containing-colon") && e.getMessage().contains(":")) {
            e.setCancelled(true);
            player.sendMessage(ConfigManager.get("block-all-commands-containing-colon"));
        }
        
        // Check if contains blocked commands
        List<String> blockedCommands = ConfigManager.getList("blocked-commands");
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
                player.sendMessage(ConfigManager.get("blocked-commands"));

                // Warn staff
                if (ConfigManager.getEnabled("blocked-commands-warn-staff"))
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission("crankedcore.blockedcommands.warn"))
                            onlinePlayer.sendMessage(ConfigManager.colorize(ConfigManager.get("blocked-commands-warn-staff-msg").replace("%player%", player.getDisplayName()).replace("%command%", e.getMessage())));
                    }
                return;
            }
        }
    }
}
