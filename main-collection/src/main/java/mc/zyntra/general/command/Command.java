package mc.zyntra.general.command;

import mc.zyntra.general.account.group.Group;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();

    Group group() default Group.DEFAULT;

    String permission() default "";

    String[] aliases() default {};

    String description() default "";

    String usage() default "";

    boolean inGameOnly() default false;

    boolean runAsync() default false;
}