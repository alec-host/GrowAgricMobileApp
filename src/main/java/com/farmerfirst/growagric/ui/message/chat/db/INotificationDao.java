package com.farmerfirst.growagric.ui.message.chat.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface INotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<Notification> notification);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Notification notification);

    @Update
    public void update(Notification notification);

    @Query("SELECT * FROM Notification ORDER BY _id ASC")
    public LiveData<List<Notification>> getAll();

    @Query("SELECT * FROM Notification ORDER BY _id ASC")
    public List<Notification> getAllSync();

    @Query("SELECT * FROM Notification WHERE subject LIKE :input_text ORDER BY _id  DESC")
    public LiveData<List<Notification>> searchNotification(String input_text);

    @Query("SELECT COUNT(_id) FROM Notification WHERE is_deleted = 0")
    public int getCount();

    @Ignore
    @Query("SELECT _id,notification_uuid,subject,message,date_created,date_created,is_deleted FROM Notification")
    public List<Notification>getNotifivcationID();

    @Delete
    public void delete(Notification notification);

    @Query("DELETE FROM Notification")
    void deleteAll();
}
