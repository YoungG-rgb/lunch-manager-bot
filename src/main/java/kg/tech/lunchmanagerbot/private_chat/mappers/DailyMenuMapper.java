package kg.tech.lunchmanagerbot.private_chat.mappers;

import kg.tech.lunchmanagerbot.private_chat.entities.DailyMenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.telegram.telegrambots.meta.api.objects.Update;

@Mapper
public interface DailyMenuMapper {

    @Mappings({
            @Mapping(target = "day", expression = "java( kg.tech.lunchmanagerbot.support.utils.DateUtils.getDateOrToday(update) )"),
            @Mapping(target = "ownerChatId", expression = "java( kg.tech.lunchmanagerbot.support.utils.TelegramUtils.getChatIdByUpdate(update) )")
    })
    DailyMenuEntity toEntity(Update update);

}

