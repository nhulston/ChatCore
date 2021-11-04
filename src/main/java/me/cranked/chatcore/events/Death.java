package me.cranked.chatcore.events;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.DeathMessagesConfigManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

import static me.cranked.chatcore.VersionManager.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

public class Death implements Listener {
    // TODO killed with named item, hover to see

    /**
     * Helper method for onDeath that picks a random death message
     * @param path The location in deathmessages.yml we want to get from
     * @return The new death message
     */
    public String getMsg(String path) {
        List<String> list = DeathMessagesConfigManager.getFromMap(path);
        int x = ThreadLocalRandom.current().nextInt(0, list.size());
        return list.get(x);
    }

    /**
     * Changes the death message to whatever is from deathmessages.yml
     * Works on versions 1.7-1.17
     * @param e PlayerDeathEvent, used to calculate what death message to use
     */
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onDeath(PlayerDeathEvent e) {
        // Disable death messages check
        if (DeathMessagesConfigManager.get().getBoolean("disable-all-death-messages")) {
            e.setDeathMessage("");
            return;
        }

        // Config check
        if (!DeathMessagesConfigManager.get().getBoolean("enable-custom-death-messages"))
            return;

        // Useful variables
        Player player = e.getEntity();
        String originalMsg = e.getDeathMessage();
        if (originalMsg == null) {
            originalMsg = "";
        }
        String msgPath = "unknown";
        boolean pvp = player.getKiller() != null ||
                originalMsg.contains("whilst") ||
                originalMsg.contains("to escape");

        EntityDamageEvent cause = player.getLastDamageCause();
        if (cause != null) {
            EntityDamageEvent.DamageCause lastCause = cause.getCause();

            if (lastCause == CONTACT) {
                msgPath = "pricked";
            } else if (lastCause == ENTITY_ATTACK || (isV9() && lastCause == ENTITY_SWEEP_ATTACK)) {
                if (player.getKiller() != null) {
                    ItemStack item;
                    if (isV9()) {
                        item = player.getKiller().getInventory().getItemInMainHand();
                    } else {
                        item = player.getKiller().getItemInHand();
                    }
                    String itemName = item.getType().toString();
                    if (itemName.contains("SWORD")) {
                        msgPath = "sword";
                    } else if (itemName.contains("AXE")) {
                        msgPath = "axe";
                    } else if (itemName.contains("TRIDENT")) {
                        msgPath = "trident";
                    } else if (itemName.contains("AIR")) {
                        msgPath = "fists";
                    } else {
                        msgPath = "other";
                    }

                    if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
                        msgPath = "custom-" + msgPath;
                    }
                } else {
                    Entity killer = ((EntityDamageByEntityEvent) cause).getDamager();
                    if (isV15() && killer instanceof Bee) {
                        msgPath = "bee";
                    } else if (killer instanceof Blaze) {
                        msgPath = "blaze-melee";
                    } else if (isV13() && killer instanceof Drowned) {
                        msgPath = "drowned-melee";
                    } else if (isV13() && killer instanceof ElderGuardian) {
                        msgPath = "elderguardian";
                    } else if (killer instanceof EnderDragon) {
                        msgPath = "enderdragon";
                    } else if (isV9() && killer instanceof DragonFireball) {
                        msgPath = "enderdragon-fireball";
                    } else if (isV9() && killer instanceof AreaEffectCloud) {
                        msgPath = "enderdragon-magic";
                    } else if (killer instanceof Enderman) {
                        msgPath = "enderman";
                    } else if (isV8() && killer instanceof Endermite) {
                        msgPath = "endermite";
                    } else if (isV11() && (killer instanceof Evoker || killer instanceof EvokerFangs)) {
                        msgPath = "evoker";
                    } else if (isV8() && killer instanceof Guardian) {
                        msgPath = "guardian";
                    } else if (killer instanceof IronGolem) {
                        msgPath = "irongolem";
                    } else if (killer instanceof MagmaCube) {
                        msgPath = "magmacube";
                    } else if (isV14() && killer instanceof Panda) {
                        msgPath = "panda";
                    } else if (isV13() && killer instanceof Phantom) {
                        msgPath = "phantom";
                    } else if (isV14() && killer instanceof Pillager) {
                        msgPath = "pillager-melee";
                    } else if (isV10() && killer instanceof PolarBear) {
                        msgPath = "polarbear";
                    } else if (isV13() && killer instanceof PufferFish) {
                        msgPath = "pufferfish";
                    } else if (isV14() && killer instanceof Ravager) {
                        msgPath = "ravager";
                    } else if (killer instanceof Silverfish) {
                        msgPath = "silverfish";
                    } else if (isV10() && killer instanceof Stray) {
                        msgPath = "stray-melee";
                    } else if (isV11() && killer instanceof WitherSkeleton) {
                        msgPath = "witherskeleton";
                    } else if (killer instanceof Skeleton) {
                        msgPath = "skeleton-melee";
                    } else if (killer instanceof Slime) {
                        msgPath = "slime";
                    } else if (killer instanceof Spider) {
                        msgPath = "spider";
                    } else if (isV11() && killer instanceof Vex) {
                        msgPath = "vex";
                    } else if (isV11() && killer instanceof Vindicator) {
                        msgPath = "vindicator";
                    } else if (killer instanceof Wolf) {
                        msgPath = "wolf";
                    } else if (killer instanceof PigZombie) {
                        msgPath = "zombiepigman";
                    } else if (isV11() && killer instanceof ZombieVillager) {
                        msgPath = "zombievillager";
                    } else if (isV10() && killer instanceof Husk) {
                        msgPath = "husk";
                    } else if (killer instanceof Zombie) {
                        msgPath = "zombie";
                    } else if (isV16() && killer instanceof Hoglin) {
                        msgPath = "hoglin";
                    } else if (isV16() && killer instanceof Zoglin) {
                        msgPath = "zoglin";
                    } else if (isV16() && killer instanceof Piglin) {
                        msgPath = "piglin";
                    } else if (isV16() && killer instanceof PiglinBrute) {
                        msgPath = "piglin-brute";
                    } else {
                        msgPath = "melee-unknown";
                    }

                    if (killer.getCustomName() != null) {
                        msgPath = "named-" + msgPath;
                    }
                }
            } else if (lastCause == PROJECTILE) {
                ProjectileSource shooter = ((Projectile) ((EntityDamageByEntityEvent) cause).getDamager()).getShooter();
                msgPath = "projectile-unknown";
                if (shooter != null) {
                    if (shooter instanceof Player) {
                        ItemStack item;
                        if (isV9()) {
                            item = player.getKiller().getInventory().getItemInMainHand();
                        } else {
                            item = player.getKiller().getItemInHand();
                        }
                        String itemName = item.getType().toString();
                        if (itemName.contains("CROSSBOW")) {
                            msgPath = "crossbow";
                        } else if (itemName.contains("BOW")) {
                            msgPath = "bow";
                        } else if (itemName.contains("TRIDENT")) {
                            msgPath = "trident";
                        }

                        if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
                            msgPath = "custom-" + msgPath;
                        }
                    }
                    else if (shooter instanceof Blaze)
                        msgPath = "blaze-fireball";
                    else if (shooter.toString().equals("CraftDrowned"))
                        msgPath = "drowned-trident";
                    else if (shooter.toString().equals("CraftGhast"))
                        msgPath = "ghast-fireball";
                    else if (isV12() && shooter instanceof Illusioner)
                        msgPath = "illusioner";
                    else if (isV11() && shooter instanceof Llama)
                        msgPath = "llama";
                    else if (isV14() && shooter instanceof Pillager)
                        msgPath = "pillager-crossbow";
                    else if (isV9() && shooter instanceof Shulker)
                        msgPath = "shulker";
                    else if (isV10() && shooter instanceof Stray)
                        msgPath = "stray-arrow";
                    else if (shooter instanceof Skeleton)
                        msgPath = "skeleton-arrow";
                    else if (shooter instanceof Wither)
                        msgPath = "wither-skull";
                }
            } else if (lastCause == SUFFOCATION) {
                msgPath = "suffocation";
            } else if (lastCause == FALL) {
                if (player.getKiller() != null) {
                    pvp = true;
                }

                if (originalMsg.contains("high place")) {
                    msgPath = "fall-far";
                } else if (cause.getFinalDamage() == 5.0) {
                    msgPath = "enderpearl";
                } else {
                    msgPath = "fall-short";
                }
            } else if (lastCause == FIRE) {
                msgPath = "burn";
            } else if (lastCause == FIRE_TICK) {
                msgPath = "fire-tick";
            } else if (lastCause == LAVA) {
                msgPath = "lava";
            } else if (lastCause == DROWNING) {
                msgPath = "drown";
            } else if (lastCause == BLOCK_EXPLOSION) {
                msgPath = "block-explosion";
            } else if (lastCause == ENTITY_EXPLOSION) {
                Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
                if (damager instanceof TNTPrimed)
                    msgPath = "tnt";
                else if (damager instanceof Creeper) {
                    if (((Creeper) damager).isPowered()) {
                        msgPath = "creeper-charged";
                    } else {
                        msgPath = "creeper";
                    }
                } else if (damager instanceof Wither) {
                    msgPath = "wither-explosion";
                }
            } else if (lastCause == VOID) {
                msgPath = "void";
                if (originalMsg.contains("in the same world")) {
                    pvp = true;
                }
            } else if (lastCause == LIGHTNING) {
                msgPath = "lightning";
            } else if (lastCause == SUICIDE) {
                msgPath = "suicide";
            } else if (lastCause == STARVATION) {
                msgPath = "starvation";
            } else if (lastCause == POISON) {
                msgPath = "POISON";
            } else if (lastCause == MAGIC) {
                if (player.getKiller() != null) {
                    if (player.getKiller().equals(player)) {
                        msgPath = "suicide-magic";
                        pvp = false;
                    } else {
                        msgPath = "magic";
                    }
                } else {
                    if (originalMsg.contains("Witch"))
                        msgPath = "witch";
                    else
                        msgPath = "magic-unknown"; // dispenser
                }
            } else if (lastCause == WITHER) {
                msgPath = "wither-potion";
            } else if (lastCause == FALLING_BLOCK) {
                msgPath = "anvil";
                pvp = false;
            } else if (lastCause == THORNS) {
                msgPath = "pvp-thorns";
            } else if (isV9() && lastCause == DRAGON_BREATH) {
                msgPath = "enderdragon-breath";
            } else if (lastCause == CUSTOM) {
                msgPath = "unknown";
            } else if (isV9() && lastCause == FLY_INTO_WALL) {
                msgPath = "fly-into-wall";
            } else if (isV10() && lastCause == HOT_FLOOR) {
                msgPath = "magma";
                if (originalMsg.contains("walked into danger zone"))
                    pvp = true;
            } else if (lastCause == CRAMMING) {
                msgPath = "cramming";
                if (originalMsg.contains("squashed"))
                    pvp = true;
            } else if (isV17() && lastCause == FREEZE) {
                msgPath = "freeze";
            }
        } else if (originalMsg.contains("fireballed by")) {
            msgPath = "fireball-unknown";
        } else if (originalMsg.contains("blown up")) {
            msgPath = "explosion-unknown"; // fireball
        } else if (originalMsg.contains("blew up")) {
            msgPath = "explosion-unknown"; //end crystal
        }

        if (player.getKiller() != null && player.getKiller().equals(player)) {
            pvp = false;
        }

        if (pvp && !msgPath.contains("pvp-"))
            msgPath = "pvp-" + msgPath;

        String deathMessage = getMsg(msgPath);

        // Replace player
        deathMessage = deathMessage.replaceAll("%player%", player.getDisplayName());

        // Replace killer name
        Player killer = player.getKiller();
        if (killer != null && msgPath.contains("pvp")) {
            deathMessage = deathMessage.replaceAll("%killer%", killer.getDisplayName());
        }

        // Replace item name
        if (killer != null && msgPath.contains("custom")) {
            ItemMeta itemMeta;
            if (isV9()) {
                itemMeta = killer.getInventory().getItemInMainHand().getItemMeta();
            } else {
                itemMeta = killer.getItemInHand().getItemMeta();
            }

            if (itemMeta != null) {
                deathMessage = deathMessage.replaceAll("%item%", itemMeta.getDisplayName());
            }
        }

        // Replace mob name
        if (cause != null && msgPath.contains("named")) {
            Entity mobKiller = ((EntityDamageByEntityEvent) cause).getDamager();
            if (mobKiller.getCustomName() != null) {
                deathMessage = deathMessage.replaceAll("%mob%", mobKiller.getCustomName());
            }
        }

        e.setDeathMessage(ConfigManager.placeholderize(deathMessage, player));
    }
}
