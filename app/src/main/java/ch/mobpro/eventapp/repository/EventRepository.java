package ch.mobpro.eventapp.repository;

import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.service.EventService;
import io.reactivex.Single;
import retrofit2.http.Headers;

import javax.inject.Inject;
import java.util.List;

public class EventRepository {

    private final EventService eventService;

    @Inject
    EventRepository(EventService eventService) {
        this.eventService = eventService;
    }

    @Headers("Content-Type: application/json")
    public Single<List<Event>> getEvents() {
        return eventService.getEvents();
    }

    public Single<Event> getEvent(String id) {
        return eventService.getEvent(id);
    }

//    public Single<Void> createEvent(Event event) {
//        return this.eventService.createEvent(event);
//    }

}