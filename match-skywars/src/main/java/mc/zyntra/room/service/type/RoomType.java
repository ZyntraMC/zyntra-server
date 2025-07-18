package mc.zyntra.room.service.type;


public enum RoomType {

    NORMAL,
    INSANO,
    DUEL;

    public static RoomType getByName(String name) {
        for (RoomType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
