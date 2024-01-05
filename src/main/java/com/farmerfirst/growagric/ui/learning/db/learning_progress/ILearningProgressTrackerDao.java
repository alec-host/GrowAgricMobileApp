package com.farmerfirst.growagric.ui.learning.db.learning_progress;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.math.BigDecimal;
import java.util.List;

@Dao
public interface ILearningProgressTrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<LearningProgressTracker> learningProgressTracker);

    @Update
    public void update(LearningProgressTracker learningProgressTracker);

    @Query("SELECT * FROM LearningProgressTracker ORDER BY tracker_id DESC")
    public List<LearningProgressTracker> getAllSync();

    @Query("SELECT * FROM LearningProgressTracker ORDER BY tracker_id DESC LIMIT 1")
    public List<LearningProgressTracker> getLastRecord();

    @Query("INSERT INTO LearningProgressTracker (course_uuid,hours_of_learning,date_created) VALUES (:input_course_uuid,:input_hours_of_learning,:input_date_created)")
    public void trackLearningHours(String input_course_uuid,String input_hours_of_learning,String input_date_created);

    @Query("SELECT SUM(hours_of_learning) AS total FROM LearningProgressTracker")
    public String totalHoursOfLearning();

    @Query("SELECT hours_of_learning AS intervals FROM LearningProgressTracker")
    public String hoursOfLearningPerInterval();

    @Query("SELECT COUNT(hours_of_learning) AS CNT FROM LearningProgressTracker")
    public int getCNT();

    @Query("UPDATE LearningProgressTracker SET hours_of_learning = :input_cummulative_hours_of_learning + :input_hours_of_learning WHERE course_uuid LIKE :input_course_uuid AND tracker_id LIKE :input_record_id")
    public void updateHoursOfLearning(String input_course_uuid,int input_record_id,String input_cummulative_hours_of_learning,String input_hours_of_learning);

    @Query("UPDATE LearningProgressTracker SET date_created = :input_date_created WHERE course_uuid LIKE :input_course_uuid AND tracker_id LIKE :input_record_id")
    public void resetLearningProgressTrackerDateCreated(String input_date_created,String input_course_uuid,int input_record_id);
}
