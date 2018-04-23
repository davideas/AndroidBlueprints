package eu.davidea.blueapp.viewmodels.message;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import eu.davidea.blueapp.persistence.repositories.MessageRepository;
import eu.davidea.blueapp.ui.items.HolderMessageItem;
import eu.davidea.flexibleadapter.livedata.FlexibleFactory;
import eu.davidea.flexibleadapter.livedata.FlexibleItemProvider;
import eu.davidea.flexibleadapter.livedata.FlexibleViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
public class MessageViewModel extends FlexibleViewModel<List<Message>, HolderMessageItem, Long> {

    private MessageRepository repository;
    private MessageItemFactory itemFactory;

    @Inject
    public MessageViewModel(MessageRepository repository) {
        super(); // super() must be called!
        Timber.d("Init MessageViewModel");
        this.itemFactory = new MessageItemFactory();
        this.repository = repository;
    }

    /*---------------------------*/
    /* VIEW MODEL IMPLEMENTATION */
    /*---------------------------*/

    /**
     * Provides "live" items to observe as input for the Adapter.
     * <p>To call from the UI Controller.</p>
     *
     * @return the LiveData to observe with the list of items for the any Adapter.
     */
    @NonNull
    @Override
    public LiveData<List<HolderMessageItem>> getLiveItems() {
        return super.getLiveItems();
    }

    /**
     * Triggers the loading of the source.
     * <p>To call from the UI Controller.</p>
     *
     * @param threadId the {@code Source} identifier to provide to the repository
     */
    public void loadMessages(Long threadId) {
        super.loadSource(threadId);
    }

    /**
     * Retrieves the LiveData coming from <i>Local</i> or <i>Remote</i> repository.
     *
     * @param threadId the {@code Source} identifier to provide to the repository
     * @return the LiveData, input for the mapping
     */
    @NonNull
    @Override
    protected LiveData<List<Message>> getSource(@NonNull Long threadId) {
        return this.repository.loadLiveConversation(threadId);
    }

    /**
     * Checks if resource is valid before mapping the items.
     * <p>Should be implemented by checking, at least, if the {@code Source} is <u>not</u>
     * {@code null} and if the original list is <u>not</u> empty.</p>
     *
     * @param messages the type of input {@code Source} LiveData containing the original list
     * @return {@code true} if source is valid, {@code false} otherwise
     */
    @Override
    protected boolean isSourceValid(@Nullable List<Message> messages) {
        return messages != null && !messages.isEmpty();
    }

    /**
     * Maps the {@code Source} containing the original list to a list suitable for the Adapter.
     * <p><b>Tip:</b> User can use a custom implementation of
     * {@link FlexibleItemProvider.Factory} that together with
     * {@link FlexibleItemProvider} will help to map the model to an Adapter item.</p>
     *
     * @param messages the type of input {@code Source} LiveData containing the original list
     * @return the mapped list suitable for the Adapter.
     */
    @Override
    protected List<HolderMessageItem> map(@NonNull List<Message> messages) {
        return FlexibleItemProvider
                .with(itemFactory)
                .from(messages);
    }

    /*---------------*/
    /* INNER CLASSES */
    /*---------------*/

    static class MessageItemFactory implements FlexibleItemProvider.Factory<Message, HolderMessageItem> {
        @NonNull
        @Override
        public HolderMessageItem create(Message message) {
            // Equivalent of: new HolderMessageItem(message);
            return FlexibleFactory.create(HolderMessageItem.class, message);
        }
    }




    /*---------------------------*/
    /* VIEW MODEL IMPLEMENTATION */
    /* with ReactiveX library    */
    /*---------------------------*/

    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Message>> messages;
    private Subject<List<Message>> subject;
    private MessageViewModel.Identifier messageIdentifier;

    @Inject
    public MessageViewModel(MessageRepository messageRepository,
                            @Named("vm") CompositeDisposable compositeDisposable) {
        Timber.d("Init MessageViewModel");
        this.repository = messageRepository;
        this.compositeDisposable = compositeDisposable;
        this.subject = PublishSubject.create();
        this.messages = new MutableLiveData<>();
    }

    public Subject<List<Message>> subscribeToConversationUpdates() {
        return subject;
    }

    public void loadConversation(Long threadId) {
        Identifier identifier = new Identifier(threadId);
        if (!identifier.equals(messageIdentifier)) {
            messageIdentifier = identifier;
            // 1) Add observable to CompositeDisposable so that it can be dispose when ViewModel is
            // ready to be destroyed 2) Call retrofit client on background thread and update database
            // with response from service using Room.
            Disposable disposable = repository.loadConversation(threadId)
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
    public void onCleared() {
        //prevents memory leaks by disposing pending observable objects
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        messageIdentifier = null;
    }

    /*---------------*/
    /* INNER CLASSES */
    /*---------------*/

    class Identifier {
        private Long threadId;
        private Long messageId;

        Identifier(Long threadId) {
            this.threadId = threadId;
        }

        Identifier(Long threadId, Long messageId) {
            this(threadId);
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