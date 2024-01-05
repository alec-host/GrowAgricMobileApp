package com.farmerfirst.growagric.ui.learning.db;

import androidx.lifecycle.ViewModelProvider;

import com.farmerfirst.growagric.ui.learning.db.course.CourseAndroidViewModel;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgress;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgressAndroidViewModel;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgressTracker;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgressTrackerAndroidViewModel;
import com.farmerfirst.growagric.utils.Utils;

import java.util.Date;
import java.util.List;

public class LearningProgressController {
    private LearningProgressAndroidViewModel viewModel;
    private LearningProgressTrackerAndroidViewModel viewModelTracker;
    private ViewModelProvider provider;
    public LearningProgressController(ViewModelProvider mprovider){
        this.provider = mprovider;
    }

    public void storeStartData(
            String progress_uuid,
            String course_uuid,
            String course_name,
            String user_uuid,
            String name,
            int total_pages,
            int current_page,
            String start_time){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        viewModel.saveLearningProgress(new LearningProgress(progress_uuid,course_uuid,course_name,user_uuid,name,total_pages,current_page,start_time,Utils.simpleDate().format(new Date())));
    }

    public void storeEndData(
            String course_uuid,
            String user_uuid,
            int total_pages,
            int current_page,
            String end_time){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        viewModel.learningCourseEndTime(current_page,end_time,course_uuid,user_uuid);
    }

    public void storeResetStarEndtData(
            String course_uuid,
            String user_uuid){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        viewModel.startEndTimeReset(course_uuid,user_uuid,Utils.simpleDate().format(new Date()),Utils.simpleDate().format(new Date()));
    }

    public void storeMarkAsCompleted(
            String course_uuid,
            String user_uuid,
            int current_page){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        viewModel.learningCourseCompleted(current_page,Utils.simpleDate().format(new Date()),course_uuid,user_uuid);
    }

    public void storeTrackedLearningHours(
            String course_uuid,
            String hours_of_learning){
        viewModelTracker = provider.get(LearningProgressTrackerAndroidViewModel.class);
        viewModelTracker.trackLearningProgress(course_uuid,hours_of_learning,Utils.simpleDate().format(new Date()));
    }

    public List<LearningProgressTracker> storeGetLastRecord(){
        viewModelTracker = provider.get(LearningProgressTrackerAndroidViewModel.class);
        return viewModelTracker.getLastRecordSync();
    }

    public void deleteLearningProgressLog(){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        viewModel.deleteAll();
    }

    public List<LearningProgress> getPageInfo(String course_uuid,String user_uuid){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        return viewModel.getCurrentPage(course_uuid,user_uuid);
    }

    public List<LearningProgress> getRecords(){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        return viewModel.getAllLearnigProgressSync();
    }

    public int getCompletedCourse(String user_uuid){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        return viewModel.getCompletedCourseCount(user_uuid);
    }

    public int getCNT(String user_uuid){
        viewModel = provider.get(LearningProgressAndroidViewModel.class);
        return viewModel.getLearningProgressCNT(user_uuid);
    }

    public String getTotalHoursOfLearning(){
        viewModelTracker = provider.get(LearningProgressTrackerAndroidViewModel.class);
        return viewModelTracker.totalSumLearningHours();
    }

    public int getTrackerCount(){
        viewModelTracker = provider.get(LearningProgressTrackerAndroidViewModel.class);
        return viewModelTracker.getCount();
    }

    public void updateHoursOfLearning(String input_course_uuid,int input_record_id,String input_cummulative_hours_of_learning,String input_hours_of_learning){
        viewModelTracker = provider.get(LearningProgressTrackerAndroidViewModel.class);
        viewModelTracker.updateHourOfLearning(input_course_uuid,input_record_id,input_cummulative_hours_of_learning,input_hours_of_learning);
    }
}
