package com.farmerfirst.growagric.ui.record_keeping.sales;

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
import com.farmerfirst.growagric.databinding.ActivitySalesBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmAndroidViewModel;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.record_keeping.RecordTypeActivity;
import com.farmerfirst.growagric.ui.record_keeping.data.ClientData;
import com.farmerfirst.growagric.ui.record_keeping.db.SimpleLedgerController;
import com.farmerfirst.growagric.ui.record_keeping.db.person.Person;
import com.farmerfirst.growagric.ui.record_keeping.db.person.PersonAndroidViewModel;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SalesActivity extends AppCompatActivity {
    private ActivitySalesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SalesActivity.this,R.layout.activity_sales);

        ComponentUtils.customActionBar("Sales", SalesActivity.this);

        AndroidBug5497Workaround.assistActivity(SalesActivity.this);

        LocalSharedPreferences prefs = new LocalSharedPreferences(SalesActivity.this);
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
                DatePickerDialog dialog = new DatePickerDialog(SalesActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editDateSold.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        binding.editDateSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SalesActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editDateSold.setText(dayOfMonth+"-"+(month+1)+"-"+year);
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
        }else if(binding.spinCustomerName.getSelectedItemPosition() == -1){
            message = "Select a customer";
            binding.spinCustomerName.requestFocus();
        }else if (TextUtils.isEmpty(binding.editDateSold.getText())){
            message = "Pick a date";
            binding.editDateSold.requestFocus();
        }else if (TextUtils.isEmpty(binding.editQuantity.getText())){
            message = "Enter quantity";
            binding.editQuantity.requestFocus();
        } else if (TextUtils.isEmpty(binding.editTotalSellingPrice.getText())){
            message = "Enter total selling price";
            binding.editTotalSellingPrice.requestFocus();
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
            String customerName = binding.spinCustomerName.getSelectedItem().toString();
            String itemSold = binding.spinItemSold.getSelectedItem().toString();
            String sellDate = binding.editDateSold.getText().toString();
            String quantity = binding.editQuantity.getText().toString();
            String sellingPrice = binding.editTotalSellingPrice.getText().toString();

            String desciption = "Sales: "+itemSold.toUpperCase()+" sold to "+customerName.toUpperCase()+" @ kes "+sellingPrice;
            String recordType = "sales";
            String notes = "sales";

            SimpleLedgerController controller = new SimpleLedgerController(new ViewModelProvider(this));

            JSONObject obj = ClientData.AddFarmRecordPayload(
                    farmID,
                    user_uuid,
                    0,
                    Double.valueOf(sellingPrice),
                    Double.valueOf(sellingPrice),
                    desciption,
                    recordType,
                    notes,
                    sellDate
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
                    binding.spinCustomerName.clearSelection();
                    binding.spinItemSold.clearSelection();
                    binding.editDateSold.setText(null);
                    binding.editQuantity.setText(null);
                    binding.editTotalSellingPrice.setText(null);
                    binding.buttonSubmit.hideLoading();
                }
            },1000);
        }else{
            ComponentUtils.hideKeyboard(SalesActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
        }
    }

    private void initSpinners(Resources res,String user_uuid){
        List<String> farmList = new ArrayList<String>();
        List<String> farmReferenceList = new ArrayList<String>();
        List<String> customerList = new ArrayList<String>();
        LocalSharedPreferences prefs = new LocalSharedPreferences(SalesActivity.this);

        List<Farm> farms = getFarmList(user_uuid);

        for(Farm farm : farms){
            farmList.add("Farm "+farm.getCounty().toString().trim() + " " +farm.getSub_county().toString().trim());
            farmReferenceList.add(farm.getFarm_uuid());
        }
        binding.spinFarm.setItem(farmList);

        List<Person> customers = getCustomerList();
        for (Person person: customers){
            customerList.add(person.getPerson_name().toString().trim());
        }
        binding.spinCustomerName.setItem(customerList);

        String[] salesItemData = res.getStringArray(R.array.salesItems);
        ArrayList<String> salesItemList = new ArrayList<>(Arrays.asList(salesItemData));
        CustomAdapter adapter = new CustomAdapter(this,salesItemList);
        binding.spinItemSold.setAdapter(adapter);

        binding.spinFarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                binding.editFarmID.setText(farmReferenceList.get(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public List<Farm> getFarmList(String userUUID){
        FarmAndroidViewModel farmAndroidViewModel = new ViewModelProvider(this).get(FarmAndroidViewModel.class);
        List<Farm> farms = farmAndroidViewModel.getAllFarmsSynchronously(userUUID);
        return farms;
    }

    public List<Person> getCustomerList(){
        int personType = 1;
        PersonAndroidViewModel personAndroidViewModel = new ViewModelProvider(this).get(PersonAndroidViewModel.class);
        List<Person> customers = personAndroidViewModel.getAllPersonSynchronously(personType);
        return customers;
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