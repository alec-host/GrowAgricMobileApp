package com.farmerfirst.growagric.ui.farm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityFarmListviewBinding;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmRecyclerViewActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.ItemArrayAdapter;
import com.farmerfirst.growagric.utils.dojo.Item;

import java.util.ArrayList;
import java.util.Objects;

public class FarmListViewActivity extends AppCompatActivity{
    private ActivityFarmListviewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_farm_listview);

        ComponentUtils.customActionBar("Farm details", FarmListViewActivity.this);

        ArrayList<Item> itemList = new ArrayList<>();

        itemList.add(new Item(R.drawable.ic_add128x128,"Add Farm",""));
        //-.itemList.add(new Item(R.drawable.ic_delay128x128,"Waiting List",""));
        itemList.add(new Item(R.drawable.ic_view128x128,"My Farm(s)",""));

        ItemArrayAdapter mItemAdapter = new ItemArrayAdapter(Objects.requireNonNull(FarmListViewActivity.this),itemList);

        binding.lvFarmOperations.setAdapter(mItemAdapter);
        mItemAdapter.notifyDataSetChanged();

        binding.lvFarmOperations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(itemList.get(position).getName().toString()){
                    case "Add Farm":
                        intent = new Intent(FarmListViewActivity.this, FarmActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    break;
                    case "My Farm(s)":
                        intent = new Intent(FarmListViewActivity.this, FarmRecyclerViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    break;
                    case "Waiting List":
                        intent = new Intent(FarmListViewActivity.this, OtherFarmedItemsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case "Add Preferred Farm Supplies":
                        Toast.makeText(FarmListViewActivity.this,"Under construction",Toast.LENGTH_SHORT).show();
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