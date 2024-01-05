package com.farmerfirst.growagric.ui.learning.db.course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseAndroidViewModel extends AndroidViewModel{
    private ICourseDao courseDao;
    private ExecutorService executorService;

    public CourseAndroidViewModel(@NonNull Application application){
        super(application);
        courseDao = CourseDatabase.getInstance(application).courseDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Course>> getAllCourse(){
        return courseDao.getAll();
    }

    public List<Course> getAllCourseSynchronously(){
        return courseDao.getAllSync();
    }

    public int getCourseCNT(String input_module_uuid){
        return courseDao.getCount(input_module_uuid);
    }

    public LiveData<List<Course>> getSearchedCourse(String input_module_uuid){
        return courseDao.searchCourse(input_module_uuid);
    }

    public void updateCoursePath(String input_path,String input_flag,String input_course_uuid){
        executorService.execute(()->courseDao.updateCoursePath(input_path,input_flag,input_course_uuid));
    }

    public int isCourseDownloaded(String input_course_uuid){
        return courseDao.getIsDownloadedCount(input_course_uuid);
    }

    public int getDownloadedCourseCount(){
        return courseDao.getIsDownloadedCourseCount();
    }

    public List<Course> downloadedCourseList(){
        return courseDao.getDownloadedCourseList();
    }

    public void saveCourse(Course course){
        executorService.execute(()->courseDao.save(course));
    }

    public void saveAllCourse(List<Course> input){executorService.execute(()->courseDao.saveAll(input));}

    public void deleteCourse(Course course){
        executorService.execute(()->courseDao.delete(course));
    }

    public void deleteAll(){
        courseDao.deleteAll();
    }
}
