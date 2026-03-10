package com.ticketplatform.event_service.dto;

import com.ticketplatform.event_service.domain.Event;

import java.time.LocalDateTime;

public record EventResponse(Long id,
                            String title,
                            String venue,
                            LocalDateTime eventDateTime) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getVenue(),
                event.getEventDateTime()
        );
    }
}
