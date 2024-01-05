package com.farmerfirst.growagric.ui.record_keeping.sales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityAddCustomerBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
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

public class AddCustomerActivity extends AppCompatActivity implements PersonAdapter.OnDeleteButtonListener,PersonAdapter.PersonAdapterListener {
    private ActivityAddCustomerBinding binding;
    private PersonAndroidViewModel androidViewModel;
    private PersonAdapter personAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AddCustomerActivity.this,R.layout.activity_add_customer);

        ComponentUtils.customActionBar("Add customer",AddCustomerActivity.this);

        keyboardAdjustment();

        setupRecyclerView();

        inputCheck();

        searchPerson();

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalSharedPreferences prefs = new LocalSharedPreferences(AddCustomerActivity.this);
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
                if(binding.customerRecyclerView.getHeight() > (height-threshold)){
                    binding.scrollView.computeScroll();
                }
            }
        });
    }

    private void setupRecyclerView(){
        int person_type = 1;
        personAdapter = new PersonAdapter(AddCustomerActivity.this,this,this);
        androidViewModel = new ViewModelProvider(this).get(PersonAndroidViewModel.class);
        androidViewModel.getAllPerson(person_type).observe(this, people -> personAdapter.setData(people));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.customerRecyclerView.setLayoutManager(mLayoutManager);
        binding.customerRecyclerView.setHasFixedSize(true);
        binding.customerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.customerRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        binding.customerRecyclerView.setAdapter(personAdapter);
    }

    private void inputCheck(){
        binding.editCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() <= 3) {
                    binding.editCustomerName.setError("Invalid name");
                }else{
                    binding.editCustomerName.setError(null);
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

        binding.editCustomerLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() <= 3) {
                    binding.editCustomerLocation.setError("Invalid location");
                }else{
                    binding.editCustomerLocation.setError(null);
                }
            }
        });
    }

    private String validateInput(){
        String message = new String();

        if(TextUtils.isEmpty(binding.editCustomerName.getText())){
            message = "Enter customer name";
        }else if(binding.editCustomerName.getText().toString().length() < 3){
            message = "Enter customer name";
        }else if(TextUtils.isEmpty(binding.editPhoneNumber.getText())){
            message = "Enter phone number";
        }else if(!binding.editPhoneNumber.getText().toString().startsWith("0") && binding.editPhoneNumber.getText().toString().length() <= 10){
            message = "Invalid phone number";
            binding.editPhoneNumber.requestFocus();
        }else if(binding.editPhoneNumber.getText().toString().startsWith("0") && binding.editPhoneNumber.getText().toString().length() < 10){
            message = "Invalid phone number";
        }else if(TextUtils.isEmpty(binding.editCustomerLocation.getText())){
            message = "Enter location";
        }else if(binding.editCustomerLocation.getText().toString().length() < 3){
            message = "Enter location";
        }else{
            message = "";
        }
        return message;
    }

    private void postClientData(LocalSharedPreferences prefs) {
        String message = validateInput();
        if(message.trim().length() == 0) {
            binding.buttonSubmit.showLoading();
            String cust_uuid = String.valueOf(Utils.generateRandomNumber(100000L, 999999L));
            String name = binding.editCustomerName.getText().toString();
            String phone = binding.editPhoneNumber.getText().toString();
            String location = binding.editCustomerLocation.getText().toString();
            String uuid = LocalData.GetUserUUID(prefs);
            int person_type = 1;
            //-.save data.
            storeToDB(cust_uuid, name, phone, location, person_type, uuid, Utils.simpleDate().format(new Date()));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.editCustomerName.requestFocus();
                    binding.editCustomerName.setText(null);
                    binding.editCustomerName.setError(null);
                    binding.editPhoneNumber.setText(null);
                    binding.editPhoneNumber.setError(null);
                    binding.editCustomerLocation.setText(null);
                    binding.editCustomerLocation.setError(null);
                    binding.buttonSubmit.hideLoading();
                    ComponentUtils.hideKeyboard(AddCustomerActivity.this);
                }
            }, 1000);
        }else{
            ComponentUtils.hideKeyboard(AddCustomerActivity.this);
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
        LocalSharedPreferences prefs = new LocalSharedPreferences(AddCustomerActivity.this);
        String user_uuid = LocalData.GetUserUUID(prefs);
        List<Person> searchList = new ArrayList<>();
        int person_type = 1;
        searchList = androidViewModel.getSearchedPersonSync(user_uuid,search_name,person_type);
        if (searchList.isEmpty()) {
            Toast.makeText(AddCustomerActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            personAdapter.setData(searchList);
        }
    }

    private void storeToDB(String cust_uuid,String name,String phone,String location,int person_type,String user_uuid,String date){
        androidViewModel.savePerson(new Person(cust_uuid,name,phone,location,person_type,user_uuid,date));
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