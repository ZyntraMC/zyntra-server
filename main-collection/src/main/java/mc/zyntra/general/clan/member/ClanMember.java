package mc.zyntra.general.clan.member;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.general.clan.member.role.MemberRole;

import java.util.UUID;

@Getter
public class ClanMember {

    private UUID uuid;
    @Setter
    private MemberRole memberRole;
    private long joinDate;

    public ClanMember(UUID uuid, MemberRole memberRole) {
        this.uuid = uuid;
        this.memberRole = memberRole;
        this.joinDate = System.currentTimeMillis();
    }
}