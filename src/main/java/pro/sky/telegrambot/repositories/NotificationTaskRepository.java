package pro.sky.telegrambot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    @Query(value = "SELECT * FROM notification_task WHERE date_and_time = :localDateTime", nativeQuery = true)
    public Collection<NotificationTask> findNotificationTasksByDateAndTime(@Param("localDateTime") LocalDateTime localDateTime);

}
