package com.farmerfirst.growagric.ui.finance.recyclerview;

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
import com.farmerfirst.growagric.databinding.ActivityFinanceRecyclerviewBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.finance.recyclerview.db.Finance;
import com.farmerfirst.growagric.ui.finance.recyclerview.db.FinanceAdapter;
import com.farmerfirst.growagric.utils.ComponentUtils;

public class FinanceRecyclerViewActivity extends AppCompatActivity implements FinanceAdapter.OnDeleteButtonListener,FinanceAdapter.FinanceAdapterListener{
    private ActivityFinanceRecyclerviewBinding binding;
    private ActivityEmptyBinding binding2;
    private FinanceAdapter financeAdapter;
    private FinanceAndroidViewModel androidViewModel;
    private int switchLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalSharedPreferences prefs = new LocalSharedPreferences(FinanceRecyclerViewActivity.this);
        androidViewModel = new ViewModelProvider(this).get(FinanceAndroidViewModel.class);
        if(androidViewModel.getFinanceCNT(LocalData.GetUserUUID(prefs)) == 0) {
            binding2 = DataBindingUtil.setContentView(FinanceRecyclerViewActivity.this, R.layout.activity_empty);
            switchLayout = 0;
        }else{
            binding = DataBindingUtil.setContentView(FinanceRecyclerViewActivity.this, R.layout.activity_finance_recyclerview);
            switchLayout = 1;
        }

        ComponentUtils.customActionBar("My Finance request(s)",FinanceRecyclerViewActivity.this);

        if(switchLayout == 1) {
            setupRecyclerView(prefs);
        }
    }
    private void setupRecyclerView(LocalSharedPreferences prefs){
        financeAdapter = new FinanceAdapter(FinanceRecyclerViewActivity.this,this,this);
        androidViewModel = new ViewModelProvider(this).get(FinanceAndroidViewModel.class);
        androidViewModel.getAllFinanceApplications(LocalData.GetUserUUID(prefs)).observe(this, finances -> financeAdapter.setData(finances));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.financeRecyclerView.setLayoutManager(mLayoutManager);
        binding.financeRecyclerView.setHasFixedSize(true);
        binding.financeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.financeRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL){
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state){

            }
        });
        binding.financeRecyclerView.setAdapter(financeAdapter);
        binding.financeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
    @Override
    public void onDeleteButtonClicked(Finance finance){
        androidViewModel.saveFinance(finance);
    }
    @Override
    public void onFinanceClick(String application_uuid){

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