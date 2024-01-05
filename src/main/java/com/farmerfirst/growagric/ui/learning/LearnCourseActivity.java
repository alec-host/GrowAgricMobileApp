package com.farmerfirst.growagric.ui.learning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.databinding.ActivityLearnCourseBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.learning.db.CourseController;
import com.farmerfirst.growagric.ui.learning.db.course.Course;
import com.farmerfirst.growagric.ui.learning.db.course.CourseAdapter;
import com.farmerfirst.growagric.ui.learning.db.course.CourseAndroidViewModel;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.EvenSpacingItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearnCourseActivity extends AppCompatActivity implements CourseAdapter.OnDeleteButtonListener,CourseAdapter.CourseAdapterListener {
    private ActivityLearnCourseBinding binding;
    private CourseAdapter courseAdapter;
    private CourseAndroidViewModel androidViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(LearnCourseActivity.this,R.layout.activity_learn_course);

        Intent intent = getIntent();
        String module_uuid = intent.getStringExtra("MODULE_UUID");
        String module_topic = intent.getStringExtra("MODULE_TOPIC");

        ComponentUtils.customActionBar(module_topic,LearnCourseActivity.this);

        downloadCourseData();
        setupRecyclerView(module_uuid);
    }

    private void downloadCourseData(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(LearnCourseActivity.this);
        String phoneNumber = LocalData.GetUserPhoneNumber(prefs);

        CourseController controller = new CourseController(new ViewModelProvider(this));
        List<Course> downloadedCourseList = controller.getDownloadedCourseList();

        learningCourse(phoneNumber, new ICustomResponseCallback() {
            @Override
            public void onSuccess(String value) {
                Gson gson = new Gson();
                List<Course> courseList = gson.fromJson(value,new TypeToken<List<Course>>(){}.getType());
                androidViewModel.saveAllCourse(courseList);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(Course course:downloadedCourseList){
                            File pdfFile = new File(course.getPath());
                            if(pdfFile.exists()){
                                controller.updateLearningCoursePath(course.getPath(),course.getCourse_uuid());
                            }
                        }
                    }
                },5);
            }
            @Override
            public void onFailure(){}
        });
    }

    private void setupRecyclerView(String module_uuid){
        courseAdapter = new CourseAdapter(LearnCourseActivity.this,this,this);
        androidViewModel = new ViewModelProvider(this).get(CourseAndroidViewModel.class);
        androidViewModel.getSearchedCourse(module_uuid).observe(this, courses -> courseAdapter.setData(courses));
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,2);
        binding.courseRecyclerView.setLayoutManager(mLayoutManager);
        binding.courseRecyclerView.setHasFixedSize(true);
        binding.courseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.courseRecyclerView.addItemDecoration(new EvenSpacingItemDecoration(this,2));
        binding.courseRecyclerView.setAdapter(courseAdapter);
    }

    private void learningCourse(String phoneNumber,ICustomResponseCallback iCustomResponseCallback){
        final IApiInterface iApiInterface = RetrofitClient.getApiService();
        iApiInterface.getCourseList(phoneNumber).enqueue(new Callback<ResponseBody>() {
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

    @Override
    public void onCourseClick(String course_uuid){}
    @Override
    public void onDeleteButtonClicked(Course course) {
        androidViewModel.deleteCourse(course);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(LearnCourseActivity.this,LearnModuleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
        return true;
    }
}