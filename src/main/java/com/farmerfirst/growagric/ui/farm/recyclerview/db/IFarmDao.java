package com.farmerfirst.growagric.ui.farm.recyclerview.db;

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
public interface IFarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<Farm> farm);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Farm farm);

    @Update
    public void update(Farm farm);

    @Query("SELECT * FROM Farm WHERE user_uuid=:user_uuid ORDER BY farm_id DESC")
    public LiveData<List<Farm>> getAll(String user_uuid);

    @Query("SELECT * FROM Farm WHERE user_uuid=:user_uuid ORDER BY farm_id DESC")
    public List<Farm> getAllSync(String user_uuid);

    @Query("SELECT * FROM Farm WHERE county LIKE :input_county ORDER BY farm_id DESC")
    public LiveData<List<Farm>> searchFarm(String input_county);

    @Query("SELECT COUNT(farm_id) FROM Farm WHERE user_uuid=:user_uuid")
    public int getCount(String user_uuid);

    @Ignore
    @Query("SELECT farm_id,user_uuid,farm_uuid, county, sub_county, ward, num_of_employees, num_of_years, house_capacity, mortality_rate, item_farmed, is_insured, insurer_name, date_created FROM Farm")
    public List<Farm>getFarmID();

    @Delete
    public void delete(Farm farm);

    @Query("DELETE FROM Farm")
    void deleteAll();
}
