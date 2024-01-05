package com.farmerfirst.growagric.ui.finance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.databinding.ActivityFinanceBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.farm.FarmActivity;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmAndroidViewModel;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.finance.data.ClientData;
import com.farmerfirst.growagric.ui.finance.recyclerview.FinanceAndroidViewModel;
import com.farmerfirst.growagric.ui.finance.recyclerview.FinanceRecyclerViewActivity;
import com.farmerfirst.growagric.ui.finance.recyclerview.db.Finance;
import com.farmerfirst.growagric.ui.finance.recyclerview.db.FinanceController;
import com.farmerfirst.growagric.ui.profile.ModifyProfileActivity;
import com.farmerfirst.growagric.ui.profile.ProfileActivity;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.FarmController;
import com.farmerfirst.growagric.utils.NetworkUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.shuhart.stepview.StepView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

public class FinanceActivity extends AppCompatActivity {
    private ActivityFinanceBinding binding;
    private FinanceViewModel financeViewModel;
    private TextWatcher textWatcher;

    private final boolean USE_IMMERSIVE_MODE = true;
    public final boolean DISABLE_IMMERSIVE_MODE_ON_KEYBOARD_OPEN = false;
    boolean edit = false;

    private int position = 0;
    private String stepTracker ="start";
    private FinanceAndroidViewModel financeAndroidViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financeViewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        binding = DataBindingUtil.setContentView(FinanceActivity.this,R.layout.activity_finance);

        ComponentUtils.customActionBar("Finance application",FinanceActivity.this);

        binding.setLifecycleOwner(this);
        binding.setFinanceViewModel(financeViewModel);

        AndroidBug5497Workaround.assistActivity(this);

        binding.stepView.done(false);

        initCalendar();
        initSpinner(getResources());
        setAverageMortalityRate();
        editTextChangeListeners();
        setupStepView();

        FinanceController controller = new FinanceController(new ViewModelProvider(this));

