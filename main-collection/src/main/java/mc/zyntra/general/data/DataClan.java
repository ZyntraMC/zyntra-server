package mc.zyntra.general.data;

import mc.zyntra.general.clan.Clan;

import java.util.Collection;
import java.util.UUID;

public interface DataClan {

    Clan create(String clanName, UUID leaderUuid, String tag);

    void save(Clan clan);

    Clan load(String clanName);

    void remove(String clanName);

    Collection<Clan> getClans();

    void saveInvite(UUID uuid, String clanName);

    String loadInvite(UUID uuid);

    void removeInvite(UUID uuid);

}