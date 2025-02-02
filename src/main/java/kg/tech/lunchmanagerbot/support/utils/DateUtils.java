package kg.tech.lunchmanagerbot.support.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class DateUtils {

    public static LocalDate getDateOrToday(Update update) {
        Integer date = null;

        if (update.hasMessage()) date = update.getMessage( ).getDate( );
        if (update.hasCallbackQuery()) date = update.getCallbackQuery( ).getMessage( ).getDate( );

        return Optional.ofNullable(date)
                .map(seconds -> Instant.ofEpochSecond(seconds).atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                .orElseGet(LocalDate::now);
    }

    public static LocalDateTime durationToDate(String timeDurationConfig) {
        if (timeDurationConfig == null) return LocalDateTime.now();
        Duration duration = Duration.parse(timeDurationConfig);
        return LocalDateTime.now().plus(duration);
    }

    public static List<Duration> parseSemicolonSeparatedDurations(String durationsString) {
        if (StringUtils.isEmpty(durationsString)) return new ArrayList<>();

        List<Duration> parsedDurations = new ArrayList<>();

        Arrays.asList(durationsString.split(",")).forEach(duration -> {
            if (StringUtils.isEmpty(duration)) return;
            parsedDurations.add(Duration.parse(duration.trim( )));
        });

        return parsedDurations;
    }

}
