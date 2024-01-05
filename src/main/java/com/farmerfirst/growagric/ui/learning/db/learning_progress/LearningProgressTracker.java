package com.farmerfirst.growagric.ui.learning.db.learning_progress;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity(indices = {@Index(value="course_uuid",unique = false)})
public class LearningProgressTracker {
    @PrimaryKey(autoGenerate = true)
    int tracker_id;

    @ColumnInfo(name="course_uuid")
    String course_uuid;

    @ColumnInfo(name="hours_of_learning",defaultValue = "0")
    String hours_of_learning;

    @ColumnInfo(name="date_created")
    String date_created;

    public LearningProgressTracker(){}

    public LearningProgressTracker(String course_uuid,String hours_of_learning,String date_created){
        this.course_uuid = course_uuid;
        this.hours_of_learning = hours_of_learning;
        this.date_created = date_created;
    }

    public int getTracker_id() {
        return tracker_id;
    }

    public void setTracker_id(int tracker_id) {
        this.tracker_id = tracker_id;
    }

    public String getCourse_uuid() {
        return course_uuid;
    }

    public void setCourse_uuid(String course_uuid) {
        this.course_uuid = course_uuid;
    }

    public String getHours_of_learning() {
        return hours_of_learning;
    }

    public void setHours_of_learning(String hours_of_learning) {
        this.hours_of_learning = hours_of_learning;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}

