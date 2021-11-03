package me.cranked.crankedcore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * Manages deathmessages.yml
 * @author Nick
 * @since 1.0
 */
public class DeathMessagesConfigManager {
    private static File file;
    private static FileConfiguration customFile;

    /**
     * Sets up deathmessages.yml
     */
    public static void setupDeathmessages() {
        try {
            setupFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        customFile.addDefault("disable-all-death-messages", Boolean.FALSE);
        customFile.addDefault("enable-custom-death-messages", Boolean.TRUE);
        customFile.options().header("              _____             _   _       __  __\n              |  __ \\           | | | |     |  \\/  |\n              | |  | | ___  __ _| |_| |__   | \\  / | ___  ___ ___  __ _  __ _  ___  ___\n              | |  | |/ _ \\/ _` | __| '_ \\  | |\\/| |/ _ \\/ __/ __|/ _` |/ _` |/ _ \\/ __|\n              | |__| |  __/ (_| | |_| | | | | |  | |  __/\\__ \\__ \\ (_| | (_| |  __/\\__ \\\n              |_____/ \\___|\\__,_|\\__|_| |_| |_|  |_|\\___||___/___/\\__,_|\\__, |\\___||___/\n                                                                         __/ |\n                                                                        |___/\n\nIf you want to disable all death messages, set disable-all-death-messages\nto true AND set enable-custom-death-messages to true. Both!\n\nYou can add multiple lines to have a random death message chosen.\nAnd, you can disable certain death messages by setting the death message to ''");
        customFile.addDefault("unknown", Arrays.asList("&c%player%&7 died.", "&c%player%&7 mysteriously died."));
        customFile.addDefault("pvp-thorns", Collections.singletonList("&c%player%&7 was killed trying to hurt %killer%&7."));
        customFile.addDefault("pvp-explosion", Collections.singletonList("&c%player%&7 was blown up while fighting %killer%&7."));
        customFile.addDefault("pvp-magic", Collections.singletonList("&c%player%&7 was killed by %killer%&7 using magic."));
        customFile.addDefault("pvp-drown", Collections.singletonList("&c%player%&7 drowned while fighting %killer%&7."));
        customFile.addDefault("pvp-burn", Collections.singletonList("&c%player%&7 burned to death while fighting %killer%&7."));
        customFile.addDefault("pvp-magma", Collections.singletonList("&c%player%&7 was burned by a magma block while fighting %killer%&7."));
        customFile.addDefault("pvp-lava", Collections.singletonList("&c%player%&7 burned in lava while fighting %killer%&7."));
        customFile.addDefault("pvp-starvation", Collections.singletonList("&c%player%&7 starved to death while fighting %killer%&7."));
        customFile.addDefault("pvp-suffocation", Collections.singletonList("&c%player%&7 suffocated to death while fighting %killer%&7."));
        customFile.addDefault("pvp-cramming", Collections.singletonList("&c%player%&7 was crammed to death while fighting %killer%&7."));
        customFile.addDefault("pvp-void", Collections.singletonList("&c%player%&7 fell out of the world while fighting %killer%&7."));
        customFile.addDefault("pvp-fall", Collections.singletonList("&c%player%&7 broke their legs while fighting %killer%&7."));
        customFile.addDefault("pvp-fly-into-wall", Collections.singletonList("&c%player%&7 flew into a wall while fighting %killer%&7."));
        customFile.addDefault("pvp-cactus", Collections.singletonList("&c%player%&7 was pricked to death while fighting %killer%&7."));
        customFile.addDefault("pvp-custom-sword", Collections.singletonList("&c%player%&7 was stabbed by %killer%&7 using %item%&7."));
        customFile.addDefault("pvp-custom-axe", Collections.singletonList("&c%player%&7 was hacked by %killer%&7 using %item%&7."));
        customFile.addDefault("pvp-custom-bow", Collections.singletonList("&c%player%&7 was shot by %killer%&7 using %item%&7."));
        customFile.addDefault("pvp-custom-crossbow", Collections.singletonList("&c%player%&7 was shot by %killer%&7 using %item%."));
        customFile.addDefault("pvp-custom-trident", Collections.singletonList("&c%player%&7 was impaled by %killer%&7 using %item%&7."));
        customFile.addDefault("pvp-custom-other", Collections.singletonList("&c%player%&7 was killed by %killer%&7 using %item%&7."));
        customFile.addDefault("pvp-sword", Collections.singletonList("&c%player%&7 was stabbed by %killer%&7."));
        customFile.addDefault("pvp-axe", Collections.singletonList("&c%player%&7 was hacked by %killer%&7."));
        customFile.addDefault("pvp-bow", Collections.singletonList("&c%player%&7 was shot by %killer%&7."));
        customFile.addDefault("pvp-crossbow", Collections.singletonList("&c%player%&7 was shot by %killer%&7."));
        customFile.addDefault("pvp-trident", Collections.singletonList("&c%player%&7 was impaled by %killer%&7's trident."));
        customFile.addDefault("pvp-other", Collections.singletonList("&c%player%&7 was killed by %killer%&7."));
        customFile.addDefault("pvp-fists", Collections.singletonList("&c%player%&7 was punched to death by %killer%&7."));
        customFile.addDefault("pvp-unknown", Collections.singletonList("&c%player%&7 was killed by %killer%&7."));
        customFile.addDefault("bee", Collections.singletonList("&c%player%&7 was stung by a lot of bees."));
        customFile.addDefault("blaze-melee", Collections.singletonList("&c%player%&7 was punched by a blaze."));
        customFile.addDefault("blaze-fireball", Collections.singletonList("&c%player%&7 was fireballed by a blaze."));
        customFile.addDefault("cavespider", Collections.singletonList("&c%player%&7 was bitten by a cave spider."));
        customFile.addDefault("creeper", Collections.singletonList("&c%player%&7 was blown up by a creeper."));
        customFile.addDefault("creeper-charged", Collections.singletonList("&c%player%&7 was blown up by a charged creeper."));
        customFile.addDefault("drowned-trident", Collections.singletonList("&c%player%&7 was impaled by a drowned's trident."));
        customFile.addDefault("drowned-melee", Collections.singletonList("&c%player%&7 was punched by a drowned really hard."));
        customFile.addDefault("elderguardian", Collections.singletonList("&7An elder guardian got to &c%player%&7."));
        customFile.addDefault("enderdragon-breath", Collections.singletonList("&c%player%&7 was killed by the ender dragon's breath."));
        customFile.addDefault("enderdragon", Collections.singletonList("&c%player%&7 was killed by the ender dragon."));
        customFile.addDefault("enderman", Collections.singletonList("&c%player%&7 was killed by an enderman. Ouch."));
        customFile.addDefault("endermite", Collections.singletonList("&c%player%&7 was killed by an endermite."));
        customFile.addDefault("evoker", Collections.singletonList("&c%player%&7 was killed by an evoker."));
        customFile.addDefault("ghast", Collections.singletonList("&c%player%&7 was blown up by a ghast."));
        customFile.addDefault("giant", Collections.singletonList("&c%player%&7 was flicked by a giant."));
        customFile.addDefault("guardian", Collections.singletonList("&c%player%&7 was killed by a guardian."));
        customFile.addDefault("illusioner", Collections.singletonList("&c%player%&7 was illusioned by an illusioner."));
        customFile.addDefault("irongolem", Collections.singletonList("&c%player%&7 was thrown by an iron golem."));
        customFile.addDefault("llama", Collections.singletonList("&c%player%&7 was killed by a llama."));
        customFile.addDefault("magmacube", Collections.singletonList("&c%player%&7 was stomped on by a magma cube."));
        customFile.addDefault("panda", Collections.singletonList("&c%player%&7 was rekt by a panda."));
        customFile.addDefault("phantom", Collections.singletonList("&c%player%&7 was killed by a phantom."));
        customFile.addDefault("pillager-crossbow", Collections.singletonList("&c%player%&7 was shot by a pillager."));
        customFile.addDefault("pillager-melee", Collections.singletonList("&c%player%&7 was hit by a pillager."));
        customFile.addDefault("polarbear", Collections.singletonList("&c%player%&7 was hugged by a polar bear."));
        customFile.addDefault("pufferfish", Collections.singletonList("&c%player%&7 was stung by a puffer fish."));
        customFile.addDefault("ravager", Collections.singletonList("&c%player%&7 was ravaged by a ravager."));
        customFile.addDefault("shulker", Collections.singletonList("&c%player%&7 was killed by a shulker."));
        customFile.addDefault("silverfish", Collections.singletonList("&c%player%&7 was killed by a silverfish."));
        customFile.addDefault("stray-arrow", Collections.singletonList("&c%player%&7 was shot by a stray."));
        customFile.addDefault("stray-melee", Collections.singletonList("&c%player%&7 was punched by a stray."));
        customFile.addDefault("witherskeleton", Collections.singletonList("&c%player%&7 was nae-naed by a wither skeleton."));
        customFile.addDefault("skeleton-arrow", Collections.singletonList("&c%player%&7 was shot by a skeleton."));
        customFile.addDefault("skeleton-melee", Collections.singletonList("&c%player%&7 was punched by a skeleton."));
        customFile.addDefault("slime", Collections.singletonList("&c%player%&7 was stomped on by a slime."));
        customFile.addDefault("spider", Collections.singletonList("&c%player%&7 was bit by a spider."));
        customFile.addDefault("vex", Collections.singletonList("&c%player%&7 was killed by a vex."));
        customFile.addDefault("vindicator", Collections.singletonList("&c%player%&7 was killed by a vindicator."));
        customFile.addDefault("witch", Collections.singletonList("&c%player%&7 was killed by a witch."));
        customFile.addDefault("wither", Collections.singletonList("&c%player%&7 was killed by a wither."));
        customFile.addDefault("wither-explosion", Collections.singletonList("&c%player%&7 was exploded by a wither."));
        customFile.addDefault("wolf", Collections.singletonList("&c%player%&7 was ripped apart by wolves."));
        customFile.addDefault("zombiepigman", Collections.singletonList("&c%player%&7 was killed by a zombie pigman."));
        customFile.addDefault("zombievillager", Collections.singletonList("&c%player%&7 was eaten by a zombie villager."));
        customFile.addDefault("husk", Collections.singletonList("&c%player%&7 was eaten by a husk."));
        customFile.addDefault("zombie", Collections.singletonList("&c%player%&7 was eaten by a zombie."));
        customFile.addDefault("named-bee", Collections.singletonList("&c%player%&7 was stung by %mob%&7."));
        customFile.addDefault("named-blaze-melee", Collections.singletonList("&c%player%&7 was punched by %mob%&7."));
        customFile.addDefault("named-blaze-fireball", Collections.singletonList("&c%player%&7 was fireballed by %mob%&7."));
        customFile.addDefault("named-cavespider", Collections.singletonList("&c%player%&7 was bitten by %mob%&7."));
        customFile.addDefault("named-creeper", Collections.singletonList("&c%player% was&7 blown up by %mob%&7."));
        customFile.addDefault("named-creeper-charged", Collections.singletonList("&c%player%&7 was blown up by %mob%&7."));
        customFile.addDefault("named-drowned-trident", Collections.singletonList("&c%player%&7 was impaled by %mob%&7's trident."));
        customFile.addDefault("named-drowned-melee", Collections.singletonList("&c%player%&7 was punched by %mob%&7 really hard."));
        customFile.addDefault("named-elderguardian", Collections.singletonList("&c%mob%&7 got to %player%&7."));
        customFile.addDefault("named-enderdragon-breath", Collections.singletonList("&c%player%&7 was killed by %mob%&7's breath."));
        customFile.addDefault("named-enderdragon", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-enderman", Collections.singletonList("&c%player%&7 was killed by %mob%&7. Ouch."));
        customFile.addDefault("named-endermite", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-evoker", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-ghast", Collections.singletonList("&c%player%&7 was blown up by %mob%&7."));
        customFile.addDefault("named-giant", Collections.singletonList("&c%player%&7 was flicked by %mob%&7."));
        customFile.addDefault("named-guardian", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-illusioner", Collections.singletonList("&c%player%&7 was illusioned by %mob%&7."));
        customFile.addDefault("named-irongolem", Collections.singletonList("&c%player%&7 was thrown by %mob%&7."));
        customFile.addDefault("named-magmacube", Collections.singletonList("&c%player%&7 was stomped on by %mob%&7."));
        customFile.addDefault("named-panda", Collections.singletonList("&c%player%&7 was rekt by %mob%&7."));
        customFile.addDefault("named-phantom", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-pillager-crossbow", Collections.singletonList("&c%player%&7 was shot by %mob%&7."));
        customFile.addDefault("named-pillager-melee", Collections.singletonList("&c%player%&7 was hit by %mob%&7."));
        customFile.addDefault("named-polarbear", Collections.singletonList("&c%player%&7 was hugged by %mob%&7."));
        customFile.addDefault("named-pufferfish", Collections.singletonList("&c%player%&7 was stung by %mob%&7."));
        customFile.addDefault("named-ravager", Collections.singletonList("&c%player%&7 was ravaged by %mob%&7."));
        customFile.addDefault("named-shulker", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-silverfish", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-stray-arrow", Collections.singletonList("&c%player%&7 was shot by %mob%&7."));
        customFile.addDefault("named-stray-melee", Collections.singletonList("&c%player%&7 was punched by %mob%&7."));
        customFile.addDefault("named-witherskeleton", Collections.singletonList("&c%player%&7 was nae-naed by %mob%&7."));
        customFile.addDefault("named-skeleton-arrow", Collections.singletonList("&c%player%&7 was shot by %mob%&7."));
        customFile.addDefault("named-skeleton-melee", Collections.singletonList("&c%player%&7 was punched by %mob%&7."));
        customFile.addDefault("named-slime", Collections.singletonList("&c%player%&7 was stomped on by %mob%&7."));
        customFile.addDefault("named-spider", Collections.singletonList("&c%player%&7 was bit by %mob%&7."));
        customFile.addDefault("named-vex", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-vindicator", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-witch", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-wither", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-wither-explosion", Collections.singletonList("&c%player%&7 was exploded by %mob%&7."));
        customFile.addDefault("named-wolf", Collections.singletonList("&c%player%&7 was ripped apart by %mob%&7."));
        customFile.addDefault("named-pigzombie", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-zombievillager", Collections.singletonList("&c%player%&7 was eaten by %mob%&7."));
        customFile.addDefault("named-husk", Collections.singletonList("&c%player%&7 was eaten by %mob%&7."));
        customFile.addDefault("named-zombie", Collections.singletonList("&c%player%&7 was eaten by %mob%&7."));
        customFile.addDefault("endercrystal", Collections.singletonList("&c%player%&7 was exploded by an ender crystal."));
        customFile.addDefault("anvil", Collections.singletonList("&c%player%&7 was crushed by an anvil."));
        customFile.addDefault("lightning", Collections.singletonList("&c%player%&7 was struck by lightning."));
        customFile.addDefault("enderpearl", Collections.singletonList("&c%player%&7 used an ender pearl at the wrong time."));
        customFile.addDefault("tnt", Collections.singletonList("&c%player%&7 was exploded by TNT."));
        customFile.addDefault("dispenser-magic", Collections.singletonList("&c%player%&7 was killed by magic from a dispenser."));
        customFile.addDefault("dispenser", Collections.singletonList("&c%player%&7 was killed by a dispenser."));
        customFile.addDefault("bed-explosion", Collections.singletonList("&c%player%&7 slept in the wrong world."));
        customFile.addDefault("cactus", Collections.singletonList("&c%player%&7 was pricked by a cactus."));
        customFile.addDefault("anvil", Collections.singletonList("&c%player%&7 was crushed by an anvil."));
        customFile.addDefault("drown", Collections.singletonList("&c%player%&7 drowned."));
        customFile.addDefault("wither-potion", Collections.singletonList("&c%player%&7 withered away."));
        customFile.addDefault("harming", Collections.singletonList("&c%player%&7 killed themself with a harming potion."));
        customFile.addDefault("burn", Collections.singletonList("&c%player%&7 burned to death."));
        customFile.addDefault("magma", Collections.singletonList("&c%player%&7 was burned by a magma block."));
        customFile.addDefault("lava", Collections.singletonList("&c%player%&7 fell in lava."));
        customFile.addDefault("lightning", Collections.singletonList("&c%player%&7 was struck by lightning."));
        customFile.addDefault("starvation", Collections.singletonList("&c%player%&7 starved to death."));
        customFile.addDefault("suffocation", Collections.singletonList("&c%player%&7 suffocated to death."));
        customFile.addDefault("cramming", Collections.singletonList("&c%player%&7 was crammed to death."));
        customFile.addDefault("void", Collections.singletonList("&c%player%&7 fell into the void."));
        customFile.addDefault("fall", Collections.singletonList("&c%player%&7 fell quite far."));
        customFile.addDefault("fly-into-wall", Collections.singletonList("&c%player%&7 flew into a wall."));
        customFile.addDefault("suicide", Collections.singletonList("&c%player%&7 committed suicide."));
        customFile.options().copyDefaults(true);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads deathmessages.yml
     */
    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Initializes deathmessages.yml
     * @throws IOException if unable to create the file
     */
    private static void setupFile() throws IOException {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("CrankedCore")).getDataFolder(), "deathmessages.yml");
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Couldn't create deathmessages.yml file");
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves deathmessages.yml
     * @throws IOException if unable to save the file
     */
    public static void save() throws IOException {
        customFile.save(file);
    }

    /**
     * Getter method for customFile
     * @return A reference to deathmessages.yml
     */
    public static FileConfiguration get() {
        return customFile;
    }
}
