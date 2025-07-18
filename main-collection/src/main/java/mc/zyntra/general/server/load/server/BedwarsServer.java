package mc.zyntra.general.server.load.server;

import java.util.Set;
import java.util.UUID;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.server.load.MinigameServer;
import mc.zyntra.general.server.load.MinigameState;

public class BedwarsServer extends MinigameServer {
   public BedwarsServer(String serverId, ServerType type, Set<UUID> players, int maxPlayers, boolean joinEnabled) {
      super(serverId, type, players, maxPlayers, joinEnabled);
      this.setState(MinigameState.WAITING);
   }

   @Override
   public boolean canBeSelected() {
      return super.canBeSelected() && !this.isInProgress() && (this.getState() == MinigameState.WAITING || this.getState() == MinigameState.STARTING);
   }

   @Override
   public boolean isInProgress() {
      return this.getState() == MinigameState.PREGAME || this.getState() == MinigameState.GAMETIME || this.getState() == MinigameState.INVINCIBILITY;
   }
}
