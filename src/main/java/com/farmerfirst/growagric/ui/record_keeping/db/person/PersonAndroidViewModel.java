package com.farmerfirst.growagric.ui.record_keeping.db.person;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonAndroidViewModel extends AndroidViewModel {
    private IPersonDao personDao;
    private ExecutorService executorService;

    public PersonAndroidViewModel(@NonNull Application application){
        super(application);
        personDao = PersonDatabase.getInstance(application).personDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Person>> getAllPerson(int person_type){return personDao.getAll(person_type);}

    public List<Person> getAllPersonSynchronously(int person_type){return personDao.getAllSync(person_type);}

    public int getPersonCNT(String input_user_uuid){
        return personDao.getCount(input_user_uuid);
    }

    public LiveData<List<Person>> getSearchedPerson(String input_user_uuid,String input_search_name){
        return personDao.searchPerson(input_user_uuid,input_search_name);
    }

    public List<Person> getSearchedPersonSync(String input_user_uuid,String input_search_name,int input_person_type){
        return personDao.searchPersonSync(input_user_uuid,input_search_name,input_person_type);
    }

    public void savePerson(Person person){
        executorService.execute(()->personDao.save(person));
    }

    public void saveAllPerson(List<Person> input){
        executorService.execute(()->personDao.saveAll(input));
    }

    public void deleteByUUID(String person_uuid){
        executorService.execute(()->personDao.deleteByUUID(person_uuid));
    }

    public void deletePerson(Person person){
        executorService.execute(()->personDao.delete(person));
    }

    public void deleteAll(){
        personDao.deleteAll();
    }
}
