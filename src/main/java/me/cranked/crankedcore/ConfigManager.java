package me.cranked.crankedcore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    private static File file;
    private static FileConfiguration customFile;
    public static CrankedCore plugin;
    private static Map<String, String> messages;
    private static Map<String, List<String>> multiLineMessages;

    public static void setup() {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("CrankedCore")).getDataFolder(), "deathmessages.yml");
        if (!file.exists())
            try {
                if (!file.createNewFile()) {
                    throw new IOException("Couldn't create deathmessages.yml file");
                }
            } catch (IOException e) {
                System.out.println("Couldn't create deathmessages.yml file");
            }
        customFile = YamlConfiguration.loadConfiguration(file);

        initConfig();
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
        initConfig();
    }

    public static String colorize(String s) {
        return s.replaceAll("&", "§");
    }

    private static void initConfig() {
        messages = new HashMap<>();
        multiLineMessages = new HashMap<>();

        // General
        messages.put("reload", getConfig("reload-msg"));
        messages.put("no-permission", getConfig("no-permission-msg"));
        multiLineMessages.put("help-msg", getListConfig("help-msg"));

        // Clear chat
        messages.put("clear", getConfig("clear-msg"));
        messages.put("clear-anon", getConfig("clear-msg-anon"));

        // Lock chat
        messages.put("lock", getConfig("lock-msg"));
        messages.put("lock-anon", getConfig("lock-msg-anon"));
        messages.put("unlock", getConfig("unlock-msg"));
        messages.put("unlock-anon", getConfig("unlock-msg-anon"));
        messages.put("locked", getConfig("locked-msg"));
        messages.put("lock-silent", getConfig("lock-silent-msg"));
        messages.put("unlock-silent", getConfig("unlock-silent-msg"));

        // Slow chat
        messages.put("slow", getConfig("slow-msg"));
        messages.put("slow-anon", getConfig("slow-msg-anon"));
        messages.put("slow-usage", getConfig("slow-usage-msg"));
        messages.put("slow-silent", getConfig("slow-silent-msg"));
        messages.put("slow-delay", getConfig("slow-delay-msg"));
        messages.put("unslow", getConfig("unslow-msg"));
        messages.put("unslow-silent", getConfig("unslow-silent-msg"));
        
        // Chat delay
        messages.put("delay", getConfig("delay-msg"));
        
        // Command delay
        messages.put("command-delay", getConfig("command-delay-msg"));

        // Staff chat
        messages.put("staff-chat-format", getConfig("staff-chat-format"));
        messages.put("staff-chat-on", getConfig("staff-chat-on-msg"));
        messages.put("staff-chat-off", getConfig("staff-chat-off-msg"));
        
        // Blocked words
        messages.put("blocked-words-replace-word", getConfig("blocked-words-replace-word"));
        messages.put("blocked-words-replace-char", getConfig("blocked-words-replace-char"));
        multiLineMessages.put("blocked-words", getListConfig("blocked-words"));
        multiLineMessages.put("blocked-words-ignore-in-bigger-words", getListConfig("blocked-words-ignore-in-bigger-words"));
        // TODO blocked-words-punishments

        // Blocked commands
        messages.put("blocked-commands", getConfig("blocked-commands-msg"));
        messages.put("blocked-commands-warn-staff", getConfig("blocked-commands-warn-staff-msg"));
        messages.put("block-all-commands-containing-colon", getConfig("block-all-commands-containing-colon-msg"));
        multiLineMessages.put("blocked-commands", getListConfig("blocked-commands"));
        // TODO blocked-commands-punishments

        // Command spy
        messages.put("command-spy-format", getConfig("command-spy-format"));
        messages.put("command-spy-on", getConfig("command-spy-on-msg"));
        messages.put("command-spy-off", getConfig("command-spy-off-msg"));
        multiLineMessages.put("command-spy-ignored-commands", getListConfig("command-spy-ignored-commands"));

        // Announce
        messages.put("announce-format", getConfig("announce-format"));
        messages.put("announce-usage", getConfig("announce-usage"));
        messages.put("announce-sound", getConfig("announce-sound"));

        // Warning
        messages.put("warning-format", getConfig("warning-format"));
        messages.put("warning-usage", getConfig("warning-usage"));
        messages.put("warning-sound", getConfig("warning-sound"));

        // Staff Announce
        messages.put("staff-announce-format", getConfig("staff-announce-format"));
        messages.put("staff-announce-usage", getConfig("staff-announce-usage"));
        messages.put("staff-announce-sound", getConfig("staff-announce-sound"));

        // Disable until move
        messages.put("disable-chat-until-move", getConfig("disable-chat-until-move-msg"));
        messages.put("disable-command-until-move", getConfig("disable-command-until-move-msg"));
        multiLineMessages.put("disable-commands-until-move", getListConfig("disable-commands-until-move"));

        // Anti ad
        messages.put("anti-ad", getConfig("anti-ad-msg"));
        messages.put("anti-ad-inform", getConfig("anti-ad-inform-msg"));
        multiLineMessages.put("anti-ad-whitelist", getListConfig("anti-ad-whitelist"));

        // Join quit
        messages.put("join", getConfig("join-msg"));
        messages.put("quit", getConfig("quit-msg"));
        messages.put("vip-join", getConfig("vip-join-msg"));
        messages.put("vip-quit", getConfig("vip-quit-msg"));

        // Chat format
        messages.put("default-format", getConfig("default-format"));
        // TODO rank-formats
        multiLineMessages.put("hover-format", getListConfig("hover-format"));
        messages.put("click-action-mode", getConfig("click-action-mode"));
        messages.put("click-action", getConfig("click-action"));

        // Mention
        messages.put("mention-color", getConfig("mention-color"));
        messages.put("mention-sound", getConfig("mention-sound"));

        // MOTD
        multiLineMessages.put("motd", getListConfig("motd"));

        // Log
        multiLineMessages.put("chat-logger-included-commands", getListConfig("chat-logger-included-commands"));
        multiLineMessages.put("command-logger-ignored-commands", getListConfig("command-logger-ignored-commands"));
        messages.put("logger-format", getConfig("logger-format"));

        // Anti ASCII
        messages.put("ascii-cancel", getConfig("ascii-cancel-msg"));
        messages.put("ascii-replace-character", getConfig("ascii-replace-character"));

        // Autobroadcast
        multiLineMessages.put("auto-broadcast-messages", getListConfig("auto-broadcast-messages"));
    }

    public static String get(String s) {
        return messages.get(s);
    }

    public static List<String> getList(String s) {
        return multiLineMessages.get(s);
    }

    private static String getConfig(String s) {
        return colorize(Objects.requireNonNull(plugin.getConfig().getString(s)));
    }

    private static List<String> getListConfig(String s) {
        return plugin.getConfig().getStringList(s);
    }
}
