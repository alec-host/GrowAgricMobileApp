package com.farmerfirst.growagric.ui.farm_visit.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerfirst.growagric.ui.record_keeping.db.person.IPersonDao;
import com.farmerfirst.growagric.ui.record_keeping.db.person.Person;
import com.farmerfirst.growagric.ui.record_keeping.db.person.PersonDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VisitAndroidViewModel extends AndroidViewModel {
    private IPersonDao personDao;
    private ExecutorService executorService;

    public VisitAndroidViewModel(@NonNull Application application){
        super(application);
        personDao = PersonDatabase.getInstance(application).personDao();
        executorService = Executors.newSingleThreadExecutor();
    }

//    public LiveData<List<Person>> getAllPerson(){return personDao.getAll();}

    //public List<Person> getAllPersonSynchronously(){return personDao.getAllSync();}

    public int getPersonCNT(String input_user_uuid){
        return personDao.getCount(input_user_uuid);
    }

    //public LiveData<List<Person>> getSearchedPerson(String input_user_uuid){
    //    return personDao.searchPerson(input_user_uuid);
    //}

    public void savePerson(Person person){
        executorService.execute(()->personDao.save(person));
    }

    public void saveAllPerson(List<Person> input){
        executorService.execute(()->personDao.saveAll(input));
    }

    public void deletePerson(Person person){
        executorService.execute(()->personDao.delete(person));
    }

    public void deleteAll(){
        personDao.deleteAll();
    }
}
