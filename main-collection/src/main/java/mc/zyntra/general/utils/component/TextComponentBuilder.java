package mc.zyntra.general.utils.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@Getter
@RequiredArgsConstructor
public class TextComponentBuilder {

    private final String text;

    private HoverEvent hoverEvent;
    private ClickEvent clickEvent;

    public TextComponentBuilder setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        return this;
    }

    public TextComponentBuilder setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public TextComponent build() {
        TextComponent component = new TextComponent(text);
        component.setHoverEvent(hoverEvent);
        component.setClickEvent(clickEvent);
        return component;
    }
}
