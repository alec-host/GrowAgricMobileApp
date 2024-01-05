package com.farmerfirst.growagric.ui.farm_visit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityFarmVisitListAvBinding;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.ItemArrayAdapter;
import com.farmerfirst.growagric.utils.dojo.Item;

import java.util.ArrayList;

public class FarmVisitListAv extends AppCompatActivity {
    //ActivityFarmVisitListAvBinding binding;
    ActivityFarmVisitListAvBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(FarmVisitListAv.this,R.layout.activity_farm_visit_list_av);

        ComponentUtils.customActionBar("Farmer records",FarmVisitListAv.this);

        ArrayList<Item> datamodels = new ArrayList<>();
        datamodels.add(new Item(R.drawable.ic_profile128x128,"Farm visit before financing",""));
        datamodels.add(new Item(R.drawable.ic_add128x128,"First farm visit after finacing",""));
        datamodels.add(new Item(R.drawable.ic_invite128x128,"Return visit after financing",""));

        ItemArrayAdapter mItemAdapter = new ItemArrayAdapter(FarmVisitListAv.this,datamodels);
        binding.listFarmVisitItems.setAdapter(mItemAdapter);

        binding.listFarmVisitItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (datamodels.get(position).getName().toLowerCase().toString()){
                    case "farm visit before financing":
                        Intent intent = new Intent(FarmVisitListAv.this, FarmVisitsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case "first farm visit after finacing":
                        Toast.makeText(FarmVisitListAv.this,"Under construction",Toast.LENGTH_SHORT).show();
                        break;
                    case "return visit after financing":
                        Toast.makeText(FarmVisitListAv.this,"Under construction",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        //Intent intent = new Intent(FarmVisitListAv.this, ProfileActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //startActivity(intent);
        finish();
        return true;
    }
}