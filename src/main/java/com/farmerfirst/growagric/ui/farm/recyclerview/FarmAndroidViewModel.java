package com.farmerfirst.growagric.ui.farm.recyclerview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.FarmDatabase;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.IFarmDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FarmAndroidViewModel extends AndroidViewModel {
    private IFarmDao farmDao;
    private ExecutorService executorService;

    public FarmAndroidViewModel(@NonNull Application application){
        super(application);
        farmDao = FarmDatabase.getInstance(application).farmDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Farm>> getAllFarms(String uuid){
        return farmDao.getAll(uuid);
    }

    public List<Farm> getAllFarmsSynchronously(String uuid){
        return farmDao.getAllSync(uuid);
    }

    public int getFarmCNT(String uuid){
        return farmDao.getCount(uuid);
    }

    public LiveData<List<Farm>> getSearchedFarm(String searchedFarm){
        return farmDao.searchFarm(searchedFarm);
    }

    public void saveFarm(Farm farm){
        executorService.execute(()->farmDao.save(farm));
    }

    public void deleteFarm(Farm farm){
        executorService.execute(()->farmDao.delete(farm));
    }

    public void deleteAll(){
        farmDao.deleteAll();
    }
}
