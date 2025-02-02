package kg.tech.lunchmanagerbot.group_chat.repositories;

import kg.tech.lunchmanagerbot.group_chat.entities.TelegramUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TelegramUserRepository extends JpaRepository<TelegramUserEntity, Long> {

    @Query(value = """
    select case when count(tu) > 0 then true else false
    end from TelegramUserEntity tu where tu.chatId = :chatId and tu.isMenuCreator = true
    """)
    boolean isMenuCreator(@Param("chatId") String chatId);

}
