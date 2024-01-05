package com.farmerfirst.growagric.ui.record_keeping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityRecordKeepingDashboardBinding;
import com.farmerfirst.growagric.ui.record_keeping.input.AddEmployeeActivity;
import com.farmerfirst.growagric.ui.record_keeping.sales.AddCustomerActivity;
import com.farmerfirst.growagric.ui.record_keeping.view_records.ViewRecordActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;

public class RecordKeepingDashboardActivity extends AppCompatActivity {
    private ActivityRecordKeepingDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RecordKeepingDashboardActivity.this, R.layout.activity_record_keeping_dashboard);

        ComponentUtils.customActionBar("Farm records",RecordKeepingDashboardActivity.this);

        setupDashboard();

    }
    private void setupDashboard(){
        binding.llDailyDataInputs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordKeepingDashboardActivity.this,RecordTypeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        binding.llViewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h = new Intent(RecordKeepingDashboardActivity.this, ViewRecordActivity.class);
                h.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(h);
            }
        });
        binding.llAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(RecordKeepingDashboardActivity.this, AddEmployeeActivity.class);
                j.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(j);
            }
        });
        binding.llAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(RecordKeepingDashboardActivity.this, AddCustomerActivity.class);
                k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(k);
            }
        });
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