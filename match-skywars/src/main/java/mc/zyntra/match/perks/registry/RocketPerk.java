package mc.zyntra.match.perks.registry;

import mc.zyntra.Leading;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.perks.Perks;
import mc.zyntra.util.Platform;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RocketPerk extends Perks {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public RocketPerk() {
        super("Foguete", 5000);

        setIcon(new ItemBuilder(Material.FIREWORK).setName("§aFoguete").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Saia voando rapidamente", "§7utilizando seu foguete e derrote", "§7seus oponentes rapidamente!", "", "§7Raridade: §9Raro", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));
        addItem(new ItemBuilder(Material.FIREWORK).setName("§cFoguete").build(), true);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player);
        if (!event.getAction().equals(Action.PHYSICAL) && player.getItemInHand().getType().equals(Material.FIREWORK)) {
            event.setCancelled(true);
            if (!hasAbility(matchPlayer, getName())) {
                player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 5.0F, 5.0F);
                player.sendMessage("§cVocê não pode utilizar esta perk!");
                return;
            }

            if (!cooldowns.containsKey(player.getUniqueId()) || System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) > 5000) {
                Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.setPower(1);
                firework.setPassenger(player);
                FireworkEffect.Type type = FireworkEffect.Type.BALL;
                Color c1 = Color.RED;
                Color c2 = Color.LIME;
                Color c3 = Color.SILVER;
                FireworkEffect effect= FireworkEffect.builder().flicker(new Random().nextBoolean()).withColor(c1).withColor(c2).withFade(c3).with(type).build();
                meta.addEffect(effect);
                firework.setFireworkMeta(meta);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 7*20, 9999));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        firework.detonate();
                    }
                }.runTaskLater(Leading.getInstance(), 4*20);
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }
    }
}
