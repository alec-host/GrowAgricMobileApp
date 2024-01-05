package com.farmerfirst.growagric.utils.dojo;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String>{
    public CustomAdapter(Context context,List<String> data){
        super(context, android.R.layout.simple_spinner_item,data);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
