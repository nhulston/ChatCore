package me.cranked.crankedcore;

import java.io.File;
import java.util.*;

import me.cranked.crankedcore.events.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrankedCore extends JavaPlugin {

    private int delay = 0;
    private boolean isChatLocked = false;
    public static Chat vaultChat = null;

    /**
     * First method ran for the entire plugin.
     * Initializes config.yml and deathmessages.yml
     * Integrates with PlaceholderAPI and VaultAPI
     * Registers commands
     * Registers events
     */
    public void onEnable() {
        // Setup config.yml
        ConfigManager.setPlugin(this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        ConfigManager.initMaps();

        // Setup deathmessages.yml
        DeathMessagesConfigManager.setupDeathmessages();

        // Log PlaceHolderAPI integration
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            System.out.println("[CrankedCore] PlaceholderAPI successfully integrated!");

        // Register commands
        Objects.requireNonNull(getCommand("chat")).setExecutor(new CommandsManager(this));

        // Register Events TODO replace all getConfig()
        if (ConfigManager.getEnabled("slow-chat"))
            getServer().getPluginManager().registerEvents(new SlowChat(this), this);
        if (getConfig().getInt("delay-in-millis") != 0)
            getServer().getPluginManager().registerEvents(new DelayChat(this), this);
        if (getConfig().getInt("command-delay-in-millis") != 0)
            getServer().getPluginManager().registerEvents(new DelayCommand(this), this);
        if (ConfigManager.getEnabled("lock-chat"))
            getServer().getPluginManager().registerEvents(new LockChat(this), this);
        if (ConfigManager.getEnabled("anti-caps"))
            getServer().getPluginManager().registerEvents(new AntiCaps(this), this);
        if (ConfigManager.getEnabled("add-period"))
            getServer().getPluginManager().registerEvents(new AddPeriod(this), this);
        if (ConfigManager.getList("blocked-words").size() != 0)
            getServer().getPluginManager().registerEvents(new BlockedWords(this), this);
        getServer().getPluginManager().registerEvents(new me.cranked.crankedcore.events.CommandSpy(), this);
        if (ConfigManager.getEnabled("staff-chat"))
            getServer().getPluginManager().registerEvents(new me.cranked.crankedcore.events.StaffChat(this), this);
        if (ConfigManager.getList("blocked-commands").size() != 0)
            getServer().getPluginManager().registerEvents(new BlockedCommands(this), this);
        if (ConfigManager.getEnabled("auto-caps"))
            getServer().getPluginManager().registerEvents(new AutoCaps(this), this);
        if (ConfigManager.getEnabled("colored-chat"))
            getServer().getPluginManager().registerEvents(new ColoredChat(), this);
        if (ConfigManager.getEnabled("disable-chat-until-move"))
            getServer().getPluginManager().registerEvents(new DisableChatUntilMove(), this);
        if (ConfigManager.getEnabled("anti-ad"))
            getServer().getPluginManager().registerEvents(new AntiAd(this), this);
        if (ConfigManager.getEnabled("custom-join-quit-messages"))
            getServer().getPluginManager().registerEvents(new JoinQuit(), this);
        if (DeathMessagesConfigManager.get().getBoolean("enable-custom-death-messages"))
            getServer().getPluginManager().registerEvents(new Death(), this);
        if (ConfigManager.getEnabled("custom-chat-format"))
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                vaultChat = getServer().getServicesManager().load(Chat.class);
                System.out.println("[CrankedCore] Vault successfully paired with custom chat formatting!");
                getServer().getPluginManager().registerEvents(new ChatFormat(this), this);
            } else {
                System.out.println("[CrankedCore] ERROR! Vault is not installed. Custom chat formatting will not be enabled.");
            }
        if (ConfigManager.getEnabled("mention"))
            getServer().getPluginManager().registerEvents(new Mention(this), this);
        if (ConfigManager.getEnabled("motd"))
            getServer().getPluginManager().registerEvents(new MOTD(), this);
        if (ConfigManager.getEnabled("chat-logger") || ConfigManager.getEnabled("command-logger")) {
            if (ConfigManager.getEnabled("chat-logger"))
                (new File(getDataFolder().getAbsolutePath() + File.separator + "Chat Logs")).mkdir();
            if (ConfigManager.getEnabled("command-logger"))
                (new File(getDataFolder().getAbsolutePath() + File.separator + "Command Logs")).mkdir();
            getServer().getPluginManager().registerEvents(new Log(this), this);
        }
        if (ConfigManager.getEnabled("disable-ascii"))
            getServer().getPluginManager().registerEvents(new AntiAscii(), this);
        List<String> messages = ConfigManager.getList("auto-broadcast-messages");
        if (ConfigManager.getEnabled("auto-broadcast") && messages.size() != 0)
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                // TODO auto broadcasting
            }, 0L, (getConfig().getInt("auto-broadcast-delay") * 20L));
    }

    /**
     * Setter method for delay
     * @param delay what we want to set the chat delay to
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Getter method for delay
     * @return The delay in chat
     */
    public int getDelay() {
        return this.delay;
    }

    /**
     * Toggles whether chat is locked
     */
    public void toggleChatLocked() {
        this.isChatLocked = !this.isChatLocked;
    }

    /**
     * Getter method for isChatLocked
     * @return true if chat is locked, false if not
     */
    public boolean getChatLocked() {
        return this.isChatLocked;
    }

    public static String placeholderColor(String msg, Player player) {
        return PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', msg));
    }
}
