package me.cranked.crankedcore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Manages deathmessages.yml
 * @author Nick
 * @since 1.0
 */
public class DeathMessagesConfigManager {
    private static File file;
    private static FileConfiguration customFile;
    private static Map<String, List<String>> deathMessages;

    /**
     * Sets up deathmessages.yml
     */
    public static void setupDeathmessages() {


        try {
            setupFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        customFile.options().header(
                "               _____             _   _       __  __\n" +
                "               |  __ \\           | | | |     |  \\/  |\n" +
                "               | |  | | ___  __ _| |_| |__   | \\  / | ___  ___ ___  __ _  __ _  ___  ___\n" +
                "               | |  | |/ _ \\/ _` | __| '_ \\  | |\\/| |/ _ \\/ __/ __|/ _` |/ _` |/ _ \\/ __|\n" +
                "               | |__| |  __/ (_| | |_| | | | | |  | |  __/\\__ \\__ \\ (_| | (_| |  __/\\__ \\\n" +
                "               |_____/ \\___|\\__,_|\\__|_| |_| |_|  |_|\\___||___/___/\\__,_|\\__, |\\___||___/\n" +
                "                                                                          __/ |\n" +
                "                                                                         |___/\n" +
                " \n" +
                " You can add multiple lines to have a random death message chosen.\n" +
                " And, you can disable certain death messages by setting the death message to '', see the following example:\n" +
                " unknown: '\n" +
                "\n" +
                " This way, you can disable all death messages except for pvp-related deaths.");
        customFile.addDefault("disable-all-death-messages", Boolean.FALSE);
        customFile.addDefault("enable-custom-death-messages", Boolean.TRUE);
        customFile.addDefault("unknown", Arrays.asList("&c%player%&7 died.", "&c%player%&7 mysteriously died."));

        customFile.addDefault("projectile-unknown", Collections.singletonList("&c%player%&7 was shot to death."));
        customFile.addDefault("pvp-unknown", Collections.singletonList("&c%player%&7 was killed by %killer%&7."));
        customFile.addDefault("explosion-unknown", Collections.singletonList("&c%player%&7 was exploded."));
        customFile.addDefault("magic-unknown", Collections.singletonList("&c%player%&7 was killed by magic from a dispenser."));
        customFile.addDefault("melee-unknown", Collections.singletonList("&c%player%&7 was meleed to death."));
        customFile.addDefault("fireball-unknown", Collections.singletonList("&c%player%&7 was blown up by a fireball."));
        customFile.addDefault("pvp-thorns", Collections.singletonList("&c%player%&7 was killed trying to hurt %killer%&7."));
        customFile.addDefault("pvp-explosion", Collections.singletonList("&c%player%&7 was blown up while fighting %killer%&7."));
        customFile.addDefault("pvp-magic", Collections.singletonList("&c%player%&7 was killed by %killer%&7 using magic."));
        customFile.addDefault("pvp-drown", Collections.singletonList("&c%player%&7 drowned while fighting %killer%&7."));
        customFile.addDefault("pvp-burn", Collections.singletonList("&c%player%&7 burned to death while fighting %killer%&7."));
        customFile.addDefault("pvp-fire-tick", Collections.singletonList("&c%player%&7 burned to death while fighting %killer%&7."));
        customFile.addDefault("pvp-magma", Collections.singletonList("&c%player%&7 was burned by a magma block while fighting %killer%&7."));
        customFile.addDefault("pvp-lava", Collections.singletonList("&c%player%&7 burned in lava while fighting %killer%&7."));
        customFile.addDefault("pvp-starvation", Collections.singletonList("&c%player%&7 starved to death while fighting %killer%&7."));
        customFile.addDefault("pvp-suffocation", Collections.singletonList("&c%player%&7 suffocated to death while fighting %killer%&7."));
        customFile.addDefault("pvp-cramming", Collections.singletonList("&c%player%&7 was crammed to death while fighting %killer%&7."));
        customFile.addDefault("pvp-freeze", Collections.singletonList("&c%player%&7 froze to death while fighting %killer%&7."));
        customFile.addDefault("pvp-void", Collections.singletonList("&c%player%&7 fell out of the world while fighting %killer%&7."));
        customFile.addDefault("pvp-fall-far", Collections.singletonList("&c%player%&7 fell quite far while fighting %killer%&7."));
        customFile.addDefault("pvp-fall-short", Collections.singletonList("&c%player%&7 broke their legs while fighting %killer%&7."));
        customFile.addDefault("pvp-fly-into-wall", Collections.singletonList("&c%player%&7 flew into a wall while fighting %killer%&7."));
        customFile.addDefault("pvp-pricked", Collections.singletonList("&c%player%&7 was pricked to death while fighting %killer%&7."));
        customFile.addDefault("pvp-lightning", Collections.singletonList("&c%player%&7 was struck by lightning while fighting %player%&7."));
        customFile.addDefault("pvp-enderpearl", Collections.singletonList("&c%player%&7 enderpearled into a wall while fighting %player%&7."));
        customFile.addDefault("pvp-tnt", Collections.singletonList("&c%player%&7 was blown up while fighting %player%&7."));
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
        customFile.addDefault("pvp-fists", Collections.singletonList("&c%player%&7 was punched to death by %killer%&7."));
        customFile.addDefault("pvp-suicide", Collections.singletonList("&c%player%&7 committed suicide while fighting %player%&7."));
        customFile.addDefault("pvp-other", Collections.singletonList("&c%player%&7 was killed by %killer%&7."));
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
        customFile.addDefault("enderdragon-fireball", Collections.singletonList("&c%player%&7 was fireballed by the ender dragon."));
        customFile.addDefault("enderdragon-magic", Collections.singletonList("&c%player%&7 was killed by the ender dragon's magic."));
        customFile.addDefault("enderdragon", Collections.singletonList("&c%player%&7 was killed by the ender dragon."));
        customFile.addDefault("enderman", Collections.singletonList("&c%player%&7 was killed by an enderman. Ouch."));
        customFile.addDefault("endermite", Collections.singletonList("&c%player%&7 was killed by an endermite."));
        customFile.addDefault("evoker", Collections.singletonList("&c%player%&7 was killed by an evoker."));
        customFile.addDefault("ghast", Collections.singletonList("&c%player%&7 was blown up by a ghast."));
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
        customFile.addDefault("hoglin", Collections.singletonList("&c%player%&7 was rammed by a hoglin."));
        customFile.addDefault("zoglin", Collections.singletonList("&c%player%&7 was rammed by a zoglin."));
        customFile.addDefault("piglin", Collections.singletonList("&c%player%&7 forgot to wear gold in the nether."));
        customFile.addDefault("piglin-brute", Collections.singletonList("&c%player%&7 forgot to wear gold in the nether."));
        customFile.addDefault("husk", Collections.singletonList("&c%player%&7 was eaten by a husk."));
        customFile.addDefault("zombie", Collections.singletonList("&c%player%&7 was eaten by a zombie."));
        customFile.addDefault("named-hoglin", Collections.singletonList("&c%player%&7 was rammed by %mob%&7."));
        customFile.addDefault("named-zoglin", Collections.singletonList("&c%player%&7 was rammed by %mob%&7."));
        customFile.addDefault("named-piglin", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-piglin-brute", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
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
        customFile.addDefault("named-enderdragon-fireball", Collections.singletonList("&c%player%&7 was fireballed by %mob%&7."));
        customFile.addDefault("named-enderdragon-magic", Collections.singletonList("&c%player%&7 was killed by %mob%&7's magic."));
        customFile.addDefault("named-melee-unknown", Collections.singletonList("&c%player%&7 was meleed to death by %mob%&7."));
        customFile.addDefault("named-enderman", Collections.singletonList("&c%player%&7 was killed by %mob%&7. Ouch."));
        customFile.addDefault("named-endermite", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-evoker", Collections.singletonList("&c%player%&7 was killed by %mob%&7."));
        customFile.addDefault("named-ghast", Collections.singletonList("&c%player%&7 was blown up by %mob%&7."));
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
        customFile.addDefault("wither-skull", Collections.singletonList("&c%player%&7 was shot by a wither."));
        customFile.addDefault("named-wither-skull", Collections.singletonList("&c%player%&7 was shot by %mob%&7."));
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
        customFile.addDefault("freeze", Collections.singletonList("&c%player%&7 froze to death."));
        customFile.addDefault("endercrystal", Collections.singletonList("&c%player%&7 was exploded by an ender crystal."));
        customFile.addDefault("anvil", Collections.singletonList("&c%player%&7 was crushed by an anvil."));
        customFile.addDefault("lightning", Collections.singletonList("&c%player%&7 was struck by lightning."));
        customFile.addDefault("enderpearl", Collections.singletonList("&c%player%&7 used an ender pearl at the wrong time."));
        customFile.addDefault("pricked", Collections.singletonList("&c%player%&7 was pricked to death."));
        customFile.addDefault("tnt", Collections.singletonList("&c%player%&7 was exploded by TNT."));
        customFile.addDefault("drown", Collections.singletonList("&c%player%&7 drowned."));
        customFile.addDefault("wither-potion", Collections.singletonList("&c%player%&7 withered away."));
        customFile.addDefault("burn", Collections.singletonList("&c%player%&7 burned to death."));
        customFile.addDefault("fire-tick", Collections.singletonList("&c%player%&7 burned to death."));
        customFile.addDefault("magma", Collections.singletonList("&c%player%&7 was burned by a magma block."));
        customFile.addDefault("lava", Collections.singletonList("&c%player%&7 fell in lava."));
        customFile.addDefault("starvation", Collections.singletonList("&c%player%&7 starved to death."));
        customFile.addDefault("suffocation", Collections.singletonList("&c%player%&7 suffocated to death."));
        customFile.addDefault("cramming", Collections.singletonList("&c%player%&7 was crammed to death."));
        customFile.addDefault("void", Collections.singletonList("&c%player%&7 fell into the void."));
        customFile.addDefault("fall-far", Collections.singletonList("&c%player%&7 fell quite far."));
        customFile.addDefault("fall-short", Collections.singletonList("&c%player%&7 hit the ground too hard."));
        customFile.addDefault("fly-into-wall", Collections.singletonList("&c%player%&7 flew into a wall."));
        customFile.addDefault("suicide-magic", Collections.singletonList("&c%player%&7 killed themselves using magic."));
        customFile.addDefault("block-explosion", Collections.singletonList("&c%player%&7 set their spawn point in the wrong world."));
        customFile.addDefault("suicide", Collections.singletonList("&c%player%&7 committed suicide."));

        customFile.options().copyDefaults(true);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes maps for deathmessages.yml, as to make reading
     * configuration files much faster.
     */
    public static void initMap() {
        deathMessages = new HashMap<>();

        getListHelper("unknown");
        getListHelper("projectile-unknown");
        getListHelper("pvp-unknown");
        getListHelper("explosion-unknown");
        getListHelper("magic-unknown");
        getListHelper("melee-unknown");
        getListHelper("fireball-unknown");
        getListHelper("pvp-thorns");
        getListHelper("pvp-explosion");
        getListHelper("pvp-magic");
        getListHelper("pvp-drown");
        getListHelper("pvp-burn");
        getListHelper("pvp-fire-tick");
        getListHelper("pvp-magma");
        getListHelper("pvp-lava");
        getListHelper("pvp-starvation");
        getListHelper("pvp-suffocation");
        getListHelper("pvp-cramming");
        getListHelper("pvp-freeze");
        getListHelper("pvp-void");
        getListHelper("pvp-fall-far");
        getListHelper("pvp-fall-short");
        getListHelper("pvp-fly-into-wall");
        getListHelper("pvp-pricked");
        getListHelper("pvp-lightning");
        getListHelper("pvp-tnt");
        getListHelper("pvp-custom-sword");
        getListHelper("pvp-custom-axe");
        getListHelper("pvp-custom-bow");
        getListHelper("pvp-custom-crossbow");
        getListHelper("pvp-custom-trident");
        getListHelper("pvp-custom-other");
        getListHelper("pvp-sword");
        getListHelper("pvp-axe");
        getListHelper("pvp-bow");
        getListHelper("pvp-crossbow");
        getListHelper("pvp-trident");
        getListHelper("pvp-fists");
        getListHelper("pvp-suicide");
        getListHelper("pvp-other");
        getListHelper("bee");
        getListHelper("blaze-melee");
        getListHelper("blaze-fireball");
        getListHelper("cavespider");
        getListHelper("creeper");
        getListHelper("creeper-charged");
        getListHelper("drowned-trident");
        getListHelper("drowned-melee");
        getListHelper("elderguardian");
        getListHelper("enderdragon-breath");
        getListHelper("enderdragon-fireball");
        getListHelper("enderdragon-magic");
        getListHelper("enderdragon");
        getListHelper("enderman");
        getListHelper("endermite");
        getListHelper("evoker");
        getListHelper("ghast");
        getListHelper("guardian");
        getListHelper("illusioner");
        getListHelper("irongolem");
        getListHelper("llama");
        getListHelper("magmacube");
        getListHelper("panda");
        getListHelper("phantom");
        getListHelper("pillager-crossbow");
        getListHelper("pillager-melee");
        getListHelper("polarbear");
        getListHelper("pufferfish");
        getListHelper("ravager");
        getListHelper("shulker");
        getListHelper("silverfish");
        getListHelper("stray-arrow");
        getListHelper("stray-melee");
        getListHelper("witherskeleton");
        getListHelper("skeleton-arrow");
        getListHelper("skeleton-melee");
        getListHelper("slime");
        getListHelper("spider");
        getListHelper("vex");
        getListHelper("vindicator");
        getListHelper("witch");
        getListHelper("wither");
        getListHelper("wither-explosion");
        getListHelper("wolf");
        getListHelper("zombiepigman");
        getListHelper("zombievillager");
        getListHelper("hoglin");
        getListHelper("zoglin");
        getListHelper("piglin");
        getListHelper("piglin-brute");
        getListHelper("husk");
        getListHelper("zombie");
        getListHelper("named-hoglin");
        getListHelper("named-zoglin");
        getListHelper("named-piglin");
        getListHelper("named-piglin-brute");
        getListHelper("named-bee");
        getListHelper("named-blaze-melee");
        getListHelper("named-blaze-fireball");
        getListHelper("named-cavespider");
        getListHelper("named-creeper");
        getListHelper("named-creeper-charged");
        getListHelper("named-drowned-trident");
        getListHelper("named-drowned-melee");
        getListHelper("named-elderguardian");
        getListHelper("named-enderdragon-breath");
        getListHelper("named-enderdragon");
        getListHelper("named-enderdragon-fireball");
        getListHelper("named-enderdragon-magic");
        getListHelper("named-melee-unknown");
        getListHelper("named-enderman");
        getListHelper("named-endermite");
        getListHelper("named-evoker");
        getListHelper("named-ghast");
        getListHelper("named-guardian");
        getListHelper("named-illusioner");
        getListHelper("named-irongolem");
        getListHelper("named-magmacube");
        getListHelper("named-panda");
        getListHelper("named-phantom");
        getListHelper("named-pillager-crossbow");
        getListHelper("named-pillager-melee");
        getListHelper("named-polarbear");
        getListHelper("named-pufferfish");
        getListHelper("named-ravager");
        getListHelper("named-shulker");
        getListHelper("named-silverfish");
        getListHelper("wither-skull");
        getListHelper("named-wither-skull");
        getListHelper("named-stray-arrow");
        getListHelper("named-stray-melee");
        getListHelper("named-witherskeleton");
        getListHelper("named-skeleton-arrow");
        getListHelper("named-skeleton-melee");
        getListHelper("named-slime");
        getListHelper("named-spider");
        getListHelper("named-vex");
        getListHelper("named-vindicator");
        getListHelper("named-witch");
        getListHelper("named-wither");
        getListHelper("named-wither-explosion");
        getListHelper("named-wolf");
        getListHelper("named-pigzombie");
        getListHelper("named-zombievillager");
        getListHelper("named-husk");
        getListHelper("named-zombie");
        getListHelper("freeze");
        getListHelper("endercrystal");
        getListHelper("anvil");
        getListHelper("lightning");
        getListHelper("enderpearl");
        getListHelper("pricked");
        getListHelper("tnt");
        getListHelper("drown");
        getListHelper("wither-potion");
        getListHelper("burn");
        getListHelper("fire-tick");
        getListHelper("magma");
        getListHelper("lava");
        getListHelper("starvation");
        getListHelper("suffocation");
        getListHelper("cramming");
        getListHelper("void");
        getListHelper("fall-far");
        getListHelper("fall-short");
        getListHelper("fly-into-wall");
        getListHelper("suicide-magic");
        getListHelper("block-explosion");
        getListHelper("suicide");
    }

    /**
     * Helper method for initMap()
     * @param s The name of the entry in deathmessages.yml we want to get from
     * @return A list of Strings, the value of that entry in deathmessages.yml
     */
    private static List<String> getListHelper(String s) {
        return deathMessages.put(s, DeathMessagesConfigManager.get().getStringList(s));
    }

    /**
     * Helper method for Death.java
     * @param s The key of in the map
     * @return The value in the map, which is the value in deathmessages.yml
     */
    public static List<String> getFromMap(String s) {
        return deathMessages.get(s);
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
