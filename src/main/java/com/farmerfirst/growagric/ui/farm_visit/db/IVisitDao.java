package com.farmerfirst.growagric.ui.farm_visit.db;

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
public interface IVisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<Visit> visits);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Visit visit);

    @Update
    public void update(Visit visit);

    @Query("SELECT * FROM Person ORDER BY person_id DESC")
    public LiveData<List<Visit>> getAll();

    @Query("SELECT * FROM Person ORDER BY person_id DESC")
    public List<Visit> getAllSync();

    @Query("SELECT * FROM Person WHERE user_uuid LIKE :input_user_uuid ORDER BY person_id DESC")
    public LiveData<List<Visit>> searchPerson(String input_user_uuid);

    @Query("SELECT COUNT(person_id) FROM Person WHERE user_uuid LIKE :input_user_uuid AND is_deleted=0")
    public int getCount(String input_user_uuid);

    @Ignore
    @Query("SELECT person_id,person_uuid,person_name,phone_number,id_number,location,person_type,date_created FROM Person WHERE is_deleted=0")
    public List<Visit>getPersonID();

    @Delete
    public void delete(Visit visit);

    @Query("DELETE FROM Person")
    void deleteAll();
}
