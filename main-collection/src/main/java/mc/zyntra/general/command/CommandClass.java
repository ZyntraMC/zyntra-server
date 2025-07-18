package mc.zyntra.general.command;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.group.Group;

/**
 * Marker interface
 */
public interface CommandClass {

    default void broadcast(String message) {
        Core.getAccountController().broadcast(message);
    }

    default void broadcast(Group group, String message) {
        Core.getAccountController().broadcast(group, message);
    }

    default void notify(String message) {
        Core.getAccountController().notify(message);
    }

    default void notify(Group group, String message) {
        Core.getAccountController().notify(group, message);
    }
}