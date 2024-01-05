package com.farmerfirst.growagric.ui.learning.db.module;

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
public interface IModuleDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<Module> module);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Module module);

    @Update
    public void update(Module module);

    @Query("SELECT * FROM Module ORDER BY module_id DESC")
    public LiveData<List<Module>> getAll();

    @Query("SELECT * FROM Module ORDER BY module_id DESC")
    public List<Module> getAllSync();

    @Query("SELECT * FROM Module WHERE topic LIKE :input_topic ORDER BY module_id DESC")
    public LiveData<List<Module>> searchModule(String input_topic);

    @Query("SELECT COUNT(module_id) FROM Module WHERE is_deleted=0")
    public int getCount();

    @Ignore
    @Query("SELECT module_id,module_uuid,topic,description,is_deleted,date_created FROM Module")
    public List<Module>getModuleID();

    @Delete
    public void delete(Module module);

    @Query("DELETE FROM Module")
    void deleteAll();
}
