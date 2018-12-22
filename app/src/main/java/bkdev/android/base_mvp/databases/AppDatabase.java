package bkdev.android.base_mvp.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import bkdev.android.base_mvp.databases.dao.UserDao;
import bkdev.android.base_mvp.models.User;


/**
 * Created by Linh NDD
 * on 5/1/2018.
 */

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public static AppDatabase getDatabase(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "MyBaby.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }

    public abstract UserDao userDao();

}
