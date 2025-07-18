package mc.zyntra.bukkit.event.server;

import mc.zyntra.bukkit.event.CustomEvent;
import mc.zyntra.general.server.ServerConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerUpdateEvent extends CustomEvent {

	private final String serverName;
	private final ServerConfiguration serverConfiguration;

}
