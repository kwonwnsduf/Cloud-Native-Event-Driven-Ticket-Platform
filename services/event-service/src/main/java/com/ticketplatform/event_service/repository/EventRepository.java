package com.ticketplatform.event_service.repository;

import com.ticketplatform.event_service.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
