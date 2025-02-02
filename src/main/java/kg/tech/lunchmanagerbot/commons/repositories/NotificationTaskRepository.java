package kg.tech.lunchmanagerbot.commons.repositories;

import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;
import kg.tech.lunchmanagerbot.commons.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTaskEntity, Long> {

    @Query(value = """
        select nt.* from notification_tasks nt
                where nt.status = :status
                  and nt.next_retry_time <= :current_run_at
                order by nt.created_at
                limit :limit
    """, nativeQuery = true)
    List<NotificationTaskEntity> findAllByStatus(@Param("status") String status, int limit, @Param("current_run_at") LocalDateTime currentRunAt);


    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update NotificationTaskEntity nt set nt.status = :newStatus where nt.status = :expectedStatus and nt.id = :id")
    int safeUpdateStatus(@Param("id") Long id, @Param("newStatus") NotificationStatus newStatus, @Param("expectedStatus") NotificationStatus expectedStatus);


}
