package mc.zyntra.bukkit.api.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

public class Scoreboard extends ScoreboardHelper {

    private int index = 15;

    public Scoreboard(String title) {
        super(Bukkit.getScoreboardManager().getNewScoreboard(), DisplaySlot.SIDEBAR);
        setDisplayName(title);
    }

    public void add(String text) {
        add(index--, text);
    }

    public void add(int index, String text) {
        Team team = getScoreboard().getTeam("score-" + index);
        String prefix = "", suffix = "";
        if (team == null) {
            team = getScoreboard().registerNewTeam("score-" + index);
            String score = ChatColor.values()[index - 1].toString();
            getObjective().getScore(score).setScore(index);
            if (!team.hasEntry(score)) {
                team.addEntry(score);
            }
        }
        if (text.length() <= 16) {
            prefix = text;
            for (int i = prefix.length(); i > 0; i--) {
                if (prefix.substring(0, i).endsWith("§"))
                    prefix = prefix.substring(0, (i - 1));
                else
                    break;
            }
        } else {
            prefix = text.substring(0, 16);
            for (int i = prefix.length(); i > 0; i--) {
                if (prefix.substring(0, i).endsWith("§"))
                    prefix = prefix.substring(0, (i - 1));
                else
                    break;
            }
            suffix = ChatColor.getLastColors(prefix) + text.substring(16);
            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }
        }
        team.setPrefix(prefix);
        team.setSuffix(suffix);
    }
}
