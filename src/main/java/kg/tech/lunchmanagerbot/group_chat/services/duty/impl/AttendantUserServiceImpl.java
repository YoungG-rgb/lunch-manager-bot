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
    public Optional<AttendantUserEntity> saveIfAbsent(User user) {
        if (user.getIsBot()) return Optional.empty();

        AttendantUserEntity attendantUserEntity = attendantUserMapper.toEntity(user);
        if (!attendantUserRepository.existsByUsername(attendantUserEntity.getUsername())) {
            attendantUserEntity.setPriority(attendantUserRepository.getMaxPriority());
            return Optional.of( attendantUserRepository.save(attendantUserEntity) );
        }

        return Optional.empty( );
    }

    @Override
    public AttendantUserEntity findFirstNextDutyUser(int priority) {
        return attendantUserRepository.findFirstNextDutyUser(priority);
    }

    @Override
    public AttendantUserEntity findFirstDutyUser() {
        return attendantUserRepository.findFirstDutyUser();
    }
}
