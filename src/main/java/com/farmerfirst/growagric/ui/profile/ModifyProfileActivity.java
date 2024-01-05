package com.farmerfirst.growagric.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.farmerfirst.growagric.MainActivity;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.ActivityModifyProfileBinding;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.dashboard.DashboardFragment;
import com.farmerfirst.growagric.ui.dashboard.DashboardViewModel;
import com.farmerfirst.growagric.ui.profile.data.ClientData;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModifyProfileActivity extends AppCompatActivity {
    private ActivityModifyProfileBinding binding;
    private ProfileViewModel profileViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.setContentView(ModifyProfileActivity.this,R.layout.activity_modify_profile);

        ComponentUtils.customActionBar("Additional info",ModifyProfileActivity.this);

        AndroidBug5497Workaround.assistActivity(this);

        binding.setLifecycleOwner(this);
        binding.setProfileViewModel(profileViewModel);

        initHearAboutUsSpinner(getResources());
        initUserProfileInformation(getResources());
        initSpinner(getResources());

        validateEditField();

        profileViewModel.getProfile().observe(this, new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile userProfile) {
                String message = null;
                try {
                    if (binding.editIDNumber.getText().toString().length() < 4) {
                        message = "Invalid ID number";
                        binding.editIDNumber.requestFocus();
                    } else if (binding.spinGender.getSelectedItemPosition() == -1) {
                        message = "Select gender";
                        binding.spinGender.requestFocus();
                    } else if (binding.spinMarried.getSelectedItemPosition() == -1){
                        message = "Select marital status";
                        binding.spinMarried.requestFocus();
                    } else if (binding.spinLevelOfEducation.getSelectedItemPosition() == -1){
                        message = "Select highest education level";
                        binding.spinLevelOfEducation.requestFocus();
                    } else if (binding.spinHomeCounty.getSelectedItemPosition() == -1){
                        message = "Select home county";
                        binding.spinHomeCounty.requestFocus();
                    /*} else if (!Utils.isValidEmail(binding.editEmail.getText().toString())){
                        message = "Invalid email";
                        binding.editEmail.requestFocus();*/
                    }else if(binding.editAge.getText().toString().isEmpty()) {
                        binding.editAge.setText(null);
                        message = "Invalid age";
                    }else if(binding.editAge.getText().toString().startsWith("0")){
                        binding.editAge.setText(null);
                        message = "Invalid age";
                    } else if (Integer.parseInt(binding.editAge.getText().toString()) < 18){
                        message = "Invalid age";
                        binding.editAge.requestFocus();
                    } else if (binding.spinPlatformOption.getSelectedItemPosition() == -1){
                        message = "Select a platform";
                    } else if (binding.spinPlatformOption.getSelectedItem().toString().isEmpty()){
                        message = "Select a platform";
                    } else if (binding.editOtherPlatform.toString().length() < 1){
                        message = "Enter other platorm";
                        binding.editOtherPlatform.requestFocus();
                    } else {

                        message = "";

                        binding.modifyProfileButton.showLoading();

                        LocalSharedPreferences prefs = new LocalSharedPreferences(ModifyProfileActivity.this);

                        String IDNumber = binding.editIDNumber.getText().toString().trim();
                        String email = binding.editEmail.getText().toString().trim();
                        String gender = binding.spinGender.getSelectedItem().toString().trim();
                        String age = binding.editAge.getText().toString().trim();
                        String maritalStatus = binding.spinMarried.getSelectedItem().toString().trim();
                        String levelOfEducation = binding.spinLevelOfEducation.getSelectedItem().toString().trim();
                        String homeCounty = binding.spinHomeCounty.getSelectedItem().toString().trim();
                        String platform = binding.spinPlatformOption.getSelectedItem().toString().trim();

                        if(!platform.isEmpty() && !platform.equalsIgnoreCase("other")){
                            binding.editOtherPlatform.setText(platform);
                        }

                        JSONObject obj = ClientData.ModifyUserPayload(IDNumber,email,gender,age,maritalStatus,levelOfEducation,homeCounty);

                        try {
                            String data = prefs.getProfileInfo();
                            JSONObject objPrefs = new JSONObject(data);
                            String user_uuid = objPrefs.getJSONObject("message").get("farmer_uuid").toString();
                            String names = objPrefs.getJSONObject("message").getString("first_name") + " " + objPrefs.getJSONObject("message").getString("last_name");
                            String phone = objPrefs.getJSONObject("message").getString("phone_number");
                            String other_platform = binding.editOtherPlatform.getText().toString();
                            handleWhereDidYouHearAboutUs(user_uuid,names,phone,other_platform);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    HttpClient.ModifiyUser(user_uuid,obj,new ICustomResponseCallback(){
                                        @Override
                                        public void onSuccess(String value) {
                                            try {
                                                if (!prefs.getProfileInfo().equals("")){
                                                    prefs.deleteProfileInfo();
                                                }

                                                prefs.setProfileInfo(value);
                                                binding.modifyProfileButton.hideLoading();

                                                prefs.setTrackProfileInfoUpdate(1);

                                                Intent i = new Intent(ModifyProfileActivity.this,ProfileActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                startActivity(i);
                                                finish();
                                            } catch (Exception e) {
                                                binding.modifyProfileButton.hideLoading();
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onFailure(){}
                                    });
                                }
                            },500);
                        } catch (JSONException ej) {
                            binding.modifyProfileButton.hideLoading();
                            ej.printStackTrace();
                        }
                    }
                    if(message.trim().length() > 0){
                        ComponentUtils.hideKeyboard(ModifyProfileActivity.this);
                        ComponentUtils.showSnackBar(binding.getRoot(),message,0,R.color.colorRed);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void handleWhereDidYouHearAboutUs(String UUID,String applicantName,String phoneNumber,String platform){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        JSONObject obj = ClientData.AddAboutUsPlatformPayload(UUID,applicantName,phoneNumber,platform,sdf.format(new Date()));
        HttpClient.postHearAboutUsSource(obj, new ICustomResponseCallback() {
            @Override
            public void onSuccess(String value){}
            @Override
            public void onFailure(){}
        });
    }

    private void validateEditField(){
        binding.editIDNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() <= 4) {
                    binding.editIDNumber.setError("Invalid ID number");
                }else{
                    binding.editIDNumber.setError(null);
                }
            }
        });

        binding.editAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() <= 1) {
                    binding.editAge.setError("Invalid age");
                }else if(x.startsWith("0")){
                    binding.editAge.setError("Invalid age");
                }else{
                    binding.editAge.setError(null);
                }
            }
        });
    }

    private void initHearAboutUsSpinner(Resources res){
        String[] hearAboutUsItemData = res.getStringArray(R.array.hear_about_us_platform);
        ArrayList<String> hearAboutUsItemList = new ArrayList<>(Arrays.asList(hearAboutUsItemData));
        CustomAdapter adapter = new CustomAdapter(this,hearAboutUsItemList);
        binding.spinPlatformOption.setAdapter(adapter);
        binding.spinPlatformOption.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);
        binding.spinPlatformOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(hearAboutUsItemList.get(position).trim().equalsIgnoreCase("other")){
                    binding.textOtherPlatform.setVisibility(View.VISIBLE);
                    if(binding.spinPlatformOption.getSelectedItem().toString().equalsIgnoreCase("other")){
                        String match = searchItem(hearAboutUsItemList,binding.spinPlatformOption.getSelectedItem().toString());
                        if(match.length() == 0) {
                            binding.editOtherPlatform.setText(null);
                        }
                    }
                    binding.editOtherPlatform.requestFocus();
                }else{
                    binding.textOtherPlatform.setVisibility(View.GONE);
                    if(!binding.spinPlatformOption.getSelectedItem().toString().equalsIgnoreCase("other")){
                        String match = searchItem(hearAboutUsItemList,binding.spinPlatformOption.getSelectedItem().toString());
                        if(match.length()>0){
                            String selected = binding.spinPlatformOption.getSelectedItem().toString().toLowerCase();
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }

    private void initSpinner(Resources res){
        String[] countyKeData = res.getStringArray(R.array.countyKe);
        ArrayList<String> countyKeList = new ArrayList<>(Arrays.asList(countyKeData));
        binding.spinHomeCounty.setItem(countyKeList);

        String[] genderData = res.getStringArray(R.array.genderItems);
        ArrayList<String> genderList = new ArrayList<>(Arrays.asList(genderData));
        binding.spinGender.setItem(genderList);

        String[] maritalStatusData = res.getStringArray(R.array.maritalStatusItems);
        ArrayList<String> maritalStatusList = new ArrayList<>(Arrays.asList(maritalStatusData));
        binding.spinMarried.setItem(maritalStatusList);

        String[] levelOfEducationData = res.getStringArray(R.array.levelOfEducationItems);
        ArrayList<String> levelOfEducationDataList = new ArrayList<>(Arrays.asList(levelOfEducationData));
        binding.spinLevelOfEducation.setItem(levelOfEducationDataList);
    }

    private void initUserProfileInformation(Resources res){
        LocalSharedPreferences prefs = new LocalSharedPreferences(ModifyProfileActivity.this);
        String data = prefs.getProfileInfo();
        try{
            JSONObject obj = new JSONObject(data);
            if(obj.getJSONObject("message").get("id_number").toString().equals("null")){
                binding.editIDNumber.setText("");
            }else{
                binding.editIDNumber.setText(obj.getJSONObject("message").get("id_number").toString());
            }

            if(obj.getJSONObject("message").get("email").toString().equals("null")){
                binding.editEmail.setText("");
            }else{
                binding.editEmail.setText(obj.getJSONObject("message").get("email").toString());
            }

            String[] genderData = res.getStringArray(R.array.genderItems);
            ArrayList<String> genderList = new ArrayList<>(Arrays.asList(genderData));
            int genderListPosition = genderList.indexOf(obj.getJSONObject("message").get("gender").toString());
            binding.spinGender.setSelection(genderListPosition);

            if(obj.getJSONObject("message").get("age").toString().equals("0")){
                binding.editAge.setText("");
            }else{
                binding.editAge.setText(obj.getJSONObject("message").get("age").toString());
            }

            String[] maritalStatusData = res.getStringArray(R.array.maritalStatusItems);
            ArrayList<String> maritalStatusList = new ArrayList<>(Arrays.asList(maritalStatusData));
            int maritalStatusPosition = maritalStatusList.indexOf(obj.getJSONObject("message").get("is_married").toString());
            binding.spinMarried.setSelection(maritalStatusPosition);

            String[] levelOfEducationData = res.getStringArray(R.array.levelOfEducationItems);
            ArrayList<String> levelOfEducationDataList = new ArrayList<>(Arrays.asList(levelOfEducationData));
            int levelOfEducationDataPosition = levelOfEducationDataList.indexOf(obj.getJSONObject("message").get("level_of_education").toString());
            binding.spinLevelOfEducation.setSelection(levelOfEducationDataPosition);

            String[] countyKeData = res.getStringArray(R.array.countyKe);
            ArrayList<String> countyKeList = new ArrayList<>(Arrays.asList(countyKeData));
            int countyPosition = countyKeList.indexOf(obj.getJSONObject("message").get("home_county").toString());
            binding.spinHomeCounty.setSelection(countyPosition);

            String[] hearAboutUsItemData = res.getStringArray(R.array.hear_about_us_platform);
            ArrayList<String> hearAboutUsItemList = new ArrayList<>(Arrays.asList(hearAboutUsItemData));
            int platformPosition = hearAboutUsItemList.indexOf(obj.getJSONObject("message").get("platform").toString());
            binding.spinPlatformOption.setSelection(platformPosition);

            if(!hearAboutUsItemList.contains(obj.getJSONObject("message").get("platform").toString())){
                int position = 6;
                binding.spinPlatformOption.setSelection(position);
                if(obj.getJSONObject("message").get("platform").toString().equals("Unspecified")){
                    binding.editOtherPlatform.setText(null);
                }else{
                    binding.editOtherPlatform.setText(obj.getJSONObject("message").get("platform").toString());
                }
            }

        }catch (JSONException je){
            je.printStackTrace();
        }
    }

    public String searchItem(ArrayList<String> arrayList,String toMatchString) {
        for (String item : arrayList) {
            if (item.toLowerCase().contains(toMatchString.toLowerCase()))
                return item;
        }
        return null;
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(ModifyProfileActivity.this, ProfileActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return true;
    }
}