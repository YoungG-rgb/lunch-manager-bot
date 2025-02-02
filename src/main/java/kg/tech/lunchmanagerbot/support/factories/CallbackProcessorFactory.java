package kg.tech.lunchmanagerbot.support.factories;

import kg.tech.lunchmanagerbot.commons.services.CallbackProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CallbackProcessorFactory {
    private final List<CallbackProcessor> callbackProcessors;

    public CallbackProcessorFactory(ApplicationContext applicationContext) {
        this.callbackProcessors = applicationContext.getBeansOfType(CallbackProcessor.class).values()
                .stream().peek(callbackProcessor -> log.info("Registered {}", callbackProcessor))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    public CallbackProcessor getCallbackProcessor(String callbackData) {
        if (callbackData == null) throw new IllegalArgumentException("CallbackData is required");

        return callbackProcessors.stream()
                .filter(callbackProcessor -> callbackProcessor.supports(callbackData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No processor found for callback: " + callbackData));
    }
}
