package mc.zyntra.general.account.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mc.zyntra.general.account.tag.enums.Tag;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
public class PlayerTagHandler {

    private Tag selectedTag = Tag.MEMBRO;
    private final Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        if (!tag.isExclusive()) return;
        tags.add(tag);
    }

    public boolean removeTag(Tag tag) {
        if (tag == selectedTag) {
            selectedTag = Tag.MEMBRO;
        }
        return tags.remove(tag);
    }

    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    public boolean setSelectedTag(Tag tag) {
        this.selectedTag = tag;
        return true;
    }
}
