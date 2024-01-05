package com.farmerfirst.growagric.ui.learning.db.course;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="course_uuid",unique = true)})
public class Course {
    @PrimaryKey(autoGenerate = true)
    int course_id;

    @ColumnInfo(name="course_uuid")
    String course_uuid;

    @ColumnInfo(name="module_uuid")
    String module_uuid;

    @ColumnInfo(name="course_name")
    String course_name;

    @ColumnInfo(name="description")
    String description;

    @ColumnInfo(name="filename")
    String filename;

    @ColumnInfo(name="path")
    String path;

    @ColumnInfo(name="date_created")
    String date_created;

    @ColumnInfo(name="is_downloaded",defaultValue = "0")
    String is_downloaded;

    @ColumnInfo(name="is_deleted",defaultValue = "0")
    String is_deleted;

    public Course(){}

    public Course(String course_uuid,String module_uuid,String course_name,String description,String filename,String path,String date_created){
        this.course_uuid = course_uuid;
        this.module_uuid = module_uuid;
        this.course_name = course_name;
        this.description = description;
        this.filename = filename;
        this.path = path;
        this.date_created = date_created;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_uuid() {
        return course_uuid;
    }

    public void setCourse_uuid(String course_uuid) {
        this.course_uuid = course_uuid;
    }

    public String getModule_uuid() {
        return module_uuid;
    }

    public void setModule_uuid(String module_uuid) {
        this.module_uuid = module_uuid;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getIs_downloaded() {
        return is_downloaded;
    }

    public void setIs_downloaded(String is_downloaded) {
        this.is_downloaded = is_downloaded;
    }
}