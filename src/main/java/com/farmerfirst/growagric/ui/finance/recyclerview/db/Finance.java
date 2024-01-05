package com.farmerfirst.growagric.ui.finance.recyclerview.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value="application_uuid",unique = true)})

public class Finance {
    @PrimaryKey(autoGenerate = true)
    int finance_id;

    @ColumnInfo(name="application_uuid")
    String application_uuid;

    @ColumnInfo(name="user_uuid")
    String user_uuid;

    @ColumnInfo(name="loan_amount")
    String loan_amount;

    @ColumnInfo(name="chick_cost")
    String chick_cost;

    @ColumnInfo(name="feed_cost")
    String feed_cost;

    @ColumnInfo(name="brooding_cost")
    String brooding_cost;

    @ColumnInfo(name="vaccine_medicine_cost")
    String vaccine_medicine_cost;

    @ColumnInfo(name="date_required")
    String date_required;

    @ColumnInfo(name="financial_sponsor")
    String financial_sponsor;

    @ColumnInfo(name="application_status")
    String application_status;

    @ColumnInfo(name="number_of_chicks_raised_now")
    String number_of_chicks_raised_now;

    @ColumnInfo(name="projected_sales_price_per_chick")
    String projected_sales_price_per_chick;

    @ColumnInfo(name="date_created")
    String date_created;

    public Finance(){}

    public Finance(String application_uuid,String user_uuid,String loan_amount,String chick_cost,String feed_cost,String brooding_cost,String vaccine_medicine_cost,String date_required,String financial_sponsor,String application_status,String number_of_chicks_raised_now,String projected_sales_price_per_chick,String date_created){
        this.application_uuid=application_uuid;
        this.user_uuid=user_uuid;
        this.loan_amount=loan_amount;
        this.chick_cost=chick_cost;
        this.feed_cost=feed_cost;
        this.brooding_cost=brooding_cost;
        this.vaccine_medicine_cost=vaccine_medicine_cost;
        this.date_required=date_required;
        this.financial_sponsor=financial_sponsor;
        this.application_status=application_status;
        this.number_of_chicks_raised_now=number_of_chicks_raised_now;
        this.projected_sales_price_per_chick=projected_sales_price_per_chick;
        this.date_created=date_created;
    }

    public int getFinance_id() {return finance_id;}
    public String getApplication_uuid() {return application_uuid;}
    public String getUser_uuid() {return user_uuid;}
    public String getLoan_amount() {return loan_amount;}
    public String getChick_cost() {return chick_cost;}
    public String getFeed_cost() {return feed_cost;}
    public String getBrooding_cost() {return brooding_cost;}
    public String getVaccine_medicine_cost() {return vaccine_medicine_cost;}
    public String getDate_required() {return date_required;}
    public String getFinancial_sponsor() {return financial_sponsor;}
    public String getApplication_status() {return application_status;}
    public String getNumber_of_chicks_raised_now() {return number_of_chicks_raised_now;}
    public String getProjected_sales_price_per_chick() {return projected_sales_price_per_chick;}
    public String getDate_created() {return date_created;}
}
