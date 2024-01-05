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
import com.farmerfirst.growagric.databinding.ActivityLabourBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmAndroidViewModel;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;
import com.farmerfirst.growagric.ui.record_keeping.data.ClientData;
import com.farmerfirst.growagric.ui.record_keeping.db.SimpleLedgerController;
import com.farmerfirst.growagric.ui.record_keeping.db.person.Person;
import com.farmerfirst.growagric.ui.record_keeping.db.person.PersonAndroidViewModel;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LabourActivity extends AppCompatActivity {
    private ActivityLabourBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(LabourActivity.this,R.layout.activity_labour);
        ComponentUtils.customActionBar("Input: labour", LabourActivity.this);
        AndroidBug5497Workaround.assistActivity(LabourActivity.this);

        LocalSharedPreferences prefs = new LocalSharedPreferences(LabourActivity.this);
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
                DatePickerDialog dialog = new DatePickerDialog(LabourActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editPaymentDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        binding.editPaymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(LabourActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.editPaymentDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
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
        }else if(binding.spinEmployeeName.getSelectedItemPosition() == -1){
            message = "Enter employee name";
            binding.spinEmployeeName.requestFocus();
        }else if (TextUtils.isEmpty(binding.editPaymentDate.getText())){
            message = "Pick a date";
            binding.editPaymentDate.requestFocus();
        }else if (TextUtils.isEmpty(binding.editSalary.getText())){
            message = "Enter salary";
            binding.editSalary.requestFocus();
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
            String name = binding.spinEmployeeName.getSelectedItem().toString();
            String paymentDate = binding.editPaymentDate.getText().toString();
            String salary = binding.editSalary.getText().toString();
            String notes = binding.editNotes.getText().toString();

            String desciption = "Salary amounting to kes "+salary+" paid to "+name.toUpperCase();
            String recordType = "labour";

            SimpleLedgerController controller = new SimpleLedgerController(new ViewModelProvider(this));

            JSONObject obj = ClientData.AddFarmRecordPayload(
                    farmID,
                    user_uuid,
                    Double.valueOf(salary),
                    0,
                    Double.valueOf(salary),
                    desciption,
                    recordType,
                    notes,
                    paymentDate
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
                    binding.spinEmployeeName.clearSelection();
                    binding.editPaymentDate.setText(null);
                    binding.editSalary.setText(null);
                    binding.editNotes.setText(null);
                    binding.buttonSubmit.hideLoading();
                }
            },1000);
        }else{
            ComponentUtils.hideKeyboard(LabourActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
        }
    }

    private void initSpinners(Resources res,String user_uuid){
        List<String> farmList = new ArrayList<String>();
        List<String> farmReferenceList = new ArrayList<String>();
        List<String> employeeList = new ArrayList<String>();
        LocalSharedPreferences prefs = new LocalSharedPreferences(LabourActivity.this);

        List<Farm> farms = getFarmList(user_uuid);

        for(Farm farm : farms){
            farmList.add("Farm "+farm.getCounty().toString().trim() + " " +farm.getSub_county().toString().trim());
            farmReferenceList.add(farm.getFarm_uuid());
        }
        binding.spinFarm.setItem(farmList);

        List<Person> employees = getEmployeeList();
        for (Person person: employees){
            employeeList.add(person.getPerson_name().toString().trim());
        }
        binding.spinEmployeeName.setItem(employeeList);

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

    public List<Person> getEmployeeList(){
        int personType = 0;
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