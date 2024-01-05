package com.farmerfirst.growagric.ui.finance.recyclerview.db;

import androidx.lifecycle.ViewModelProvider;

import com.farmerfirst.growagric.ui.finance.recyclerview.FinanceAndroidViewModel;

public class FinanceController {
    private FinanceAndroidViewModel viewModel;
    private ViewModelProvider provider;

    public FinanceController(ViewModelProvider mprovider){
        this.provider = mprovider;
    }

    public void storeToDB(String application_uuid,
                           String user_uuid,
                           String loan_amount,
                           String chick_cost,
                           String feed_cost,
                           String brooding_cost,
                           String vaccine_medicine_cost,
                           String date_required,
                           String financial_sponsor,
                           String application_status,
                           String number_of_chicks_raised_now,
                           String projected_sales_price_per_chick,
                           String date_created){
        viewModel = provider.get(FinanceAndroidViewModel.class);
        viewModel.saveFinance(new Finance(application_uuid,user_uuid,loan_amount,chick_cost,feed_cost,brooding_cost,vaccine_medicine_cost,date_required,financial_sponsor,application_status,number_of_chicks_raised_now,projected_sales_price_per_chick,date_created));
    }
}
