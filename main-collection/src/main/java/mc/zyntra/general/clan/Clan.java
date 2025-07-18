package mc.zyntra.general.clan;

import lombok.*;
import mc.zyntra.general.clan.aura.AuraLevel;
import mc.zyntra.general.clan.member.ClanMember;
import mc.zyntra.general.clan.member.role.MemberRole;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Clan {

    private UUID uniqueId;
    private int id;
    private String clanName;
    private String tag;
    private ClanType type = ClanType.CASUAL;
    private long createdAt = System.currentTimeMillis();
    private Map<UUID, ClanMember> memberMap = new HashMap<>();
    private int aura = 0;

    public Clan(UUID uniqueId, String clanName, String tag, ClanMember creator) {
        this.uniqueId = uniqueId;
        this.clanName = clanName;
        this.tag = tag;
        this.createdAt = System.currentTimeMillis();
        this.memberMap = new HashMap<>();
        this.aura = 0;

        addMember(creator);
    }

    public void addAura(int amount) {
        setAura(Math.max(0, this.aura + amount));
    }

    public void removeAura(int amount) {
        setAura(Math.max(0, this.aura - amount));
    }

    public AuraLevel getAuraLevel() {
        return AuraLevel.fromAura(this.aura);
    }

    public AuraLevel getNextAuraLevel() {
        return getAuraLevel().getNextLevel();
    }

    public void addMember(ClanMember member) {
        this.memberMap.put(member.getUuid(), member);
    }

    public void removeMember(UUID uuid) {
        this.memberMap.remove(uuid);
    }

    public boolean isMember(UUID uuid) {
        return this.memberMap.containsKey(uuid);
    }

    public ClanMember getMember(UUID uuid) {
        return this.memberMap.get(uuid);
    }

    public boolean isLeader(UUID uuid) {
        ClanMember member = getMember(uuid);
        return member != null && member.getMemberRole() == MemberRole.LEADER;
    }

    public int getSize() {
        return this.memberMap.size();
    }

    public enum ClanType {
        CASUAL,
        COMPETITIVE
    }
}