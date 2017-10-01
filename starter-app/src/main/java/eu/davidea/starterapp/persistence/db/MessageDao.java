package eu.davidea.starterapp.persistence.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import eu.davidea.starterapp.viewmodels.message.Message;
import io.reactivex.Flowable;

/**
 * @author Davide
 * @since 17/09/2017
 */
@Dao
public interface MessageDao {

    @Query("select * from message where threadId = :threadId order by creDate asc")
    Flowable<List<Message>> getConversation(Long threadId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMessage(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> saveMessages(List<Message> messages);

    @Delete
    int delete(Message message);

}