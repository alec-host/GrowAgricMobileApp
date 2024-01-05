package com.farmerfirst.growagric.ui.record_keeping.db;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger.SimpleLedger;
import com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger.SimpleLedgerAndroidViewModel;
import com.farmerfirst.growagric.utils.Utils;

import java.util.Date;

public class SimpleLedgerController {
    private SimpleLedgerAndroidViewModel viewModel;
    private ViewModelProvider provider;

    public SimpleLedgerController(ViewModelProvider mprovider){
        this.provider = mprovider;
    }
    public void storeToDB(
                           String transaction_uuid,
                           String farm_uuid,
                           String user_uuid,
                           String cr,
                           String dr,
                           String running_balance,
                           String description,
                           String record_type,
                           String notes,
                           String entry_date){
        viewModel = provider.get(SimpleLedgerAndroidViewModel.class);
        System.out.println(transaction_uuid+" "+farm_uuid+" "+user_uuid+" "+cr+" "+running_balance +" " +description +"  " +record_type);
        viewModel.saveSimpleLedger(new SimpleLedger(transaction_uuid,farm_uuid,user_uuid,cr,dr,running_balance,description,record_type,notes,entry_date,Utils.simpleDate().format(new Date())));
    }
}