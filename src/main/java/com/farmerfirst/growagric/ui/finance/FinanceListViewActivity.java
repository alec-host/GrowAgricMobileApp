package com.farmerfirst.growagric.ui.finance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityFinanceListviewBinding;
import com.farmerfirst.growagric.ui.finance.recyclerview.FinanceRecyclerViewActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.ItemArrayAdapter;
import com.farmerfirst.growagric.utils.dojo.Item;

import java.util.ArrayList;
import java.util.Objects;

public class FinanceListViewActivity extends AppCompatActivity {
    private ActivityFinanceListviewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_finance_listview);

        ComponentUtils.customActionBar("Finance details",FinanceListViewActivity.this);

        ArrayList<Item> itemList = new ArrayList<>();

        itemList.add(new Item(R.drawable.ic_add128x128,"Register for Finance",""));
        itemList.add(new Item(R.drawable.ic_view128x128,"My Finance Request(s)",""));

        ItemArrayAdapter mItemAdapter = new ItemArrayAdapter(Objects.requireNonNull(FinanceListViewActivity.this),itemList);

        binding.lvFarmOperations.setAdapter(mItemAdapter);
        mItemAdapter.notifyDataSetChanged();

        binding.lvFarmOperations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(itemList.get(position).getName().toString()){
                    case "Register for Finance":
                        intent = new Intent(FinanceListViewActivity.this, FinanceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case "My Finance Request(s)":
                        intent = new Intent(FinanceListViewActivity.this, FinanceRecyclerViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}