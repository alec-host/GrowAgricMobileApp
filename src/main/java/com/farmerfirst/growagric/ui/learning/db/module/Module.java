package com.farmerfirst.growagric.ui.learning.db.module;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="module_uuid",unique = true)})
public class Module {

    @PrimaryKey(autoGenerate = true)
    int module_id;

    @ColumnInfo(name="module_uuid")
    String module_uuid;

    @ColumnInfo(name="topic")
    String topic;

    @ColumnInfo(name="description")
    String description;

    @ColumnInfo(name="date_created")
    String date_created;

    @ColumnInfo(name="is_deleted",defaultValue = "0")
    int is_deleted;

    public Module(){}

    public Module(String module_uuid,String topic,String description,String date_created,int is_deleted){
        this.module_uuid = module_uuid;
        this.topic = topic;
        this.description = description;
        this.date_created = date_created;
        this.is_deleted = is_deleted;
    }

    public String getModule_uuid() {
        return module_uuid;
    }

    public void setModule_uuid(String module_uuid) {
        this.module_uuid = module_uuid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }
}
