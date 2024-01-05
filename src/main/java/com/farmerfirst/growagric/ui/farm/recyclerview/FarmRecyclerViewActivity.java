package com.farmerfirst.growagric.ui.farm.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityEmptyBinding;
import com.farmerfirst.growagric.databinding.ActivityFarmRecyclerviewBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.utils.ComponentUtils;

public class FarmRecyclerViewActivity extends AppCompatActivity implements FarmAdapter.OnDeleteButtonListener,FarmAdapter.FarmAdapterListener{
    private ActivityFarmRecyclerviewBinding binding;
    private ActivityEmptyBinding binding2;

    private FarmAdapter farmAdapter;
    private FarmAndroidViewModel androidViewModel;
    private int switchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalSharedPreferences prefs = new LocalSharedPreferences(FarmRecyclerViewActivity.this);

        androidViewModel = new ViewModelProvider(this).get(FarmAndroidViewModel.class);
        if(androidViewModel.getFarmCNT(LocalData.GetUserUUID(prefs)) == 0){
            binding2 = DataBindingUtil.setContentView(FarmRecyclerViewActivity.this,R.layout.activity_empty);
            switchLayout = 0;
        }else {
            binding = DataBindingUtil.setContentView(FarmRecyclerViewActivity.this, R.layout.activity_farm_recyclerview);
            switchLayout = 1;
        }

        ComponentUtils.customActionBar("My farm(s)", FarmRecyclerViewActivity.this);

        if(switchLayout == 1){
            setupRecyclerView(prefs);
        }
    }
    private void setupRecyclerView(LocalSharedPreferences prefs){
        farmAdapter = new FarmAdapter(FarmRecyclerViewActivity.this,this,this);
        androidViewModel = new ViewModelProvider(this).get(FarmAndroidViewModel.class);
        androidViewModel.getAllFarms(LocalData.GetUserUUID(prefs)).observe(this, farms -> farmAdapter.setData(farms));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.farmRecyclerView.setLayoutManager(mLayoutManager);
        binding.farmRecyclerView.setHasFixedSize(true);
        binding.farmRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.farmRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL){
            @Override
            public void onDraw(Canvas c, RecyclerView parent,RecyclerView.State state){

            }
        });
        binding.farmRecyclerView.setAdapter(farmAdapter);
        binding.farmRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onDeleteButtonClicked(Farm farm) {
        androidViewModel.deleteFarm(farm);
    }
    @Override
    public void onFarmClick(String farm_uuid){}
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