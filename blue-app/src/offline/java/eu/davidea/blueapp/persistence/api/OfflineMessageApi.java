package eu.davidea.blueapp.persistence.api;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.blueapp.viewmodels.message.Message;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.mock.BehaviorDelegate;
import timber.log.Timber;

/**
 * @author Davide
 * @since 26/09/2017
 * AndroidStarterApp
 */

public class OfflineMessageApi implements MessageApi {

    private static final int ITEMS = 10;
    private static final String MESSAGE_URL = "/v1/thread";
    private final BehaviorDelegate<MessageApi> delegate;

    public OfflineMessageApi(BehaviorDelegate<MessageApi> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Flowable<List<Message>> getConversation(Long threadId, Long userId) {
        return delegate.returningResponse(initMessages(threadId, userId))
                .getConversation(threadId, userId)
                .doOnSubscribe((subscription -> Timber.d("Generating Conversation %s %s", threadId, userId)));
    }

    @Override
    public Observable<Message> getMessage(Long threadId, Long messageId) {
        return null;
    }

    @Override
    public void sendMessage(Message message) {
    }

    private List<Message> initMessages(Long threadId, Long userId) {
        List<Message> messages = new ArrayList<>(10);
        for (long i = 0; i < ITEMS; i++) {
            messages.add(new Message(userId, threadId));
        }
        return messages;
    }

}