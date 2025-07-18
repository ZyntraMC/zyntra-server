package mc.zyntra.bukkit.api.hologram;

import mc.zyntra.bukkit.BukkitMain;
import org.bukkit.entity.Player;

public class HologramAPI {

    public static void spawnGlobal(Hologram hologram) {
        BukkitMain.getInstance().getHologramManager().addGlobal(hologram);
    }

    public static Hologram spawn(Hologram hologram, Player... players) {
        for (Player player : players)
            BukkitMain.getInstance().getHologramManager().addPlayerHologram(player, hologram);

        return hologram;
    }

    public static void removeGlobal(Hologram hologram) {
        BukkitMain.getInstance().getHologramManager().getGlobalHolograms().remove(hologram);
    }

    public static void remove(Hologram hologram, Player... players) {
        for (Player player : players)
        BukkitMain.getInstance().getHologramManager().getPlayerHolograms().remove(player, hologram);
    }


}
