package kg.tech.lunchmanagerbot.group_chat.repositories;

import kg.tech.lunchmanagerbot.group_chat.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByDayAndUserExternalId(LocalDate day, long userExternalId);

    @Query("""
    select case when count(o) > 0 then true else false
    end from OrderEntity o where o.day = :day and o.userExternalId = :extId and o.menuItemName = :menuItemName
    """)
    boolean existsBy(@Param("day") LocalDate day, @Param("extId") long userExternalId, @Param("menuItemName") String menuItemName);

    @Modifying
    @Transactional
    @Query("""
    update OrderEntity o set o.amount = o.amount + 1
    where o.day = :day and o.userExternalId = :extId and o.menuItemName = :menuItemName
    """)
    void updateAmount(@Param("day") LocalDate day, @Param("extId") long userExternalId, @Param("menuItemName") String menuItemName);

}
