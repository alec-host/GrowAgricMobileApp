package com.farmerfirst.growagric.ui.profile.view_model;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;

public class ProfileStatusData {
    private ProfileStatusViewModel viewModel;
    public ProfileStatusData(ProfileStatusViewModel viewModel){
        this.viewModel = viewModel;
    }

    public void fetchData(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(App.getContext());
        String profileUpdataStatus = "hello world "+prefs.getTrackProfileInfoUpdate();
        viewModel.updateData(profileUpdataStatus);
    }
}
