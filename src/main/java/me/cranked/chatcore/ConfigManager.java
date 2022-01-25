package me.cranked.chatcore;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getServer;

/**
 * Manages config.yml
 * @author Nick
 * @since 2.0
 */
public class ConfigManager {
    private static ChatCore plugin;
    private static Map<String, String> messages;
    private static Map<String, List<String>> multiLineMessages;
    private static Map<String, Boolean> enabled;
    private static Map<String, Integer> ints;

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    /**
     * Setter method for plugin
     * @param plugin Reference to the plugin we are setting
     */
    public static void setPlugin(ChatCore plugin) {
        ConfigManager.plugin = plugin;
    }

    /**
     * Initializes maps for config.yml, as to make reading
     * configuration files much faster.
     */
    public static void initMaps() {
        messages = new HashMap<>();
        multiLineMessages = new HashMap<>();
        enabled = new HashMap<>();
        ints = new HashMap<>();

        // General
        messages.put("reload", getHelper("reload-msg"));
        messages.put("no-permission", getHelper("no-permission-msg"));
        multiLineMessages.put("help-msg", getListHelper("help-msg"));

        // Clear chat
        enabled.put("clear-chat", getEnabledHelper("clear-chat-enabled"));
        messages.put("clear", getHelper("clear-msg"));
        messages.put("clear-anon", getHelper("clear-msg-anon"));

        // Lock chat
        enabled.put("lock-chat", getEnabledHelper("lock-chat-enabled"));
        messages.put("lock", getHelper("lock-msg"));
        messages.put("lock-anon", getHelper("lock-msg-anon"));
        messages.put("unlock", getHelper("unlock-msg"));
        messages.put("unlock-anon", getHelper("unlock-msg-anon"));
        messages.put("locked", getHelper("locked-msg"));
        messages.put("lock-silent", getHelper("lock-silent-msg"));
        messages.put("unlock-silent", getHelper("unlock-silent-msg"));

        // Slow chat
        enabled.put("slow-chat", getEnabledHelper("slow-chat-enabled"));
        messages.put("slow", getHelper("slow-msg"));
        messages.put("slow-anon", getHelper("slow-msg-anon"));
        messages.put("slow-usage", getHelper("slow-usage-msg"));
        messages.put("slow-silent", getHelper("slow-silent-msg"));
        messages.put("slow-delay", getHelper("slow-delay-msg"));
        messages.put("unslow", getHelper("unslow-msg"));
        messages.put("unslow-silent", getHelper("unslow-silent-msg"));
        
        // Chat delay
        ints.put("delay-in-millis", getIntHelper("delay-in-millis"));
        messages.put("delay", getHelper("delay-msg"));
        
        // Command delay
        ints.put("command-delay-in-millis", getIntHelper("command-delay-in-millis"));
        messages.put("command-delay", getHelper("command-delay-msg"));

        // Staff chat
        enabled.put("staff-chat", getEnabledHelper("staff-chat-enabled"));
        messages.put("staff-chat-format", getHelper("staff-chat-format"));
        messages.put("staff-chat-on", getHelper("staff-chat-on-msg"));
        messages.put("staff-chat-off", getHelper("staff-chat-off-msg"));

        // Anti caps
        ints.put("anti-caps-percentage", getIntHelper("anti-caps-percentage"));
        ints.put("anti-caps-min-length", getIntHelper("anti-caps-min-length"));
        enabled.put("anti-caps", getEnabledHelper("anti-caps-enabled"));

        // Add period
        ints.put("add-period-min-length", getIntHelper("add-period-min-length"));
        enabled.put("add-period", getEnabledHelper("add-period-enabled"));
        
        // Blocked words
        messages.put("blocked-words-replace-word", getHelper("blocked-words-replace-word"));
        messages.put("blocked-words-replace-char", getHelper("blocked-words-replace-char"));
        multiLineMessages.put("blocked-words", getListHelper("blocked-words"));
        multiLineMessages.put("blocked-words-ignore-in-bigger-words", getListHelper("blocked-words-ignore-in-bigger-words"));

        // Blocked commands
        enabled.put("blocked-commands-warn-staff", getEnabledHelper("blocked-commands-warn-staff"));
        enabled.put("block-all-commands-containing-colon", getEnabledHelper("block-all-commands-containing-colon"));
        messages.put("blocked-commands", getHelper("blocked-commands-msg"));
        messages.put("blocked-commands-warn-staff", getHelper("blocked-commands-warn-staff-msg"));
        messages.put("block-all-commands-containing-colon", getHelper("block-all-commands-containing-colon-msg"));
        multiLineMessages.put("blocked-commands", getListHelper("blocked-commands"));

        // Command spy
        enabled.put("command-spy", getEnabledHelper("command-spy-enabled"));
        enabled.put("command-spy-enabled-on-join", getEnabledHelper("command-spy-enabled-on-join"));
        messages.put("command-spy-format", getHelper("command-spy-format"));
        messages.put("command-spy-on", getHelper("command-spy-on-msg"));
        messages.put("command-spy-off", getHelper("command-spy-off-msg"));
        multiLineMessages.put("command-spy-ignored-commands", getListHelper("command-spy-ignored-commands"));

        // Auto caps
        ints.put("auto-caps-min-length", getIntHelper("auto-caps-min-length"));
        enabled.put("auto-caps", getEnabledHelper("auto-caps-enabled"));

        // Announce
        enabled.put("announce", getEnabledHelper("announce-enabled"));
        messages.put("announce-format", getHelper("announce-format"));
        messages.put("announce-usage", getHelper("announce-usage"));
        messages.put("announce-sound", getHelper("announce-sound"));

        // Warning
        enabled.put("warning", getEnabledHelper("warning-enabled"));
        messages.put("warning-format", getHelper("warning-format"));
        messages.put("warning-usage", getHelper("warning-usage"));
        messages.put("warning-sound", getHelper("warning-sound"));

        // Staff Announce
        enabled.put("staff-announce", getEnabledHelper("staff-announce-enabled"));
        messages.put("staff-announce-format", getHelper("staff-announce-format"));
        messages.put("staff-announce-usage", getHelper("staff-announce-usage"));
        messages.put("staff-announce-sound", getHelper("staff-announce-sound"));

        // Colored chat
        enabled.put("colored-chat", getEnabledHelper("colored-chat-enabled"));

        // Disable until move
        enabled.put("disable-chat-until-move", getEnabledHelper("disable-chat-until-move"));
        messages.put("disable-chat-until-move", getHelper("disable-chat-until-move-msg"));
        messages.put("disable-command-until-move", getHelper("disable-command-until-move-msg"));
        multiLineMessages.put("disable-commands-until-move", getListHelper("disable-commands-until-move"));

        // Anti ad
        ints.put("anti-ad-setting", getIntHelper("anti-ad-setting"));
        enabled.put("anti-ad", getEnabledHelper("anti-ad-enabled"));
        messages.put("anti-ad", getHelper("anti-ad-msg"));
        messages.put("anti-ad-inform", getHelper("anti-ad-inform-msg"));
        multiLineMessages.put("anti-ad-whitelist", getListHelper("anti-ad-whitelist"));

        // Join quit
        enabled.put("custom-join-quit-messages", getEnabledHelper("custom-join-quit-messages-enabled"));
        messages.put("join", getHelper("join-msg"));
        messages.put("quit", getHelper("quit-msg"));
        messages.put("vip-join", getHelper("vip-join-msg"));
        messages.put("vip-quit", getHelper("vip-quit-msg"));

        // Chat format
        enabled.put("custom-chat-format", getEnabledHelper("custom-chat-format-enabled"));
        enabled.put("name-hover", getEnabledHelper("name-hover-enabled"));
        messages.put("default-format", getHelper("default-format"));
        multiLineMessages.put("hover-format", getListHelper("hover-format"));
        messages.put("click-action-mode", getHelper("click-action-mode"));
        messages.put("click-action", getHelper("click-action"));

        // Mention
        enabled.put("mention", getEnabledHelper("mention-enabled"));
        messages.put("mention-color", getHelper("mention-color"));
        messages.put("mention-sound", getHelper("mention-sound"));

        // MOTD
        enabled.put("motd", getEnabledHelper("motd-enabled"));
        multiLineMessages.put("motd", getListHelper("motd"));

        // Autobroadcast
        ints.put("auto-broadcast-delay", getIntHelper("auto-broadcast-delay"));
        enabled.put("auto-broadcast", getEnabledHelper("auto-broadcast-enabled"));
        enabled.put("auto-broadcast-random", getEnabledHelper("auto-broadcast-random"));
        multiLineMessages.put("auto-broadcast-messages", getListHelper("auto-broadcast-messages"));

        // Log
        enabled.put("chat-logger", getEnabledHelper("chat-logger-enabled"));
        enabled.put("chat-logger-staff-chat", getEnabledHelper("chat-logger-staff-chat-enabled"));
        enabled.put("command-logger", getEnabledHelper("command-logger-enabled"));
        multiLineMessages.put("chat-logger-included-commands", getListHelper("chat-logger-included-commands"));
        multiLineMessages.put("command-logger-ignored-commands", getListHelper("command-logger-ignored-commands"));
        messages.put("logger-format", getHelper("logger-format"));

        // Anti ASCII
        enabled.put("disable-ascii", getEnabledHelper("disable-ascii"));
        enabled.put("ascii-cancel", getEnabledHelper("ascii-cancel"));
        messages.put("ascii-cancel", getHelper("ascii-cancel-msg"));
        messages.put("ascii-replace-character", getHelper("ascii-replace-character"));
    }

