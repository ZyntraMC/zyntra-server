package mc.zyntra.general.account.party;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.general.account.party.parser.PartyParser;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Party {

    private UUID uniqueId;
    private PartyParser parser;

    public Party(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.parser = null;
    }

    public void create(List<UUID> players, int limitForRank, long timeToDissolve) {
        if (parser != null) return;
        this.parser = new PartyParser(uniqueId, players, limitForRank, timeToDissolve);
    }

    public void dissolve() {
        if (parser == null) return;
        this.parser = null;
    }

    public void transfer(UUID newOwner) {
        if (parser != null && parser.getOwner().equals(uniqueId)) {
            parser.setOwner(newOwner);
            parser.getPlayers().add(uniqueId);
        }
    }
}
