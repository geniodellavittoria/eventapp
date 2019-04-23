package ch.mobpro.eventapp.dto;

import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.repository.SessionTokenRepository;

import javax.inject.Inject;
import java.time.*;

public class CreateEventFormEventMapper {

    public Event event = new Event();

    public CreateEventFormEventMapper(CreateEventForm createEventForm) {
        this.event.setName(createEventForm.getName());
        Instant startTime = mapToInstant(createEventForm.getStartTime(), createEventForm.getStartDate());
        Instant endTime = mapToInstant(createEventForm.getEndTime(), createEventForm.getEndDate());
        this.event.setStartTime(startTime);
        this.event.setEndTime(endTime);
        this.event.setCreationTime(Instant.now());
        this.event.setPrice(createEventForm.getPrice());
        this.event.setPrivateEvent(createEventForm.isPrivateEvent());
        this.event.setDescription(createEventForm.getDescription());
    }

    private Instant mapToInstant(LocalTime time, LocalDate date) {
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

}
