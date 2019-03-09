package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.model.EventCategory;;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

import static ch.mobpro.eventapp.service.RestClientConstants.*;

public interface EventCategoryService {

    @GET(EVENT + "/" + ID + EVENT_CATEGORY)
    Single<List<EventCategory>> getEventCategories(@Path(ID) String eventId);

    @POST(EVENT + "/" + ID + EVENT_CATEGORY)
    Single<EventCategory> createEventCategory(@Path(ID) String eventId, @Body EventCategory eventCategory);

    @DELETE(EVENT + "/" + ID + EVENT_CATEGORY + CATEGORY_ID)
    Single<Void> deleteEventCategory(@Path(ID) String eventId, @Path(CATEGORY_ID) String categoryId);

}
