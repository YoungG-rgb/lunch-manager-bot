package kg.tech.lunchmanagerbot.support.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.time.LocalDate;
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

}
