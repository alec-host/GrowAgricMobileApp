package com.farmerfirst.growagric.ui.record_keeping.input.custom_dialog;

import android.app.AlertDialog;
import android.app.BroadcastOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.ui.record_keeping.RecordTypeActivity;
import com.farmerfirst.growagric.ui.record_keeping.input.BroodingActivity;
import com.farmerfirst.growagric.ui.record_keeping.input.ChickenActivity;
import com.farmerfirst.growagric.ui.record_keeping.input.FeedsActivity;
import com.farmerfirst.growagric.ui.record_keeping.input.LabourActivity;
import com.farmerfirst.growagric.ui.record_keeping.input.MedicinesActivity;
import com.farmerfirst.growagric.ui.record_keeping.sales.SalesActivity;
import com.farmerfirst.growagric.utils.dojo.Item;

import java.util.ArrayList;

public class CustomDialog extends DialogFragment {

    public static CustomDialog newInstance(String recordType){
        CustomDialog dialog = new CustomDialog();
        Bundle args = new Bundle();
        args.putString("myValue",recordType);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String inputType = getArguments().getString("myValue");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Html.fromHtml("<font color='#008436' size='10px'>Select option</font>"));

        ListView listView = setupListview(inputType);

        builder.setView(listView);

        return builder.create();
    }

    private ListView setupListview(String inputType){
        ListView listView = new ListView(getActivity());
        ArrayList<String> dataInputType = new ArrayList<>();
        if(inputType == "inputs") {
            dataInputType.add("");
            dataInputType.add("Chicken");
            dataInputType.add("Feeds");
            dataInputType.add("Labour");
            dataInputType.add("Medicine");
            dataInputType.add("Brooding");
            dataInputType.add("");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_activated_1,dataInputType);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (dataInputType.get(position).toLowerCase().toString()) {
                    case "chicken":
                        Intent i = new Intent(App.getContext(), ChickenActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                        break;
                    case "feeds":
                        Intent j = new Intent(App.getContext(), FeedsActivity.class);
                        j.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(j);
                        break;
                    case "labour":
                        Intent k = new Intent(App.getContext(), LabourActivity.class);
                        k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(k);
                        break;
                    case "medicine":
                        Intent b = new Intent(App.getContext(), MedicinesActivity.class);
                        b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(b);
                        break;
                    case "brooding":
                        Intent m = new Intent(App.getContext(), BroodingActivity.class);
                        m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(m);
                        break;
                }
                dismiss();
            }
        });

        return listView;

    }
}