        financeViewModel.getFinaceRequest().observe(this, new Observer<AddFinance>() {
            @Override
            public void onChanged(AddFinance addFinance) {
                String message = null;
                if(binding.spinFarmDetail.getSelectedItemPosition() == -1){
                    message = "Select a farm";
                    binding.spinFarmDetail.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFinance).getStrNumberOfBirdsToRaisedNow())){
                    message = "Enter number of chicks you are raising now";
                    binding.editNumberOfBirdsToRaisedNow.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFinance).getStrAverageMortalityRate())){
                    message = "Enter mortality rate";
                    binding.editAverageMortalityRate.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFinance).getStrLoanAmount())){
                    message = "Enter loan amount";
                    binding.editLoanAmount.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFinance).getStrFinanceDate())){
                    message = "Pick a date";
                    binding.editFinanceDate.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFinance).getStrTotalCostOfChicks())){
                    message = "Enter total chicks cost";
                    binding.editTotalCostOfChicks.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFinance).getStrTotalCostOfFeeds())){
                    message = "Enter total feeds cost";
                    binding.editTotalCostOfFeeds.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFinance).getStrTotalBroodingCost())){
                    message = "Enter total broading cost";
                    binding.editTotalBroodingCost.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(addFinance).getStrMedicineVaccineCost())) {
                    message = "Enter total medicine & vaccine cost";
                    binding.editMedicineVaccineCost.requestFocus();
                }else{

                    message = "";

                    binding.buttonSubmit.showLoading();

                    LocalSharedPreferences prefs = new LocalSharedPreferences(FinanceActivity.this);
                    String profileData = prefs.getProfileInfo();
                    String applicantName = new String();
                    String phoneNumber = new String();
                    String farmUUID = new String();

                    try {
                        JSONObject obj = new JSONObject(profileData);
                        applicantName = obj.getJSONObject("message").getString("first_name") +" "+obj.getJSONObject("message").getString("last_name");
                        phoneNumber = obj.getJSONObject("message").getString("phone_number");
                        farmUUID = obj.getJSONObject("message").getString("farmer_uuid");
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }

                    String noOfChickRaisedNow = Utils.getCommalessNumber(binding.editNumberOfBirdsToRaisedNow.getText().toString());
                    String mortalityRate = Utils.getCommalessNumber(binding.editAverageMortalityRate.getText().toString());
                    String loanAmount = Utils.getCommalessNumber(binding.editLoanAmount.getText().toString());
                    String dateWhenLoanNeeded = Utils.getCommalessNumber(binding.editFinanceDate.getText().toString());
                    String totalChickCost = Utils.getCommalessNumber(binding.editTotalCostOfChicks.getText().toString());
                    String totalFeedCost = Utils.getCommalessNumber(binding.editTotalCostOfFeeds.getText().toString());
                    String totalBroodingCost = Utils.getCommalessNumber(binding.editTotalBroodingCost.getText().toString());
                    String totalMedicineVaccineCost = Utils.getCommalessNumber(binding.editMedicineVaccineCost.getText().toString());
                    String projectedChickSalePrice = binding.editProjectSalePerChick.getText().toString();
                    String chickSupplier = binding.editChickSupplier.getText().toString();
                    String feedSupplier = binding.editFeedsSupplier.getText().toString();

                    JSONObject objPayload = ClientData.AddFinancePayload(applicantName,phoneNumber,farmUUID,loanAmount,noOfChickRaisedNow,mortalityRate,totalChickCost,totalFeedCost,totalBroodingCost,totalMedicineVaccineCost,projectedChickSalePrice,chickSupplier,feedSupplier);

                    final IApiInterface iApiInterface = RetrofitClient.getApiService();

                    iApiInterface.addFinance(objPayload.toString()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                                LocalSharedPreferences prefs = new LocalSharedPreferences(FinanceActivity.this);
                                String responseBody = response.body().string();
                                if (response.code() == 200 || response.code() == 201){
                                    JSONObject obj = new JSONObject(responseBody);
                                    if(obj.get("success").toString().trim() == "true"){
                                        prefs.setFinance(obj.toString());

                                        controller.storeToDB(
                                            obj.getJSONObject("message").get("application_uuid").toString(),
                                            obj.getJSONObject("message").get("farmer_uuid").toString(),
                                            obj.getJSONObject("message").get("loan_amount").toString(),
                                            obj.getJSONObject("message").get("chick_cost").toString(),
                                            obj.getJSONObject("message").get("feed_cost").toString(),
                                            obj.getJSONObject("message").get("brooding_cost").toString(),
                                            obj.getJSONObject("message").get("vaccine_medicine_cost").toString(),
                                            obj.getJSONObject("message").get("date_required").toString(),
                                            "0" /*obj.getJSONObject("message").get("financial_sponsor").toString()*/,
                                            obj.getJSONObject("message").get("application_status").toString(),
                                            obj.getJSONObject("message").get("number_of_chicks_raised_now").toString(),
                                            obj.getJSONObject("message").get("projected_sales_price_per_chick").toString(),
                                            sdf.format(new Date())
                                        );
                                        resetActivityComponents();
                                        binding.buttonSubmit.hideLoading();
                                        ComponentUtils.showSuccessToast(binding.getRoot(), "Finance request has been received successfully!");
                                        Intent j = new Intent(FinanceActivity.this, FinanceRecyclerViewActivity.class);
                                        startActivity(j);
                                        finish();
                                    }else{
                                        binding.buttonSubmit.hideLoading();
                                        customBottomDialog(obj.get("message").toString());
                                    }
                                }else{
                                    binding.buttonSubmit.hideLoading();
                                    ComponentUtils.hideKeyboard(FinanceActivity.this);
                                    ComponentUtils.showSuccessToast(binding.getRoot(),"Something wrong has happened.");
                                }
                            }catch (Exception e){
                                binding.buttonSubmit.hideLoading();
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call,Throwable t) {
                            binding.buttonSubmit.hideLoading();
                            t.printStackTrace();
                        }
                    });
                }
                if(message.trim().length() > 0){
                    ComponentUtils.hideKeyboard(FinanceActivity.this);
                    ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
                }
            }
        });
    }

    private void initCalendar() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        binding.buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FinanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editFinanceDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
        binding.editFinanceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FinanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editFinanceDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
    }
    private void initSpinner(Resources res){
        List<String> farmList = new ArrayList<String>();
        LocalSharedPreferences prefs = new LocalSharedPreferences(FinanceActivity.this);

        List<Farm> farms = getFarmList(LocalData.GetUserUUID(prefs));

        for(Farm farm : farms){
            farmList.add("Farm "+farm.getCounty().toString().trim() + " " +farm.getSub_county().toString().trim());
        }

        binding.spinFarmDetail.setItem(farmList);

        String[] chickSuppliesItemData = res.getStringArray(R.array.chickSupplier);
        ArrayList<String> chickSupplierItemList = new ArrayList<>(Arrays.asList(chickSuppliesItemData));
        CustomAdapter adapter = new CustomAdapter(this,chickSupplierItemList);
        binding.spinPreferredChickSupplier.setAdapter(adapter);
        binding.spinPreferredChickSupplier.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);

        binding.spinPreferredChickSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(chickSupplierItemList.get(position).trim().equalsIgnoreCase("other")){
                    binding.textChickSupplier.setVisibility(View.VISIBLE);
                    binding.editChickSupplier.getText().clear();
                }else{
                    binding.editChickSupplier.setText(chickSupplierItemList.get(position).toString());
                    binding.textChickSupplier.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] feedsSuppliesItemData = res.getStringArray(R.array.feedSupplier);
        ArrayList<String> feedsSupplierItemList = new ArrayList<>(Arrays.asList(feedsSuppliesItemData));
        adapter = new CustomAdapter(this,feedsSupplierItemList);
        binding.spinPreferredFeedsSupplier.setAdapter(adapter);
        binding.spinPreferredFeedsSupplier.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);

        binding.spinPreferredFeedsSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(feedsSupplierItemList.get(position).trim().equalsIgnoreCase("other")){
                    binding.textFeedsSupplier.setVisibility(View.VISIBLE);
                    binding.editFeedsSupplier.getText().clear();
                }else{
                    binding.editFeedsSupplier.setText(feedsSupplierItemList.get(position).toString());
                    binding.textFeedsSupplier.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setAverageMortalityRate(){
        binding.editAverageMortalityRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if(!input.isEmpty()){
                    float value = Float.parseFloat(input.replace("%",""));
                    if(value > 100){
                        binding.editAverageMortalityRate.setText("100%");
                    }
                }
            }
        });
    }
    private void editTextChangeListeners(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(binding.editTotalCostOfChicks.getText().toString().trim())
                        || !TextUtils.isEmpty(binding.editTotalCostOfFeeds.getText().toString().trim())
                        || !TextUtils.isEmpty(binding.editTotalBroodingCost.getText().toString().trim())
                        || !TextUtils.isEmpty(binding.editMedicineVaccineCost.getText().toString().trim())){

                    int chickCost = TextUtils.isEmpty(binding.editTotalCostOfChicks.getText().toString().trim()) ? 0 : Integer.parseInt(Utils.getCommalessNumber(binding.editTotalCostOfChicks.getText().toString().trim()));
                    int feedCost = TextUtils.isEmpty(binding.editTotalCostOfFeeds.getText().toString().trim()) ? 0 : Integer.parseInt(Utils.getCommalessNumber(binding.editTotalCostOfFeeds.getText().toString().trim()));
                    int broodCost = TextUtils.isEmpty(binding.editTotalBroodingCost.getText().toString().trim()) ? 0 : Integer.parseInt(Utils.getCommalessNumber(binding.editTotalBroodingCost.getText().toString().trim()));
                    int medicineVaccineCost = TextUtils.isEmpty(binding.editMedicineVaccineCost.getText().toString().trim()) ? 0 : Integer.parseInt(Utils.getCommalessNumber(binding.editMedicineVaccineCost.getText().toString().trim()));

                    int total = computeCostBreakDownEstimate(chickCost,feedCost,broodCost,medicineVaccineCost);

                    binding.editLoanAmount.setText(Utils.formatWithCommas(String.valueOf(total)));
                }else{
                    binding.editLoanAmount.setText("0");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        binding.editTotalCostOfChicks.addTextChangedListener(textWatcher);
        binding.editTotalCostOfFeeds.addTextChangedListener(textWatcher);
        binding.editTotalBroodingCost.addTextChangedListener(textWatcher);
        binding.editMedicineVaccineCost.addTextChangedListener(textWatcher);

        binding.editNumberOfBirdsToRaisedNow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                binding.editNumberOfBirdsToRaisedNow.removeTextChangedListener(this);
                String formattedString = s.toString().replace(",","");
                binding.editNumberOfBirdsToRaisedNow.setText(Utils.formatWithCommas(formattedString));
                binding.editNumberOfBirdsToRaisedNow.setSelection(formattedString.length());
                binding.editNumberOfBirdsToRaisedNow.addTextChangedListener(this);
            }
        });
        binding.editTotalCostOfChicks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                binding.editTotalCostOfChicks.removeTextChangedListener(this);
                String formattedString = s.toString().replace(",","");
                binding.editTotalCostOfChicks.setText(Utils.formatWithCommas(formattedString));
                binding.editTotalCostOfChicks.setSelection(formattedString.length());
                binding.editTotalCostOfChicks.addTextChangedListener(this);
            }
        });
        binding.editTotalCostOfFeeds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                binding.editTotalCostOfFeeds.removeTextChangedListener(this);
                String formattedString = s.toString().replace(",","");
                binding.editTotalCostOfFeeds.setText(Utils.formatWithCommas(formattedString));
                binding.editTotalCostOfFeeds.setSelection(formattedString.length());
                binding.editTotalCostOfFeeds.addTextChangedListener(this);
            }
        });
        binding.editTotalBroodingCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                binding.editTotalBroodingCost.removeTextChangedListener(this);
                String formattedString = s.toString().replace(",","");
                binding.editTotalBroodingCost.setText(Utils.formatWithCommas(formattedString));
                binding.editTotalBroodingCost.setSelection(formattedString.length());
                binding.editTotalBroodingCost.addTextChangedListener(this);
            }
        });
        binding.editMedicineVaccineCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                binding.editMedicineVaccineCost.removeTextChangedListener(this);
                String formattedString = s.toString().replace(",","");
                binding.editMedicineVaccineCost.setText(Utils.formatWithCommas(formattedString));
                binding.editMedicineVaccineCost.setSelection(formattedString.length());
                binding.editMedicineVaccineCost.addTextChangedListener(this);
            }
        });
    }
    public int computeCostBreakDownEstimate(int chickCost,int feedCost,int broodCost,int medicineVaccineCost){
        int sumTotal = (chickCost+feedCost+broodCost+medicineVaccineCost);
        return sumTotal;
    }
    private void setupStepView(){
        binding.stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                List<String> steps = new ArrayList<>();
                steps.add(Arrays.asList(getResources().getStringArray(R.array.finance_steps)).toString());
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
                binding.loanTwo.setVisibility(View.VISIBLE);
                binding.loanOne.setVisibility(View.GONE);
            }else if(position == 2){
                binding.loanOne.setVisibility(View.GONE);
                binding.loanTwo.setVisibility(View.GONE);
                binding.loanThree.setVisibility(View.VISIBLE);
            }else if(position == 3){
                binding.loanOne.setVisibility(View.GONE);
                binding.loanTwo.setVisibility(View.GONE);
                binding.loanThree.setVisibility(View.GONE);
                binding.loanFour.setVisibility(View.VISIBLE);
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
                binding.loanOne.setVisibility(View.VISIBLE);
                binding.loanTwo.setVisibility(View.GONE);
                stepTracker ="start";
            }else if(position == 1){
                binding.loanOne.setVisibility(View.GONE);
                binding.loanTwo.setVisibility(View.VISIBLE);
                binding.loanThree.setVisibility(View.GONE);
            }else if(position == 2){
                binding.loanThree.setVisibility(View.VISIBLE);
                binding.loanFour.setVisibility(View.GONE);
                binding.submitLayout.setVisibility(View.GONE);
                binding.navigationLayout.setVisibility(View.VISIBLE);
            }
        }
        binding.stepView.done(false);
        binding.stepView.go(position,true);
    }
    private void resetActivityComponents(){
        binding.editNumberOfBirdsToRaisedNow.setText(null);
        binding.editAverageMortalityRate.setText(null);
        binding.editLoanAmount.setText(null);
        binding.editFinanceDate.setText(null);
        binding.editTotalCostOfChicks.setText(null);
        binding.editTotalCostOfFeeds.setText(null);
        binding.editTotalBroodingCost.setText(null);
        binding.editMedicineVaccineCost.setText(null);
        binding.editProjectSalePerChick.setText(null);
        binding.editChickSupplier.setText(null);
        binding.editFeedsSupplier.setText(null);
        binding.spinPreferredChickSupplier.setSelection(0);
        binding.spinPreferredFeedsSupplier.setSelection(0);
        binding.buttonSubmit.setEnabled(false);
    }

    public List<Farm> getFarmList(String userUUID){
        FarmAndroidViewModel farmAndroidViewModel = new ViewModelProvider(this).get(FarmAndroidViewModel.class);
        List<Farm> farms = farmAndroidViewModel.getAllFarmsSynchronously(userUUID);
        return farms;
    }


    private void customBottomDialog(String message) {
        new BottomDialog.Builder(this)
                .setTitle("NOTICE")
                .setContent(message)
                .setPositiveText("OK")
                .setPositiveBackgroundColor(ContextCompat.getColor(this, R.color.custom_app_theme_color))
                .setNegativeTextColor(ContextCompat.getColor(this, R.color.custom_app_theme_color))
                .setPositiveTextColor(ContextCompat.getColor(this, R.color.white))
                .setCancelable(false)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
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