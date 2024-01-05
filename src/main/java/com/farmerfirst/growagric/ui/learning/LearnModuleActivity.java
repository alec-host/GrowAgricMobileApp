package com.farmerfirst.growagric.ui.learning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.databinding.ActivityLearnModuleBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.learning.chart.LearningChartActivity;
import com.farmerfirst.growagric.ui.learning.db.CourseController;
import com.farmerfirst.growagric.ui.learning.db.LearningProgressController;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgress;
import com.farmerfirst.growagric.ui.learning.db.module.Module;
import com.farmerfirst.growagric.ui.learning.db.module.ModuleAdapter;
import com.farmerfirst.growagric.ui.learning.db.module.ModuleAndroidViewModel;
import com.farmerfirst.growagric.ui.learning.online_file.ViewOnlinePdfActivity;
import com.farmerfirst.growagric.ui.record_keeping.RecordKeepingDashboardActivity;
import com.farmerfirst.growagric.ui.record_keeping.RecordTypeActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.DateUtils;
import com.farmerfirst.growagric.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LearnModuleActivity extends AppCompatActivity implements ModuleAdapter.OnDeleteButtonListener,ModuleAdapter.ModuleAdapterListener{
    private ActivityLearnModuleBinding binding;
    private ModuleAdapter moduleAdapter;
    private ModuleAndroidViewModel androidViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(LearnModuleActivity.this,R.layout.activity_learn_module);

        ComponentUtils.customActionBar("Learning modules", LearnModuleActivity.this);

        setupCardView();
        learningMetric();
        downloadModuleData();
        setupRecyclerView();
    }

    private void learningMetric(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  total sum:"+getTotalHoursOfLearning());
                String hoursOfLearning = (getTotalHoursOfLearning());
                int completedCourse = getCompletedCourseCount();
                int savedCourse = getSavedCourseCount();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hoursOfLearning != null) {
                            binding.tvHours.setText(String.valueOf(String.format("%.4f",Float.parseFloat(hoursOfLearning))));
                        }
                        binding.tvCompleted.setText(String.valueOf(completedCourse));
                        binding.tvSaved.setText(String.valueOf(savedCourse));
                    }
                });
            }
        });
    }

    private void downloadModuleData(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(LearnModuleActivity.this);
        String phoneNumber = LocalData.GetUserPhoneNumber(prefs);
        learningModule(phoneNumber, new ICustomResponseCallback() {
            @Override
            public void onSuccess(String value) {
                Gson gson = new Gson();
                List<Module> moduleList = gson.fromJson(value, new TypeToken<List<Module>>() {
                }.getType());
                androidViewModel.saveAllModule(moduleList);
            }
            @Override
            public void onFailure(){}
        });
    }

    private void setupCardView(){
        binding.cardViewHoursOfLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate();
            }
        });

        binding.cardViewSavedLearningMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate();
            }
        });

        binding.cardViewCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate();
            }
        });
    }

    private void setupRecyclerView(){
        moduleAdapter = new ModuleAdapter(LearnModuleActivity.this,this,this);
        androidViewModel = new ViewModelProvider(this).get(ModuleAndroidViewModel.class);
        androidViewModel.getAllModule().observe(this, modules -> moduleAdapter.setData(modules));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.moduleRecyclerView.setLayoutManager(mLayoutManager);
        binding.moduleRecyclerView.setHasFixedSize(true);
        binding.moduleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.moduleRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        binding.moduleRecyclerView.setAdapter(moduleAdapter);
    }

    private void learningModule(String phoneNumber,ICustomResponseCallback iCustomResponseCallback){
        final IApiInterface iApiInterface = RetrofitClient.getApiService();
        iApiInterface.getModuleList(phoneNumber).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.code() == 200 || response.code() == 201){
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.get("data").toString());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }

    private void navigate(){
        Intent intent = new Intent(LearnModuleActivity.this, LearningChartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private String getTotalHoursOfLearning(){
        LearningProgressController controller = new LearningProgressController(new ViewModelProvider(this));
        String total = controller.getTotalHoursOfLearning();
        return total;
    }

    private int getCompletedCourseCount(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(LearnModuleActivity.this);
        LearningProgressController controller = new LearningProgressController(new ViewModelProvider(this));
        int count = controller.getCompletedCourse(LocalData.GetUserUUID(prefs));

        return count;
    }

    private int getSavedCourseCount(){
        CourseController controller = new CourseController(new ViewModelProvider(this));
        int count = controller.getDownloadedCourseCount();

        return count;
    }

    @Override
    public void onDeleteButtonClicked(Module module) {
        androidViewModel.deleteModule(module);
    }
    @Override
    public void onModuleClick(String course_uuid){}
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