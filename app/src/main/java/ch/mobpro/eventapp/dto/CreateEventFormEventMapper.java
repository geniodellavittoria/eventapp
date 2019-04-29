package ch.mobpro.eventapp.dto;

import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.model.EventCategory;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class CreateEventFormEventMapper {

    public Event event = new Event();
    private final ArrayList<String> categories = new ArrayList<String>(R.array.eventCategories);

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
        String selectedCategory = categories.get(createEventForm.getCategoryIndex());
        List<EventCategory> eventCategories = new ArrayList<EventCategory>();
        eventCategories.add(new EventCategory(selectedCategory));
        this.event.setCategories(eventCategories);
        this.event.setLatitude(createEventForm.getLatitude());
        this.event.setLongitude(createEventForm.getLongitude());
    }

    private Instant mapToInstant(LocalTime time, LocalDate date) {
        if (time == null || date == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

}
