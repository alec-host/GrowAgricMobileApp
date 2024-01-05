package com.farmerfirst.growagric.ui.farm.recyclerview.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="farm_uuid",unique = true)})
public class Farm {

    @PrimaryKey(autoGenerate = true)
    int farm_id;

    @ColumnInfo(name="farm_uuid")
    String farm_uuid;

    @ColumnInfo(name="user_uuid")
    String user_uuid;

    @ColumnInfo(name="county")
    String county;

    @ColumnInfo(name="sub_county")
    String sub_county;

    @ColumnInfo(name="ward")
    String ward;

    @ColumnInfo(name="num_of_employees")
    String num_of_employees;

    @ColumnInfo(name="num_of_years")
    String num_of_years;

    @ColumnInfo(name="house_capacity")
    String house_capacity;

    @ColumnInfo(name="mortality_rate")
    String mortality_rate;

    @ColumnInfo(name="item_farmed")
    String item_farmed;

    @ColumnInfo(name="is_insured")
    String is_insured;

    @ColumnInfo(name="insurer_name")
    String insurer_name;

    @ColumnInfo(name="date_created")
    String date_created;

    public Farm(){}

    public Farm(String farm_uuid, String user_uuid, String county, String sub_county, String ward, String num_of_employees, String num_of_years, String house_capacity, String mortality_rate, String item_farmed, String is_insured, String insurer_name, String date_created ){
        this.farm_uuid=farm_uuid;
        this.user_uuid=user_uuid;
        this.county=county;
        this.sub_county=sub_county;
        this.ward=ward;
        this.num_of_employees=num_of_employees;
        this.num_of_years=num_of_years;
        this.house_capacity=house_capacity;
        this.mortality_rate=mortality_rate;
        this.item_farmed=item_farmed;
        this.is_insured=is_insured;
        this.insurer_name=insurer_name;
        this.date_created=date_created;
    }

    public int getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(int farm_id) {this.farm_id = farm_id;}

    public String getFarm_uuid() {return farm_uuid;}

    public void setFarm_uuid(String farm_uuid){this.farm_uuid = farm_uuid;}

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSub_county() {
        return sub_county;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getNum_of_employees() {
        return num_of_employees;
    }

    public void setNum_of_employees(String num_of_employees) {
        this.num_of_employees = num_of_employees;
    }

    public String getNum_of_years() {
        return num_of_years;
    }

    public void setNum_of_years(String num_of_years) {
        this.num_of_years = num_of_years;
    }

    public String getHouse_capacity() {
        return house_capacity;
    }

    public void setHouse_capacity(String house_capacity) {
        this.house_capacity = house_capacity;
    }

    public String getMortality_rate() {
        return mortality_rate;
    }

    public void setMortality_rate(String mortality_rate) {
        this.mortality_rate = mortality_rate;
    }

    public String getItem_farmed() {
        return item_farmed;
    }

    public void setItem_farmed(String item_farmed) {
        this.item_farmed = item_farmed;
    }

    public String getIs_insured() {
        return is_insured;
    }

    public void setIs_insured(String is_insured) {
        this.is_insured = is_insured;
    }

    public String getInsurer_name() {
        return insurer_name;
    }

    public void setInsurer_name(String insurer_name) {
        this.insurer_name = insurer_name;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
