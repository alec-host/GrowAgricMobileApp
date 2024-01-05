package com.farmerfirst.growagric.ui.record_keeping.input;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.ActivityChickenBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.farm.FarmActivity;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmAndroidViewModel;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.finance.recyclerview.FinanceAndroidViewModel;
import com.farmerfirst.growagric.ui.finance.recyclerview.db.Finance;
import com.farmerfirst.growagric.ui.record_keeping.data.ClientData;
import com.farmerfirst.growagric.ui.record_keeping.db.SimpleLedgerController;
import com.farmerfirst.growagric.ui.record_keeping.db.person.PersonAndroidViewModel;
import com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger.SimpleLedger;
import com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger.SimpleLedgerAndroidViewModel;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.FarmController;
import com.farmerfirst.growagric.utils.Utils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ChickenActivity extends AppCompatActivity {
    private ActivityChickenBinding binding;
    private SimpleLedgerAndroidViewModel simpleLedgerAndroidViewModel;
    private SimpleLedgerController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ChickenActivity.this,R.layout.activity_chicken);
        ComponentUtils.customActionBar("Input: chicken", ChickenActivity.this);

        AndroidBug5497Workaround.assistActivity(ChickenActivity.this);

        controller = new SimpleLedgerController(new ViewModelProvider(this));

        LocalSharedPreferences prefs = new LocalSharedPreferences(ChickenActivity.this);
        String user_uuid = LocalData.GetUserUUID(prefs);

        getRecordCount(user_uuid);

        initCalendar();
        initSpinners(getResources(),user_uuid);

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClientData(user_uuid);
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
                DatePickerDialog dialog = new DatePickerDialog(ChickenActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editPurchaseDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        binding.editPurchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(ChickenActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editPurchaseDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
    }

    public String validateInput(){
        String message = new String();
        if(binding.spinFarm.getSelectedItemPosition() == -1){
            message = "Select a farm";
            binding.spinFarm.requestFocus();
        }else if(binding.spinChickenSupplier.getSelectedItemPosition() == -1){
            message = "Select chicken supplier";
            binding.spinChickenSupplier.requestFocus();
        }else if(TextUtils.isEmpty(binding.editPurchaseDate.getText())){
            message = "Pick a date";
            binding.editPurchaseDate.requestFocus();
        }else if(TextUtils.isEmpty(binding.editAddQuantity.getText())){
            message = "Enter quantity";
            binding.editAddQuantity.requestFocus();
        } else if(TextUtils.isEmpty(binding.editTotalBuyingPrice.getText())){
            message = "Enter buying price";
            binding.editTotalBuyingPrice.requestFocus();
        } else {
            message = "";
        }
        return message;
    }

    private void postClientData(String user_uuid){

        String message = validateInput();

        if(message.trim().length() == 0){
            binding.buttonSubmit.showLoading();
            String farmID = binding.editFarmID.getText().toString();
            String chickenSupplier = binding.spinChickenSupplier.getSelectedItem().toString();
            String purchaseDate = binding.editPurchaseDate.getText().toString();
            String quantity = binding.editAddQuantity.getText().toString();
            String buyingPrice = binding.editTotalBuyingPrice.getText().toString();
            String notes = binding.editNotes.getText().toString();

            String desciption = quantity+" Chicken bought@ kes "+buyingPrice+" from "+chickenSupplier.toUpperCase();
            String recordType = "chicken";



            JSONObject obj = ClientData.AddFarmRecordPayload(
                    farmID,
                    user_uuid,
                    Double.valueOf(buyingPrice),
                    0,
                    Double.valueOf(buyingPrice),
                    desciption,
                    recordType,
                    notes,
                    purchaseDate
                    );

            HttpClient.PostFarmRecord(obj, new ICustomResponseCallback() {
                @Override
                public void onSuccess(String value){
                    try {
                        if (value.length() > 0) {
                            System.out.println("XXXXX nnnnnnnnnnnnnnnnn "+value);
                            JSONObject obj = new JSONObject(value);
                            controller.storeToDB(
                                    obj.getJSONObject("message").get("transaction_uuid").toString(),
                                    obj.getJSONObject("message").get("farm_uuid").toString(),
                                    obj.getJSONObject("message").get("farmer_uuid").toString(),
                                    obj.getJSONObject("message").get("cr").toString(),
                                    obj.getJSONObject("message").get("dr").toString(),
                                    obj.getJSONObject("message").get("running_balance").toString(),
                                    obj.getJSONObject("message").get("description").toString(),
                                    obj.getJSONObject("message").get("record_type").toString(),
                                    obj.getJSONObject("message").get("notes").toString(),
                                    obj.getJSONObject("message").get("entry_date").toString());
                        } else {
                            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXX RED RED RED REDRDE ");
                            binding.buttonSubmit.hideLoading();
                        }
                    }catch (Exception ex){
                        binding.buttonSubmit.hideLoading();
                        ex.printStackTrace();
                    }
                }
                @Override
                public void onFailure() {
                    binding.buttonSubmit.hideLoading();
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.editFarmID.setText(null);
                    binding.spinFarm.clearSelection();
                    binding.spinChickenSupplier.clearSelection();
                    binding.editPurchaseDate.setText(null);
                    binding.editAddQuantity.setText(null);
                    binding.editTotalBuyingPrice.setText(null);
                    binding.editNotes.setText(null);
                    binding.buttonSubmit.hideLoading();
                }
            },1000);
        }else{
            ComponentUtils.hideKeyboard(ChickenActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
        }
    }

    private void initSpinners(Resources res,String user_uuid){
        List<String> farmList = new ArrayList<String>();
        List<String> farmReferenceList = new ArrayList<String>();

        List<Farm> farms = getFarmList(user_uuid);

        for(Farm farm : farms){
            farmList.add("Farm "+farm.getCounty().toString().trim() + " " +farm.getSub_county().toString().trim());
            farmReferenceList.add(farm.getFarm_uuid());
        }
        binding.spinFarm.setItem(farmList);

        String[] chickSuppliesItemData = res.getStringArray(R.array.chickSupplier);
        ArrayList<String> chickSupplierItemList = new ArrayList<>(Arrays.asList(chickSuppliesItemData));
        CustomAdapter adapter = new CustomAdapter(this,chickSupplierItemList);
        binding.spinChickenSupplier.setAdapter(adapter);

        binding.spinFarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                binding.editFarmID.setText(farmReferenceList.get(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public List<Farm> getFarmList(String user_uuid){
        FarmAndroidViewModel farmAndroidViewModel = new ViewModelProvider(this).get(FarmAndroidViewModel.class);
        List<Farm> farms = farmAndroidViewModel.getAllFarmsSynchronously(user_uuid);
        return farms;
    }

    public void getRecordCount(String uuid){
        simpleLedgerAndroidViewModel = new ViewModelProvider(this).get(SimpleLedgerAndroidViewModel.class);
        List<SimpleLedger> simpleLedgers = simpleLedgerAndroidViewModel.getAllSimpleLedgerSynchronously();
        for(SimpleLedger simpleLedger: simpleLedgers){
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXxxx "+simpleLedger.getDescription()+"    "+simpleLedger.getUser_uuid());
        }
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