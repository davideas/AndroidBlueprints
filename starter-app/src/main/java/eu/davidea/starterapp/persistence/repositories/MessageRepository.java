package eu.davidea.starterapp.persistence.repositories;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.starterapp.infrastructure.AppExecutors;
import eu.davidea.starterapp.persistence.api.MessageApi;
import eu.davidea.starterapp.persistence.db.MessageDao;
import eu.davidea.starterapp.persistence.db.StarterDatabase;
import eu.davidea.starterapp.viewmodels.message.Message;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
public class MessageRepository {

    private MessageApi api;
    private MessageDao messageDao;
    private AppExecutors executors;
    Subject<List<Message>> subject = PublishSubject.create();

    @Inject
    public MessageRepository(StarterDatabase database, MessageApi api, AppExecutors executors) {
        this.messageDao = database.messageDao();
        this.api = api;
        this.executors = executors;
    }

    public Subject<List<Message>> getConversation(Long threadId, Long messageId) {
        return subject;
    }

    public Disposable loadConversation(Long threadId, Long messageId) {
        Flowable<List<Message>> dbFlowable = messageDao.getConversation(threadId);
        Flowable<List<Message>> apiFlowable = api.getConversation(threadId, messageId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(messages -> {
                    saveMessages(messages);
                    return messages;
                });

//        return dbFlowable
//                .subscribeOn(Schedulers.computation())
//                .flatMap((messages) -> {
//                    if (!messages.isEmpty()) {
//                        return Flowable.just(messages);
//                    } else {
//                        return apiFlowable;
//                    }
//                })
//                .subscribe((messages) -> {
//                    subject.onNext(messages);
//                    subject.onComplete();
//                });

        // https://stackoverflow.com/questions/45549827/how-to-use-switchifempty
        return dbFlowable
                .subscribeOn(Schedulers.computation())
                .take(1)
                .filter(messages -> !messages.isEmpty())
                .switchIfEmpty(apiFlowable)
                .subscribe((messages) -> {
                    subject.onNext(messages);
                    subject.onComplete();
                });
    }

    private void saveMessages(List<Message> messages) {
        executors.diskIO().execute(() -> {
            Timber.d("Saving %d messages to DB", messages.size());
            messageDao.saveMessages(messages);
        });
    }

}