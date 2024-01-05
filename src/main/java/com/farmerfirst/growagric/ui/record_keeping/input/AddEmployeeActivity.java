package com.farmerfirst.growagric.ui.record_keeping.input;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityAddEmployeeBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.record_keeping.db.person.IPersonDao;
import com.farmerfirst.growagric.ui.record_keeping.db.person.Person;
import com.farmerfirst.growagric.ui.record_keeping.db.person.PersonAdapter;
import com.farmerfirst.growagric.ui.record_keeping.db.person.PersonAndroidViewModel;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class AddEmployeeActivity extends AppCompatActivity implements  PersonAdapter.OnDeleteButtonListener,PersonAdapter.PersonAdapterListener{
    private ActivityAddEmployeeBinding binding;
    private PersonAndroidViewModel androidViewModel;
    private PersonAdapter personAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AddEmployeeActivity.this, R.layout.activity_add_employee);

        ComponentUtils.customActionBar("Add employee",AddEmployeeActivity.this);

        keyboardAdjustment();

        setupRecyclerView();

        inputCheck();

        searchPerson();

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LocalSharedPreferences prefs = new LocalSharedPreferences(AddEmployeeActivity.this);
                postClientData(prefs);
            }
        });
    }

    private void keyboardAdjustment(){
        binding.getRoot().getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = binding.getRoot().getHeight()/2;
                int threshold = height/2;
                if(binding.employeeRecyclerView.getHeight() > (height-threshold)){
                    binding.scrollView.computeScroll();
                }
            }
        });
    }

    private void setupRecyclerView(){
        int person_type = 0;
        personAdapter = new PersonAdapter(AddEmployeeActivity.this,this,this);
        androidViewModel = new ViewModelProvider(this).get(PersonAndroidViewModel.class);
        androidViewModel.getAllPerson(person_type).observe(this, people -> personAdapter.setData(people));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.employeeRecyclerView.setLayoutManager(mLayoutManager);
        binding.employeeRecyclerView.setHasFixedSize(true);
        binding.employeeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.employeeRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        binding.employeeRecyclerView.setAdapter(personAdapter);
    }

    private void inputCheck(){
        binding.editEmployeeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() <= 3) {
                    binding.editEmployeeName.setError("Invalid name");
                }else{
                    binding.editEmployeeName.setError(null);
                }
            }
        });

        binding.editPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(!x.startsWith("0") && x.length() <= 10) {
                    binding.editPhoneNumber.setError("Invalid phone number");
                }else if(x.startsWith("0") && x.length() < 10){
                    binding.editPhoneNumber.setError("Invalid phone number");
                }else{
                    binding.editPhoneNumber.setError(null);
                }
            }
        });

        binding.editEmployeeID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() <= 3) {
                    binding.editEmployeeID.setError("Invalid ID number");
                }else{
                    binding.editEmployeeID.setError(null);
                }
            }
        });
    }

    public String validateInput(){
        String message = new String();
        if(TextUtils.isEmpty(binding.editEmployeeName.getText())){
            message = "Enter employee name";
        }else if(binding.editEmployeeName.getText().toString().length() < 3){
            message = "Enter employee name";
        }else if(TextUtils.isEmpty(binding.editPhoneNumber.getText())){
            message = "Enter phone number";
        }else if(!binding.editPhoneNumber.getText().toString().startsWith("0") && binding.editPhoneNumber.getText().toString().length() < 11){
            message = "Invalid phone number";
            binding.editPhoneNumber.requestFocus();
        }else if(binding.editPhoneNumber.getText().toString().startsWith("0") && binding.editPhoneNumber.getText().toString().length() < 10){
            message = "Invalid phone number";
        }else if(TextUtils.isEmpty(binding.editEmployeeID.getText())){
            message = "Enter ID number";
        }else if(binding.editEmployeeID.getText().toString().length() < 3){
            message = "Enter  ID number";
        }else{
            message = "";
        }
        return message;
    }

    private void postClientData(LocalSharedPreferences prefs){
        String message = validateInput();
        if(message.trim().length() == 0){
            binding.buttonSubmit.showLoading();
            String emp_uuid = String.valueOf(Utils.generateRandomNumber(100000L,999999L));
            String name = binding.editEmployeeName.getText().toString().trim();
            String phone = binding.editPhoneNumber.getText().toString().trim();
            String id_number = binding.editEmployeeID.getText().toString().trim();
            String user_uuid = LocalData.GetUserUUID(prefs);
            //-.save data.
            storeToDB(emp_uuid,name,phone,id_number,user_uuid,Utils.simpleDate().format(new Date()));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.editEmployeeName.requestFocus();
                    binding.editEmployeeName.setText(null);
                    binding.editEmployeeName.setError(null);
                    binding.editPhoneNumber.setText(null);
                    binding.editPhoneNumber.setError(null);
                    binding.editEmployeeID.setText(null);
                    binding.editEmployeeID.setError(null);
                    binding.buttonSubmit.hideLoading();
                    ComponentUtils.hideKeyboard(AddEmployeeActivity.this);
                }
            },1000);
        }else{
            ComponentUtils.hideKeyboard(AddEmployeeActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
        }
    }

    private void searchPerson(){
        int person_type = 0;
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String search_name = binding.editSearch.getText().toString().toLowerCase();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filter(search_name);
                    }
                },500);
            }
        });
    }

    private void filter(String search_name) {
        LocalSharedPreferences prefs = new LocalSharedPreferences(AddEmployeeActivity.this);
        String user_uuid = LocalData.GetUserUUID(prefs);
        List<Person> searchList = new ArrayList<>();
        int person_type = 0;
        searchList = androidViewModel.getSearchedPersonSync(user_uuid,search_name,person_type);
        if (searchList.isEmpty()) {
            Toast.makeText(AddEmployeeActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            personAdapter.setData(searchList);
        }
    }

    private void storeToDB(String employee_uuid,String name,String phone,String ID,String user_uuid,String date){
        androidViewModel.savePerson(new Person(employee_uuid,name,phone,ID,user_uuid,date));
    }

    @Override
    public void onDeleteButtonClicked(Person person) {
        new BottomDialog.Builder(this)
                .setTitle("CONFIRM")
                .setContent("Do you want to delete the selected contact?")
                .setNegativeText("DELETE")
                .setPositiveText("CANCEL")
                .setPositiveBackgroundColor(ContextCompat.getColor(this,R.color.custom_app_theme_color))
                .setNegativeTextColor(ContextCompat.getColor(this,R.color.colorRed))
                .setPositiveTextColor(ContextCompat.getColor(this,R.color.white))
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog dialog) {
                        androidViewModel.deletePerson(person);
                    }
                }).show();
    }

    @Override
    public void onPersonClick(String person_uuid){}

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