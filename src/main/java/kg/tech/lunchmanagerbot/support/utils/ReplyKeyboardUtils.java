package kg.tech.lunchmanagerbot.support.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ReplyKeyboardUtils {

    /**
     * Создает и возвращает клаву с кнопками, расположенными по две в ряд. <p>
     * Examples:
     * <blockquote><pre>
     * Map<String, String> buttons = new HashMap<>();
     * buttons.put("callback_1", "Кнопка 1");
     * buttons.put("callback_2", "Кнопка 2");
     * buttons.put("callback_3", "Кнопка 3");
     * </pre></blockquote>
     * <p><b>Итоговый вид клавиатуры:</b></p>
     * <pre>
     * [Кнопка 1] [Кнопка 2]
     * [Кнопка 3]
     * </pre>
     * @param nameWithCallbacksMap мапа, содержащая названия кнопок в качестве значений
     * и соответствующие callbackData в качестве ключей.
     */
    public static InlineKeyboardMarkup buildKeyboard(Map<String, String> nameWithCallbacksMap) {
        List<List<InlineKeyboardButton>> multiLines = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (Map.Entry<String, String> callbackData : nameWithCallbacksMap.entrySet( )) {
            InlineKeyboardButton button = new InlineKeyboardButton(callbackData.getValue());
            button.setCallbackData(callbackData.getKey());
            rowInline.add(button);

            if (rowInline.size() == 2) {
                multiLines.add(rowInline);
                rowInline = new ArrayList<>();
            }

        }

        if (!rowInline.isEmpty()) {
            multiLines.add(rowInline);
        }

        return new InlineKeyboardMarkup(multiLines);
    }

    /**
     * Создает и возвращает клаву, где каждая кнопка находится в отдельном ряду. <p>
     * Examples:
     * <blockquote><pre>
     * Map<String, String> buttons = new HashMap<>();
     * buttons.put("callback_1", "Кнопка 1");
     * buttons.put("callback_2", "Кнопка 2");
     * buttons.put("callback_3", "Кнопка 3");
     * </pre></blockquote>
     * <p><b>Итоговый вид клавиатуры:</b></p>
     * <pre>
     * [Кнопка 1]
     * [Кнопка 2]
     * [Кнопка 3]
     * </pre>
     * @param nameWithCallbacksMap мапа, содержащая названия кнопок в качестве значений
     * и соответствующие callbackData в качестве ключей.
     */
    public static InlineKeyboardMarkup buildSingleRowKeyboard(Map<String, String> nameWithCallbacksMap) {
        List<List<InlineKeyboardButton>> multiLines = new ArrayList<>();

        for (Map.Entry<String, String> callbackData : nameWithCallbacksMap.entrySet( )) {
            InlineKeyboardButton button = new InlineKeyboardButton(callbackData.getValue());
            button.setCallbackData(callbackData.getKey());

            List<InlineKeyboardButton> rowLine = new ArrayList<>();
            rowLine.add(button);
            multiLines.add(rowLine);
        }

        return new InlineKeyboardMarkup(multiLines);
    }

}
