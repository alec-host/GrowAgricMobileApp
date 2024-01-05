package com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleLedgerAndroidViewModel extends AndroidViewModel {
    private ISimpleLedgerDao simpleLedgerDao;
    private ExecutorService executorService;

    public SimpleLedgerAndroidViewModel(@NonNull Application application){
        super(application);
        simpleLedgerDao = SimpleLedgerDatabase.getInstance(application).simpleLedgerDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<SimpleLedger>> getAllSimpleLedger(){return simpleLedgerDao.getAll();}

    public List<SimpleLedger> getAllSimpleLedgerSynchronously(){return  simpleLedgerDao.getAllSync();}

    public int getSimpleLedgerCNT(){
        return simpleLedgerDao.getCount();
    }

    public LiveData<List<SimpleLedger>> getSearchedSimpleLedger(String input_user_uuid,String search_value){
        return simpleLedgerDao.searchSimpleLedger(input_user_uuid,search_value);
    }

    public List<SimpleLedger>getSearchRecordSync(String input_user_uuid,String input_search_value){
        return simpleLedgerDao.searchSimpleLedgerSync(input_user_uuid,input_search_value);
    }

    public void saveSimpleLedger(SimpleLedger simpleLedger){
        executorService.execute(()->simpleLedgerDao.save(simpleLedger));
    }

    public void saveAllSimpleLedger(List<SimpleLedger> input){
        executorService.execute(()->simpleLedgerDao.saveAll(input));
    }

    public void deleteSimpleLedger(SimpleLedger simpleLedger){
        executorService.execute(()->simpleLedgerDao.delete(simpleLedger));
    }
    public void deleteAll(){
        simpleLedgerDao.deleteAll();
    }
}
