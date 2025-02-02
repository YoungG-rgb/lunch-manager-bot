package kg.tech.lunchmanagerbot.group_chat.repositories;

import kg.tech.lunchmanagerbot.group_chat.entities.AttendantUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttendantUserRepository extends JpaRepository<AttendantUserEntity, Long> {

    boolean existsByUsername(String username);

    Optional<AttendantUserEntity> findByPriority(Integer priority);

    @Query(value = """
    select u from AttendantUserEntity u
    where u.priority = (select min(u2.priority) from AttendantUserEntity u2 where u2.dutyActive = true)
    """)
    AttendantUserEntity findFirstDutyUser();

    @Query(value = """
    select u.* from users u
    where u.is_active = true and (u.priority > :current_priority or
    not exists(select 1 from users u2 where u2.priority > :current_priority and u2.is_active = true))
    order by u.priority fetch first 1 row only
    """, nativeQuery = true)
    AttendantUserEntity findFirstNextDutyUser(@Param("current_priority") Integer priority);

    @Query(value = "select count(u) from AttendantUserEntity u")
    int getAllCountUsers();

    @Query(value = "select max(u.priority) from AttendantUserEntity u")
    int getMaxPriority();

}
