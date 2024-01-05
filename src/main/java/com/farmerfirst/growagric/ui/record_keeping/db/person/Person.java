package com.farmerfirst.growagric.ui.record_keeping.db.person;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="person_uuid",unique = true)})
public class Person {
    @PrimaryKey(autoGenerate = true)
    int person_id;

    @ColumnInfo(name="person_uuid")
    String person_uuid;

    @ColumnInfo(name="person_name")
    String person_name;

    @ColumnInfo(name="id_number")
    String id_number;

    @ColumnInfo(name="phone_number")
    String phone_number;

    @ColumnInfo(name="location")
    String location;

    @ColumnInfo(name="person_type",defaultValue = "0")
    int person_type;

    @ColumnInfo(name="user_uuid")
    String user_uuid;

    @ColumnInfo(name="date_created")
    String date_created;

    @ColumnInfo(name="is_deleted",defaultValue = "0")
    int is_deleted;

    public Person(){}

    public Person(String person_uuid){
        this.person_uuid = person_uuid;
    }

    public Person(String person_uuid,String person_name,String phone_number,String id_number,String user_uuid,String date_created){
        this.person_uuid = person_uuid;
        this.person_name = person_name;
        this.phone_number = phone_number;
        this.id_number = id_number;
        this.user_uuid = user_uuid;
        this.date_created = date_created;
    }

    public Person(String person_uuid,String person_name,String phone_number,String location,int person_type,String user_uuid,String date_created){
        this.person_uuid = person_uuid;
        this.person_name = person_name;
        this.phone_number = phone_number;
        this.location = location;
        this.person_type = person_type;
        this.user_uuid = user_uuid;
        this.date_created = date_created;
    }

    public String getPerson_uuid() {
        return person_uuid;
    }

    public void setPerson_uuid(String person_uuid) {
        this.person_uuid = person_uuid;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPerson_type() {
        return person_type;
    }

    public void setPerson_type(int person_type) {
        this.person_type = person_type;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
