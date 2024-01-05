package com.farmerfirst.growagric.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.farmerfirst.growagric.MainActivity;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityProfileBinding;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.account.AccountFragment;
import com.farmerfirst.growagric.ui.dashboard.DashboardFragment;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.register.RegisterActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.ItemArrayAdapter;
import com.farmerfirst.growagric.utils.dojo.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(ProfileActivity.this,R.layout.activity_profile);

        ComponentUtils.customActionBar("Profile",ProfileActivity.this);

        readProfileInformation();

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,ModifyProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });
    }

    private void readProfileInformation(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(ProfileActivity.this);
        ArrayList<Item> itemList = new ArrayList<>();
        //-.get profile information.
        String profileData = prefs.getProfileInfo();

        try {
            if(!profileData.equals("")) {
                JSONObject obj = new JSONObject(profileData);
                itemList.add(new Item(R.drawable.ic_checklist128x128, "Phone", obj.getJSONObject("message").getString("phone_number")));
                if(obj.getJSONObject("message").getString("email").toString().equals("null")) {
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "ID number", "Not provided"));
                }else{
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "ID number", obj.getJSONObject("message").getString("id_number")));
                }
                if(obj.getJSONObject("message").getString("email").toString().equals("null") || obj.getJSONObject("message").getString("email").toString().equals("")){
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Email", "Not provided"));
                }else {
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Email", obj.getJSONObject("message").getString("email")));
                }
                if(obj.getJSONObject("message").getString("gender").toString().equals("Unspecified")) {
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Gender","Not provided"));
                }else{
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Gender", obj.getJSONObject("message").getString("gender")));
                }
                if(obj.getJSONObject("message").getString("age").toString().equals("0")) {
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Age", "Not provided"));
                }else{
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Age", obj.getJSONObject("message").getString("age")));
                }
                if(obj.getJSONObject("message").getString("is_married").toString().equals("Unspecified")) {
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Marital status", "Not provided"));
                }else{
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Marital status", obj.getJSONObject("message").getString("is_married")));
                }
                if(obj.getJSONObject("message").getString("level_of_education").toString().equals("Unspecified")) {
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Level of education", "Not provided"));
                }else{
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Level of education", obj.getJSONObject("message").getString("level_of_education")));
                }
                if(obj.getJSONObject("message").getString("home_county").toString().equals("Unspecified")) {
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Home county", "Not provided"));
                }else{
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Home county", obj.getJSONObject("message").getString("home_county")));
                }
                if(obj.getJSONObject("message").getString("platform").toString().equals("Unspecified")) {
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Hear about us", "Not provided"));
                }else{
                    itemList.add(new Item(R.drawable.ic_checklist128x128, "Hear about us", obj.getJSONObject("message").getString("platform")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ItemArrayAdapter mItemAdapter = new ItemArrayAdapter(Objects.requireNonNull(ProfileActivity.this),itemList);

        binding.itemList.setAdapter(mItemAdapter);
        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
        startActivity(intent);
        finish();
        return true;
    }
}