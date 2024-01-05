package com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="transaction_uuid",unique = true)})
public class SimpleLedger {
    @PrimaryKey(autoGenerate = true)
    int trx_id;

    @ColumnInfo(name="transaction_uuid")
    String transaction_uuid;

    @ColumnInfo(name="farm_uuid")
    String farm_uuid;

    @ColumnInfo(name="user_uuid")
    String user_uuid;

    @ColumnInfo(name="cr")
    String cr;

    @ColumnInfo(name="dr")
    String dr;

    @ColumnInfo(name="running_balance")
    String running_balance;

    @ColumnInfo(name="description")
    String description;

    @ColumnInfo(name="record_type")
    String record_type;

    @ColumnInfo(name="notes")
    String notes;

    @ColumnInfo(name="entry_date")
    String entry_date;

    @ColumnInfo(name="date_created")
    String date_created;

    @ColumnInfo(name="is_deleted",defaultValue = "0")
    String is_deleted;

    public SimpleLedger(){}

    public SimpleLedger(String transaction_uuid,String farm_uuid,String user_uuid,String cr,String dr,String running_balance,String description,String record_type,String notes,String entry_date,String date_created){
        this.transaction_uuid = transaction_uuid;
        this.farm_uuid = farm_uuid;
        this.user_uuid = user_uuid;
        this.cr = cr;
        this.dr = dr;
        this.running_balance = running_balance;
        this.description = description;
        this.record_type = record_type;
        this.notes = notes;
        this.entry_date = entry_date;
        this.date_created = date_created;
    }

    public String getTransaction_uuid() {
        return transaction_uuid;
    }

    public void setTransaction_uuid(String transaction_uuid) {
        this.transaction_uuid = transaction_uuid;
    }

    public String getFarm_uuid() {
        return farm_uuid;
    }

    public void setFarm_uuid(String farm_uuid) {
        this.farm_uuid = farm_uuid;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getRunning_balance() {
        return running_balance;
    }

    public void setRunning_balance(String running_balance) {
        this.running_balance = running_balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getRecord_type() {
        return record_type;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }
}
