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
public interface IChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<Chat> chat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Chat chat);

    @Update
    public void update(Chat chat);

    @Query("SELECT * FROM Chat WHERE farmer_uuid=:farmer_uuid ORDER BY chat_id ASC")
    public LiveData<List<Chat>> getAll(String farmer_uuid);

    @Query("SELECT * FROM Chat WHERE farmer_uuid=:farmer_uuid ORDER BY chat_id ASC")
    public List<Chat> getAllSync(String farmer_uuid);

    @Query("SELECT * FROM Chat WHERE message LIKE :input_text ORDER BY chat_id DESC")
    public LiveData<List<Chat>> searchChatMessage(String input_text);

    @Query("SELECT COUNT(chat_id) FROM Chat WHERE farmer_uuid=:farmer_uuid")
    public int getCount(String farmer_uuid);

    @Ignore
    @Query("SELECT chat_id,chat_uuid,farmer_uuid,message,message_origin,is_deleted,date_created FROM Chat")
    public List<Chat>getChatID();

    @Delete
    public void delete(Chat chat);

    @Query("DELETE FROM Chat")
    void deleteAll();
}
