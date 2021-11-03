package me.cranked.crankedcore.events;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import me.cranked.crankedcore.CrankedCore;
import me.cranked.crankedcore.DeathMessagesConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Stray;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

// TODO fix this entire class for 1.16 & 1.17
public class Death implements Listener {
    public String getMsg(String path) {
        int x = ThreadLocalRandom.current().nextInt(0, DeathMessagesConfigManager.get().getStringList(path).size());
        return DeathMessagesConfigManager.get().getStringList(path).get(x);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        // Config check
        if (!DeathMessagesConfigManager.get().getBoolean("enable-custom-death-messages"))
            return;

        // Disable death messages check
        if (DeathMessagesConfigManager.get().getBoolean("disable-all-death-messages")) {
            e.setDeathMessage("");
            return;
        }

        // Useful variables
        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();
        String playerName = player.getDisplayName();
        Entity mobKiller;
        EntityDamageEvent lastDamageCause = player.getLastDamageCause();
        LivingEntity livingEntity = null;
        String msg;

        // Killed by mob, set killer/shooter
        if (killer == null && lastDamageCause instanceof EntityDamageByEntityEvent) {
            mobKiller = ((EntityDamageByEntityEvent)lastDamageCause).getDamager();
            if (mobKiller instanceof Projectile) {
                try {
                    LivingEntity shooter = (LivingEntity) ((Projectile) mobKiller).getShooter();
                    if (shooter != null)
                        livingEntity = shooter;
                } catch (ClassCastException e2) {
                    EntityDamageEvent.DamageCause damageCause = lastDamageCause.getCause();
                    if (damageCause.equals(EntityDamageEvent.DamageCause.MAGIC)) {
                        int x = (int)(Math.random() * DeathMessagesConfigManager.get().getStringList("dispenser-magic").size());
                        e.setDeathMessage(CrankedCore.placeholderColor(DeathMessagesConfigManager.get().getStringList("dispenser-magic").get(x), player).replaceAll("%player%", playerName));
                    } else {
                        int x = (int)(Math.random() * DeathMessagesConfigManager.get().getStringList("dispenser").size());
                        e.setDeathMessage(CrankedCore.placeholderColor(DeathMessagesConfigManager.get().getStringList("dispenser").get(x), player).replaceAll("%player%", playerName));
                    }
                    return;
                }
            }
        }
        assert lastDamageCause != null;
        EntityDamageEvent.DamageCause lastCause = lastDamageCause.getCause();
        if (Objects.requireNonNull(e.getDeathMessage()).contains("was killed trying to hurt")) {
            int killerLoc = e.getDeathMessage().indexOf("hurt");
            String killerName = e.getDeathMessage().substring(killerLoc + 5);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getDisplayName().contains(killerName)) {
                    assert killer != null;
                    killerName = killer.getDisplayName();
                }
            }
            int x = (int)(Math.random() * DeathMessagesConfigManager.get().getStringList("pvp-thorns").size());
            msg = DeathMessagesConfigManager.get().getStringList("pvp-thorns").get(x).replaceAll("%killer%", killerName);
        } else {
            boolean fireDeath = (lastCause.equals(EntityDamageEvent.DamageCause.FIRE) || lastCause.equals(EntityDamageEvent.DamageCause.FIRE_TICK));
            if (killer != null && killer != player) {
                ItemStack item;
                if (Bukkit.getVersion().contains("1.8")) {
                    item = killer.getItemInHand();
                } else {
                    item = killer.getInventory().getItemInMainHand();
                }
                String killerName = killer.getDisplayName();
                if (lastCause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    msg = getMsg("pvp-explosion");
                } else if (lastCause.equals(EntityDamageEvent.DamageCause.MAGIC)) {
                    msg = getMsg("pvp-magic");
                } else if (lastCause.equals(EntityDamageEvent.DamageCause.DROWNING)) {
                    msg = getMsg("pvp-drown");
                } else if (fireDeath) {
                    msg = getMsg("pvp-burn");
                } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && lastCause.equals(EntityDamageEvent.DamageCause.HOT_FLOOR)) {
                    msg = getMsg("pvp-magma");
                } else if (lastCause.equals(EntityDamageEvent.DamageCause.LAVA)) {
                    msg = getMsg("pvp-lava");
                } else if (lastCause.equals(EntityDamageEvent.DamageCause.STARVATION)) {
                    msg = getMsg("pvp-starvation");
                } else if (lastCause.equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                    msg = getMsg("pvp-suffocation");
                } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && lastCause.equals(EntityDamageEvent.DamageCause.CRAMMING)) {
                    msg = getMsg("pvp-cramming");
                } else if (lastCause.equals(EntityDamageEvent.DamageCause.VOID)) {
                    msg = getMsg("pvp-void");
                } else if (lastCause.equals(EntityDamageEvent.DamageCause.FALL)) {
                    msg = getMsg("pvp-fall");
                } else if (!Bukkit.getVersion().contains("1.8") && lastCause.equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) {
                    msg = getMsg("pvp-fly-into-wall");
                } else if (lastCause.equals(EntityDamageEvent.DamageCause.CONTACT)) {
                    msg = getMsg("pvp-cactus");
                } else if (item.getType() != Material.AIR && Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
                    Material material = item.getType();
                    String itemName = item.getItemMeta().getDisplayName();
                    if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && (material == Material.DIAMOND_SWORD || material == Material.IRON_SWORD || material == Material.GOLDEN_SWORD || material == Material.STONE_SWORD || material == Material.WOODEN_SWORD)) {
                        msg = getMsg("pvp-custom-sword");
                    } else if ((Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12")) && (material == Material.DIAMOND_SWORD || material == Material.IRON_SWORD || material == Material.STONE_SWORD)) {
                        msg = getMsg("pvp-custom-sword");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && (material == Material.DIAMOND_AXE || material == Material.IRON_AXE || material == Material.GOLDEN_AXE || material == Material.STONE_AXE || material == Material.WOODEN_AXE)) {
                        msg = getMsg("pvp-custom-axe");
                    } else if ((Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12")) && (material == Material.DIAMOND_AXE || material == Material.IRON_AXE || material == Material.STONE_AXE)) {
                        msg = getMsg("pvp-custom-axe");
                    } else if (material == Material.BOW) {
                        msg = getMsg("pvp-custom-bow");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && material == Material.CROSSBOW) {
                        msg = getMsg("pvp-custom-crossbow");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && material == Material.TRIDENT) {
                        msg = getMsg("pvp-custom-trident");
                    } else {
                        msg = getMsg("pvp-custom-other");
                    }
                    msg = msg.replaceAll("%item%", itemName);
                } else if (item.getType() != Material.AIR && !Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
                    Material material = item.getType();
                    if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && (material == Material.DIAMOND_SWORD || material == Material.IRON_SWORD || material == Material.GOLDEN_SWORD || material == Material.STONE_SWORD || material == Material.WOODEN_SWORD)) {
                        msg = getMsg("pvp-sword");
                    } else if ((Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12")) && (material == Material.DIAMOND_SWORD || material == Material.IRON_SWORD || material == Material.STONE_SWORD)) {
                        msg = getMsg("pvp-sword");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && (material == Material.DIAMOND_AXE || material == Material.IRON_AXE || material == Material.GOLDEN_AXE || material == Material.STONE_AXE || material == Material.WOODEN_AXE)) {
                        msg = getMsg("pvp-axe");
                    } else if ((Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12")) && (material == Material.DIAMOND_AXE || material == Material.IRON_AXE || material == Material.STONE_AXE)) {
                        msg = getMsg("pvp-axe");
                    } else if (material == Material.BOW) {
                        msg = getMsg("pvp-bow");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && material == Material.CROSSBOW) {
                        msg = getMsg("pvp-crossbow");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && material == Material.TRIDENT) {
                        msg = getMsg("pvp-trident");
                    } else {
                        msg = getMsg("pvp-other");
                    }
                } else if (item.getType() == Material.AIR) {
                    msg = getMsg("pvp-fists");
                } else {
                    msg = getMsg("pvp-unknown");
                }
                msg = msg.replaceAll("%killer%", killerName);
            } else if (livingEntity != null) {
                if (livingEntity.getCustomName() == null) {
                    if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && !Bukkit.getVersion().contains("1.14") && livingEntity instanceof org.bukkit.entity.Bee) {
                        msg = getMsg("bee");
                    } else if (livingEntity instanceof org.bukkit.entity.Blaze) {
                        if (lastCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                            msg = getMsg("blaze-melee");
                        } else {
                            msg = getMsg("blaze-fireball");
                        }
                    } else if (livingEntity instanceof org.bukkit.entity.CaveSpider) {
                        msg = getMsg("cavespider");
                    } else if (livingEntity instanceof Creeper) {
                        if (((Creeper)livingEntity).isPowered()) {
                            msg = getMsg("creeper-charged");
                        } else {
                            msg = getMsg("creeper");
                        }
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && livingEntity instanceof Drowned) {
                        if (Objects.requireNonNull(livingEntity.getEquipment()).getItemInMainHand().getType() == Material.TRIDENT) {
                            msg = getMsg("drowned-trident");
                        } else {
                            msg = getMsg("drowned-melee");
                        }
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.ElderGuardian) {
                        msg = getMsg("elderguardian");
                    } else if (livingEntity instanceof org.bukkit.entity.EnderDragon) {
                        if (lastCause.equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)) {
                            msg = getMsg("enderdragon-breath");
                        } else {
                            msg = getMsg("enderdragon");
                        }
                    } else if (livingEntity instanceof org.bukkit.entity.Enderman) {
                        msg = getMsg("enderman");
                    } else if (livingEntity instanceof org.bukkit.entity.Endermite) {
                        msg = getMsg("endermite");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Evoker) {
                        msg = getMsg("evoker");
                    } else if (livingEntity instanceof org.bukkit.entity.Ghast) {
                        msg = getMsg("ghast");
                    } else if (livingEntity instanceof org.bukkit.entity.Giant) {
                        msg = getMsg("giant");
                    } else if (livingEntity instanceof org.bukkit.entity.Guardian) {
                        msg = getMsg("guardian");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && livingEntity instanceof org.bukkit.entity.Illusioner) {
                        msg = getMsg("illusioner");
                    } else if (livingEntity instanceof org.bukkit.entity.IronGolem) {
                        msg = getMsg("irongolem");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Llama) {
                        msg = getMsg("llama");
                    } else if (livingEntity instanceof org.bukkit.entity.MagmaCube) {
                        msg = getMsg("magmacube");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && livingEntity instanceof org.bukkit.entity.Panda) {
                        msg = getMsg("panda");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && livingEntity instanceof org.bukkit.entity.Phantom) {
                        msg = getMsg("phantom");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && livingEntity instanceof Pillager) {
                        if (((EntityEquipment)Objects.<EntityEquipment>requireNonNull(((Pillager)livingEntity).getEquipment())).getItemInMainHand().getType() == Material.CROSSBOW) {
                            msg = getMsg("pillager-crossbow");
                        } else {
                            msg = getMsg("pillager-melee");
                        }
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && livingEntity instanceof org.bukkit.entity.PolarBear) {
                        msg = getMsg("polarbear");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && livingEntity instanceof org.bukkit.entity.PufferFish) {
                        msg = getMsg("pufferfish");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && livingEntity instanceof org.bukkit.entity.Ravager) {
                        msg = getMsg("ravager");
                    } else if (!Bukkit.getVersion().contains("1.8") && livingEntity instanceof org.bukkit.entity.Shulker) {
                        msg = getMsg("shulker");
                    } else if (livingEntity instanceof org.bukkit.entity.Silverfish) {
                        msg = getMsg("silverfish");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof Stray) {
                        if (Objects.requireNonNull(livingEntity.getEquipment()).getItemInMainHand().getType() == Material.BOW) {
                            msg = getMsg("stray-arrow");
                        } else {
                            msg = getMsg("stray-melee");
                        }
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.WitherSkeleton) {
                        msg = getMsg("witherskeleton");
                    } else if (livingEntity instanceof Skeleton) {
                        if ((Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.9")) && ((Skeleton)livingEntity).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
                            msg = getMsg("witherskeleton");
                        } else if (Objects.requireNonNull(livingEntity.getEquipment()).getItemInMainHand().getType() == Material.BOW) {
                            msg = getMsg("skeleton-arrow");
                        } else {
                            msg = getMsg("skeleton-melee");
                        }
                    } else if (livingEntity instanceof org.bukkit.entity.Slime) {
                        msg = getMsg("slime");
                    } else if (livingEntity instanceof org.bukkit.entity.Spider) {
                        msg = getMsg("spider");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Vex) {
                        msg = getMsg("vex");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Vindicator) {
                        msg = getMsg("vindicator");
                    } else if (livingEntity instanceof org.bukkit.entity.Witch) {
                        msg = getMsg("witch");
                    } else if (livingEntity instanceof org.bukkit.entity.Wither) {
                        if (lastCause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                            msg = getMsg("wither-explosion");
                        } else {
                            msg = getMsg("wither");
                        }
                    } else if (livingEntity instanceof org.bukkit.entity.Wolf) {
                        msg = getMsg("wolf");
                    } else if (livingEntity instanceof org.bukkit.entity.PigZombie) {
                        msg = getMsg("zombiepigman");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.ZombieVillager) {
                        msg = getMsg("zombievillager");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Husk) {
                        msg = getMsg("husk");
                    } else if (livingEntity instanceof org.bukkit.entity.Zombie) {
                        msg = getMsg("zombie");
                    } else if (livingEntity instanceof org.bukkit.entity.EnderCrystal) {
                        msg = getMsg("endercrystal");
                    } else if (lastCause.equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
                        msg = getMsg("anvil");
                    } else if (lastCause.equals(EntityDamageEvent.DamageCause.LIGHTNING)) {
                        msg = getMsg("lightning");
                    } else if (lastCause.equals(EntityDamageEvent.DamageCause.FALL)) {
                        msg = getMsg("enderpearl");
                    } else if (livingEntity.getName().equalsIgnoreCase("Area Effect Cloud")) {
                        msg = getMsg("enderdragon-breath");
                    } else if (lastCause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                        msg = getMsg("tnt");
                    } else {
                        msg = getMsg("unknown");
                    }
                } else {
                    String mobName = livingEntity.getCustomName();
                    if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && !Bukkit.getVersion().contains("1.14") && livingEntity instanceof org.bukkit.entity.Bee) {
                        msg = getMsg("named-bee");
                    } else if (livingEntity instanceof org.bukkit.entity.Blaze) {
                        if (lastCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                            msg = getMsg("named-blaze-melee");
                        } else {
                            msg = getMsg("named-blaze-fireball");
                        }
                    } else if (livingEntity instanceof org.bukkit.entity.CaveSpider) {
                        msg = getMsg("named-cavespider");
                    } else if (livingEntity instanceof Creeper) {
                        if (((Creeper)livingEntity).isPowered()) {
                            msg = getMsg("named-creeper-charged");
                        } else {
                            msg = getMsg("named-creeper");
                        }
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && livingEntity instanceof Drowned) {
                        if (Objects.requireNonNull(livingEntity.getEquipment()).getItemInMainHand().getType() == Material.TRIDENT) {
                            msg = getMsg("named-drowned-trident");
                        } else {
                            msg = getMsg("named-drowned-melee");
                        }
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.ElderGuardian) {
                        msg = getMsg("named-elderguardian");
                    } else if (livingEntity instanceof org.bukkit.entity.EnderDragon) {
                        msg = getMsg("named-enderdragon");
                    } else if (livingEntity instanceof org.bukkit.entity.Enderman) {
                        msg = getMsg("named-enderman");
                    } else if (livingEntity instanceof org.bukkit.entity.Endermite) {
                        msg = getMsg("named-endermite");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Evoker) {
                        msg = getMsg("named-evoker");
                    } else if (livingEntity instanceof org.bukkit.entity.Ghast) {
                        msg = getMsg("named-ghast");
                    } else if (livingEntity instanceof org.bukkit.entity.Giant) {
                        msg = getMsg("named-giant");
                    } else if (livingEntity instanceof org.bukkit.entity.Guardian) {
                        msg = getMsg("named-guardian");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && livingEntity instanceof org.bukkit.entity.Illusioner) {
                        msg = getMsg("named-illusioner");
                    } else if (livingEntity instanceof org.bukkit.entity.IronGolem) {
                        msg = getMsg("named-irongolem");
                    } else if (livingEntity instanceof org.bukkit.entity.MagmaCube) {
                        msg = getMsg("named-magmacube");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && livingEntity instanceof org.bukkit.entity.Panda) {
                        msg = getMsg("named-panda");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && livingEntity instanceof org.bukkit.entity.Phantom) {
                        msg = getMsg("named-phantom");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && livingEntity instanceof Pillager) {
                        if (Objects.requireNonNull(livingEntity.getEquipment()).getItemInMainHand().getType() == Material.CROSSBOW) {
                            msg = getMsg("named-pillager-crossbow");
                        } else {
                            msg = getMsg("named-pillager-melee");
                        }
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && livingEntity instanceof org.bukkit.entity.PolarBear) {
                        msg = getMsg("named-polarbear");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && livingEntity instanceof org.bukkit.entity.PufferFish) {
                        msg = getMsg("pufferfish");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12") && !Bukkit.getVersion().contains("1.13") && livingEntity instanceof org.bukkit.entity.Ravager) {
                        msg = getMsg("named-ravager");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && livingEntity instanceof org.bukkit.entity.Shulker) {
                        msg = getMsg("named-shulker");
                    } else if (livingEntity instanceof org.bukkit.entity.Silverfish) {
                        msg = getMsg("named-silverfish");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof Stray) {
                        if (Objects.requireNonNull(livingEntity.getEquipment()).getItemInMainHand().getType() == Material.BOW) {
                            msg = getMsg("named-stray-arrow");
                        } else {
                            msg = getMsg("named-stray-melee");
                        }
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.WitherSkeleton) {
                        msg = getMsg("named-witherskeleton");
                    } else if (livingEntity instanceof Skeleton) {
                        if ((Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")) && ((Skeleton)livingEntity).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
                            msg = getMsg("named-witherskeleton");
                        } else if (Objects.requireNonNull(livingEntity.getEquipment()).getItemInMainHand().getType() == Material.BOW) {
                            msg = getMsg("named-skeleton-arrow");
                        } else {
                            msg = getMsg("named-skeleton-melee");
                        }
                    } else if (livingEntity instanceof org.bukkit.entity.Slime) {
                        msg = getMsg("named-slime");
                    } else if (livingEntity instanceof org.bukkit.entity.Spider) {
                        msg = getMsg("named-spider");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Vex) {
                        msg = getMsg("named-vex");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Vindicator) {
                        msg = getMsg("named-vindicator");
                    } else if (livingEntity instanceof org.bukkit.entity.Witch) {
                        msg = getMsg("named-witch");
                    } else if (livingEntity instanceof org.bukkit.entity.Wither) {
                        if (lastCause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                            msg = getMsg("named-wither-explosion");
                        } else {
                            msg = getMsg("named-wither");
                        }
                    } else if (livingEntity instanceof org.bukkit.entity.Wolf) {
                        msg = getMsg("named-wolf");
                    } else if (livingEntity instanceof org.bukkit.entity.PigZombie) {
                        msg = getMsg("named-zombiepigman");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.ZombieVillager) {
                        msg = getMsg("named-zombievillager");
                    } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && livingEntity instanceof org.bukkit.entity.Husk) {
                        msg = getMsg("named-husk");
                    } else if (livingEntity instanceof org.bukkit.entity.Zombie) {
                        msg = getMsg("named-zombie");
                    } else {
                        msg = getMsg("unknown");
                    }
                    msg = msg.replaceAll("%mob%", mobName);
                }
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                msg = getMsg("bed-explosion");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.CONTACT)) {
                msg = getMsg("cactus");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
                msg = getMsg("anvil");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.DROWNING)) {
                msg = getMsg("drown");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.WITHER)) {
                msg = getMsg("wither-potion");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.MAGIC)) {
                msg = getMsg("harming");
            } else if (fireDeath) {
                msg = getMsg("burn");
            } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && lastCause.equals(EntityDamageEvent.DamageCause.HOT_FLOOR)) {
                msg = getMsg("magma");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.LAVA)) {
                msg = getMsg("lava");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.LIGHTNING)) {
                msg = getMsg("lightning");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.STARVATION)) {
                msg = getMsg("starvation");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                msg = getMsg("suffocation");
            } else if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && lastCause.equals(EntityDamageEvent.DamageCause.CRAMMING)) {
                msg = getMsg("cramming");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.VOID)) {
                msg = getMsg("void");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.FALL)) {
                msg = getMsg("fall");
            } else if (!Bukkit.getVersion().contains("1.8") && lastCause.equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) {
                msg = getMsg("fly-into-wall");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.SUICIDE)) {
                msg = getMsg("suicide");
            } else if (lastCause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                msg = getMsg("tnt");
            } else {
                msg = getMsg("unknown");
            }
        }
        e.setDeathMessage(CrankedCore.placeholderColor(msg.replaceAll("%player%", playerName), player));
    }
}
