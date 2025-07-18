package mc.zyntra.parser;

import mc.zyntra.Match;
import mc.zyntra.config.MatchType;
import mc.zyntra.config.sub.MatchSubType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchParser {
    private HashMap<String, Match> games = new HashMap<>();

    public void load(Match match) {
        games.put(match.getName(), match);
    }

    public void unload(String name) {
        games.remove(name);
    }

    public Match getGameInfo(String name) {
        return games.get(name);
    }

    public boolean exists(Match match) {
        return games.containsKey(match.getName());
    }

    public void clear() {
        games.clear();
    }

    public void replace(Match match) {
        games.replace(match.getName(), match);
    }

    public List<Match> getGameInfos() {
        List<Match> infos = new ArrayList<>();
        for (Match g : games.values()) {
            infos.add(g);
        }
        return infos;
    }

    public List<Match> getGameInfos(MatchType type) {
        List<Match> infos = new ArrayList<>();
        for (Match g : games.values()) {
            if (g.getGame().equals(type)) {
                infos.add(g);
            }
        }
        return infos;
    }

    public List<Match> getGameInfos(MatchSubType type) {
        List<Match> infos = new ArrayList<>();
        for (Match g : games.values()) {
            if (g.getType().equals(type)) {
                infos.add(g);
            }
        }
        return infos;
    }
}
