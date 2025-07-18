package mc.zyntra.match.scoreboard.constructor;

import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.bukkit.api.scoreboard.Scoreboard;
import mc.zyntra.bukkit.event.update.UpdateEvent;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.utils.string.WaveAnimation;
import mc.zyntra.util.Platform;
import mc.zyntra.Leading;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.room.service.type.RoomSubType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mc.zyntra.match.scoreboard.ScheduleScoreboard;
import mc.zyntra.room.Room;

import java.util.*;


public class ScoreboardConstructor implements ScheduleScoreboard, Listener {

    private static final HashMap<UUID, Scoreboard> scoreboardMap = new HashMap<>();
    private static WaveAnimation SB = new WaveAnimation("SKY WARS", "§6§l", "§e§l", "§f§l");
    private static String SCOREBOARD_TITLE;

    private List<MatchPlayer> playerList = new ArrayList<>();
    @Override
    public void handleScoreboard(Player player) {
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
        Scoreboard scoreboard = new Scoreboard("§6§lSKY WARS");
        Room room = Leading.getInstance().getRoom();
        switch (room.getStage()) {
            case ESPERANDO:
            case INICIANDO:
                scoreboard.add(11, "");
                scoreboard.add(10, "§fJogadores: §a" + room.getPlayers().size() + "/" + room.getMaxPlayers());
                scoreboard.add(9, "");
                scoreboard.add(8, "§fInicia em: §a" + timeUnit(room.getTime()));
                scoreboard.add(7, "");
                scoreboard.add(6, "§fKit: §7" + matchPlayer.getKit());
                scoreboard.add(5, "");
                scoreboard.add(4, "§fMapa: §a" + room.getMap());
                scoreboard.add(3, "§fModo: §7" + (room.getSubType() == RoomSubType.SOLO ? "Solo" : "Duplas"));
                scoreboard.add(2, "");
                scoreboard.add(1, "§e" + Constant.SERVER_ADDRESS);
                break;
            case EM_JOGO:
            case ENCERRANDO:
            case REINICIANDO:
                scoreboard.add(12, "");
                scoreboard.add(11, "§fTempo: §a" + time(room.getTime()));
                scoreboard.add(10, "");
                scoreboard.add(9, "§fRestantes: §a" + room.getPlayers().size());
                scoreboard.add(8, "");
                scoreboard.add(7, "§fKit: §7" + matchPlayer.getKit());
                scoreboard.add(6, "§fKills: §7" + matchPlayer.getKills());
                scoreboard.add(5, "");
                scoreboard.add(4, "§fMapa: §a" + room.getMap());
                scoreboard.add(3, "§fModo: §7" + (room.getSubType() == RoomSubType.SOLO ? "Solo" : "Duplas"));
                scoreboard.add(2, "");
                scoreboard.add(1, "§e" + Constant.SERVER_ADDRESS);
                break;
        }
        matchPlayer.setScoreboard(scoreboard);
        player.setScoreboard(scoreboard.getScoreboard());
        NametagController.getInstance().update(player);
    }

    @Override
    public void updateScoreboard() {
        for (MatchPlayer matchPlayer : Platform.getPlayerLoader().list()) {
            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(matchPlayer.getUniqueId());
            Scoreboard scoreboard = matchPlayer.getScoreboard();
            Room room = Leading.getInstance().getRoom();
            if (room != null) {
                switch (room.getStage()) {
                    case ESPERANDO:
                    case INICIANDO:
                        scoreboard.add(11, "");
                        scoreboard.add(10, "§fJogadores: §a" + room.getPlayers().size() + "/" + room.getMaxPlayers());
                        scoreboard.add(9, "");
                        scoreboard.add(8, "§fInicia em: §a" + timeUnit(room.getTime()));
                        scoreboard.add(7, "");
                        scoreboard.add(6, "§fKit: §7" + matchPlayer.getKit());
                        scoreboard.add(5, "");
                        scoreboard.add(4, "§fMapa: §a" + room.getMap());
                        scoreboard.add(3, "§fModo: §7" + (room.getSubType() == RoomSubType.SOLO ? "Solo" : "Duplas"));
                        scoreboard.add(2, "");
                        scoreboard.add(1, "§e" + Constant.SERVER_ADDRESS);
                        break;
                    case EM_JOGO:
                    case ENCERRANDO:
                    case REINICIANDO:
                        scoreboard.add(12, "");
                        scoreboard.add(11, "§fTempo: §a" + time(room.getTime()));
                        scoreboard.add(10, "");
                        scoreboard.add(9, "§fRestantes: §a" + room.getPlayers().size());
                        scoreboard.add(8, "");
                        scoreboard.add(7, "§fKit: §7" + matchPlayer.getKit());
                        scoreboard.add(6, "§fKills: §7" + matchPlayer.getKills());
                        scoreboard.add(5, "");
                        scoreboard.add(4, "§fMapa: §a" + room.getMap());
                        scoreboard.add(3, "§fModo: §7" + (room.getSubType() == RoomSubType.SOLO ? "Solo" : "Duplas"));
                        scoreboard.add(2, "");
                        scoreboard.add(1, "§e" + Constant.SERVER_ADDRESS);
                        break;
                }
            }
        }
    }

    private String time(int time) {
        return time / 60 + ":" + (time % 60 < 10 ? "0" : "") + time % 60;
    }

    private String timeUnit(int time) {
        if (time < 60) {
            return time + "s";
        } else {
            return (time / 60) + "m " + (time % 60 == 0 ? "" : time % 60 + "s");
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getCurrentTick() % 2 == 0) {
            updateScoreboard();
            Leading.getInstance().getRoom().getPlayers().forEach(players -> {
                for (Player spec : Leading.getInstance().getRoom().getSpectators()) {
                    players.hidePlayer(spec);
                }
            });
        }
    }
}

