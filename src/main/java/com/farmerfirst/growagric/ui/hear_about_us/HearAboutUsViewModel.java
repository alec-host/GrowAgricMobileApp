package com.farmerfirst.growagric.ui.hear_about_us;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.farmerfirst.growagric.ui.finance.AddFinance;

public class HearAboutUsViewModel extends ViewModel {

    public MutableLiveData<String> MediaSource = new MutableLiveData<>();

    public MutableLiveData<HearAboutUs> HearAboutUsMutableLiveData;

    public MutableLiveData<HearAboutUs> getMediaSource() {
        if(HearAboutUsMutableLiveData == null) {
            HearAboutUsMutableLiveData = new MutableLiveData<>();
        }
        return HearAboutUsMutableLiveData;
    }

    public void onClick(View view) {
        HearAboutUs hearAboutUs = new HearAboutUs(MediaSource.getValue());
        HearAboutUsMutableLiveData.setValue(hearAboutUs);
    }
}
