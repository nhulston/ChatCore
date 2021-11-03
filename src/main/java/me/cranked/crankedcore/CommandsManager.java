package me.cranked.crankedcore;

import java.util.List;
import me.cranked.crankedcore.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandsManager implements CommandExecutor {
    private final CrankedCore plugin;

    public CommandsManager(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("crankedcore.help")) {
                List<String> messages = ConfigManager.getList("help-msg");
                for (String msg : messages)
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aServer is running CrankedCore v1.0 by &bteeditator&a.")); 
                // TODO replace teeditator with spigot/mcm link
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("crankedcore.reload") || !(sender instanceof Player)) {
                ConfigManager.reload();
                DeathMessagesConfigManager.reload();
                sender.sendMessage(ConfigManager.get("reload"));
            } else {
                sender.sendMessage(ConfigManager.get("no-permission"));
            }
        } else if (args[0].equalsIgnoreCase("clear")) {
            Clear clear = new Clear();
            clear.command(sender, args);
        } else if (args[0].equalsIgnoreCase("slow")) {
            Slow slow = new Slow(plugin);
            slow.command(sender, args);
        } else if (args[0].equalsIgnoreCase("lock") || args[0].equalsIgnoreCase("mute")) {
            Lock lock = new Lock(plugin);
            lock.command(sender, args);
        } else if (ConfigManager.getEnabled("staff-chat") && args[0].equalsIgnoreCase("staff")) {
            StaffChat staffchat = new StaffChat(plugin);
            staffchat.command(sender, args);
        } else if (args[0].equalsIgnoreCase("spy")) {
            CommandSpy spy = new CommandSpy();
            spy.command(sender);
        } else if (args[0].equalsIgnoreCase("announce") || args[0].equalsIgnoreCase("shout") || args[0].equalsIgnoreCase("broadcast")) {
            Announce announce = new Announce();
            announce.command(sender, args);
        } else if (args[0].equalsIgnoreCase("warning") || args[0].equalsIgnoreCase("warn")) {
            Warning warn = new Warning();
            warn.command(sender, args);
        } else if (args[0].equalsIgnoreCase("staffannounce")) {
            StaffAnnounce staff = new StaffAnnounce();
            staff.command(sender, args);
        }
        return true;
    }
}
