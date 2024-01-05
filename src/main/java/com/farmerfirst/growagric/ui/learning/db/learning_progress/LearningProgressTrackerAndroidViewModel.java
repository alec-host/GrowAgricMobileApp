package com.farmerfirst.growagric.ui.learning.db.learning_progress;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LearningProgressTrackerAndroidViewModel extends AndroidViewModel {
    private ILearningProgressTrackerDao learningProgressTrackerDao;
    private ExecutorService executorService;

    public LearningProgressTrackerAndroidViewModel(@NonNull Application application){
        super(application);
        learningProgressTrackerDao = LearningProgressDatabase.getInstance(application).learningProgressTrackerDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public List<LearningProgressTracker> getAllLearningProgressTrackerSync(){
        return learningProgressTrackerDao.getAllSync();
    }

    public List<LearningProgressTracker> getLastRecordSync(){
        return learningProgressTrackerDao.getLastRecord();
    }

    public void trackLearningProgress(String input_course_uuid,String input_hours_of_learning, String input_date_created){
        learningProgressTrackerDao.trackLearningHours(input_course_uuid,input_hours_of_learning,input_date_created);
    }

    public String totalSumLearningHours(){
        return learningProgressTrackerDao.totalHoursOfLearning();
    }

    public int getCount(){
        return learningProgressTrackerDao.getCNT();
    }

    public void updateHourOfLearning(String input_course_uuid,int input_record_id,String input_cummulative_hours_of_learning,String input_hours_of_learning){
        executorService.execute(()-> learningProgressTrackerDao.updateHoursOfLearning(input_course_uuid,input_record_id,input_cummulative_hours_of_learning,input_hours_of_learning));
    }
}
