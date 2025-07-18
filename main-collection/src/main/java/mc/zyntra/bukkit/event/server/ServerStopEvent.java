package mc.zyntra.bukkit.event.server;

import mc.zyntra.bukkit.event.CustomEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerStopEvent extends CustomEvent {

	private final String serverName;

}
