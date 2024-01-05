package com.farmerfirst.growagric.ui.record_keeping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityRecordTypeBinding;
import com.farmerfirst.growagric.ui.record_keeping.input.custom_dialog.CustomDialog;
import com.farmerfirst.growagric.ui.record_keeping.mortality_disease.DiseaseActivity;
import com.farmerfirst.growagric.ui.record_keeping.other_expense.OtherExpenseActivity;
import com.farmerfirst.growagric.ui.record_keeping.other_income.OtherIncomeActivity;
import com.farmerfirst.growagric.ui.record_keeping.sales.SalesActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.ItemArrayAdapter;
import com.farmerfirst.growagric.utils.dojo.Item;

import java.util.ArrayList;
import java.util.Objects;

public class RecordTypeActivity extends AppCompatActivity {
    private ActivityRecordTypeBinding binding;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RecordTypeActivity.this,R.layout.activity_record_type);

        ComponentUtils.customActionBar("Record type", RecordTypeActivity.this);

        ArrayList<Item> datamodels = new ArrayList<>();
        datamodels.add(new Item(R.drawable.ic_input128x128,"Inputs",""));
        datamodels.add(new Item(R.drawable.ic_sales128x128,"Sales",""));
        datamodels.add(new Item(R.drawable.ic_disease128x128,"Mortality & diseases",""));
        datamodels.add(new Item(R.drawable.ic_income128x128,"Other income",""));
        datamodels.add(new Item(R.drawable.ic_expense128x128,"Other expense",""));

        ItemArrayAdapter mItemAdapter = new ItemArrayAdapter(Objects.requireNonNull(RecordTypeActivity.this),datamodels);
        binding.listRecordTypeItems.setAdapter(mItemAdapter);

        binding.listRecordTypeItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (datamodels.get(position).getName().toLowerCase().toString()) {
                    case "inputs":
                        showPopupDialog("inputs");
                        break;
                    case "sales":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(RecordTypeActivity.this, SalesActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(i);
                            }
                        },100);
                        break;
                    case "mortality & diseases":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent j = new Intent(RecordTypeActivity.this, DiseaseActivity.class);
                                j.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(j);
                            }
                        },100);
                        break;
                    case "other income":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent k = new Intent(RecordTypeActivity.this, OtherIncomeActivity.class);
                                k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(k);
                            }
                        },100);
                        break;
                    case "other expense":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent h = new Intent(RecordTypeActivity.this, OtherExpenseActivity.class);
                                h.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(h);
                            }
                        },100);
                        break;
                }
            }
        });
    }
    private void showPopupDialog(String recordType) {
        CustomDialog dialog = CustomDialog.newInstance(recordType);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        dialog.show(transaction,"CustomDialog");
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