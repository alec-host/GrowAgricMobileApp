package com.farmerfirst.growagric.ui.farm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.farmerfirst.growagric.MainActivity;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityOtherFarmedItemsBinding;
import com.farmerfirst.growagric.ui.profile.ModifyProfileActivity;
import com.farmerfirst.growagric.ui.profile.ProfileActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;

public class OtherFarmedItemsActivity extends AppCompatActivity {
    private ActivityOtherFarmedItemsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(OtherFarmedItemsActivity.this,com.farmerfirst.growagric.R.layout.activity_other_farmed_items);

        ComponentUtils.customActionBar("Join our waiting list",OtherFarmedItemsActivity.this);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}