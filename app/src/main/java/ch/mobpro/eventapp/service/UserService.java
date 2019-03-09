package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.dto.ChangePasswordForm;
import ch.mobpro.eventapp.model.User;
import io.reactivex.Single;
import retrofit2.http.*;

import java.util.List;

import static ch.mobpro.eventapp.service.RestClientConstants.*;

public interface UserService {

    @GET(USER)
    Single<List<User>> getUsers();

    @GET(USER + USER_USER_ID)
    Single<User> getUser(@Path(ID) String userId);

    @PUT(USER + USER_USER_ID)
    Single<User> updateUser(@Path(ID) String userId);

    @DELETE(USER + USER_USER_ID)
    Single<Void> deleteUser(@Path(ID) String userId);

    @POST(USER + USER_USER_ID + USER_CHANGE_PASSWORD)
    Single<User> changePassword(@Path(ID) String userId, @Body ChangePasswordForm changePasswordForm);
}
