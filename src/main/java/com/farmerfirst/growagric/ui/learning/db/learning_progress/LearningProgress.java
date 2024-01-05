package com.farmerfirst.growagric.ui.learning.db.learning_progress;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="course_uuid",unique = true)})
public class LearningProgress {
    @PrimaryKey(autoGenerate = true)
    int _id;

    @ColumnInfo(name="progress_uuid")
    String progress_uuid;

    @ColumnInfo(name="course_uuid")
    String course_uuid;

    @ColumnInfo(name="course_name")
    String course_name;

    @ColumnInfo(name="user_uuid")
    String user_uuid;

    @ColumnInfo(name="name")
    String name;

    @ColumnInfo(name="total_pages")
    int total_pages;

    @ColumnInfo(name="current_page")
    int current_page;

    @ColumnInfo(name="start_time",defaultValue = "0")
    String start_time;

    @ColumnInfo(name="end_time",defaultValue = "0")
    String end_time;

    @ColumnInfo(name="hours_of_learning",defaultValue = "0")
    String hours_of_learning;

    @ColumnInfo(name="completed",defaultValue = "0")
    int completed;

    @ColumnInfo(name="is_saved",defaultValue = "0")
    int is_saved;

    @ColumnInfo(name="date_created")
    String date_created;

    @ColumnInfo(name="is_archived",defaultValue = "0")
    int is_archived;

    public LearningProgress(){}

    public LearningProgress(String progress_uuid,String course_uuid,String course_name,String user_uuid,String name,int total_pages,int current_page,String start_time,String date_created){
        this.progress_uuid = progress_uuid;
        this.course_uuid = course_uuid;
        this.course_name = course_name;
        this.user_uuid = user_uuid;
        this.name = name;
        this.total_pages = total_pages;
        this.current_page = current_page;
        this.start_time = start_time;
        this.date_created = date_created;
    }

    public LearningProgress(String course_uuid,String user_uuid,int total_pages,int current_page,String end_time){
        this.course_uuid = course_uuid;
        this.user_uuid = user_uuid;
        this.total_pages = total_pages;
        this.current_page = current_page;
        this.end_time = end_time;
    }

    public String getProgress_uuid() {
        return progress_uuid;
    }

    public void setProgress_uuid(String progress_uuid) {
        this.progress_uuid = progress_uuid;
    }

    public String getCourse_uuid() {
        return course_uuid;
    }

    public void setCourse_uuid(String course_uuid) {
        this.course_uuid = course_uuid;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}