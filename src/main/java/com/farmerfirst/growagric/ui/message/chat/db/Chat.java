package com.farmerfirst.growagric.ui.message.chat.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="chat_uuid",unique = true)})
public class Chat {

    @PrimaryKey(autoGenerate = true)
    int chat_id;

    @ColumnInfo(name="chat_uuid")
    String chat_uuid;

    @ColumnInfo(name="farmer_uuid")
    String farmer_uuid;

    @ColumnInfo(name="message")
    String message;

    @ColumnInfo(name="message_origin",defaultValue = "0")
    int message_origin;

    @ColumnInfo(name="date_created")
    String date_created;

    @ColumnInfo(name="is_deleted",defaultValue = "0")
    int is_deleted;

    public Chat(){}

    public Chat(String chat_uuid,String farmer_uuid,String message,int message_origin,String date_created){
        this.chat_uuid = chat_uuid;
        this.farmer_uuid = farmer_uuid;
        this.message = message;
        this.message_origin = message_origin;
        this.date_created = date_created;
    }

    public int getChat_id() {
        return chat_id;
    }

    public String getChat_uuid() {
        return chat_uuid;
    }

    public String getFarmer_uuid() {
        return farmer_uuid;
    }

    public String getMessage() {
        return message;
    }

    public int getMessage_origin() {
        return message_origin;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public String getDate_created() {
        return date_created;
    }
}
