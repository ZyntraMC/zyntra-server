package mc.zyntra.general.account.fake;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.general.account.tag.enums.Tag;

@Getter @Setter
public class Fake {

    private String name;
    private Tag tag;

    public Fake(String name, Tag tag) {
        this.name = name;
        this.tag = tag;
    }
}
