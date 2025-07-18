package mc.zyntra.general.networking;

import mc.zyntra.general.Core;
import mc.zyntra.general.server.ServerConfiguration;
import mc.zyntra.general.utils.json.JsonBuilder;
import lombok.Getter;

@Getter
public class PacketServerInfo extends Packet {

    public PacketServerInfo(ServerConfiguration serverConfiguration, Action action) {
        super(new JsonBuilder()
                .addProperty("serverConfiguration", Core.getGson().toJson(serverConfiguration))
                .addProperty("action", action.name())
                .build());
    }

    public ServerConfiguration getServerConfiguration(Class<? extends ServerConfiguration> clazz) {
        return Core.getGson().fromJson(getJsonObject().get("serverConfiguration").getAsString(), clazz);
    }

    public Action getAction() {
        return Action.valueOf(getJsonObject().get("action").getAsString());
    }

    public enum Action {
        START, STOP, UPDATE
    }
}
