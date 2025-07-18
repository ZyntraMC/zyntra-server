package mc.zyntra.match;

import mc.zyntra.config.MatchType;
import mc.zyntra.config.sub.MatchSubType;
import mc.zyntra.general.Core;
import mc.zyntra.parser.MatchParser;
import mc.zyntra.stage.MatchStage;
import mc.zyntra.util.Platform;
import mc.zyntra.Leading;
import mc.zyntra.room.Room;
import mc.zyntra.room.service.type.RoomSubType;
import mc.zyntra.room.service.type.RoomType;
import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;


public class Match {

    @Getter
    private static final MatchParser matchParser = new MatchParser();

    public void load() {
        for (String name : Leading.getInstance().getConfig().getConfigurationSection("rooms").getKeys(false)) {
            Room r = new Room(name, Leading.getInstance().getConfig().getString("rooms." + name + ".map"), Leading.getInstance().getLocation().deserialize(Leading.getInstance().getConfig().getString("rooms." + name + ".lobby")));

            r.setType(RoomType.getByName(Leading.getInstance().getConfig().getString("rooms." + name + ".type")));
            r.setSubType(RoomSubType.getByName(Leading.getInstance().getConfig().getString("rooms." + name + ".subtype")));
            r.setTime(60);

            for (String s : Leading.getInstance().getConfig().getStringList("rooms." + name + ".spawns")) {
                r.addSpawn(Leading.getInstance().getLocation().deserialize(s));
            }
            r.setMaxPlayers(Leading.getInstance().getConfig().getInt("rooms." + name + ".max"));
            r.setMinPlayers(Leading.getInstance().getConfig().getInt("rooms." + name + ".min"));

            MatchSubType roomType = (r.getSubType() == RoomSubType.SOLO ? MatchSubType.SOLO : r.getSubType() == RoomSubType.DUPLA ? MatchSubType.DUPLA : MatchSubType.SOLO);
            mc.zyntra.Match gameInfo = new mc.zyntra.Match(Core.getServerName(), name, r.getMap(), MatchType.SKYWARS, roomType);
            gameInfo.setMaxPlayers(r.getSubType() == RoomSubType.SOLO ? 12 : 16);
            gameInfo.setPlayers(0);
            gameInfo.setStage(MatchStage.ESPERANDO);
            getMatchParser().load(gameInfo);
            r.start();
            Platform.getRoomLoader().load(r);
            Leading.getInstance().setRoom(r);
            Core.getDataServer().getLocalServer().setMap(Leading.getInstance().getRoom().getMap());
            Core.getLogger().info("Room loaded as " + name + "!");
        }
    }

    public void create(Room room) {
        List<String> spawns = new ArrayList<>();
        for (Location location : room.getSpawns()) {
            spawns.add(Leading.getInstance().getLocation().serialize(location));
        }
        Leading.getInstance().getConfig().set("rooms." + room.getName() + ".lobby", Leading.getInstance().getLocation().serialize(room.getLobby()));
        Leading.getInstance().getConfig().set("rooms." + room.getName() + ".type", room.getType().name());
        Leading.getInstance().getConfig().set("rooms." + room.getName() + ".subtype", room.getSubType().name());
        Leading.getInstance().getConfig().set("rooms." + room.getName() + ".map", room.getMap());
        Leading.getInstance().getConfig().set("rooms." + room.getName() + ".spawns", spawns);
        Leading.getInstance().getConfig().set("rooms." + room.getName() + ".max", room.getMaxPlayers());
        Leading.getInstance().getConfig().set("rooms." + room.getName() + ".min", room.getMinPlayers());
        Leading.getInstance().saveConfig();
        Platform.getRoomLoader().load(room);
    }

    public void delete(String name) {
        Leading.getInstance().getConfig().set("rooms." + name, null);
        Leading.getInstance().saveConfig();
    }
}
