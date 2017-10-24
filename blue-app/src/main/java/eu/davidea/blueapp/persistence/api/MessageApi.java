package eu.davidea.blueapp.persistence.api;

import java.util.List;

import eu.davidea.blueapp.viewmodels.message.Message;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Davide
 * @since 24/09/2017
 */
public interface MessageApi {

    String THREAD_URL = "/v1/threads";

    @GET(THREAD_URL + "/{threadId}")
    Flowable<List<Message>> getConversation(@Path("threadId") Long threadId,
                                            @Query("messageId") Long messageId);

    @GET(THREAD_URL + "/{threadId}/{messageId}")
    Observable<Message> getMessage(@Path("threadId") Long threadId,
                                   @Path("messageId") Long messageId);

    @POST(THREAD_URL)
    void sendMessage(@Body Message message);

}