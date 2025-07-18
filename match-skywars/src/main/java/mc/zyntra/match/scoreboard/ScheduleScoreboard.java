package mc.zyntra.match.scoreboard;

import org.bukkit.entity.Player;


public interface ScheduleScoreboard {

    /**
     * Set the lobby scoreboard for the player
     */
    void handleScoreboard(Player player);

    /**
     * Update lobby scoreboard for everyone
     */
    void updateScoreboard();

}
