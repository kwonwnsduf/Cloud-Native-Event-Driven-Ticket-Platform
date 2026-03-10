package com.ticketplatform.notification_service.repository;

import com.ticketplatform.notification_service.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

}
