package com.farmerfirst.growagric.ui.farm_visit.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="visit_uuid",unique = true)})
public class Visit {
    @PrimaryKey(autoGenerate = true)
    int visit_id;

    @ColumnInfo(name="visit_uuid")
    String visit_uuid;

    @ColumnInfo(name="farm_uuid")
    String farm_uuid;

    @ColumnInfo(name="farmer_name")
    String farmer_name;

    @ColumnInfo(name="farmer_uuid")
    String farmer_uuid;


    @ColumnInfo(name="date_created")
    String date_created;

    @ColumnInfo(name="is_deleted",defaultValue = "0")
    int is_deleted;

    public Visit(){}

    public Visit(String visit_uuid, String farm_uuid, String farmer_name, String farmer_uuid, String date_created){
        this.visit_uuid = visit_uuid;
        this.farm_uuid = farm_uuid;
        this.farmer_name = farmer_name;
        this.farmer_uuid = farmer_uuid;
        this.date_created = date_created;
    }

    public int getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }

    public String getVisit_uuid() {
        return visit_uuid;
    }

    public void setVisit_uuid(String visit_uuid) {
        this.visit_uuid = visit_uuid;
    }

    public String getFarm_uuid() {
        return farm_uuid;
    }

    public void setFarm_uuid(String farm_uuid) {
        this.farm_uuid = farm_uuid;
    }

    public String getFarmer_uuid() {
        return farmer_uuid;
    }

    public void setFarmer_uuid(String farmer_uuid) {
        this.farmer_uuid = farmer_uuid;
    }

    public String getFarmer_name() {
        return farmer_name;
    }

    public void setFarmer_name(String farmer_name) {
        this.farmer_name = farmer_name;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
