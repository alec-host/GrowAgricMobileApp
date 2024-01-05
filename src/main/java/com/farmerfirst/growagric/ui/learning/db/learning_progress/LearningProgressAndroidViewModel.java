package com.farmerfirst.growagric.ui.learning.db.learning_progress;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LearningProgressAndroidViewModel extends AndroidViewModel{
    private ILearningProgressDao learningProgressDao;
    private ExecutorService executorService;

    public LearningProgressAndroidViewModel(@NonNull Application application){
        super(application);
        learningProgressDao = LearningProgressDatabase.getInstance(application).learningProgressDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<LearningProgress>> getAllLearnigProgress(){
        return learningProgressDao.getAll();
    }

    public List<LearningProgress> getAllLearnigProgressSync(){
        return learningProgressDao.getAllSync();
    }

    public void updateLearningProgress(LearningProgress learningProgress){
        executorService.execute(()->learningProgressDao.update(learningProgress));
    }

    public int getLearningProgressCNT(String input_user_uuid){
        return learningProgressDao.getCount(input_user_uuid);
    }

    public List<LearningProgress> getCurrentPage(String input_course_uuid,String input_user_uuid){
        return learningProgressDao.getCurrentPage(input_course_uuid,input_user_uuid);
    }

    public int getCompletedCourseCount(String input_user_uuid){
        return learningProgressDao.getCompletedCNT(input_user_uuid);
    }

    public int getSavedCourseCount(String input_user_uuid){
        return learningProgressDao.getSavedCNT(input_user_uuid);
    }

    public LiveData<List<LearningProgress>> getSearchedProgress(String input_user_uuid){
        return learningProgressDao.searchProgress(input_user_uuid);
    }

    public void saveLearningProgress(LearningProgress learningProgress){
        executorService.execute(()-> learningProgressDao.save(learningProgress));
    }

    public void saveAllLearningProgress(List<LearningProgress> input){executorService.execute(()-> learningProgressDao.saveAll(input));}

    public void learningCourseEndTime(int int_current_page,String input_end_date,String input_course_uuid,String input_user_uuid){
        executorService.execute(()->learningProgressDao.updateEndTime(int_current_page,input_end_date,input_course_uuid,input_user_uuid));
    }

    public void learningCourseCompleted(int input_current_page,String input_end_time,String input_course_uuid,String input_user_uuid){
        executorService.execute(()->learningProgressDao.updateCompleted(input_current_page,input_end_time,input_course_uuid,input_user_uuid));
    }

    public void learningCourseIsSaved(String input_is_saved,String input_course_uuid,String input_user_uuid){
        executorService.execute(()->learningProgressDao.updateIsSaved(input_is_saved,input_course_uuid,input_user_uuid));
    }

    public void startEndTimeReset(String input_course_uuid, String input_user_uuid, String input_start_date, String input_end_time){
        executorService.execute(()->learningProgressDao.invalidateStartEndTime(input_course_uuid,input_user_uuid,input_start_date,input_end_time));
    }

    public void deleteLearningProgress(LearningProgress learningProgress){
        executorService.execute(()-> learningProgressDao.delete(learningProgress));
    }

    public void deleteAll(){
        learningProgressDao.deleteAll();
    }
}
