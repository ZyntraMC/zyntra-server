package mc.zyntra.general.command;

import java.lang.reflect.Method;
import java.util.List;

public interface CommandFramework {

    default void registerCommands(final CommandClass command) {
        final Method[] methods = command.getClass().getMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(Command.class)) {
                final Class[] params = method.getParameterTypes();
                if (params.length != 1) {
                    throw new IllegalStateException("The method " + method.getName() + " in " + command.getClass().getName() + " cannot be registered: Invalid parameter count.");
                } else {
                    if (!params[0].equals(CommandContext.class)) {
                        throw new IllegalStateException("The method " + method.getName() + " in " + command.getClass().getName() + " cannot be registered: Invalid parameter type.");
                    }
                    registerCommand(method.getDeclaredAnnotation(Command.class), method, command);
                }
            } else if (method.isAnnotationPresent(Completer.class)) {
                final Class[] params = method.getParameterTypes();
                if (params.length != 1) {
                    throw new IllegalStateException("The method " + method.getName() + " in " + command.getClass().getName() + " cannot be registered: Invalid parameter count.");
                } else {
                    if (!params[0].equals(CommandContext.class)) {
                        throw new IllegalStateException("The method " + method.getName() + " in " + command.getClass().getName() + " cannot be registered: Invalid parameter type.");
                    }
                    if (method.getReturnType() != List.class) {
                        throw new IllegalStateException("The method " + method.getName() + " in " + command.getClass().getName() + " cannot be registered: Invalid return type.");
                    }
                    registerCompleter(method.getDeclaredAnnotation(Completer.class), method, command);
                }
            }
        }
    }

    String[] getCommandLabels();

    void registerCommand(Command command, Method method, CommandClass commandObject);

    void registerCompleter(Completer completer, Method method, CommandClass commandObject);

    void setInGameOnlyMessage(String msg);

}