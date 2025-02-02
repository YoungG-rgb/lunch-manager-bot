package kg.tech.lunchmanagerbot.support.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static boolean isEmpty(final CharSequence charSequence) {
        return charSequence == null || charSequence.isEmpty();
    }

}
