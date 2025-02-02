package kg.tech.lunchmanagerbot.group_chat.services.duty.impl;

import kg.tech.lunchmanagerbot.group_chat.entities.AttendantUserEntity;
import kg.tech.lunchmanagerbot.group_chat.mappers.AttendantUserMapper;
import kg.tech.lunchmanagerbot.group_chat.repositories.AttendantUserRepository;
import kg.tech.lunchmanagerbot.group_chat.services.duty.AttendantUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendantUserServiceImpl implements AttendantUserService {
    private final AttendantUserRepository attendantUserRepository;
    private final AttendantUserMapper attendantUserMapper;

    @Override
    public Optional<AttendantUserEntity> saveIfAbsent(User user, String groupChatId) {
        if (user.getIsBot()) return Optional.empty();

        if (!attendantUserRepository.existsByUsername(user.getUserName())) {
            AttendantUserEntity attendantUserEntity = attendantUserMapper.toNewEntity(user, attendantUserRepository.getMaxPriority(), groupChatId);
            return Optional.of( attendantUserRepository.save(attendantUserEntity) );
        }

        return Optional.empty( );
    }

    @Override
    public AttendantUserEntity findFirstNextDutyUser(int priority, String groupChatId) {
        return attendantUserRepository.findFirstNextDutyUser(priority, groupChatId);
    }

    @Override
    public AttendantUserEntity findFirstDutyUser(String groupChatId) {
        return attendantUserRepository.findFirstDutyUser(groupChatId);
    }

    @Override
    public void activateAttendant(String username) {
        attendantUserRepository.activateAttendantByUsername(username);
    }

    @Override
    public void deactivateAttendants( ) {
        attendantUserRepository.deactivateAttendants();
    }
}
