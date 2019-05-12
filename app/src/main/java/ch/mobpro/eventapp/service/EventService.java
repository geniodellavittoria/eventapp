package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.model.Event;
import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

import static ch.mobpro.eventapp.service.RestClientConstants.EVENT;
import static ch.mobpro.eventapp.service.RestClientConstants.ID;

public interface EventService {

    @GET(EVENT)
    Single<List<Event>> getEvents();

    @Headers("Content-Type: application/json")
    @POST(EVENT)
    Single<Event> createEvent(@Body Event event);

    @Headers("Content-Type: application/json")
    @PUT(EVENT + "/" + ID)
    Single<Event> updateEvent(@Path("id") String id, @Body Event event);

    @DELETE(EVENT + "/" + ID)
    Single<Response> deleteEvent(@Path("id") String id);

    @GET(EVENT + "/" + ID)
    Single<Event> getEvent(String id);


}
