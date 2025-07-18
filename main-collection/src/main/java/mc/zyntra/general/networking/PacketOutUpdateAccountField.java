package mc.zyntra.general.networking;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.utils.json.JsonBuilder;

import java.util.UUID;

public class PacketOutUpdateAccountField extends Packet {

    public PacketOutUpdateAccountField(UUID uniqueId, ZyntraPlayer zyntraPlayer, String fieldName) {
        super(new JsonBuilder()
                .addProperty("uniqueId", uniqueId.toString())
                .addProperty("zyntraPlayer", Core.getGson().toJson(zyntraPlayer))
                .addProperty("fieldName", fieldName)
                .build());
    }

    public UUID getUniqueId() {
        return UUID.fromString(getJsonObject().get("uniqueId").getAsString());
    }

    public ZyntraPlayer getZyntraPlayer() {
        return Core.getGson().fromJson(getJsonObject().get("zyntraPlayer").getAsString(), ZyntraPlayer.class);
    }

    public String getFieldName() {
        return getJsonObject().get("fieldName").getAsString();
    }
}
