package mc.zyntra.general.account.pass;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BattlePass {

    private UUID uniqueId;
    private Integer exp, levelPass;

    public BattlePass(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.exp = 0;
        this.levelPass = 0;
    }

    public void addPass() {
        this.levelPass += 1;
    }

    public void addExp(int exp) {
        this.exp += exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
