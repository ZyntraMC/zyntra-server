package mc.zyntra;

import mc.zyntra.stage.MatchStage;
import mc.zyntra.config.MatchType;
import mc.zyntra.config.sub.MatchSubType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Match {
    private String name;
    private String display;
    private String map;

    private MatchType game;
    private MatchSubType type;

    private MatchStage stage;

    private Integer maxPlayers;
    private Integer players;

    public Match(String name, String display, String map, MatchType game, MatchSubType type) {
        this.name = name;
        this.display = map;
        this.game = game;
        this.type = type;
    }
}
