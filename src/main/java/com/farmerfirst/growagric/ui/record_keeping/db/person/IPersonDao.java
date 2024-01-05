package com.farmerfirst.growagric.ui.record_keeping.db.person;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger.SimpleLedger;

import java.util.List;

@Dao
public interface IPersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<Person> person);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Person person);

    @Update
    public void update(Person person);

    @Query("SELECT * FROM Person WHERE person_type LIKE :input_person_type ORDER BY person_id DESC")
    public LiveData<List<Person>> getAll(int input_person_type);

    @Query("SELECT * FROM Person WHERE person_type LIKE :input_person_type ORDER BY person_id DESC")
    public List<Person> getAllSync(int input_person_type);

    @Query("SELECT * FROM Person WHERE user_uuid LIKE :input_user_uuid AND person_name LIKE :input_search_name ORDER BY person_id DESC")
    public LiveData<List<Person>> searchPerson(String input_user_uuid,String input_search_name);

    @Query("SELECT * FROM Person WHERE user_uuid LIKE :input_user_uuid AND person_name LIKE '%' || :input_search_name || '%' AND person_type LIKE :input_person_type ORDER BY person_id DESC")
    public List<Person> searchPersonSync(String input_user_uuid,String input_search_name,int input_person_type);

    @Query("SELECT COUNT(person_id) FROM Person WHERE user_uuid LIKE :input_user_uuid AND is_deleted=0")
    public int getCount(String input_user_uuid);

    @Query("DELETE FROM Person WHERE person_uuid LIKE :input_person_uuid AND is_deleted=0")
    public int deleteByUUID(String input_person_uuid);

    @Ignore
    @Query("SELECT person_id,person_uuid,person_name,phone_number,id_number,location,person_type,date_created,is_deleted FROM Person WHERE is_deleted=0")
    public List<Person>getPersonID();

    @Delete
    public void delete(Person person);

    @Query("DELETE FROM Person")
    void deleteAll();
}
