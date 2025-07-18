package mc.zyntra.bukkit.api.scoreboard;

import com.google.common.base.Preconditions;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class ScoreboardHelper {

    private Objective objective;
    private final Scoreboard scoreboard;

    public ScoreboardHelper(Scoreboard scoreboard, DisplaySlot slot) {
        this.scoreboard = scoreboard;
        this.objective = scoreboard.registerNewObjective(slot.name().toLowerCase(), "dummy");
        this.objective.setDisplaySlot(slot);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setDisplayName(String name) {
        Preconditions.checkArgument(name != null, "Parameter 'name' cannot be null");
        objective.setDisplayName(name);
    }

    public final void destroy() {
        objective.unregister();
        objective = null;
    }
}
