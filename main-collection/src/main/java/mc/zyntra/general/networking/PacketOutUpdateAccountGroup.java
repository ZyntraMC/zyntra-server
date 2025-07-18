package mc.zyntra.general.networking;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.group.GroupData;
import mc.zyntra.general.utils.json.JsonBuilder;

import java.util.UUID;

public class PacketOutUpdateAccountGroup extends Packet {

    public PacketOutUpdateAccountGroup(UUID uniqueId, GroupData groupData) {
        super(new JsonBuilder()
                .addProperty("uniqueId", uniqueId.toString())
                .addProperty("groupData", Core.getGson().toJson(groupData))
                .build());
    }

    public UUID getUniqueId() {
        return UUID.fromString(getJsonObject().get("uniqueId").getAsString());
    }

    public GroupData getPrimaryGroupData() {
        return Core.getGson().fromJson(getJsonObject().get("groupData").getAsString(), GroupData.class);
    }
}
