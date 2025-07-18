package mc.zyntra.room.creator.controller;

import mc.zyntra.room.creator.RoomCreator;

import java.util.HashMap;
import java.util.UUID;


public class RoomCreatorController {
    private HashMap<UUID, RoomCreator> creator = new HashMap<>();

    public void load(RoomCreator roomCreator) {
        creator.put(roomCreator.getPlayer().getUniqueId(), roomCreator);
    }

    public void unload(UUID uniqueId) {
        creator.remove(uniqueId);
    }

    public RoomCreator getRoomCreator(UUID uniqueId) {
        return creator.get(uniqueId);
    }
}
