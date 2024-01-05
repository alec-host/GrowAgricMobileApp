package com.farmerfirst.growagric.ui.learning.db;

import androidx.lifecycle.ViewModelProvider;

import com.farmerfirst.growagric.ui.learning.db.course.Course;
import com.farmerfirst.growagric.ui.learning.db.course.CourseAndroidViewModel;

import java.util.List;

public class CourseController {
    private CourseAndroidViewModel viewModel;
    private ViewModelProvider provider;
    public CourseController(ViewModelProvider mProvider){
        this.provider = mProvider;
    }

    public void updateLearningCoursePath(String path,String course_uuid){
        String input_flag = "1";
        viewModel = provider.get(CourseAndroidViewModel.class);
        viewModel.updateCoursePath(path,input_flag,course_uuid);
    }

    public int isCourseDownloaded(String course_uuid){
        viewModel = provider.get(CourseAndroidViewModel.class);
        int downloaded = viewModel.isCourseDownloaded(course_uuid);

        return downloaded;
    }

    public List<Course> getDownloadedCourseList(){
        viewModel = provider.get(CourseAndroidViewModel.class);
        List<Course> downloadedCourseList = viewModel.downloadedCourseList();

        return downloadedCourseList;
    }

    public int getDownloadedCourseCount(){
        viewModel = provider.get(CourseAndroidViewModel.class);
        int count = viewModel.getDownloadedCourseCount();

        return count;
    }
}
