package com.farmerfirst.growagric.ui.farm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.databinding.ActivityEmptyBinding;
import com.farmerfirst.growagric.databinding.ActivityFarmBinding;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.jsondata.WardKe;
import com.farmerfirst.growagric.ui.farm.data.ClientData;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmAndroidViewModel;
import com.farmerfirst.growagric.jsondata.SubCountyKe;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmRecyclerViewActivity;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.profile.ModifyProfileActivity;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;
import com.shuhart.stepview.StepView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FarmActivity extends AppCompatActivity {
    private FarmViewModel farmViewModel;
    private ActivityFarmBinding binding;
    private Resources res;
    private int position = 0;
    private String stepTracker ="start";
    private FarmAndroidViewModel farmAndroidViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        farmViewModel = new ViewModelProvider(this).get(FarmViewModel.class);

        binding = DataBindingUtil.setContentView(FarmActivity.this,com.farmerfirst.growagric.R.layout.activity_farm);

        ComponentUtils.customActionBar("Add farm",FarmActivity.this);

        binding.setLifecycleOwner(this);
        binding.setFarmViewModel(farmViewModel);

        AndroidBug5497Workaround.assistActivity(FarmActivity.this);

        binding.stepView.done(false);

        initSpinners(getResources());
        editTextChangeListener();
        setupStepView();
        checkBoxManagement();
        checkOtherFarmedValue();

        farmViewModel.getCreatedFarm().observe(this, new Observer<AddFarm>() {
            @Override
            public void onChanged(AddFarm addFarm) {
                String message = new String();
                if(binding.spinFarmedItem.getSelectedItemPosition() == -1){
                    message = "Select an item";
                    binding.spinFarmedItem.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFarm).getStrChickHouseCapacity())){
                    message = "Enter house capacity";
                    binding.editChickHouseCapacity.requestFocus();
                }else if(binding.spinCounty.getSelectedItemPosition() ==- 1){
                    message = "Select county";
                    binding.spinCounty.requestFocus();
                }else if(binding.spinSubCounty.getSelectedItemPosition() == -1){
                    message = "Select sub-county";
                    binding.spinSubCounty.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFarm).getStrNumberOfYearsFarmingSelectedItem())){
                    message = "Enter no. of years farming "+binding.spinFarmedItem.getSelectedItem().toString();
                    binding.editNumberOfYearsFarmingSelectedItem.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFarm).getStrNumberOfEmployees())){
                    message = "Enter no. of employees";
                    binding.editNumberOfEmployees.requestFocus();
                }else if(binding.spinIsFarmInsured.getSelectedItemPosition() == -1){
                    message = "Select an option";
                    binding.spinIsFarmInsured.requestFocus();
                }else if(binding.spinIsFarmInsured.getSelectedItem().toString().toLowerCase().trim()  == "yes"){
                    message = "Enter insurer name";
                    binding.editInsurerName.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFarm).getStrListOfChallenges())){
                    message = "Select challenge that you face";
                    binding.checkLackOfFinance.requestFocus();
                }else if(binding.checkOther.isChecked()){
                    if(TextUtils.isEmpty(Objects.requireNonNull(addFarm).getStrOtherChallenges())){
                        message = "Enter other challenge that you face";
                        binding.editOtherChallenges.requestFocus();
                    }
                }else{

                    message="";

                    binding.buttonSubmit.showLoading();

                    LocalSharedPreferences prefs = new LocalSharedPreferences(FarmActivity.this);
                    String profileData = prefs.getProfileInfo();
                    String phoneNumber = new String();
                    String farmerUUID = new String();

                    try{
                        JSONObject obj = new JSONObject(profileData);
                        phoneNumber = obj.getJSONObject("message").getString("phone_number");
                        farmerUUID = obj.getJSONObject("message").getString("farmer_uuid");
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }

                    String farmedItem = binding.spinFarmedItem.getSelectedItem().toString();
                    String chickHouseCapacity = Utils.getCommalessNumber(binding.editChickHouseCapacity.getText().toString());
                    String county = binding.spinCounty.getSelectedItem().toString();
                    String subCounty = binding.spinSubCounty.getSelectedItem().toString();
                    String ward = binding.spinWard.getSelectedItem().toString();
                    String otherFarmedItem = binding.editOtherFarmedItem.getText().toString();
                    String noOfYearsFarmingSelectedItem = binding.editNumberOfYearsFarmingSelectedItem.getText().toString();
                    String noOfEmployees = binding.editNumberOfEmployees.getText().toString();
                    String isInsured = binding.spinIsFarmInsured.getSelectedItem().toString();
                    String hasInsurance = binding.spinIsFarmInsured.getSelectedItem().toString().length() > 2  ? "1": "0";
                    String insurerName = binding.editInsurerName.getText().toString().length() > 3 ? binding.editInsurerName.getText().toString() : "0";
                    String challenges = binding.textViewChallenges.getText().toString();
                    String otherChallenges = binding.editOtherChallenges.getText().toString().length() > 1 ? binding.editOtherChallenges.getText().toString() : "0";

                    JSONObject objPayload = ClientData.AddFarmPayload(phoneNumber,farmerUUID,farmedItem,chickHouseCapacity,county,subCounty,ward,otherFarmedItem,noOfYearsFarmingSelectedItem,noOfEmployees,hasInsurance,insurerName,challenges,otherChallenges);

                    final IApiInterface iApiInterface = RetrofitClient.getApiService();

                    iApiInterface.addFarm(objPayload.toString()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                                LocalSharedPreferences prefs = new LocalSharedPreferences(FarmActivity.this);
                                if (response.code() == 200 || response.code() == 201) {

                                    String responseBody = Objects.requireNonNull(response.body()).string();
                                    JSONObject obj = new JSONObject(responseBody);
                                    prefs.setFarm(obj.toString());
                                    storeToDB(
                                        obj.getJSONObject("message").get("farm_uuid").toString(),
                                        obj.getJSONObject("message").get("farmer_uuid").toString(),
                                        obj.getJSONObject("message").get("county").toString(),
                                        obj.getJSONObject("message").get("sub_county").toString(),
                                        obj.getJSONObject("message").get("ward").toString(),
                                        obj.getJSONObject("message").get("number_of_employees").toString(),
                                        obj.getJSONObject("message").get("number_of_years_farming").toString(),
                                        obj.getJSONObject("message").get("bird_house_capacity").toString(),
                                        obj.getJSONObject("message").get("mortality_rate").toString(),
                                        obj.getJSONObject("message").get("item_farmed").toString(),
                                        obj.getJSONObject("message").get("is_insured").toString(),
                                        obj.getJSONObject("message").get("insurer").toString(),
                                        sdf.format(new Date())
                                    );
                                    resetActivityComponents();
                                    binding.buttonSubmit.hideLoading();
                                    ComponentUtils.showSuccessToast(binding.getRoot(),"Farm added successfully!");
                                    Intent j = new Intent(FarmActivity.this,FarmRecyclerViewActivity.class);
                                    startActivity(j);
                                    finish();
                                }else{
                                    binding.buttonSubmit.hideLoading();
                                    String responseBody = "{\"success\":\"false\",\"error\":\"true\",\"message\":\"something wrong has happened\"}";
                                }
                            }catch (Exception e){
                                binding.buttonSubmit.hideLoading();
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t){
                            binding.buttonSubmit.hideLoading();
                            t.printStackTrace();
                        }
                    });
                }
                if(message.trim().length() > 0){
                    ComponentUtils.hideKeyboard(FarmActivity.this);
                    ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
                }
            }
        });
    }
    private void initSpinners(Resources res){

        String[] farmedItemData = res.getStringArray(R.array.farmedItems);
        ArrayList<String> farmedItemList = new ArrayList<>(Arrays.asList(farmedItemData));
        CustomAdapter adapter = new CustomAdapter(this,farmedItemList);
        binding.spinFarmedItem.setAdapter(adapter);
        binding.spinFarmedItem.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);
        binding.spinFarmedItem.setFocusable(true);
        binding.spinFarmedItem.setFocusableInTouchMode(true);
        binding.spinFarmedItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.editNumberOfYearsFarmingSelectedItem.setHint("Number of years farming "+farmedItemList.get(position).toString().toLowerCase().trim());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] countyKeData = res.getStringArray(R.array.countyKe);
        ArrayList<String> countyKeList = new ArrayList<>(Arrays.asList(countyKeData));
        adapter = new CustomAdapter(this,countyKeList);
        binding.spinCounty.setAdapter(adapter);
        binding.spinCounty.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);
        binding.spinCounty.setFocusable(true);
        binding.spinCounty.setFocusableInTouchMode(true);
        binding.spinCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addSubCountyList(countyKeList.get(position).toLowerCase().toString().trim());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] insuranceData = res.getStringArray(R.array.isInsured);
        ArrayList<String> insuranceList = new ArrayList<>(Arrays.asList(insuranceData));
        adapter = new CustomAdapter(this,insuranceList);
        binding.spinIsFarmInsured.setAdapter(adapter);
        binding.spinIsFarmInsured.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);
        binding.spinIsFarmInsured.setFocusable(true);
        binding.spinIsFarmInsured.setFocusableInTouchMode(true);
        binding.spinIsFarmInsured.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(insuranceList.get(position).toString().toLowerCase().equals("yes")){
                    binding.textInsurerName.setVisibility(View.VISIBLE);
                    binding.editInsurerName.setVisibility(View.VISIBLE);
                }else{
                    binding.textInsurerName.setVisibility(View.GONE);
                    binding.editInsurerName.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void editTextChangeListener(){
        binding.editChickHouseCapacity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                binding.editChickHouseCapacity.removeTextChangedListener(this);
                String formattedString = s.toString().replace(",","");
                binding.editChickHouseCapacity.setText(Utils.formatWithCommas(formattedString));
                binding.editChickHouseCapacity.setSelection(formattedString.length());
                binding.editChickHouseCapacity.addTextChangedListener(this);
            }
        });
    }
    private void addSubCountyList(String selectedCounty){
        String jsonString = SubCountyKe.getDataList().toString();
        try {
            List<String> subCountyList = new ArrayList<String>();
            JSONArray countyArray = new JSONArray(jsonString);
            for(int j=0;j<countyArray.length();j++){
                JSONObject countyObject = countyArray.getJSONObject(j);
                String name = countyObject.getString("name");
                if(name.trim().toLowerCase().equals(selectedCounty)) {
                    JSONArray subCountiesArray = countyObject.getJSONArray("sub_counties");
                    String[] subCounties = new String[subCountiesArray.length()];
                    for (int k = 0; k < subCountiesArray.length(); k++) {
                        subCounties[k] = subCountiesArray.getString(k);
                        System.out.println(subCounties[k]);
                        subCountyList.add(subCounties[k].toString());
                    }
                }
            }
            CustomAdapter adapter = new CustomAdapter(this,subCountyList);
            binding.spinSubCounty.setAdapter(adapter);
            binding.spinSubCounty.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);
            binding.spinSubCounty.setFocusable(true);
            binding.spinSubCounty.setFocusableInTouchMode(true);
            binding.spinSubCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    addWardList(subCountyList.get(position).toLowerCase().toString().trim());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void addWardList(String selectedSubCounty){
        String jsonString = WardKe.getDataList().toString();
        try {
            List<String> wardList = new ArrayList<String>();
            JSONObject obj = new JSONObject(jsonString);
            JSONArray subCountyArray = new JSONArray(obj.get("counties").toString());
            for(int j=0;j<subCountyArray.length();j++){
                JSONObject subCountyObject = subCountyArray.getJSONObject(j);
                JSONArray subCountiesArray = subCountyObject.getJSONArray("sub_counties");
                String[] wards = new String[subCountiesArray.length()];
                for(int i=0;i<subCountiesArray.length();i++){
                    JSONObject wardsObject = subCountiesArray.getJSONObject(i);
                    String constituencyName = wardsObject.getString("name");
                    if(constituencyName.trim().equalsIgnoreCase(selectedSubCounty.trim())) {
                        JSONArray wardArray = new JSONArray(wardsObject.get("wards").toString());
                        for(int z=0;z<wardArray.length();z++) {
                            wards[i] = wardArray.getString(z);
                            wardList.add(wards[i].toString());
                        }
                    }
                }
            }
            if(wardList.size() == 0) {
                wardList.add("Not available");
                binding.spinWard.setVisibility(View.GONE);
            }else{
                binding.spinWard.setVisibility(View.VISIBLE);
            }
            CustomAdapter adapter = new CustomAdapter(this,wardList);
            binding.spinWard.setAdapter(adapter);
            binding.spinWard.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);
            binding.spinWard.setFocusable(true);
            binding.spinWard.setFocusableInTouchMode(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void resetActivityComponents(){
        binding.spinFarmedItem.setSelection(0);
        binding.editChickHouseCapacity.setText(null);
        binding.spinCounty.setSelection(0);
        binding.spinSubCounty.setSelection(0);
        String ward = "testWard";
        binding.editNumberOfYearsFarmingSelectedItem.setText(null);
        binding.editNumberOfEmployees.setText(null);
        binding.spinIsFarmInsured.setSelection(0);
        binding.spinIsFarmInsured.setSelection(0);
        binding.editInsurerName.setText(null);
        binding.textViewChallenges.setText(null);
        binding.editOtherChallenges.setText(null);
        binding.checkLackOfFinance.setChecked(false);
        binding.checkLackOfMarket.setChecked(false);
        binding.checkLackOfTraining.setChecked(false);
        binding.checkDiseases.setChecked(false);
        binding.checkOther.setChecked(false);
        binding.buttonSubmit.setEnabled(false);
    }
    private void setupStepView(){
        binding.stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                List<String> steps = new ArrayList<>();
                steps.add(Arrays.asList(getResources().getStringArray(R.array.farm_steps)).toString());
                if(stepTracker == "start") {
                    stepNextPositionTracker();
                }else if(stepTracker == "end"){
                    stepBackPositionTracker();
                }
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepNextPositionTracker();
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepBackPositionTracker();
            }
        });
    }
    private void stepNextPositionTracker(){
        if (position < binding.stepView.getStepCount() - 1) {
            position++;
            binding.stepView.go(position, true);
            if(position == 1){
                binding.farmDetails.setVisibility(View.VISIBLE);
                binding.farmLocation.setVisibility(View.GONE);
                binding.otherFarmedItems.setVisibility(View.VISIBLE);
            }else if(position == 2){
                binding.farmLocation.setVisibility(View.GONE);
                binding.farmDetails.setVisibility(View.GONE);
                binding.otherFarmedItems.setVisibility(View.GONE);
                binding.farmInsurance.setVisibility(View.VISIBLE);
            }else if(position == 3){
                binding.farmLocation.setVisibility(View.GONE);
                binding.farmDetails.setVisibility(View.GONE);
                binding.otherFarmedItems.setVisibility(View.GONE);
                binding.farmInsurance.setVisibility(View.GONE);
                binding.farmChallenges.setVisibility(View.VISIBLE);
                binding.navigationLayout.setVisibility(View.GONE);
                binding.submitLayout.setVisibility(View.VISIBLE);
                stepTracker ="end";
            }
        } else {
            binding.stepView.done(true);
        }
    }
    private void stepBackPositionTracker(){
        if (position > 0){
            position--;
            if(position == 0){
                binding.farmLocation.setVisibility(View.VISIBLE);
                binding.farmDetails.setVisibility(View.GONE);
                binding.otherFarmedItems.setVisibility(View.GONE);
                stepTracker ="start";
            }else if(position == 1){
                binding.farmLocation.setVisibility(View.GONE);
                binding.farmDetails.setVisibility(View.VISIBLE);
                binding.otherFarmedItems.setVisibility(View.VISIBLE);
                binding.farmInsurance.setVisibility(View.GONE);
            }else if(position == 2){
                binding.farmInsurance.setVisibility(View.VISIBLE);
                binding.farmChallenges.setVisibility(View.GONE);
                binding.submitLayout.setVisibility(View.GONE);
                binding.navigationLayout.setVisibility(View.VISIBLE);
            }
        }
        binding.stepView.done(false);
        binding.stepView.go(position,true);
    }
    private void checkBoxManagement(){
        binding.checkLackOfFinance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(binding.textViewChallenges.getText().toString().isEmpty()){
                        binding.textViewChallenges.setText(binding.checkLackOfFinance.getText().toString());
                    }else{
                        if(!binding.textViewChallenges.getText().toString().contains(binding.checkLackOfFinance.getText().toString())) {
                            String input = binding.textViewChallenges.getText().toString() + "," + binding.checkLackOfFinance.getText().toString();
                            binding.textViewChallenges.setText(input);
                        }
                    }
                }else{
                    if(binding.textViewChallenges.getText().toString().contains("Lack of finance")) {
                        binding.textViewChallenges.setText(binding.textViewChallenges.getText().toString().replace("Lack of finance","").replace(",Lack of finance",""));
                    }
                }
            }
        });
        binding.checkLackOfMarket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (binding.textViewChallenges.getText().toString().isEmpty()) {
                        binding.textViewChallenges.setText(binding.checkLackOfMarket.getText().toString());
                    } else {
                        if (!binding.textViewChallenges.getText().toString().contains(binding.checkLackOfMarket.getText().toString())) {
                            String input = binding.textViewChallenges.getText().toString() + "," + binding.checkLackOfMarket.getText().toString();
                            binding.textViewChallenges.setText(input);
                        }
                    }
                }else{
                    if(binding.textViewChallenges.getText().toString().contains("Lack of market")) {
                        binding.textViewChallenges.setText(binding.textViewChallenges.getText().toString().replace("Lack of market","").replace(",Lack of market",""));
                    }
                }
            }
        });
        binding.checkLackOfTraining.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (binding.textViewChallenges.getText().toString().isEmpty()) {
                        binding.textViewChallenges.setText(binding.checkLackOfTraining.getText().toString());
                    } else {
                        if (!binding.textViewChallenges.getText().toString().contains(binding.checkLackOfTraining.getText().toString())) {
                            String input = binding.textViewChallenges.getText().toString() + "," + binding.checkLackOfTraining.getText().toString();
                            binding.textViewChallenges.setText(input);
                        }
                    }
                }else{
                    if(binding.textViewChallenges.getText().toString().contains("Lack of training")) {
                        binding.textViewChallenges.setText(binding.textViewChallenges.getText().toString().replace("Lack of training","").replace(",Lack of training",""));
                    }
                }
            }
        });
        binding.checkDiseases.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (binding.textViewChallenges.getText().toString().isEmpty()) {
                        binding.textViewChallenges.setText(binding.checkDiseases.getText().toString());
                    } else {
                        if (!binding.textViewChallenges.getText().toString().contains(binding.checkDiseases.getText().toString())) {
                            String input = binding.textViewChallenges.getText().toString() + "," + binding.checkDiseases.getText().toString();
                            binding.textViewChallenges.setText(input);
                        }
                    }
                }else{
                    if(binding.textViewChallenges.getText().toString().contains("Diseases impacting mortality rate")) {
                        binding.textViewChallenges.setText(binding.textViewChallenges.getText().toString().replace("Diseases impacting mortality rate","").replace(",Diseases impacting mortality rate",""));
                    }
                }
            }
        });
        binding.checkOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.textOtherChallenges.setVisibility(View.VISIBLE);
                }else{
                    binding.textOtherChallenges.setVisibility(View.GONE);
                }
            }
        });
    }
    private void checkOtherFarmedValue(){
        binding.checkCows.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkCows.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkCows.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkCows.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkCows.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkCows.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkGoats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkGoats.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkGoats.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkGoats.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkGoats.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkGoats.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkDairy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkDairy.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkDairy.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkDairy.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkDairy.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkDairy.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkPigs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkPigs.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkPigs.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkPigs.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkPigs.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkPigs.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkKienyejiChicken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkKienyejiChicken.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkKienyejiChicken.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkKienyejiChicken.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkKienyejiChicken.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkKienyejiChicken.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkTurkey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkTurkey.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkTurkey.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkTurkey.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkTurkey.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkTurkey.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkDucks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkDucks.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkDucks.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkDucks.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkDucks.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkDucks.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkRabbits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkRabbits.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkRabbits.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkRabbits.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkRabbits.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkRabbits.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkFish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkFish.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkFish.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkFish.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkFish.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkFish.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkBees.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkBees.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkBees.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkBees.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkBees.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkFish.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkSnail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkSnail.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkSnail.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkSnail.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkSnail.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkSnail.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });

        binding.checkMushroom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(binding.editOtherFarmedItem.getText().toString().isEmpty()){
                        binding.editOtherFarmedItem.setText(binding.checkMushroom.getText().toString());
                    }else{
                        if (!binding.editOtherFarmedItem.getText().toString().contains(binding.checkMushroom.getText().toString())) {
                            String input = binding.editOtherFarmedItem.getText().toString() + "," + binding.checkMushroom.getText().toString();
                            binding.editOtherFarmedItem.setText(input);
                        }
                    }
                }else{
                    binding.editOtherFarmedItem.setText(binding.editOtherFarmedItem.getText().toString().replace(","+binding.checkMushroom.getText().toString(),""));
                    if(binding.editOtherFarmedItem.getText().toString().contains(binding.checkMushroom.getText().toString())){
                        binding.editOtherFarmedItem.setText(null);
                    }
                }
            }
        });
    }

    private void storeToDB(String farm_uuid,String user_uuid,
                          String county,String sub_county,
                          String ward,String num_of_employees,
                          String num_of_years,String house_capacity,
                          String mortality_rate,String item_farmed,
                          String is_insured,String insurer_name,String date_created){
        farmAndroidViewModel = new ViewModelProvider(this).get(FarmAndroidViewModel.class);
        farmAndroidViewModel.saveFarm(new Farm(farm_uuid,user_uuid,county,sub_county,ward,num_of_employees,num_of_years,house_capacity,mortality_rate,item_farmed,is_insured,insurer_name,date_created));
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