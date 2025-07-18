package mc.zyntra.general.controller;

import mc.zyntra.general.Core;
import mc.zyntra.general.clan.Clan;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClanController {

    private final Map<String, Clan> clanByName = new ConcurrentHashMap<>();
    private final Map<String, Clan> clanByTag = new ConcurrentHashMap<>();
    private final Map<UUID, String> pendingInvites = new ConcurrentHashMap<>();

    public ClanController() {
        loadAllClans();
    }

    public void loadAllClans() {
        for (Clan clan : Core.getDataClan().getClans()) {
            clanByName.put(clan.getClanName().toLowerCase(), clan);
            clanByTag.put(clan.getTag().toLowerCase(), clan);
        }
    }

    public Clan createClan(String name, UUID leaderUuid, String tag) {
        Clan clan = Core.getDataClan().create(name, leaderUuid, tag);
        clanByName.put(name.toLowerCase(), clan);
        clanByTag.put(tag.toLowerCase(), clan);
        return clan;
    }

    public void deleteClan(Clan clan) {
        clanByName.remove(clan.getClanName().toLowerCase());
        clanByTag.remove(clan.getTag().toLowerCase());
        Core.getDataClan().remove(clan.getClanName());
    }

    public Clan getClanByName(String name) {
        return clanByName.get(name.toLowerCase());
    }

    public Clan getClanByTag(String tag) {
        return clanByTag.get(tag.toLowerCase());
    }

    public Collection<Clan> getAllClans() {
        return clanByName.values();
    }

    public void saveClan(Clan clan) {
        Core.getDataClan().save(clan);
    }

    public void invitePlayer(UUID targetUuid, Clan clan) {
        pendingInvites.put(targetUuid, clan.getClanName());
        Core.getDataClan().saveInvite(targetUuid, clan.getClanName());
    }

    public boolean hasInvite(UUID targetUuid) {
        if (pendingInvites.containsKey(targetUuid)) return true;
        String stored = Core.getDataClan().loadInvite(targetUuid);
        if (stored != null) {
            pendingInvites.put(targetUuid, stored);
            return true;
        }
        return false;
    }

    public String getInvite(UUID targetUuid) {
        return pendingInvites.get(targetUuid);
    }

    public void removeInvite(UUID targetUuid) {
        pendingInvites.remove(targetUuid);
        Core.getDataClan().removeInvite(targetUuid);
    }
}
