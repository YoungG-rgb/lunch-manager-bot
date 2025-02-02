package kg.tech.lunchmanagerbot.commons.mappers;

import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface NotificationTaskEntityMapper {

    @Mappings({
            @Mapping(target = "chatId", expression = "java( chatId )"),
            @Mapping(target = "message", expression = "java( message )"),
            @Mapping(target = "currentRetries", expression = "java( 0 )"),
            @Mapping(target = "maxRetries", expression = "java( 3 )"),
            @Mapping(target = "nextRetryTime", expression = "java( java.time.LocalDateTime.now() )"),
            @Mapping(target = "status", expression = "java( kg.tech.lunchmanagerbot.commons.enums.NotificationStatus.NEW )")
    })
    NotificationTaskEntity init(String chatId, String message);

}
