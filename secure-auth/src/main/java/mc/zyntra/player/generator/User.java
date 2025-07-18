package mc.zyntra.player.generator;

import mc.zyntra.general.Constant;
import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.bukkit.api.scoreboard.Scoreboard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
public class User {

    private final UUID uniqueId;

    private boolean completedCaptcha = false, buildEnabled = false;
    private int timeLeftCaptcha = 30, timeLeftLogin = 60;
    private int remainingAttemptsCaptcha = 3, remainingAttemptsLogin = 3;

    private Scoreboard scoreboard;

    private Player player;

    public void initializeScoreboard() {
        scoreboard = new Scoreboard("§4§lAUTENTICAÇÃO");
        scoreboard.add(4, "");
        scoreboard.add(3, "§b§l ➜ §fEm aguardo...");
        scoreboard.add(2, "");
        scoreboard.add(1, "§eplay." + Constant.SERVER_ADDRESS);
        player.setScoreboard(scoreboard.getScoreboard());
        NametagController.getInstance().update(player);
    }
}
