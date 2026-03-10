package com.ticketplatform.event_service.service;

import com.ticketplatform.event_service.domain.Event;
import com.ticketplatform.event_service.domain.Seat;
import com.ticketplatform.event_service.dto.EventResponse;
import com.ticketplatform.event_service.dto.SeatResponse;
import com.ticketplatform.event_service.repository.EventRepository;
import com.ticketplatform.event_service.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public List<EventResponse> getEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventResponse::from)
                .toList();
    }

    public EventResponse getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다. id=" + id));

        return EventResponse.from(event);
    }

    public List<SeatResponse> getSeats(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new IllegalArgumentException("이벤트를 찾을 수 없습니다. id=" + eventId);
        }

        List<Seat> seats = seatRepository.findByEventId(eventId);

        return seats.stream()
                .map(SeatResponse::from)
                .toList();
    }
}
