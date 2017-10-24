package eu.davidea.blueapp.persistence.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import eu.davidea.blueapp.viewmodels.user.User;
import eu.davidea.blueapp.viewmodels.user.UserToken;

/**
 * @author Davide
 * @since 17/09/2017
 */
@Dao
public interface UserDao {

    @Query("select * from userToken")
    LiveData<UserToken> getLoggedUser();

    @Query("select * from user where id = :userId")
    LiveData<User> getUser(Long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveLoggedUser(UserToken userToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveUser(User userToken);

    @Delete
    void logout(UserToken userToken);

}