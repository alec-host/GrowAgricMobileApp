package com.farmerfirst.growagric.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.utils.dojo.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private List<Item> itemList;
    private Context mContext;
    public ItemArrayAdapter(@NonNull Context context, ArrayList<Item> list) {
        super(context,0, list);
        mContext = context;
        itemList = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(com.farmerfirst.growagric.R.layout.list_item,parent,false);

        Item currentItem = itemList.get(position);

        ImageView imageView = listItem.findViewById(R.id.img_poster);
        imageView.setImageResource(currentItem.getImage());

        TextView accountProfile = listItem.findViewById(R.id.row_item);
        accountProfile.setText(currentItem.getName());

        TextView valueProfile = listItem.findViewById(R.id.row_item2);
        valueProfile.setText(currentItem.getValue());

        // Return the completed view to render on screen
        return listItem;
    }
}

