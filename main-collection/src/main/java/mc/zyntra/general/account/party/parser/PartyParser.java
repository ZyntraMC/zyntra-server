package mc.zyntra.general.account.party.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PartyParser {
    private UUID owner;
    private List<UUID> players;
    private int limitForRank;
    private long timeToDissolve;
}
