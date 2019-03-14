package ch.mobpro.eventapp.repository;

import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.service.EventService;
import io.reactivex.Single;

import javax.inject.Inject;
import java.util.List;

public class EventRepository {

    private final EventService eventService;

    @Inject
    public EventRepository(EventService eventService) {
        this.eventService = eventService;
    }

    public Single<List<Event>> getEvents() {
        return eventService.getEvents();
    }

    public Single<Event> getEvent(String id) {
        return eventService.getEvent(id);
    }
}