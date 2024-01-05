package com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger;

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
public interface ISimpleLedgerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<SimpleLedger> simpleLedgers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(SimpleLedger simpleLedger);

    @Update
    public void update(SimpleLedger simpleLedger);

    @Query("SELECT * FROM SimpleLedger ORDER BY trx_id DESC")
    public LiveData<List<SimpleLedger>> getAll();

    @Query("SELECT * FROM SimpleLedger ORDER BY trx_id DESC")
    public List<SimpleLedger> getAllSync();

    @Query("SELECT * FROM SimpleLedger WHERE user_uuid LIKE :input_user_uuid AND record_type LIKE :input_search_value ORDER BY trx_id DESC")
    public LiveData<List<SimpleLedger>> searchSimpleLedger(String input_user_uuid,String input_search_value);

    @Query("SELECT * FROM SimpleLedger WHERE user_uuid LIKE :input_user_uuid AND record_type LIKE '%' || :input_search_value || '%' ORDER BY trx_id DESC")
    public List<SimpleLedger> searchSimpleLedgerSync(String input_user_uuid,String input_search_value);

    //@Query("SELECT * FROM Person WHERE user_uuid LIKE :input_user_uuid AND person_name LIKE '%' || :input_search_name || '%' AND person_type LIKE :input_person_type ORDER BY person_id DESC")
    //public List<Person> searchPersonSync(String input_user_uuid,String input_search_name,int input_person_type);

    @Query("SELECT COUNT(trx_id) FROM SimpleLedger")
    public int getCount();

    @Ignore
    @Query("SELECT trx_id,transaction_uuid,farm_uuid,user_uuid,cr,dr,running_balance,record_type,description,notes,entry_date,date_created FROM SimpleLedger WHERE is_deleted='0'")
    public List<SimpleLedger>getTransactionID();

    @Delete
    public void delete(SimpleLedger simpleLedger);

    @Query("DELETE FROM SimpleLedger")
    void deleteAll();
}
