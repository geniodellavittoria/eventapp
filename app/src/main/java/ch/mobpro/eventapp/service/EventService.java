package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.model.Event;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

import static ch.mobpro.eventapp.service.RestClientConstants.EVENT;
import static ch.mobpro.eventapp.service.RestClientConstants.ID;

public interface EventService {

    @GET(EVENT)
    Single<List<Event>> getEvents();

    @POST(EVENT)
    Single<Void> createEvent(@Body Event event);

    @PUT(EVENT)
    Single<Void> updateEvent();

    @DELETE(EVENT + ID)
    Single<Void> deleteEvent();

    @GET(EVENT + ID)
    Single<Event> getEvent(String id);


}
