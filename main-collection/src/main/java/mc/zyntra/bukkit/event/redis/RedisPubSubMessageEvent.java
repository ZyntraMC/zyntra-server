package mc.zyntra.bukkit.event.redis;

import mc.zyntra.bukkit.event.CustomEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RedisPubSubMessageEvent extends CustomEvent {

    private final String channel;
    private final String message;


}
