package com.farmerfirst.growagric.ui.record_keeping.view_records;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityEmptyBinding;
import com.farmerfirst.growagric.databinding.ActivityViewRecordBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.record_keeping.RecordKeepingDashboardActivity;
import com.farmerfirst.growagric.ui.record_keeping.db.person.Person;
import com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger.SimpleLedger;
import com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger.SimpleLedgerAdapter;
import com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger.SimpleLedgerAndroidViewModel;
import com.farmerfirst.growagric.ui.record_keeping.input.AddEmployeeActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ViewRecordActivity extends AppCompatActivity implements SimpleLedgerAdapter.OnDeleteButtonListener,SimpleLedgerAdapter.SimpleLedgerAdapterListener{
    private ActivityViewRecordBinding binding;
    private ActivityEmptyBinding binding2;
    private SimpleLedgerAdapter simpleLedgerAdapter;
    private SimpleLedgerAndroidViewModel androidViewModel;
    private int switchLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalSharedPreferences prefs = new LocalSharedPreferences(ViewRecordActivity.this);
        androidViewModel = new ViewModelProvider(this).get(SimpleLedgerAndroidViewModel.class);
        if(androidViewModel.getSimpleLedgerCNT() == 0) {
            binding2 = DataBindingUtil.setContentView(ViewRecordActivity.this,R.layout.activity_empty);
            switchLayout = 0;
        }else{
            binding = DataBindingUtil.setContentView(ViewRecordActivity.this, R.layout.activity_view_record);
            switchLayout = 1;
        }

        ComponentUtils.customActionBar("Records", ViewRecordActivity.this);

        if(switchLayout == 1) {
            initSpinners(getResources());
            searchRecordType();
            setupRecyclerView(prefs);
        }
    }

    private void setupRecyclerView(LocalSharedPreferences prefs){
        simpleLedgerAdapter = new SimpleLedgerAdapter(ViewRecordActivity.this,this,this);
        androidViewModel = new ViewModelProvider(this).get(SimpleLedgerAndroidViewModel.class);
        androidViewModel.getAllSimpleLedger().observe(this, ledgers -> simpleLedgerAdapter.setData(ledgers));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.ledgerRecyclerView.setLayoutManager(mLayoutManager);
        binding.ledgerRecyclerView.setHasFixedSize(true);
        binding.ledgerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.ledgerRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        binding.ledgerRecyclerView.setAdapter(simpleLedgerAdapter);
    }

    private void initSpinners(Resources res){
        List<String> farmList = new ArrayList<String>();
        List<String> farmReferenceList = new ArrayList<String>();

        String[] recordTypeItemData = res.getStringArray(R.array.recordTypeItems);
        ArrayList<String> recordTypeItemList = new ArrayList<>(Arrays.asList(recordTypeItemData));
        if(!recordTypeItemList.isEmpty() && recordTypeItemList!=null) {
            CustomAdapter adapter = new CustomAdapter(this,recordTypeItemList);
            binding.spinRecordType.setAdapter(adapter);
        }

        binding.spinRecordType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //binding.editFarmID.setText(farmReferenceList.get(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void searchRecordType(){
        int record_type = 0;

        binding.spinRecordType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String search_value = binding.spinRecordType.getSelectedItem().toString().toLowerCase().replace("input","").replace("records","").trim();
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX   "+search_value);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filter(search_value);
                    }
                },50);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filter(String search_value) {
        LocalSharedPreferences prefs = new LocalSharedPreferences(ViewRecordActivity.this);
        String user_uuid = LocalData.GetUserUUID(prefs);
        List<SimpleLedger> searchList = new ArrayList<>();
        int person_type = 0;
        searchList = androidViewModel.getSearchRecordSync(user_uuid,search_value);
        if (searchList.isEmpty()) {
            Toast.makeText(ViewRecordActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
            simpleLedgerAdapter.setData(searchList);
            DataBindingUtil.setContentView(ViewRecordActivity.this,R.layout.activity_empty);
        } else {
            simpleLedgerAdapter.setData(searchList);
        }
    }

    @Override
    public void onDeleteButtonClicked(SimpleLedger simpleLedger) {
        androidViewModel.deleteSimpleLedger(simpleLedger);
    }
    @Override
    public void onSimpleLegerClick(String transaction_uuid){

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}