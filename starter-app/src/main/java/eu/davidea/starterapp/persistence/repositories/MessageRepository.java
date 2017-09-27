package eu.davidea.starterapp.persistence.repositories;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.starterapp.infrastructure.AppExecutors;
import eu.davidea.starterapp.persistence.api.MessageApi;
import eu.davidea.starterapp.persistence.db.MessageDao;
import eu.davidea.starterapp.persistence.db.StarterDatabase;
import eu.davidea.starterapp.viewmodels.message.Message;
import io.reactivex.Observable;
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
		// TODO: Caching - http://www.zoftino.com/android-persistency-room-rxjava
		// TODO: Caching - https://medium.com/@Miqubel/caching-with-realm-and-rxjava-80f48c5f5e37

		return messageDao.getConversation(threadId)
				.subscribe((messages) -> {
					if (messages.isEmpty()) {
						Observable.just(1)
								.subscribeOn(Schedulers.computation())
								.flatMap(i -> api.getConversation(threadId, messageId))
								.subscribeOn(Schedulers.io())
								.subscribe(messageList -> {
									saveMessages(messageList);
									subject.onNext(messageList);
								}, throwable -> {
									Timber.e(throwable, "exception getting coupons");
								});
					}
					subject.onNext(messages);
				});

	}

	private void saveMessages(List<Message> messages) {
		executors.diskIO().execute(() -> {
			Timber.d("Saving %d messages to DB", messages.size());
			messageDao.saveMessages(messages);
		});
	}

}