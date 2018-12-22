package bkdev.android.base_mvp.databases.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import bkdev.android.base_mvp.models.User;

/**
 * Created by Linh NDD
 * on 5/2/2018.
 */
@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(User user);

    @Delete
    void delete(User user);

    @Transaction
    @Query("SELECT * FROM User ")
    List<User> loadAllBottle();

    @Transaction
    @Query("DELETE FROM User ")
    void deleteAll();
}
