package me.cranked.crankedcore;

import java.io.File;
import java.util.*;
import me.cranked.crankedcore.commands.*;
import me.cranked.crankedcore.commands.CommandSpy;
import me.cranked.crankedcore.commands.StaffChat;
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

    public void onEnable() {
        // Setup config
        ConfigManager.plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        setupConfigManager();

        // Log PlaceHolderAPI integration
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            System.out.println("[CrankedCore] PlaceholderAPI successfully integrated!");

        // Register commands
        Objects.requireNonNull(getCommand("chat")).setExecutor(new CommandsManager(this));

        // Regitser Events
        if (getConfig().getBoolean("slow-enabled"))
            getServer().getPluginManager().registerEvents(new SlowChat(this), this);
        if (getConfig().getInt("delay-in-millis") != 0)
            getServer().getPluginManager().registerEvents(new DelayChat(this), this);
        if (getConfig().getInt("command-delay-in-millis") != 0)
            getServer().getPluginManager().registerEvents(new DelayCommand(this), this);
        if (getConfig().getBoolean("lock-enabled"))
            getServer().getPluginManager().registerEvents(new LockChat(this), this);
        if (getConfig().getBoolean("anti-caps-enabled"))
            getServer().getPluginManager().registerEvents(new AntiCaps(this), this);
        if (getConfig().getBoolean("add-period-enabled"))
            getServer().getPluginManager().registerEvents(new AddPeriod(this), this);
        if (ConfigManager.getList("blocked-words").size() != 0)
            getServer().getPluginManager().registerEvents(new BlockedWords(this), this);
        getServer().getPluginManager().registerEvents(new me.cranked.crankedcore.events.CommandSpy(this), this);
        if (getConfig().getBoolean("staff-chat-enabled"))
            getServer().getPluginManager().registerEvents(new me.cranked.crankedcore.events.StaffChat(this), this);
        if (ConfigManager.getList("blocked-commands").size() != 0)
            getServer().getPluginManager().registerEvents(new BlockedCommands(this), this);
        if (getConfig().getBoolean("auto-caps-enabled"))
            getServer().getPluginManager().registerEvents(new AutoCaps(this), this);
        if (getConfig().getBoolean("colored-chat-enabled"))
            getServer().getPluginManager().registerEvents(new ColoredChat(this), this);
        if (getConfig().getBoolean("disable-chat-until-move"))
            getServer().getPluginManager().registerEvents(new DisableChatUntilMove(this), this);
        if (getConfig().getBoolean("anti-ad-enabled"))
            getServer().getPluginManager().registerEvents(new AntiAd(this), this);
        if (getConfig().getBoolean("custom-join-quit-messages-enabled"))
            getServer().getPluginManager().registerEvents(new JoinQuit(this), this);
        if (ConfigManager.get().getBoolean("enable-custom-death-messages"))
            getServer().getPluginManager().registerEvents(new Death(), this);
        if (getConfig().getBoolean("custom-chat-format-enabled"))
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                vaultChat = (Chat)getServer().getServicesManager().load(Chat.class);
                System.out.println("[CrankedCore] Vault successfully paired with custom chat formatting!");
                getServer().getPluginManager().registerEvents(new ChatFormat(this), this);
            } else {
                System.out.println("[CrankedCore] ERROR! Vault is not installed. Custom chat formatting will not be enabled.");
            }
        if (getConfig().getBoolean("mention-enabled"))
            getServer().getPluginManager().registerEvents(new Mention(this), this);
        if (getConfig().getBoolean("motd-enabled"))
            getServer().getPluginManager().registerEvents(new MOTD(this), this);
        if (getConfig().getBoolean("chat-logger-enabled") || getConfig().getBoolean("command-logger-enabled")) {
            if (getConfig().getBoolean("chat-logger-enabled"))
                (new File(getDataFolder().getAbsolutePath() + File.separator + "Chat Logs")).mkdir();
            if (getConfig().getBoolean("command-logger-enabled"))
                (new File(getDataFolder().getAbsolutePath() + File.separator + "Command Logs")).mkdir();
            getServer().getPluginManager().registerEvents(new Log(this), this);
        }
        if (getConfig().getBoolean("disable-ascii"))
            getServer().getPluginManager().registerEvents(new AntiAscii(this), this);
        List<String> messages = ConfigManager.getList("auto-broadcast-messages");
        if (getConfig().getBoolean("auto-broadcast-enabled") && messages.size() != 0)
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                // TODO auto broadcasting
            }, 0L, (getConfig().getInt("auto-broadcast-delay") * 20L));
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return this.delay;
    }

    public void toggleChatLocked() {
        this.isChatLocked = !this.isChatLocked;
    }

    public boolean getChatLocked() {
        return this.isChatLocked;
    }

    public void setupConfigManager() {
        ConfigManager.setup();
        ConfigManager.get().addDefault("disable-all-death-messages", Boolean.FALSE);
        ConfigManager.get().addDefault("enable-custom-death-messages", Boolean.TRUE);
        ConfigManager.get().options().header("              _____             _   _       __  __\n              |  __ \\           | | | |     |  \\/  |\n              | |  | | ___  __ _| |_| |__   | \\  / | ___  ___ ___  __ _  __ _  ___  ___\n              | |  | |/ _ \\/ _` | __| '_ \\  | |\\/| |/ _ \\/ __/ __|/ _` |/ _` |/ _ \\/ __|\n              | |__| |  __/ (_| | |_| | | | | |  | |  __/\\__ \\__ \\ (_| | (_| |  __/\\__ \\\n              |_____/ \\___|\\__,_|\\__|_| |_| |_|  |_|\\___||___/___/\\__,_|\\__, |\\___||___/\n                                                                         __/ |\n                                                                        |___/\n\nIf you want to disable all death messages, set disable-all-death-messages\nto true AND set enable-custom-death-messages to true. Both!\n\nYou can add multiple lines to have a random death message chosen.\nAnd, you can disable certain death messages by setting the death message to ''");
        ConfigManager.get().addDefault("unknown", Arrays.asList("&c%player%&7 died.", "&c%player%&7 mysteriously died."));
        ConfigManager.get().addDefault("pvp-thorns", Collections.singletonList("&c%player%&7 was killed trying to hurt %killer%&7."));
        ConfigManager.get().addDefault("pvp-explosion", Collections.singletonList("&c%player%&7 was blown up while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-magic", Collections.singletonList("&c%player%&7 was killed by %killer%&7 using magic."));
        ConfigManager.get().addDefault("pvp-drown", Collections.singletonList("&c%player%&7 drowned while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-burn", Collections.singletonList("&c%player%&7 burned to death while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-magma", Collections.singletonList("&c%player%&7 was burned by a magma block while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-lava", Collections.singletonList("&c%player%&7 burned in lava while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-starvation", Collections.singletonList("&c%player%&7 starved to death while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-suffocation", Collections.singletonList("&c%player%&7 suffocated to death while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-cramming", Collections.singletonList("&c%player%&7 was crammed to death while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-void", Collections.singletonList("&c%player%&7 fell out of the world while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-fall", Collections.singletonList("&c%player%&7 broke their legs while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-fly-into-wall", Collections.singletonList("&c%player%&7 flew into a wall while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-cactus", Collections.singletonList("&c%player%&7 was pricked to death while fighting %killer%&7."));
        ConfigManager.get().addDefault("pvp-custom-sword", Collections.singletonList("&c%player%&7 was stabbed by %killer%&7 using %item%&7."));
        ConfigManager.get().addDefault("pvp-custom-axe", Collections.singletonList("&c%player%&7 was hacked by %killer%&7 using %item%&7."));
        ConfigManager.get().addDefault("pvp-custom-bow", Collections.singletonList("&c%player%&7 was shot by %killer%&7 using %item%&7."));
        ConfigManager.get().addDefault("pvp-custom-crossbow", Collections.singletonList("&c%player%&7 was shot by %killer%&7 using %item%."));
        ConfigManager.get().addDefault("pvp-custom-trident", Collections.singletonList("&c%player%&7 was impaled by %killer%&7 using %item%&7."));
        ConfigManager.get().addDefault("pvp-custom-other", Collections.singletonList("&c%player%&7 was killed by %killer%&7 using %item%&7."));
        ConfigManager.get().addDefault("pvp-sword", Collections.singletonList("&c%player%&7 was stabbed by %killer%&7."));
        ConfigManager.get().addDefault("pvp-axe", Collections.singletonList("&c%player%&7 was hacked by %killer%&7."));
        ConfigManager.get().addDefault("pvp-bow", Collections.singletonList("&c%player%&7 was shot by %killer%&7."));
        ConfigManager.get().addDefault("pvp-crossbow", Collections.singletonList("&c%player%&7 was shot by %killer%&7."));
        ConfigManager.get().addDefault("pvp-trident", Collections.singletonList("&c%player%&7 was impaled by %killer%&7's trident."));
        ConfigManager.get().addDefault("pvp-other", Collections.singletonList("&c%player%&7 was killed by %killer%&7."));
        ConfigManager.get().addDefault("pvp-fists", Collections.singletonList("&c%player%&7 was punched to death by %killer%&7."));
        ConfigManager.get().addDefault("pvp-unknown", Collections.singletonList("&c%player%&7 was killed by %killer%&7."));
        ConfigManager.get().addDefault("bee", Collections.singletonList("&c%player%&7 was stung by a lot of bees."));
        ConfigManager.get().addDefault("blaze-melee", Collections.singletonList("&c%player%&7 was punched by a blaze."));
        ConfigManager.get().addDefault("blaze-fireball", Collections.singletonList("&c%player%&7 was fireballed by a blaze."));
        ConfigManager.get().addDefault("cavespider", Collections.singletonList("&c%player%&7 was bitten by a cave spider."));
        ConfigManager.get().addDefault("creeper", Collections.singletonList("&c%player%&7 was blown up by a creeper."));
        ConfigManager.get().addDefault("creeper-charged", Collections.singletonList("&c%player%&7 was blown up by a charged creeper."));
        ConfigManager.get().addDefault("drowned-trident", Collections.singletonList("&c%player%&7 was impaled by a drowned's trident."));
        ConfigManager.get().addDefault("drowned-melee", Collections.singletonList("&c%player%&7 was punched by a drowned really hard."));
        ConfigManager.get().addDefault("elderguardian", Collections.singletonList("&7An elder guardian got to &c%player%&7."));
        ConfigManager.get().addDefault("enderdragon-breath", Collections.singletonList("&c%player%&7 was killed by the ender dragon's breath."));
        ConfigManager.get().addDefault("enderdragon", Collections.singletonList("&c%player%&7 was killed by the ender dragon."));
        ConfigManager.get().addDefault("enderman", Collections.singletonList("&c%player%&7 was killed by an enderman. Ouch."));
        ConfigManager.get().addDefault("endermite", Collections.singletonList("&c%player%&7 was killed by an endermite."));
        ConfigManager.get().addDefault("evoker", Collections.singletonList("&c%player%&7 was killed by an evoker."));
        ConfigManager.get().addDefault("ghast", Collections.singletonList("&c%player%&7 was blown up by a ghast."));
        ConfigManager.get().addDefault("giant", Collections.singletonList("&c%player%&7 was flicked by a giant."));
        ConfigManager.get().addDefault("guardian", Collections.singletonList("&c%player%&7 was killed by a guardian."));
        ConfigManager.get().addDefault("illusioner", Collections.singletonList("&c%player%&7 was illusioned by an illusioner."));
        ConfigManager.get().addDefault("irongolem", Collections.singletonList("&c%player%&7 was thrown by an iron golem."));
        ConfigManager.get().addDefault("llama", Collections.singletonList("&c%player%&7 was killed by a llama."));
        ConfigManager.get().addDefault("magmacube", Collections.singletonList("&c%player%&7 was stomped on by a magma cube."));
        ConfigManager.get().addDefault("panda", Collections.singletonList("&c%player%&7 was rekt by a panda."));
        ConfigManager.get().addDefault("phantom", Collections.singletonList("&c%player%&7 was killed by a phantom."));
        ConfigManager.get().addDefault("pillager-crossbow", Collections.singletonList("&c%player%&7 was shot by a pillager."));
        ConfigManager.get().addDefault("pillager-melee", Collections.singletonList("&c%player%&7 was hit by a pillager."));
        ConfigManager.get().addDefault("polarbear", Collections.singletonList("&c%player%&7 was hugged by a polar bear."));
        ConfigManager.get().addDefault("pufferfish", Collections.singletonList("&c%player%&7 was stung by a puffer fish."));
        ConfigManager.get().addDefault("ravager", Collections.singletonList("&c%player%&7 was ravaged by a ravager."));
        ConfigManager.get().addDefault("shulker", Collections.singletonList("&c%player%&7 was killed by a shulker."));
        ConfigManager.get().addDefault("silverfish", Collections.singletonList("&c%player%&7 was killed by a silverfish."));
        ConfigManager.get().addDefault("stray-arrow", Collections.singletonList("&c%player%&7 was shot by a stray."));
        ConfigManager.get().addDefault("stray-melee", Collections.singletonList("&c%player%&7 was punched by a stray."));
        ConfigManager.get().addDefault("witherskeleton", Collections.singletonList("&c%player%&7 was nae-naed by a wither skeleton."));
        ConfigManager.get().addDefault("skeleton-arrow", Collections.singletonList("&c%player%&7 was shot by a skeleton."));
        ConfigManager.get().addDefault("skeleton-melee", Collections.singletonList("&c%player%&7 was punched by a skeleton."));
        ConfigManager.get().addDefault("slime", Collections.singletonList("&c%player%&7 was stomped on by a slime."));
        ConfigManager.get().addDefault("spider", Collections.singletonList("&c%player%&7 was bit by a spider."));
        ConfigManager.get().addDefault("vex", Collections.singletonList("&c%player%&7 was killed by a vex."));
        ConfigManager.get().addDefault("vindicator", Collections.singletonList("&c%player%&7 was killed by a vindicator."));
        ConfigManager.get().addDefault("witch", Collections.singletonList("&c%player%&7 was killed by a witch."));
        ConfigManager.get().addDefault("wither", Collections.singletonList("&c%player%&7 was killed by a wither."));
        ConfigManager.get().addDefault("wither-explosion", Collections.singletonList("&c%player%&7 was exploded by a wither."));
        ConfigManager.get().addDefault("wolf", Collections.singletonList("&c%player%&7 was ripped apart by wolves."));
        ConfigManager.get().addDefault("zombiepigman", Collections.singletonList("&c%player%&7 was killed by a zombie pigman."));
        ConfigManager.get().addDefault("zombievillager", Collections.singletonList("&c%player%&7 was eaten by a zombie villager."));
        ConfigManager.get().addDefault("husk", Collections.singletonList("&c%player%&7 was eaten by a husk."));
        ConfigManager.get().addDefault("zombie", Collections.singletonList("&c%player%&7 was eaten by a zombie."));
        ConfigManager.get().addDefault("named-bee", Collections.singletonList("&c%player%&7 was stung by %mob%&7."));
        ConfigManager.get().addDefault("named-blaze-melee", Collections.singletonList("&c%player%&7 was punched by %mob%&7."));
        ConfigManager.get().addDefault("named-blaze-fireball", Collections.singletonList("&c%player%&7 was fireballed by %mob%&7."));
        ConfigManager.get().addDefault("named-cavespider", Collections.singletonList("&c%player%&7 was bitten by %mob%&7."));
        ConfigManager.get().addDefault("named-creeper", Collections.singletonList("&c%player% was&7 blown up by %mob%&7."));
        ConfigManager.get().addDefault("named-creeper-charged", Collections.singletonList("&c%player%&7 was blown up by %mob%&7."));
        ConfigManager.get().addDefault("named-drowned-trident", Collections.singletonList("&c%player%&7 was impaled by %mob%&7's trident."));
        ConfigManager.get().addDefault("named-drowned-melee", Collections.singletonList("&c%player%&7 was punched by %mob%&7 really hard."));
        ConfigManager.get().addDefault("named-elderguardian", Collections.singletonList("&c%mob%&7 got to %player%&7."));
        ConfigManager.get().addDefault("named-enderdragon-breath", Collections.singletonList("&c%player%&7 was killed by %mob%&7's breath."));
        ConfigManager.get().addDefault("named-enderdragon", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-enderman", Collections.singletonList("&c%player%&7 was killed by %mob%&7. Ouch."));
        ConfigManager.get().addDefault("named-endermite", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-evoker", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-ghast", Collections.singletonList("&c%player%&7 was blown up by %mob%&7."));
        ConfigManager.get().addDefault("named-giant", Collections.singletonList("&c%player%&7 was flicked by %mob%&7."));
        ConfigManager.get().addDefault("named-guardian", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-illusioner", Collections.singletonList("&c%player%&7 was illusioned by %mob%&7."));
        ConfigManager.get().addDefault("named-irongolem", Collections.singletonList("&c%player%&7 was thrown by %mob%&7."));
        ConfigManager.get().addDefault("named-magmacube", Collections.singletonList("&c%player%&7 was stomped on by %mob%&7."));
        ConfigManager.get().addDefault("named-panda", Collections.singletonList("&c%player%&7 was rekt by %mob%&7."));
        ConfigManager.get().addDefault("named-phantom", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-pillager-crossbow", Collections.singletonList("&c%player%&7 was shot by %mob%&7."));
        ConfigManager.get().addDefault("named-pillager-melee", Collections.singletonList("&c%player%&7 was hit by %mob%&7."));
        ConfigManager.get().addDefault("named-polarbear", Collections.singletonList("&c%player%&7 was hugged by %mob%&7."));
        ConfigManager.get().addDefault("named-pufferfish", Collections.singletonList("&c%player%&7 was stung by %mob%&7."));
        ConfigManager.get().addDefault("named-ravager", Collections.singletonList("&c%player%&7 was ravaged by %mob%&7."));
        ConfigManager.get().addDefault("named-shulker", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-silverfish", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-stray-arrow", Collections.singletonList("&c%player%&7 was shot by %mob%&7."));
        ConfigManager.get().addDefault("named-stray-melee", Collections.singletonList("&c%player%&7 was punched by %mob%&7."));
        ConfigManager.get().addDefault("named-witherskeleton", Collections.singletonList("&c%player%&7 was nae-naed by %mob%&7."));
        ConfigManager.get().addDefault("named-skeleton-arrow", Collections.singletonList("&c%player%&7 was shot by %mob%&7."));
        ConfigManager.get().addDefault("named-skeleton-melee", Collections.singletonList("&c%player%&7 was punched by %mob%&7."));
        ConfigManager.get().addDefault("named-slime", Collections.singletonList("&c%player%&7 was stomped on by %mob%&7."));
        ConfigManager.get().addDefault("named-spider", Collections.singletonList("&c%player%&7 was bit by %mob%&7."));
        ConfigManager.get().addDefault("named-vex", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-vindicator", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-witch", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-wither", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-wither-explosion", Collections.singletonList("&c%player%&7 was exploded by %mob%&7."));
        ConfigManager.get().addDefault("named-wolf", Collections.singletonList("&c%player%&7 was ripped apart by %mob%&7."));
        ConfigManager.get().addDefault("named-pigzombie", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        ConfigManager.get().addDefault("named-zombievillager", Collections.singletonList("&c%player%&7 was eaten by %mob%&7."));
        ConfigManager.get().addDefault("named-husk", Collections.singletonList("&c%player%&7 was eaten by %mob%&7."));
        ConfigManager.get().addDefault("named-zombie", Collections.singletonList("&c%player%&7 was eaten by %mob%&7."));
        ConfigManager.get().addDefault("endercrystal", Collections.singletonList("&c%player%&7 was exploded by an ender crystal."));
        ConfigManager.get().addDefault("anvil", Collections.singletonList("&c%player%&7 was crushed by an anvil."));
        ConfigManager.get().addDefault("lightning", Collections.singletonList("&c%player%&7 was struck by lightning."));
        ConfigManager.get().addDefault("enderpearl", Collections.singletonList("&c%player%&7 used an ender pearl at the wrong time."));
        ConfigManager.get().addDefault("tnt", Collections.singletonList("&c%player%&7 was exploded by TNT."));
        ConfigManager.get().addDefault("dispenser-magic", Collections.singletonList("&c%player%&7 was killed by magic from a dispenser."));
        ConfigManager.get().addDefault("dispenser", Collections.singletonList("&c%player%&7 was killed by a dispenser."));
        ConfigManager.get().addDefault("bed-explosion", Collections.singletonList("&c%player%&7 slept in the wrong world."));
        ConfigManager.get().addDefault("cactus", Collections.singletonList("&c%player%&7 was pricked by a cactus."));
        ConfigManager.get().addDefault("anvil", Collections.singletonList("&c%player%&7 was crushed by an anvil."));
        ConfigManager.get().addDefault("drown", Collections.singletonList("&c%player%&7 drowned."));
        ConfigManager.get().addDefault("wither-potion", Collections.singletonList("&c%player%&7 withered away."));
        ConfigManager.get().addDefault("harming", Collections.singletonList("&c%player%&7 killed themself with a harming potion."));
        ConfigManager.get().addDefault("burn", Collections.singletonList("&c%player%&7 burned to death."));
        ConfigManager.get().addDefault("magma", Collections.singletonList("&c%player%&7 was burned by a magma block."));
        ConfigManager.get().addDefault("lava", Collections.singletonList("&c%player%&7 fell in lava."));
        ConfigManager.get().addDefault("lightning", Collections.singletonList("&c%player%&7 was struck by lightning."));
        ConfigManager.get().addDefault("starvation", Collections.singletonList("&c%player%&7 starved to death."));
        ConfigManager.get().addDefault("suffocation", Collections.singletonList("&c%player%&7 suffocated to death."));
        ConfigManager.get().addDefault("cramming", Collections.singletonList("&c%player%&7 was crammed to death."));
        ConfigManager.get().addDefault("void", Collections.singletonList("&c%player%&7 fell into the void."));
        ConfigManager.get().addDefault("fall", Collections.singletonList("&c%player%&7 fell quite far."));
        ConfigManager.get().addDefault("fly-into-wall", Collections.singletonList("&c%player%&7 flew into a wall."));
        ConfigManager.get().addDefault("suicide", Collections.singletonList("&c%player%&7 committed suicide."));
        ConfigManager.get().options().copyDefaults(true);
        ConfigManager.save();
    }

    public static String placeholderColor(String msg, Player player) {
        return PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', msg));
    }
}
