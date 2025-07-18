package mc.zyntra.general.data;

import mc.zyntra.general.account.ZyntraPlayer;

import java.util.Collection;
import java.util.UUID;

public interface DataPlayer {

    ZyntraPlayer create(UUID uniqueId, String name);

    void update(ZyntraPlayer zyntraPlayer, String fieldName);

    void save(ZyntraPlayer zyntraPlayer);

    boolean exists(UUID uniqueId);

    boolean exists(String name);

    ZyntraPlayer load(UUID uniqueId);

    ZyntraPlayer load(String name);

    Collection<ZyntraPlayer> getPlayers();

    Collection<ZyntraPlayer> ranking(String fieldName);

}