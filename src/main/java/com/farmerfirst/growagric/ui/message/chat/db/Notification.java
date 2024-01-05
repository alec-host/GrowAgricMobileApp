package com.farmerfirst.growagric.ui.message.chat.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="notification_uuid",unique = true)})
public class Notification {

    @PrimaryKey(autoGenerate = true)
    int _id;

    @ColumnInfo(name="notification_uuid")
    String notification_uuid;

    @ColumnInfo(name="subject")
    String subject;

    @ColumnInfo(name="message")
    String message;

    @ColumnInfo(name="date_created")
    String date_created;

    @ColumnInfo(name="is_deleted",defaultValue = "0")
    int is_deleted;

    public Notification() {}

    public Notification(String notification_uuid,String subject,String message,String date_created){
        this.notification_uuid = notification_uuid;
        this.subject = subject;
        this.message = message;
        this.date_created = date_created;
    }

    public String getNotification_uuid() {
        return notification_uuid;
    }

    public void setNotification_uuid(String notification_uuid) {
        this.notification_uuid = notification_uuid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
