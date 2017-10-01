package eu.davidea.starterapp.viewmodels.message;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import eu.davidea.starterapp.persistence.repositories.MessageRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
public class MessageViewModel extends ViewModel {

    private MessageRepository repository;
    private CompositeDisposable compositeDisposable;
    private MessageViewModel.Identifier messageIdentifier;
    private MutableLiveData<List<Message>> messages;
    private Subject<List<Message>> subject;

    public MessageViewModel() {
        // Needed by ViewModelProviders
    }

    @Inject
    public MessageViewModel(MessageRepository messageRepository,
                            @Named("vm") CompositeDisposable compositeDisposable) {
        Timber.d("Init MessageViewModel");
        this.repository = messageRepository;
        this.compositeDisposable = compositeDisposable;
        this.messageIdentifier = new Identifier();
        this.subject = PublishSubject.create();
        this.messages = new MutableLiveData<>();
    }

    public Subject<List<Message>> subscribeToConversationUpdates() {
        return subject;
    }

    public void loadConversation(Long threadId, Long messageId) {
        Identifier identifier = new Identifier(threadId, messageId);
        if (!identifier.equals(messageIdentifier)) {
            messageIdentifier = identifier;
            // 1) Add observable to CompositeDisposable so that it can be dispose when ViewModel is
            // ready to be destroyed 2) Call retrofit client on background thread and update database
            // with response from service using Room.
            Disposable disposable = repository.loadConversation(threadId, messageId)
                    .subscribe((messages) -> {
                        this.messages.postValue(messages);
                        subject.onNext(messages);
                    });
            compositeDisposable.add(disposable);
        } else {
            subject.onNext(messages.getValue());
        }
    }

    @Override
    public void onCleared(){
        //prevents memory leaks by disposing pending observable objects
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        messageIdentifier = null;
    }

    private class Identifier {
        private Long threadId;
        private Long messageId;

        Identifier() {
        }

        Identifier(Long threadId, Long messageId) {
            this.threadId = threadId;
            this.messageId = messageId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Identifier that = (Identifier) o;

            if (threadId != null ? !threadId.equals(that.threadId) : that.threadId != null)
                return false;
            return messageId != null ? messageId.equals(that.messageId) : that.messageId == null;
        }

        @Override
        public int hashCode() {
            int result = threadId != null ? threadId.hashCode() : 0;
            result = 31 * result + (messageId != null ? messageId.hashCode() : 0);
            return result;
        }
    }
}