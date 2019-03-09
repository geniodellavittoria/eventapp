package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.model.EventInvitation;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

import static ch.mobpro.eventapp.service.RestClientConstants.*;
import static ch.mobpro.eventapp.service.RestPlaceholderVariables.EVENT_INVITATION_ID;

public interface EventInvitationService {

    @GET(EVENT + "/" +  ID + EVENT_INVITATION)
    Single<List<EventInvitation>> getEventInvitations(@Path(ID) String eventId);

    @POST(EVENT + "/" +  ID + EVENT_INVITATION)
    Single<EventInvitation> createEventInvitation(@Path(ID) String eventId, @Body EventInvitation eventInvitation);

    @DELETE(EVENT + "/" +  ID + EVENT_INVITATION + INVITATION_ID)
    Single<Void> deleteEventInvitation(@Path(ID) String eventId, @Path(EVENT_INVITATION_ID) String invitationId);

}
