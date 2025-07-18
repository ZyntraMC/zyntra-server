package mc.zyntra.general.account.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupData {

    private Group group;
    private GroupAttribute attribute;
    private long date;
    private long duration;
    private String addedBy;

}
