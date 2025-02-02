package kg.tech.lunchmanagerbot.support.factories;


import kg.tech.lunchmanagerbot.support.BaseMessageHandler;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.support.domain.MessageHandlerType;
import kg.tech.lunchmanagerbot.support.domain.MessageHandlerTypeGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MessageHandlersFactory {
    private final EnumMap<MessageHandlerType, BaseMessageHandler> messageHandlersMap;
    private final EnumMap<MessageHandlerTypeGroup, List<BaseMessageHandler>> messageHandlersMapByGroup;

    public MessageHandlersFactory(ApplicationContext applicationContext) {
        this.messageHandlersMap = new EnumMap<>(MessageHandlerType.class);
        this.messageHandlersMapByGroup = new EnumMap<>(MessageHandlerTypeGroup.class);

        Map<String, BaseMessageHandler> beansOfType = applicationContext.getBeansOfType(BaseMessageHandler.class);
        for (BaseMessageHandler messageHandler: beansOfType.values()) {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(messageHandler);
            MessageHandlerType messageHandlerType = targetClass.getAnnotation(TypedMessageHandler.class).value();

            if (!messageHandlersMap.containsKey(messageHandlerType)) {
                messageHandlersMap.put(messageHandlerType, messageHandler);
                log.info("Registered new MessageHandler {}", messageHandlerType);
            } else {
                throw new IllegalStateException(String.format("MessageHandler %s is already registered", messageHandler.getClass().getName()));
            }
        }

        for (MessageHandlerTypeGroup group : MessageHandlerTypeGroup.values( )) {
            List<BaseMessageHandler> messageHandlers = group.getMessageHandlers( ).stream( ).map(messageHandlersMap::get).toList( );
            messageHandlersMapByGroup.put(group, messageHandlers);
            log.info("Registered new MessageHandlerGroup {}", group);
        }
    }

    public List<BaseMessageHandler> getAllMessageHandlersByGroup(MessageHandlerTypeGroup messageHandlerTypeGroup) {
        List<BaseMessageHandler> messageHandlers = messageHandlersMapByGroup.get(messageHandlerTypeGroup);
        if (messageHandlers.stream().allMatch(Objects::isNull)) return Collections.emptyList();

        return messageHandlers;
    }
}