package kg.tech.lunchmanagerbot.group_chat.services.duty;

import kg.tech.lunchmanagerbot.group_chat.entities.AttendantUserEntity;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

public interface AttendantUserService {

    Optional<AttendantUserEntity> saveIfAbsent(User user);

    /**
     * @param priority Очередность или приоритет, используемый для выбора дежурного
     * @implNote Метод выбирает следующего дежурного из списка, учитывая заданный приоритет.
     * Если подходящий дежурный не найден, возвращается первый активный дежурный.
     */
    AttendantUserEntity findFirstNextDutyUser(int priority);

    /**
     * @implNote Метод находит первого активного дежурного пользователя.
     */
    AttendantUserEntity findFirstDutyUser();

}

