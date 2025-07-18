package mc.zyntra.general.networking;

import java.util.ArrayList;
import java.util.List;

public enum Payload {

    SERVER_INFO,

    UPDATE_ACCOUNT_GROUP_ADD,
    UPDATE_ACCOUNT_GROUP_REMOVE,
    UPDATE_ACCOUNT_CONFIG,
    UPDATE_ACCOUNT_FIELD;

    public static String[] toChannelsArray() {
        List<String> list = new ArrayList<>();

        for (Payload type : values())
            list.add(type.toString());
        return list.toArray(new String[] {});
    }
}
