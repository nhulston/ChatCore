package me.cranked.crankedcore;

import java.io.File;
import java.util.*;
import me.cranked.crankedcore.events.*;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Start of the plugin
 * @author Nick
 * @since 1.0
 */
public final class CrankedCore extends JavaPlugin {

    // TODO Bukkit.broadcast(msg, permission); - staffAnnounce, staffChat, etc.
    // TODO sounds for more things
    // TODO bring silent messages to clear chat, etc.
    // TODO config tips section. Like how to change unknown command message
    // TODO better plugin hiding see https://www.spigotmc.org/resources/pluginhider-pluginhiderplus-hide-your-plugins-anti-tab-complete-all-message-replace.51583/
    // TODO auto respawn
    // TODO console filtering

    private static int delay = 0;
    private static boolean isChatLocked = false;
    public static Chat vaultChat = null;
    public static CrankedCore plugin; // TODO temp

    /**
     * First method ran for the entire plugin.
     * Initializes config.yml and deathmessages.yml
     * Integrates with PlaceholderAPI and VaultAPI
     * Registers commands
     * Registers events
     */
    public void onEnable() {
        // Make reference to plugin
        plugin = this;

        // Setup versions
        VersionManager.initVersions();

        // Setup config.yml
        ConfigManager.setPlugin(this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        ConfigManager.initMaps();

        // Setup deathmessages.yml
        DeathMessagesConfigManager.setupDeathmessages();
        DeathMessagesConfigManager.initMap();

        // Log PlaceHolderAPI integration
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            Bukkit.getLogger().finest("[CrankedCore] PlaceholderAPI successfully integrated!");

        // Register commands
        Objects.requireNonNull(getCommand("chat")).setExecutor(new CommandsManager());

        // Register Events
        if (ConfigManager.getEnabled("slow-chat"))
            getServer().getPluginManager().registerEvents(new SlowChat(), this);
        if (ConfigManager.getInt("delay-in-millis") != 0)
            getServer().getPluginManager().registerEvents(new DelayChat(), this);
        if (ConfigManager.getInt("command-delay-in-millis") != 0)
            getServer().getPluginManager().registerEvents(new DelayCommand(), this);
        if (ConfigManager.getEnabled("lock-chat"))
            getServer().getPluginManager().registerEvents(new LockChat(), this);
        if (ConfigManager.getEnabled("anti-caps"))
            getServer().getPluginManager().registerEvents(new AntiCaps(), this);
        if (ConfigManager.getEnabled("add-period"))
            getServer().getPluginManager().registerEvents(new AddPeriod(), this);
        if (ConfigManager.getList("blocked-words").size() != 0)
            getServer().getPluginManager().registerEvents(new BlockedWords(), this);
        getServer().getPluginManager().registerEvents(new me.cranked.crankedcore.events.CommandSpy(), this);
        if (ConfigManager.getEnabled("staff-chat"))
            getServer().getPluginManager().registerEvents(new me.cranked.crankedcore.events.StaffChat(), this);
        if (ConfigManager.getList("blocked-commands").size() != 0)
            getServer().getPluginManager().registerEvents(new BlockedCommands(), this);
        if (ConfigManager.getEnabled("auto-caps"))
            getServer().getPluginManager().registerEvents(new AutoCaps(), this);
        if (ConfigManager.getEnabled("colored-chat"))
            getServer().getPluginManager().registerEvents(new ColoredChat(), this);
        if (ConfigManager.getEnabled("disable-chat-until-move"))
            getServer().getPluginManager().registerEvents(new DisableChatUntilMove(), this);
        if (ConfigManager.getEnabled("anti-ad"))
            getServer().getPluginManager().registerEvents(new AntiAd(), this);
        if (ConfigManager.getEnabled("custom-join-quit-messages"))
            getServer().getPluginManager().registerEvents(new JoinQuit(), this);
        if (DeathMessagesConfigManager.get().getBoolean("enable-custom-death-messages"))
            getServer().getPluginManager().registerEvents(new Death(), this);
        if (ConfigManager.getEnabled("custom-chat-format"))
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                vaultChat = getServer().getServicesManager().load(Chat.class);
                Bukkit.getLogger().finest("[CrankedCore] Vault successfully paired with custom chat formatting!");
                getServer().getPluginManager().registerEvents(new ChatFormat(), this);
            } else {
                Bukkit.getLogger().severe("[CrankedCore] ERROR! Vault is not installed. Custom chat formatting will not be enabled.");
            }
        if (ConfigManager.getEnabled("mention"))
            getServer().getPluginManager().registerEvents(new Mention(), this);
        if (ConfigManager.getEnabled("motd"))
            getServer().getPluginManager().registerEvents(new MOTD(), this);
        if (ConfigManager.getEnabled("chat-logger") || ConfigManager.getEnabled("command-logger")) {
            if (ConfigManager.getEnabled("chat-logger")) {
                if (!((new File(getDataFolder().getAbsolutePath() + File.separator + "Chat Logs")).mkdir())) {
                    Bukkit.getLogger().info("[CrankedCore] Chat log directory already exists");
                }
            }
            if (ConfigManager.getEnabled("command-logger")) {
                if (!(new File(getDataFolder().getAbsolutePath() + File.separator + "Command Logs")).mkdir()) {
                    Bukkit.getLogger().info("[CrankedCore] Command log directory already exists");
                }
            }
            getServer().getPluginManager().registerEvents(new Log(), this);
        }
        if (ConfigManager.getEnabled("disable-ascii"))
            getServer().getPluginManager().registerEvents(new AntiAscii(), this);
        List<String> messages = ConfigManager.getList("auto-broadcast-messages");
        if (ConfigManager.getEnabled("auto-broadcast") && messages.size() != 0)
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                // TODO auto broadcasting
            }, 0L, (ConfigManager.getInt("auto-broadcast-delay") * 20L));
    }

    /**
     * Setter method for delay
     * @param delay what we want to set the chat delay to
     */
    public static void setDelay(int delay) {
        CrankedCore.delay = delay;
    }

    /**
     * Getter method for delay
     * @return The delay in chat
     */
    public static int getDelay() {
        return CrankedCore.delay;
    }

    /**
     * Toggles whether chat is locked
     */
    public static void toggleChatLocked() {
        CrankedCore.isChatLocked = !CrankedCore.isChatLocked;
    }

    /**
     * Getter method for isChatLocked
     * @return true if chat is locked, false if not
     */
    public static boolean getChatLocked() {
        return CrankedCore.isChatLocked;
    }

    /**
     * Used by every command to check if the sender has permission
     * If no permission, sends the player the no-permission message
     * @param s The permission we are checking
     * @param sender The user we are checking to see if they have permission
     * @return true if the sender doesn't have permission, false otherwise
     */
    public static boolean noPermission(String s, CommandSender sender) {
        if (sender instanceof Player && !sender.hasPermission(s)) {
            sender.sendMessage(ConfigManager.get("no-permission"));
            return true;
        }
        return false;
    }
}