    /**
     * Reloads config.yml
     */
    public static void reload() {
        plugin.reloadConfig();
        initMaps();
        DeathMessagesConfigManager.initMap();
        ChatCore.broadcastDelay = ConfigManager.getInt("auto-broadcast-delay");
        if (getEnabled("auto-broadcast")) {
            getServer().getScheduler().cancelTask(ChatCore.taskId);
            ChatCore.startAutoBroadcaster(ConfigManager.getList("auto-broadcast-messages"));
        }
    }

    /**
     * Colorizes messages
     * @param s The original message we want to colorize
     * @return A colorized String
     */
    public static String colorize(String s) {
        if (VersionManager.isV16()) {
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                String color = s.substring(matcher.start(), matcher.end());
                s = s.substring(0, matcher.start()) + ChatColor.of(color) + s.substring(matcher.end());
                matcher = pattern.matcher(s);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Colorizes messages and replaces placeholders
     * @param s The original message we want to update
     * @return A colorizes String with placeholders replaced
     */
    public static String placeholderize(String s, Player p) {
        return PlaceholderAPI.setPlaceholders(p, colorize(s));
    }

    /**
     * Gets any String from config.yml
     * @param s The key to get from the map
     * @return A String from the map, which is from the config
     */
    public static String get(String s) {
        return messages.get(s);
    }

    /**
     * Gets any list of Strings from config.yml
     * @param s The key to get from the map
     * @return A list of Strings from the map, which is from the config
     */
    public static List<String> getList(String s) {
        return multiLineMessages.get(s);
    }

    /**
     * Gets whether a section is enabled from config.yml
     * @param s The key to get from the map
     * @return True if the section is enabled, false if not
     */
    public static boolean getEnabled(String s) {
        return enabled.get(s);
    }

    /**
     * Gets any int from config.yml
     * @param s The key to get from the map
     * @return An int from the map, which is from the config
     */
    public static int getInt(String s) {
        return ints.get(s);
    }

    /**
     * Helper method for initMaps()
     * @param s The name of the entry in config.yml we want to get from
     * @return The String, the value of that entry in config.yml
     */
    private static String getHelper(String s) {
        return colorize(Objects.requireNonNull(plugin.getConfig().getString(s)));
    }

    /**
     * Helper method for initMaps()
     * @param s The name of the entry in config.yml we want to get from
     * @return A list of Strings, the value of that entry in config.yml
     */
    private static List<String> getListHelper(String s) {
        return plugin.getConfig().getStringList(s);
    }

    /**
     * Helper method for initMaps()
     * @param s The name of the entry in config.yml we want to get from
     * @return True if that section is enabled, false if not
     */
    private static boolean getEnabledHelper(String s) {
        return plugin.getConfig().getBoolean(s);
    }

    /**
     * Helper method for initConfig()
     * @param s The name of the entry in config.yml we want to get from
     * @return The int, the value of that entry in config.yml
     */
    private static int getIntHelper(String s) {
        return plugin.getConfig().getInt(s);
    }
}
