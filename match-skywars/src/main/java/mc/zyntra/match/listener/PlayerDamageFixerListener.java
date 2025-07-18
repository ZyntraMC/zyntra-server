package mc.zyntra.match.listener;

import mc.zyntra.Leading;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PlayerDamageFixerListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;

        Player attacker = (Player) e.getDamager();
        Player target = (Player) e.getEntity();

        ItemStack weapon = attacker.getItemInHand();
        Material weaponType = (weapon != null) ? weapon.getType() : Material.AIR;

        double baseDamage = getBaseWeaponDamage(weaponType);
        baseDamage += getSharpnessBonus(weapon);
        baseDamage = applyStrengthPotion(attacker, baseDamage);

        if (isCriticalHit(attacker)) {
            baseDamage *= 1.5;
        }

        double defenseReduction = calculateArmorReduction(target);
        double enchantmentReduction = calculateEnchantmentReduction(target);

        double finalDamage = baseDamage * (1 - defenseReduction) * (1 - enchantmentReduction);

        finalDamage = applyThorns(target, attacker, finalDamage);

        // Pega multiplicador da config
        double multiplier = Leading.getInstance().getConfig().getDouble("pvp.damage-multiplier", 1.0);
        finalDamage *= multiplier;

        e.setDamage(Math.max(0.5, finalDamage));
    }

    private double getBaseWeaponDamage(Material material) {
        switch (material) {
            case WOOD_SWORD: return 4.0;
            case STONE_SWORD: return 5.0;
            case GOLD_SWORD: return 4.0;
            case IRON_SWORD: return 6.0;
            case DIAMOND_SWORD: return 7.0;
            default: return 1.0;
        }
    }

    private double getSharpnessBonus(ItemStack weapon) {
        if (weapon == null) return 0.0;
        int level = weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        if (level > 0) {
            return 1.0 + (level * 0.5) + (Math.max(0, level - 1) * 0.25);
        }
        return 0.0;
    }

    private double applyStrengthPotion(Player player, double damage) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                int amplifier = effect.getAmplifier() + 1;
                damage += amplifier * 3;
                break;
            }
        }
        return damage;
    }

    private boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !player.isOnGround() && !player.isSprinting() && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }

    private double calculateArmorReduction(Player target) {
        double armorPoints = 0.0;
        for (ItemStack armorPiece : target.getInventory().getArmorContents()) {
            if (armorPiece != null) {
                armorPoints += getArmorPoints(armorPiece.getType());
            }
        }
        return Math.min(0.8, armorPoints * 0.04);
    }

    private double getArmorPoints(Material material) {
        switch (material) {
            case LEATHER_HELMET:
            case LEATHER_BOOTS: return 1;
            case LEATHER_LEGGINGS: return 2;
            case LEATHER_CHESTPLATE: return 3;
            case CHAINMAIL_HELMET:
            case GOLD_HELMET:
            case CHAINMAIL_BOOTS:
            case GOLD_BOOTS: return 1;
            case CHAINMAIL_LEGGINGS:
            case GOLD_LEGGINGS: return 4;
            case CHAINMAIL_CHESTPLATE:
            case GOLD_CHESTPLATE: return 5;
            case IRON_HELMET: return 2;
            case IRON_BOOTS: return 2;
            case IRON_LEGGINGS: return 5;
            case IRON_CHESTPLATE: return 6;
            case DIAMOND_HELMET: return 3;
            case DIAMOND_BOOTS: return 3;
            case DIAMOND_LEGGINGS: return 6;
            case DIAMOND_CHESTPLATE: return 8;
            default: return 0;
        }
    }

    private double calculateEnchantmentReduction(Player target) {
        int protLevel = 0;
        for (ItemStack armorPiece : target.getInventory().getArmorContents()) {
            if (armorPiece != null) {
                protLevel += armorPiece.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
        }
        return Math.min(0.8, protLevel * 0.04);
    }

    private double applyThorns(Player target, Player attacker, double damage) {
        int thornsLevel = 0;
        for (ItemStack armorPiece : target.getInventory().getArmorContents()) {
            if (armorPiece != null) {
                thornsLevel += armorPiece.getEnchantmentLevel(Enchantment.THORNS);
            }
        }
        if (thornsLevel > 0) {
            Random random = new Random();
            if (random.nextInt(100) < thornsLevel * 15) {
                attacker.damage(1.0 + random.nextDouble() * thornsLevel);
            }
        }
        return damage;
    }
}
