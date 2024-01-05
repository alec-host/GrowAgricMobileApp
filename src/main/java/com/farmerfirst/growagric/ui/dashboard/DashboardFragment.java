package com.farmerfirst.growagric.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.MainActivity;
import com.farmerfirst.growagric.R;
//import com.farmerfirst.growagric.generated.callback.OnClickListener;

import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.FragmentDashboardBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.Splash;
import com.farmerfirst.growagric.ui.account.AccountFragment;
import com.farmerfirst.growagric.ui.farm.FarmActivity;
import com.farmerfirst.growagric.ui.farm.FarmListViewActivity;
import com.farmerfirst.growagric.ui.learning.LearnModuleActivity;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmAndroidViewModel;
import com.farmerfirst.growagric.ui.finance.FinanceListViewActivity;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.profile.ProfileActivity;
import com.farmerfirst.growagric.ui.profile.view_model.ProfileStatusViewModel;
import com.farmerfirst.growagric.ui.record_keeping.RecordKeepingDashboardActivity;
import com.farmerfirst.growagric.ui.register.RegisterActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.Utils;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Glide.with(getActivity()).load(R.mipmap.home_background).transition(DrawableTransitionOptions.withCrossFade()).into(binding.backgroundImageView);

        setUpUserFirstName();
        setUpMainDashboard();

        //-.create content dir.
        Utils.createLearningMaterialFolder(getActivity());

        return root;
    }

    private void setUpUserFirstName(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(App.getContext());
        if(!prefs.getProfileInfo().equalsIgnoreCase("")) {
            String name = LocalData.GetUserFirstName(prefs);
            binding.tvUserFirstName.setText(name);
        }else{
            binding.tvUserFirstName.setText(null);
        }
    }

    private void setUpMainDashboard(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(App.getContext());
        int profileUpdateStatus = prefs.getTrackProfileInfoUpdate();
        binding.addFarmCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(profileUpdateStatus == 0){
                    checkProfileCompletion();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), FarmListViewActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                        }
                    }).start();
                }
            }
        });

        binding.financeRequestCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FarmAndroidViewModel farmAndroidViewModel = new ViewModelProvider(getActivity()).get(FarmAndroidViewModel.class);
                if(farmAndroidViewModel.getFarmCNT(LocalData.GetUserUUID(prefs)) > 0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(),FinanceListViewActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                        }
                    }).start();
                }else{
                    String message = "Please add a farm";
                    ComponentUtils.showSnackBar(v.getRootView(),message,0,R.color.colorRed);
                }
            }
        });
        binding.addFarmRecordCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(),RecordKeepingDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                }).start();
            }
        });
        binding.trainingCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(),LearnModuleActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                }).start();
            }
        });
    }

    /*
    private void gb(){
        LiveData<String> data = dashboardViewModel.getData();
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXx "+data.getValue());
    }

     */

    private void checkProfileCompletion(){
        customBottomDialog();
    }

    private void customBottomDialog() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.bottom_sheet_dialog, null);
        new BottomDialog.Builder(getContext())
                .setTitle("PROFILE")
                .setCustomView(customView)
                .setNegativeText("CANCEL")
                .setPositiveText("ADD PROFILE")
                .setPositiveBackgroundColor(ContextCompat.getColor(getContext(), R.color.custom_app_theme_color))
                .setNegativeTextColor(ContextCompat.getColor(getContext(), R.color.custom_app_theme_color))
                .setPositiveTextColor(ContextCompat.getColor(getContext(), R.color.white))
                .setCancelable(false)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog dialog) {
                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        //switchBottomNavMenuSelection();
                    }
                }).show();
    }

    public void switchBottomNavMenuSelection(){
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
        navController.navigate(R.id.navigation_account);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}