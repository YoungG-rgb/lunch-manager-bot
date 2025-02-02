package kg.tech.lunchmanagerbot.support.annotations;

import kg.tech.lunchmanagerbot.support.domain.MessageHandlerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypedMessageHandler {
    MessageHandlerType value();
}
