package eu.davidea.starterapp.viewmodels.message;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import eu.davidea.starterapp.persistence.repositories.MessageRepository;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
public class MessageViewModel extends ViewModel {

    private MessageRepository repository;
    private CompositeDisposable compositeDisposable;

    public MessageViewModel() {
        // Needed by ViewModelProviders
    }

    @Inject
    public MessageViewModel(MessageRepository messageRepository,
                            @Named("vm") CompositeDisposable compositeDisposable) {
        Timber.d("Init MessageViewModel");
        this.repository = messageRepository;
        this.compositeDisposable = compositeDisposable;
    }

    public Flowable<List<Message>> getConversation(Long threadId, Long messageId) {
        return repository.getConversation(threadId, messageId);
    }

    public void loadConversation(Long threadId, Long messageId) {
        // 1) Add observable to CompositeDisposable so that it can be dispose when ViewModel is
        // ready to be destroyed 2) Call retrofit client on background thread and update database
        // with response from service using Room.
        Disposable disposable = repository.loadConversation(threadId, messageId);
        compositeDisposable.add(disposable);
    }

    @Override
    public void onCleared(){
        //prevents memory leaks by disposing pending observable objects
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}