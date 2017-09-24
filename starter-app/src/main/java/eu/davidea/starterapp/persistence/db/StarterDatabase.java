package eu.davidea.starterapp.persistence.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import eu.davidea.starterapp.viewmodels.user.User;
import eu.davidea.starterapp.viewmodels.user.UserToken;


@Database(entities = {User.class, UserToken.class}, version = 1)
@TypeConverters({DateConverters.class, EnumConverters.class})
public abstract class StarterDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "StarterDatabase.db";

    public abstract UserDao userDao();

}