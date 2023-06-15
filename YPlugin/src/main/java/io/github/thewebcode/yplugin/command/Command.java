package io.github.thewebcode.yplugin.command;

import org.bukkit.command.CommandSender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String description() default "";

    String identifier();

    boolean onlyPlayers() default true;

    String[] permissions() default {};
}
