package mc.zyntra.collectibles.parser.particles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mc.zyntra.cMain;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.collectibles.parser.CollectiblePlayer;
import mc.zyntra.collectibles.parser.hats.Hats;
import mc.zyntra.general.account.group.Group;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


@Getter
@AllArgsConstructor
public enum Particles {

    HEART("Corações", EnumParticle.HEART, Group.GOLD, new ItemBuilder(Material.APPLE).setName("§aCorações").build()),
    FIREWORK("Foguetes", EnumParticle.FIREWORKS_SPARK, Group.DIAMOND, new ItemBuilder(Material.FIREWORK).setName("§aFoguetes").build()),
    FIRE("Fogo", EnumParticle.FLAME, Group.DIAMOND, new ItemBuilder(Material.BLAZE_POWDER).setName("§aFogo").build());

    private String display;
    private EnumParticle particle;
    private Group permission;
    private ItemStack item;

    public static Particles getParticleByDisplay(String display) {
        for (Particles p : Particles.values())
            if (p.getDisplay().equalsIgnoreCase(display))
                return p;
        return null;
    }

    public static void play(Player player) {
        CollectiblePlayer p = cMain.getPlayerLoader().get(player.getUniqueId());
        p.setAlpha(p.getAlpha() + Math.PI / 16);
        double alpha = p.getAlpha();

        Location location = player.getLocation();
        Location firstLocation = location.clone().add(Math.cos(alpha), Math.sin(alpha) + 1, Math.sin(alpha));
        Location secondLocation = location.clone().add(Math.cos(alpha + Math.PI), Math.sin(alpha) + 1, Math.sin(alpha + Math.PI));

        PacketPlayOutWorldParticles packetPlayOutWorldParticles = new PacketPlayOutWorldParticles(p.getParticle().getParticle(), true, (float) firstLocation.getX(), (float) firstLocation.getY(), (float) firstLocation.getZ(), 0, 0, 0, 0, 1);
        PacketPlayOutWorldParticles packetPlayOutWorldParticles2 = new PacketPlayOutWorldParticles(p.getParticle().getParticle(), true, (float) secondLocation.getX(), (float) secondLocation.getY(), (float) secondLocation.getZ(), 0, 0, 0, 0, 1);

        for (Player players : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) players).getHandle().playerConnection.sendPacket(packetPlayOutWorldParticles);
            ((CraftPlayer) players).getHandle().playerConnection.sendPacket(packetPlayOutWorldParticles2);
        }
    }

    public static int getHatsNumber(Group playerGroup) {
        int count = 0;
        for (Hats hat : Hats.values()) {
            if (hat.getPermission() == playerGroup) {
                count++;
            }
        }
        return count;
    }

    public static int getTotalHats() {
        return Hats.values().length;
    }

    public static double getPercentageComplete(Group playerGroup) {
        int totalHatsCount = Hats.values().length;
        int playerHatsCount = getHatsNumber(playerGroup);

        if (totalHatsCount == 0) {
            return 0.0;
        }

        double percentageComplete = (double) playerHatsCount / totalHatsCount * 100.0;
        return percentageComplete;
    }
}
