package ch.mobpro.eventapp.dto;

import ch.mobpro.eventapp.model.Event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

public class CreateEventFormEventMapper {
    public Event event;

    public CreateEventFormEventMapper(CreateEventForm createEventForm) {
        this.event.setName(createEventForm.getName());
        //Instant startTime = mapToInstant(createEventForm.getStartTime(),createEventForm.getStartDate());
        //this.event.setStartTime(startTime);
        this.event.setDescription(createEventForm.getDescription());
    }

}
