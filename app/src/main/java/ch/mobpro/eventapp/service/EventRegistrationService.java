package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.model.EventRegistration;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

import static ch.mobpro.eventapp.service.RestClientConstants.*;
import static ch.mobpro.eventapp.service.RestPlaceholderVariables.EVENT_REGISTRATION_ID;

public interface EventRegistrationService {

    @GET(EVENT + "/" + ID + EVENT_REGISTRATION)
    Single<List<EventRegistration>> getEventRegistrations(@Path(ID) String eventId);

    @POST(EVENT + "/" + ID + EVENT_REGISTRATION)
    Single<EventRegistration> createEventRegistration(@Path(ID) String eventId, @Body EventRegistration eventRegistration);

    @DELETE(EVENT + "/" + ID + EVENT_REGISTRATION + REGISTRATION_ID)
    Single<Void> deleteEventRegistration(@Path(ID) String eventId, @Path(EVENT_REGISTRATION_ID) String eventRegistrationId);
}
