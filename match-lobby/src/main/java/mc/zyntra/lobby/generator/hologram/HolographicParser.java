package mc.zyntra.lobby.generator.hologram;

import mc.zyntra.Main;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.hologram.Hologram;
import mc.zyntra.bukkit.api.hologram.HologramAPI;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class HolographicParser implements Listener {

    private final Hologram how_to_play;

    public HolographicParser() {
        Location location = Main.getInstance().getLocationFromConfig("hologram.play");
        how_to_play = new Hologram(location.clone().add(0, 0.5, 0), Arrays.asList(
                "§b'§b§lCOMO JOGAR?§b'",
                "§ePara iniciar sua jogatina,",
                "§ebasta clicar no NPC do",
                "§emodo de jogo desejado!"));
        BukkitMain.getInstance().getHologramManager().getGlobalHolograms().add(how_to_play);
        HologramAPI.spawnGlobal(how_to_play);
    }

}
