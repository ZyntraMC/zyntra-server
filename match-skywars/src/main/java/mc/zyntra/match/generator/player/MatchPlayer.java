package mc.zyntra.match.generator.player;

import mc.zyntra.bukkit.api.scoreboard.Scoreboard;
import mc.zyntra.general.account.statistics.types.GameStatistics;
import mc.zyntra.room.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@RequiredArgsConstructor
public class MatchPlayer {
    private final UUID uniqueId;
    private transient Scoreboard scoreboard;
    private Room room;
    private GameStatistics statistics;
    private String kit = "Nenhum";
    private List<String> kits = new ArrayList<>();
    private String perk = "Nenhum";
    private List<String> perks = new ArrayList<>();
    private Integer kills = 0;
    private boolean protect = true;
}
