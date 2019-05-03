package ch.mobpro.eventapp.dto;

import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.model.EventCategory;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class CreateEventFormEventMapper {

    public Event event = new Event();
    private final ArrayList<String> categories = new ArrayList<>();

    public CreateEventFormEventMapper(EventDetailForm createEventForm) {
        this.event.setName(createEventForm.getName());
        Instant startTime = mapToInstant(createEventForm.getStartTime(), createEventForm.getStartDate());
        Instant endTime = mapToInstant(createEventForm.getEndTime(), createEventForm.getEndDate());
        this.event.setStartTime(startTime);
        this.event.setEndTime(endTime);
        this.event.setCreationTime(Instant.now());
        this.event.setPrice(createEventForm.getPrice());
        this.event.setPrivateEvent(createEventForm.isPrivateEvent());
        this.event.setDescription(createEventForm.getDescription());
        List<EventCategory> eventCategories = new ArrayList<EventCategory>();
        eventCategories.add(new EventCategory(createEventForm.getCategory()));
        this.event.setCategories(eventCategories);
        this.event.setLatitude(createEventForm.getLatitude());
        this.event.setLongitude(createEventForm.getLongitude());
        this.event.setPlace(createEventForm.getPlace());
    }

    private Instant mapToInstant(LocalTime time, LocalDate date) {
        if (time == null || date == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

}
