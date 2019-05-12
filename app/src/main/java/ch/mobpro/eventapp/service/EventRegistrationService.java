package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.dto.EventRegistrationForm;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.model.EventRegistration;
import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

import static ch.mobpro.eventapp.service.RestClientConstants.*;


public interface EventRegistrationService {

    @GET(EVENT + "/" + ID + EVENT_REGISTRATION)
    Single<List<EventRegistration>> getEventRegistrations(@Path("id") String eventId);

    @POST(EVENT + "/" + ID + EVENT_REGISTRATION)
    Single<Event> createEventRegistration(@Path("id") String eventId, @Body EventRegistrationForm eventRegistrationForm);

    @DELETE(EVENT + "/" + ID + EVENT_REGISTRATION + USER_ID)
    Maybe<Event> deleteEventRegistration(@Path("id") String eventId, @Path(RestPlaceholderVariables.USER_ID) String eventRegistrationId);
}
