package com.farmerfirst.growagric.ui.learning.db.course;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ICourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<Course> course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Course course);

    @Update
    public void update(Course course);

    @Query("SELECT * FROM Course ORDER BY course_id DESC")
    public LiveData<List<Course>> getAll();

    @Query("SELECT * FROM Course ORDER BY course_id DESC")
    public List<Course> getAllSync();

    @Query("SELECT * FROM Course WHERE module_uuid LIKE :input_module_uuid ORDER BY course_id DESC")
    public LiveData<List<Course>> searchCourse(String input_module_uuid);

    @Query("SELECT COUNT(course_id) FROM Course WHERE module_uuid LIKE :input_module_uuid AND is_deleted=0")
    public int getCount(String input_module_uuid);

    @Ignore
    @Query("SELECT course_id,course_uuid,course_name,module_uuid,description,filename,path,date_created,is_downloaded FROM Course")
    public List<Course>getCourseID();

    @Query("SELECT COUNT(course_id) FROM Course WHERE course_uuid LIKE :input_course_uuid AND is_downloaded=1")
    public int getIsDownloadedCount(String input_course_uuid);

    @Query("SELECT COUNT(course_id) FROM Course WHERE is_downloaded=1")
    public int getIsDownloadedCourseCount();

    @Query("SELECT course_id,course_uuid,path FROM Course WHERE is_downloaded=1")
    public List<Course> getDownloadedCourseList();

    @Query("UPDATE Course SET path=:input_path,is_downloaded=:input_flag WHERE course_uuid LIKE :input_course_uuid")
    public void updateCoursePath(String input_path,String input_flag,String input_course_uuid);

    @Delete
    public void delete(Course course);

    @Query("DELETE FROM Course")
    void deleteAll();
}
