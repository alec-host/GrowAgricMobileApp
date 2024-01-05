package com.farmerfirst.growagric.ui.finance.recyclerview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerfirst.growagric.ui.finance.recyclerview.db.Finance;
import com.farmerfirst.growagric.ui.finance.recyclerview.db.FinanceDatabase;
import com.farmerfirst.growagric.ui.finance.recyclerview.db.IFinanceDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinanceAndroidViewModel extends AndroidViewModel {
    private IFinanceDao financeDao;
    private ExecutorService executorService;

    public FinanceAndroidViewModel(@NonNull Application application){
        super(application);

        financeDao = FinanceDatabase.getInstance(application).financeDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Finance>> getAllFinanceApplications(String uuid){
        return financeDao.getAll(uuid);
    }

    public List<Finance> getAllFinanceApplicationSynchronously(String uuid){
        return  financeDao.getAllSync(uuid);
    }

    public int getFinanceCNT(String uuid){
        return financeDao.getCount(uuid);
    }

    public LiveData<List<Finance>> getSearchedFinanceApplication(String searchedFinanceApplication){
        return financeDao.searchFinance(searchedFinanceApplication);
    }

    public void saveFinance(Finance finance){
        executorService.execute(() -> financeDao.save(finance));
    }

    public void deleteFinance(Finance finance){
        executorService.execute(() -> financeDao.delete(finance));
    }

    public void deleteAll(){
        financeDao.deleteAll();
    }
}
