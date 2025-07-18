package mc.zyntra.general.data.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mc.zyntra.general.Core;
import mc.zyntra.general.clan.Clan;
import mc.zyntra.general.clan.member.ClanMember;
import mc.zyntra.general.clan.member.role.MemberRole;
import mc.zyntra.general.data.DataClan;
import org.bson.Document;

import java.util.*;
import java.util.function.Consumer;

public class DataClanImpl implements DataClan {

    private final MongoCollection<Document> clanCollection = Core.getMongoStorage().getDatabase().getCollection("clan");
    private final MongoCollection<Document> inviteCollection = Core.getMongoStorage().getDatabase().getCollection("clan_invites");

    @Override
    public Clan create(String clanName, UUID leaderUuid, String tag) {
        Clan clan = new Clan(UUID.randomUUID(), clanName, tag, new ClanMember(leaderUuid, MemberRole.LEADER));
        Document found = clanCollection.find(Filters.eq("clanName", clanName)).first();

        if (found == null) {
            Document doc = clanToDocument(clan);
            clanCollection.insertOne(doc);
        }
        return clan;
    }

    @Override
    public void save(Clan clan) {
        clanCollection.updateOne(Filters.eq("clanName", clan.getClanName()),
                new Document("$set", clanToDocument(clan)));
    }

    @Override
    public Clan load(String clanName) {
        Document found = clanCollection.find(Filters.eq("clanName", clanName)).first();
        return found == null ? null : documentToClan(found);
    }

    @Override
    public void remove(String clanName) {
        clanCollection.deleteOne(Filters.eq("clanName", clanName));
    }

    @Override
    public Collection<Clan> getClans() {
        List<Clan> clans = new ArrayList<>();
        clanCollection.find().forEach((Consumer<? super Document>) doc -> clans.add(documentToClan(doc)));
        return clans;
    }

    @Override
    public void saveInvite(UUID uuid, String clanName) {
        Document doc = new Document("uuid", uuid.toString()).append("clanName", clanName);
        inviteCollection.deleteOne(Filters.eq("uuid", uuid.toString()));
        inviteCollection.insertOne(doc);
    }

    @Override
    public String loadInvite(UUID uuid) {
        Document found = inviteCollection.find(Filters.eq("uuid", uuid.toString())).first();
        return found == null ? null : found.getString("clanName");
    }

    @Override
    public void removeInvite(UUID uuid) {
        inviteCollection.deleteOne(Filters.eq("uuid", uuid.toString()));
    }

    private Document clanToDocument(Clan clan) {
        Document doc = new Document();
        doc.put("uniqueId", clan.getUniqueId().toString());
        doc.put("clanName", clan.getClanName());
        doc.put("tag", clan.getTag());
        doc.put("type", clan.getType().name());
        doc.put("createdAt", clan.getCreatedAt());
        doc.put("aura", clan.getAura());

        UUID leaderUuid = clan.getMemberMap().values().stream()
                .filter(m -> m.getMemberRole() == MemberRole.LEADER)
                .map(ClanMember::getUuid)
                .findFirst().orElse(null);
        doc.put("leader", leaderUuid != null ? leaderUuid.toString() : null);

        List<String> members = new ArrayList<>();
        for (UUID uuid : clan.getMemberMap().keySet()) {
            members.add(uuid.toString());
        }
        doc.put("members", members);

        return doc;
    }

    private Clan documentToClan(Document doc) {
        UUID uniqueId = UUID.fromString(doc.getString("uniqueId"));
        String clanName = doc.getString("clanName");
        String tag = doc.getString("tag");
        Clan.ClanType type = Clan.ClanType.valueOf(doc.getString("type"));
        long createdAt = doc.getLong("createdAt");
        int aura = doc.getInteger("aura", 0);

        Clan clan = new Clan();
        clan.setUniqueId(uniqueId);
        clan.setClanName(clanName);
        clan.setTag(tag);
        clan.setType(type);
        clan.setCreatedAt(createdAt);
        clan.setAura(aura);

        Map<UUID, ClanMember> memberMap = new HashMap<>();
        List<String> members = (List<String>) doc.get("members");
        if (members != null) {
            for (String uuidStr : members) {
                UUID uuid = UUID.fromString(uuidStr);
                memberMap.put(uuid, new ClanMember(uuid, MemberRole.DEFAULT));
            }
        }

        String leaderStr = doc.getString("leader");
        if (leaderStr != null) {
            UUID leaderUuid = UUID.fromString(leaderStr);
            memberMap.put(leaderUuid, new ClanMember(leaderUuid, MemberRole.LEADER));
        }
        clan.setMemberMap(memberMap);

        return clan;
    }
}
