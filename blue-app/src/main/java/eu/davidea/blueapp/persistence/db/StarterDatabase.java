package eu.davidea.blueapp.persistence.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import eu.davidea.blueapp.viewmodels.message.Message;
import eu.davidea.blueapp.viewmodels.user.User;
import eu.davidea.blueapp.viewmodels.user.UserToken;

/**
 * Database Migration
 * http://www.zoftino.com/database-migration-with-room
 */
@Database(entities = {User.class, UserToken.class, Message.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverters.class, EnumConverters.class})
public abstract class StarterDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "StarterDatabase.db";

    public abstract UserDao userDao();

    public abstract MessageDao messageDao();

}