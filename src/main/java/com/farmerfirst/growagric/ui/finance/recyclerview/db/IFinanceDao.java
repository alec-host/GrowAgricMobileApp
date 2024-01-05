package com.farmerfirst.growagric.ui.finance.recyclerview.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.farmerfirst.growagric.ui.finance.recyclerview.db.Finance;

import java.util.List;

@Dao
public interface IFinanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<Finance> finance);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Finance finance);

    @Update
    public void update(Finance finance);

    @Query("SELECT * FROM Finance WHERE user_uuid=:user_uuid ORDER BY finance_id DESC")
    public LiveData<List<Finance>> getAll(String user_uuid);

    @Query("SELECT * FROM Finance WHERE user_uuid=:user_uuid ORDER BY finance_id DESC")
    public List<Finance> getAllSync(String user_uuid);

    @Query("SELECT * FROM Finance WHERE application_status LIKE :input_status ORDER BY finance_id DESC")
    public LiveData<List<Finance>> searchFinance(String input_status);

    @Query("SELECT COUNT(finance_id) FROM Finance WHERE user_uuid=:user_uuid")
    public int getCount(String user_uuid);

    @Ignore
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT finance_id,application_uuid,user_uuid,chick_cost,feed_cost,brooding_cost,application_status,financial_sponsor,date_required,date_created FROM Finance")
    public List<Finance>getFinanceID();

    @Delete
    public void delete(Finance finance);

    @Query("DELETE FROM Finance")
    void deleteAll();
}
