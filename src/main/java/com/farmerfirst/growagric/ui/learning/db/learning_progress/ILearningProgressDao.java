package com.farmerfirst.growagric.ui.learning.db.learning_progress;

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
public interface ILearningProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveAll(List<LearningProgress> learningProgresses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(LearningProgress learningProgress);

    @Update
    public void update(LearningProgress learningProgress);

    @Query("SELECT * FROM LearningProgress ORDER BY _id DESC")
    public LiveData<List<LearningProgress>> getAll();

    @Query("SELECT * FROM LearningProgress ORDER BY _id DESC")
    public List<LearningProgress> getAllSync();

    @Query("SELECT * FROM LearningProgress WHERE user_uuid LIKE :input_user_uuid ORDER BY _id DESC")
    public LiveData<List<LearningProgress>> searchProgress(String input_user_uuid);

    @Query("SELECT COUNT(_id) FROM LearningProgress WHERE user_uuid LIKE :input_user_uuid AND is_archived=0")
    public int getCount(String input_user_uuid);

    @Query("SELECT _id,course_uuid,course_name,user_uuid,name,total_pages,current_page,start_time,end_time,completed,is_saved,date_created,is_archived FROM LearningProgress WHERE course_uuid LIKE :input_course_uuid AND user_uuid LIKE :input_user_uuid AND is_archived=0")
    public List<LearningProgress>getCurrentPage(String input_course_uuid,String input_user_uuid);

    @Query("SELECT COUNT(_id) FROM LearningProgress WHERE user_uuid LIKE :input_user_uuid AND completed=1 AND is_archived=0")
    public int getCompletedCNT(String input_user_uuid);

    @Query("SELECT COUNT(_id) FROM LearningProgress WHERE user_uuid LIKE :input_user_uuid AND is_saved=1 AND is_archived=0")
    public int getSavedCNT(String input_user_uuid);

    @Ignore
    @Query("SELECT _id,course_uuid,course_name,user_uuid,name,total_pages,current_page,start_time,end_time,completed,hours_of_learning,is_saved,date_created,is_archived FROM LearningProgress")
    public List<LearningProgress>getLearningProgressID();

    @Query("UPDATE LearningProgress SET current_page =:input_current_page,end_time = :input_end_date WHERE course_uuid LIKE :input_course_uuid AND user_uuid LIKE :input_user_uuid AND is_archived=0")
    public void updateEndTime(int input_current_page,String input_end_date,String input_course_uuid,String input_user_uuid);

    @Query("UPDATE LearningProgress SET current_page = :input_current_page,end_time = :input_end_time,completed = 1 WHERE course_uuid LIKE :input_course_uuid AND user_uuid LIKE :input_user_uuid AND is_archived=0")
    public void updateCompleted(int input_current_page,String input_end_time,String input_course_uuid,String input_user_uuid);

    @Query("UPDATE LearningProgress SET is_saved = :input_is_saved WHERE course_uuid LIKE :input_course_uuid AND user_uuid LIKE :input_user_uuid AND is_archived=0")
    public void updateIsSaved(String input_is_saved,String input_course_uuid,String input_user_uuid);

    @Query("UPDATE LearningProgress SET start_time = :input_start_date,end_time = :input_end_time,date_created = :input_end_time WHERE course_uuid LIKE :input_course_uuid AND user_uuid LIKE :input_user_uuid AND is_archived=0")
    public void invalidateStartEndTime(String input_course_uuid,String input_user_uuid,String input_start_date,String input_end_time);

    @Delete
    public void delete(LearningProgress learningProgress);

    @Query("DELETE FROM LearningProgress")
    void deleteAll();
}
