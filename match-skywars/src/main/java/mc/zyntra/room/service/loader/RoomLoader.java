package mc.zyntra.room.service.loader;

import mc.zyntra.room.Room;

import java.util.Collection;
import java.util.HashMap;


public class RoomLoader {
    private HashMap<String, Room> rooms = new HashMap<>();

    public void load(Room room) {
        rooms.put(room.getName(), room);
    }

    public void unload(String name) {
        rooms.remove(name);
    }

    public Room getRoom(String name) {
        return rooms.get(name);
    }

    public Collection<Room> getRooms() {
        return rooms.values();
    }
}
