package com.farmerfirst.growagric.ui.learning.db.module;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModuleAndroidViewModel extends AndroidViewModel {
    private IModuleDao moduleDao;
    private ExecutorService executorService;

    public ModuleAndroidViewModel(@NonNull Application application){
        super(application);
        moduleDao = ModuleDatabase.getInstance(application).moduleDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Module>> getAllModule(){
        return moduleDao.getAll();
    }

    public List<Module> getAllModuleSynchronously(){
        return moduleDao.getAllSync();
    }

    public int getModuleCNT(){
        return moduleDao.getCount();
    }

    public LiveData<List<Module>> getSearchedModule(String searchedTopic){
        return moduleDao.searchModule(searchedTopic);
    }

    public void saveModule(Module module){
        executorService.execute(()->moduleDao.save(module));
    }

    public void saveAllModule(List<Module> input){
        executorService.execute(()->moduleDao.saveAll(input));
    }

    public void deleteModule(Module module){
        executorService.execute(()->moduleDao.delete(module));
    }

    public void deleteAll(){
        moduleDao.deleteAll();
    }
}
