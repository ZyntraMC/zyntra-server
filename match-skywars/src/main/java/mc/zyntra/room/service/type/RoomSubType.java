package mc.zyntra.room.service.type;


public enum RoomSubType {
    SOLO,
    DUPLA;

    public static RoomSubType getByName(String name) {
        for (RoomSubType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
