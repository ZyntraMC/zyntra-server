package mc.zyntra.general.networking;

import mc.zyntra.general.Core;
import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public class Packet {

    private final JsonObject jsonObject;

    public Packet(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.jsonObject.addProperty("source", Core.getServerName());
    }

    public String getSource() {
        return jsonObject.get("source").getAsString();
    }
}
