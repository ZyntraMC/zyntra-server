package mc.zyntra.general.account.medal;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.general.account.medal.enums.Medal;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PlayerMedalHandler {

    private final Set<Medal> medals = new HashSet<>();
    private Medal selectedMedal = Medal.NONE;

    public void addMedal(Medal medal) {
        medals.add(medal);
    }

    public boolean removeMedal(Medal medal) {
        if (medal == selectedMedal) {
            selectedMedal = null;
        }
        return medals.remove(medal);
    }

    public boolean hasMedal(Medal medal) {
        return medals.contains(medal);
    }

    public boolean setSelectedMedal(Medal medal) {
        if (!medals.contains(medal)) return false;
        this.selectedMedal = medal;
        return true;
    }
}
