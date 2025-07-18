package mc.zyntra;

import org.bukkit.entity.Player;

public interface General {

    void handleInventory(Player player);

    /**
     * Set the lobby scoreboard for the player
     */
    void handleScoreboard(Player player);

    /**
     * Update lobby scoreboard for everyone
     */
    void updateScoreboard();

}
