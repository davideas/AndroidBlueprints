package eu.davidea.blueapp.persistence.repositories;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.blueapp.infrastructure.AppExecutors;
import eu.davidea.blueapp.persistence.api.MessageApi;
import eu.davidea.blueapp.persistence.db.MessageDao;
import eu.davidea.blueapp.persistence.db.StarterDatabase;
import eu.davidea.blueapp.viewmodels.message.Message;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
public class MessageRepository {

    private MessageApi api;
    private MessageDao messageDao;
    private AppExecutors executors;

    @Inject
    public MessageRepository(StarterDatabase database, MessageApi api, AppExecutors executors) {
        this.messageDao = database.messageDao();
        this.api = api;
        this.executors = executors;
    }

    public Flowable<List<Message>> loadConversation(Long threadId, Long messageId) {
        Flowable<List<Message>> dbFlowable = messageDao.getConversation(threadId);
        Flowable<List<Message>> apiFlowable = api.getConversation(threadId, messageId)
                .subscribeOn(Schedulers.from(executors.networkIO()))
                .observeOn(Schedulers.from(executors.diskIO()))
                .map(messages -> {
                    saveMessages(messages);
                    return messages;
                });

        // https://stackoverflow.com/questions/45549827/how-to-use-switchifempty
        return dbFlowable
                .subscribeOn(Schedulers.from(executors.diskIO()))
                .take(1)
                .filter(messages -> {
                    Timber.d("%s messages found", messages.size());
                    return !messages.isEmpty();
                })
                .switchIfEmpty(apiFlowable);
    }

    private void saveMessages(List<Message> messages) {
        executors.diskIO().execute(() -> {
            Timber.d("Saving %d messages to DB", messages.size());
            List<Long> inserted = messageDao.saveMessages(messages);
            Timber.d("Saved %d: %s", inserted.size(), inserted);
        });
    }

}