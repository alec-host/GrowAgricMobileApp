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
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.ActivityBroodingBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmAndroidViewModel;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.record_keeping.data.ClientData;
import com.farmerfirst.growagric.ui.record_keeping.db.SimpleLedgerController;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BroodingActivity extends AppCompatActivity {
    private ActivityBroodingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BroodingActivity.this,R.layout.activity_brooding);
        ComponentUtils.customActionBar("Input: brooding", BroodingActivity.this);

        AndroidBug5497Workaround.assistActivity(BroodingActivity.this);

        LocalSharedPreferences prefs = new LocalSharedPreferences(BroodingActivity.this);
        String user_uuid = LocalData.GetUserUUID(prefs);

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
                DatePickerDialog dialog = new DatePickerDialog(BroodingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editBroodingDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        binding.editBroodingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(BroodingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editBroodingDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
    }

    private void initSpinners(Resources res,String user_uuid){
        List<String> farmList = new ArrayList<String>();
        List<String> farmReferenceList = new ArrayList<String>();
        LocalSharedPreferences prefs = new LocalSharedPreferences(BroodingActivity.this);

        List<Farm> farms = getFarmList(user_uuid);

        for(Farm farm : farms){
            farmList.add("Farm "+farm.getCounty().toString().trim() + " " +farm.getSub_county().toString().trim());
            farmReferenceList.add(farm.getFarm_uuid());
        }
        binding.spinFarm.setItem(farmList);

        String[] broodingTypeItemData = res.getStringArray(R.array.broodingItems);
        ArrayList<String> broodingTypeItemList = new ArrayList<>(Arrays.asList(broodingTypeItemData));
        CustomAdapter adapter = new CustomAdapter(this,broodingTypeItemList);
        binding.spinBroodingType.setAdapter(adapter);

        binding.spinBroodingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(broodingTypeItemList.get(position).trim().equalsIgnoreCase("other")){
                    binding.textBroodingTypeItem.setVisibility(View.VISIBLE);
                    binding.editBroodingTypeItem.getText().clear();
                }else{
                    binding.editBroodingTypeItem.setText(broodingTypeItemList.get(position).toString());
                    binding.textBroodingTypeItem.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.spinFarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                binding.editFarmID.setText(farmReferenceList.get(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public String validateInput(){
        String message = new String();
        if(binding.spinFarm.getSelectedItemPosition() == -1){
            message = "Select a farm";
            binding.spinFarm.requestFocus();
        }else if(binding.spinBroodingType.getSelectedItemPosition() == -1){
            message = "Select an item";
            binding.spinBroodingType.requestFocus();
        }else if(TextUtils.isEmpty(binding.editBroodingTypeItem.getText()) && binding.spinBroodingType.getSelectedItem().toString().equalsIgnoreCase("other")){
            message = "Enter other brooding item";
            binding.editBroodingTypeItem.requestFocus();
        }else if (TextUtils.isEmpty(binding.editBroodingDate.getText())){
            message = "Pick brooding date";
            binding.editBroodingDate.requestFocus();
        }else if (TextUtils.isEmpty(binding.editAddQuantity.getText())){
            message = "Enter quantity";
            binding.editAddQuantity.requestFocus();
        } else if (TextUtils.isEmpty(binding.editAmountSpent.getText())){
            message = "Enter amount spent";
            binding.editAmountSpent.requestFocus();
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
            String broodingItem = binding.editBroodingTypeItem.getText().toString();
            String broodingDate = binding.editBroodingDate.getText().toString();
            String quantity = binding.editAddQuantity.getText().toString();
            String amountSpent = binding.editAmountSpent.getText().toString();
            String notes = binding.editNotes.getText().toString();

            String desciption = "Kes "+amountSpent+" spent on ("+quantity+") "+broodingItem.toUpperCase();
            String recordType = "brooding";

            SimpleLedgerController controller = new SimpleLedgerController(new ViewModelProvider(this));

            JSONObject obj = ClientData.AddFarmRecordPayload(
                    farmID,
                    user_uuid,
                    Double.valueOf(amountSpent),
                    0,
                    Double.valueOf(amountSpent),
                    desciption,
                    recordType,
                    notes,
                    broodingDate
            );

            HttpClient.PostFarmRecord(obj, new ICustomResponseCallback() {
                @Override
                public void onSuccess(String value){
                    try {
                        if (value.length() > 0) {
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
                    binding.spinBroodingType.clearSelection();
                    binding.editBroodingTypeItem.setText(null);
                    binding.editBroodingDate.setText(null);
                    binding.editAddQuantity.setText(null);
                    binding.editAmountSpent.setText(null);
                    binding.editNotes.setText(null);
                    binding.buttonSubmit.hideLoading();
                }
            },1000);
        }else{
            ComponentUtils.hideKeyboard(BroodingActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
        }
    }

    public List<Farm> getFarmList(String userUUID){
        FarmAndroidViewModel farmAndroidViewModel = new ViewModelProvider(this).get(FarmAndroidViewModel.class);
        List<Farm> farms = farmAndroidViewModel.getAllFarmsSynchronously(userUUID);
        return farms;
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