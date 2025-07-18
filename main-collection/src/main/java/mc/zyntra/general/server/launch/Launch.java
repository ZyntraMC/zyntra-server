package mc.zyntra.general.server.launch;

import lombok.AllArgsConstructor;
import mc.zyntra.general.server.launch.constructor.LaunchConst;

@AllArgsConstructor
public class Launch {
    private String name;
    private LaunchConst serverToLaunch;
    private boolean alwaysLaunched;
    private long timeToLaunch;
}
