package mc.zyntra.bukkit.api.vanish;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.bukkit.event.vanish.PlayerVanishEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager implements Listener {

    private static final Set<UUID> vanishers = new HashSet<>();
    private static final HashMap<UUID, ItemStack[]> inventory = new HashMap<>();
    private static final HashMap<UUID, ItemStack[]> armor = new HashMap<>();
    private static final HashMap<UUID, Double> health = new HashMap<>();

    public static void vanish(ZyntraPlayer zyntraPlayer) {
        Player player = zyntraPlayer.toPlayer();

        Group group = zyntraPlayer.getPrimaryGroupData().getGroup();


        if (hasVanish(player)) {
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().setContents(inventory.get(player.getUniqueId()));
            player.getInventory().setArmorContents(armor.get(player.getUniqueId()));
            player.setHealth(health.get(player.getUniqueId()));

            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(player));

            vanishers.remove(player.getUniqueId());
            inventory.remove(player.getUniqueId());
            armor.remove(player.getUniqueId());
            health.remove(player.getUniqueId());

            player.sendMessage("§dVocê saiu do modo Vanish!");
            player.sendMessage("§dAgora você está visível para todos os jogadores.");
        } else {
            vanishers.add(player.getUniqueId());
            inventory.put(player.getUniqueId(), player.getInventory().getContents());
            armor.put(player.getUniqueId(), player.getInventory().getArmorContents());
            health.put(player.getUniqueId(), player.getHealth());

            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            Core.getAccountController().list().stream()
                    .filter(onlinePlayer -> !onlinePlayer.hasGroupPermission(group))
                    .forEach(onlinePlayer -> {
                        onlinePlayer.toPlayer().hidePlayer(player);
                    });

            player.sendMessage("§dVocê entrou no modo Vanish!");
            player.sendMessage("§dAgora você está visível somente para " + group.getName() + " e acima.");
        }

        Bukkit.getPluginManager().callEvent(new PlayerVanishEvent(zyntraPlayer));
    }

    public static boolean hasVanish(Player player) {
        return vanishers.contains(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void quit(PlayerQuitEvent event) {
        vanishers.remove(event.getPlayer().getUniqueId());
        inventory.remove(event.getPlayer().getUniqueId());
        armor.remove(event.getPlayer().getUniqueId());
        health.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void openInventory(PlayerInteractEntityEvent event) {
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(event.getPlayer().getUniqueId());
        if (zyntraPlayer == null) return;

        Entity entity = event.getRightClicked();
        if ((!(entity instanceof Player))) return;

        Player bukkitPlayer = (Player) entity;

        if (hasVanish(event.getPlayer())) {
            event.getPlayer().openInventory(bukkitPlayer.getInventory());
        }
    }
}