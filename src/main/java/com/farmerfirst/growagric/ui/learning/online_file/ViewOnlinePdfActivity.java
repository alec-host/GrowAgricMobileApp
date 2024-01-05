package com.farmerfirst.growagric.ui.learning.online_file;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseBodyCallback;
import com.farmerfirst.growagric.databinding.ActivityViewOnlinePdfBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.learning.LearnCourseActivity;
import com.farmerfirst.growagric.ui.learning.LearnModuleActivity;
import com.farmerfirst.growagric.ui.learning.db.CourseController;
import com.farmerfirst.growagric.ui.learning.db.LearningProgressController;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgress;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgressAndroidViewModel;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgressTracker;
import com.farmerfirst.growagric.ui.learning.db.learning_progress.LearningProgressTrackerAndroidViewModel;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.CopyHelper;
import com.farmerfirst.growagric.utils.DateUtils;
import com.farmerfirst.growagric.utils.FileCopyUtil;
import com.farmerfirst.growagric.utils.NetworkUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOnlinePdfActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    private ActivityViewOnlinePdfBinding binding;
    private PDFView pdfView;
    private LearningProgressAndroidViewModel androidViewModel;
    private LearningProgressTrackerAndroidViewModel androidViewModel2;
    private String course_uuid;
    private String file_name;
    private String course_name;
    private int storedCurrentPage=0;
    private int currentPage = 0;
    private final int REQUEST_CODE = 1;

    private DownloadManager downloadManager;
    private long downloadId;

    private CourseController courseController;

    private double hoursDifference = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(ViewOnlinePdfActivity.this, R.layout.activity_view_online_pdf);

        Intent intent = getIntent();

        String path = getIntent().getStringExtra("FILE_PATH");
        file_name = getIntent().getStringExtra("FILE_NAME");
        course_uuid = getIntent().getStringExtra("COURSE_UUID");
        course_name = getIntent().getStringExtra("COURSE_NAME");

        ComponentUtils.customActionBar(file_name,ViewOnlinePdfActivity.this);

        courseController = new CourseController(new ViewModelProvider(this));

        //testReadLocalDb();
        //testReadLocalDb2();

        requestPermissions();

        storedCurrentPage = getStoredPage();

        int is_course_downloaded = courseController.isCourseDownloaded(course_uuid);

        if(is_course_downloaded == 1){
            displayPdfFromFile(new File(path));
        }else{
            new Thread(new Runnable(){
                @Override
                public void run(){
                    getOnlineFileResource(path, new ICustomResponseBodyCallback(){
                        @Override
                        public void onSuccess(ResponseBody value){
                            displayPdf(value.byteStream());
                        }
                        @Override
                        public void onFailure() {}
                    });
                }
            }).start();
            downloadPdfMaterial(path,file_name);
        }

        customPdfNavigationButton();
    }

    private void displayPdf(InputStream stream) {
        binding.pdfView.fromStream(stream)
            .defaultPage(storedCurrentPage)
            .enableSwipe(true)
            .enableDoubletap(true)
            .swipeHorizontal(false)
            .onRender(new OnRenderListener() {
                @Override
                public void onInitiallyRendered(int nbPages) {
                    hideProgressBar();
                }
            })
            .onPageChange(this)
            .onLoad(this)
            .scrollHandle(new DefaultScrollHandle(this))
            .pageFitPolicy(FitPolicy.WIDTH)
            .spacing(10)
            .enableAntialiasing(true)
            .onError(Throwable::printStackTrace)
            .load();
    }

    private void displayPdfFromFile(File file) {
        binding.pdfView.fromFile(file)
            .defaultPage(storedCurrentPage)
            .enableSwipe(true)
            .enableDoubletap(true)
            .swipeHorizontal(false)
            .onRender(new OnRenderListener() {
                @Override
                public void onInitiallyRendered(int nbPages) {
                    hideProgressBar();
                }
            })
            .onPageChange(this)
            .onLoad(this)
            .scrollHandle(new DefaultScrollHandle(this))
            .pageFitPolicy(FitPolicy.WIDTH)
            .spacing(10)
            .enableAntialiasing(true)
            .onError(Throwable::printStackTrace)
            .load();
    }

    private void customPdfNavigationButton(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage > 0){
                    currentPage --;
                    displayPage(currentPage);
                }
            }
        });

        binding.btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage < binding.pdfView.getPageCount()-1){
                    currentPage ++;
                    displayPage(currentPage);
                }
            }
        });
    }

    private void getOnlineFileResource(String url, ICustomResponseBodyCallback iCustomResponseBodyCallback){
        final IApiInterface iApiInterface = RetrofitClient.getApiService();
        iApiInterface.downloadFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
                if(response.isSuccessful()){
                    iCustomResponseBodyCallback.onSuccess(response.body());
                }else{
                    hideProgressBar();
                    iCustomResponseBodyCallback.onFailure();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                iCustomResponseBodyCallback.onFailure();
            }
        });
    }

    private void hideProgressBar(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setIndeterminate(false);
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        },100);
    }

    private InputStream fileToStream(ResponseBody body){
        InputStream inputStream= body.byteStream();
        return inputStream;
    }

    private void saveFile(InputStream inputStream){}

    private void resetStartData(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(ViewOnlinePdfActivity.this);
        LearningProgressController controller = new LearningProgressController(new ViewModelProvider(this));
        controller.storeResetStarEndtData(course_uuid,LocalData.GetUserUUID(prefs));
    }

    private void saveStartData(int total_page,int current_page,LocalSharedPreferences prefs,LearningProgressController controller){
        String progress_uuid = String.valueOf(Utils.generateRandomNumber(100000L,999999L));
        controller.storeStartData(
                progress_uuid,
                course_uuid,
                course_name,
                LocalData.GetUserUUID(prefs),
                LocalData.GetUserFirstName(prefs),
                total_page,
                current_page,
                Utils.simpleDate().format(new Date()));
    }

    private void saveTrackingData(int total_page,int current_page,LocalSharedPreferences prefs,LearningProgressController controller){
        controller.storeEndData(
                course_uuid,
                LocalData.GetUserUUID(prefs),
                total_page,
                current_page,
                Utils.simpleDate().format(new Date()));
    }

    private int getStoredPage(){
        int page = 0;
        LocalSharedPreferences prefs = new LocalSharedPreferences(ViewOnlinePdfActivity.this);
        LearningProgressController controller = new LearningProgressController(new ViewModelProvider(this));
        List<LearningProgress> mlist = controller.getPageInfo(course_uuid,LocalData.GetUserUUID(prefs));
        for(LearningProgress learningProgress:mlist){
            page = learningProgress.getCurrent_page();
        }
        return page;
    }

    private void markCourseCompleted(int current_page,LocalSharedPreferences prefs,LearningProgressController controller){
        controller.storeMarkAsCompleted(
                course_uuid,
                LocalData.GetUserUUID(prefs),
                current_page);
    }

    private void testReadLocalDb(){
        androidViewModel = new ViewModelProvider(this).get(LearningProgressAndroidViewModel.class);
        List<LearningProgress> learningProgressList = androidViewModel.getAllLearnigProgressSync();
        for(LearningProgress learningProgress:learningProgressList){
            System.out.println("XXXXXXXXXXXXXXXXXXXXXX "+learningProgress.getCourse_name()+ "  " +learningProgress.getUser_uuid()+" "+learningProgress.getCourse_uuid()+" "+learningProgress.getCurrent_page() +" "+learningProgress.getTotal_pages());
        }
        //androidViewModel.deleteAll();
    }

    private void testReadLocalDb2(){
        androidViewModel2 = new ViewModelProvider(this).get(LearningProgressTrackerAndroidViewModel.class);
        List<LearningProgressTracker> learningProgressTrackerList = androidViewModel2.getAllLearningProgressTrackerSync();
        for(LearningProgressTracker learningProgressTracker:learningProgressTrackerList){
            System.out.println("XXXXXXXXXXXXXXXXXXXXXX "+learningProgressTracker.getCourse_uuid()+ "  " +learningProgressTracker.getHours_of_learning()+" gil "+learningProgressTracker.getDate_created());
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount){
        LocalSharedPreferences prefs = new LocalSharedPreferences(ViewOnlinePdfActivity.this);
        LearningProgressController controller = new LearningProgressController(new ViewModelProvider(this));
        //-do something when page changes
        Toast.makeText(ViewOnlinePdfActivity.this,"Page "+page+" of "+pageCount,Toast.LENGTH_SHORT).show();
        currentPage = page;

        if(page==0) {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    saveStartData(pageCount,page,prefs,controller);
                }
            });
        }
        if(page > 0){
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    saveTrackingData(pageCount,page,prefs,controller);
                    getLearningHoursProgress();
                }
            });
        }
        if((page+1) == pageCount){
            markCourseCompleted(page,prefs,controller);
        }
    }

    private void getLearningHoursProgress(){
        String start_time = "";
        String end_time = "";
        String course_uuid = "";
        LocalSharedPreferences prefs = new LocalSharedPreferences(ViewOnlinePdfActivity.this);
        LearningProgressController controller = new LearningProgressController(new ViewModelProvider(this));
        List<LearningProgress> mlist = controller.getRecords();

        if(controller.getCNT(LocalData.GetUserUUID(prefs)) > 0){
            for (LearningProgress learningProgress : mlist) {
                start_time = learningProgress.getStart_time();
                end_time = learningProgress.getEnd_time();
                course_uuid = learningProgress.getCourse_uuid();
            }
            System.out.println("XXXXXX start date " + start_time);
            System.out.println("XXXXXX end date " + end_time);
            System.out.println("XXXXXX course " + course_uuid);
            if(start_time != null && end_time != null){
                hoursDifference = DateUtils.calHoursDifference(start_time,end_time);
            }else{
                hoursDifference = 0.00;
            }
        }else{
            hoursDifference = 0.00;
        }

        //String hoursOfLearning = new String();
        if(hoursDifference > 0.00) {
            if(DateUtils.formatToDayOfWeek(end_time) < DateUtils.getDayOfWeek()) {
                controller.storeTrackedLearningHours(course_uuid,String.valueOf(hoursDifference));
            }else{
                //hoursOfLearning = sumTotalLearningHours();

                if(controller.getTrackerCount() == 0){
                    controller.storeTrackedLearningHours(course_uuid,"0");
                }
                trackLearningProgressTrackerLastRecord(course_uuid,controller);
            }
        }else{
            //hoursDifference = 0;
        }
    }

    private void trackLearningProgressTrackerLastRecord(String course_uuid,LearningProgressController controller){

        List<LearningProgressTracker> mLastRecordList = controller.storeGetLastRecord();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double daysDifferenceInHours = 0.00;
        String currentDate = new String();
        String previousDate = new String();
        String hoursOfLearning = new String();
        int record_id = 0;
        for (LearningProgressTracker learningProgressTracker : mLastRecordList) {
            record_id = (learningProgressTracker.getTracker_id());
            hoursOfLearning = learningProgressTracker.getHours_of_learning();
            currentDate = String.valueOf(DateFormat.format("yyyy-MM-dd HH:mm:ss",new Date()));
            previousDate = learningProgressTracker.getDate_created();
            daysDifferenceInHours = DateUtils.calHoursDifference(currentDate,previousDate);
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX   record id"  +record_id);
        }
        int dayDifference = (int)(Math.floor(daysDifferenceInHours/24));
        if(dayDifference > 0){
            controller.storeTrackedLearningHours(course_uuid,"0");
        }else{
            if(hoursOfLearning == null){
                hoursOfLearning = "0";
            }
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXx james bond "+hoursDifference);
            controller.updateHoursOfLearning(course_uuid,record_id,hoursOfLearning,String.valueOf(hoursDifference));
        }
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx --> course " + course_uuid + "  hours of learning  " + hoursOfLearning + "  diff " + hoursDifference);
    }

    private String sumTotalLearningHours(){
        LearningProgressController controller = new LearningProgressController(new ViewModelProvider(this));
        String total = controller.getTotalHoursOfLearning();
        return total;
    }

    private void displayPage(int pageNumber) {
        binding.pdfView.jumpTo(pageNumber,true);
    }

    public void downloadPdfMaterial(String url,String pdf_name){
        if(NetworkUtils.getInstance(App.getContext()).isOnline()) {
            try {
                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle("GrowAgric learning");
                request.setDescription("downloading...");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,pdf_name);
                downloadId = downloadManager.enqueue(request);
                registerReceiver(downloadBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private final BroadcastReceiver downloadBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            long completedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if(completedDownloadId == downloadId){
                Toast.makeText(ViewOnlinePdfActivity.this,"Download complete",Toast.LENGTH_SHORT).show();

                String sourceFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + file_name;
                System.out.println("XXXXXXXX src "+sourceFilePath);
                String destinationDirPath = getFilesDir() + File.separator + "GrowAgricLearn";
                System.out.println("XXXXXXXX dest "+destinationDirPath);

                if(FileCopyUtil.copyFileToAppDirectory(sourceFilePath,destinationDirPath)){
                    System.out.println("XXXXXXXX file copied successfully "+course_uuid);
                    courseController.updateLearningCoursePath(sourceFilePath,course_uuid);
                }else{
                    System.out.println("XXXXXXXX error failed to copy file");
                }
            }
        }
    };

    private void requestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                System.out.println("permission granted.");
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }else{
            System.out.println("permission granted");
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        resetStartData();
        binding.floatingBtnLayout.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(downloadBroadcastReceiver);
        }catch (Exception e){

        }
    }
    @Override
    protected void onStop(){
        try{
            unregisterReceiver(downloadBroadcastReceiver);
        }catch (Exception e){
            // already unregistered
        }
        super.onStop();
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