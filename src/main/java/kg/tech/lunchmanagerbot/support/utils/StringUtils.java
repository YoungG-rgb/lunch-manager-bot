package kg.tech.lunchmanagerbot.support.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtils {

    public static boolean isEmpty(final CharSequence charSequence) {
        return charSequence == null || charSequence.isEmpty();
    }

    public static String joinNonEmptyStrings(CharSequence separator, String... stringsToJoin) {
        return Arrays.stream(stringsToJoin).filter(s -> !isEmpty(s))
                .collect(Collectors.joining(separator));
    }


}
