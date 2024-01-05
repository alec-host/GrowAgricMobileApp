package com.farmerfirst.growagric.ui.learning.pdf;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.farmerfirst.growagric.R;

import java.util.ArrayList;
import java.lang.Override;

public class PDFAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PDF> pdfList;

    public PDFAdapter(Context context, ArrayList<PDF> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @Override
    public int getCount(){
        return pdfList.size();
    }

    @Override
    public Object getItem(int position){
        return pdfList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String word;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pdf_model, viewGroup,false);
        }

        final PDF pdf = (PDF) this.getItem(i);

        TextView txtPdf = view.findViewById(R.id.txtPdf);
        ImageView imgPdf = view.findViewById(R.id.imgPdf);

        if(pdf.getName() != null) {
            //-.length of the string.
            if (pdf.getName().length() <= 10) {
                word = pdf.getName();
            } else {
                word = pdf.getName().substring(0, 10) + "...";
            }
            //-.bind data.
            txtPdf.setText(word);
            imgPdf.setImageResource(R.drawable.ic_course_cover);
            //-.view item click.
            view.setOnClickListener(v -> {
                //customSharedPreferences = new CustomSharedPreferences(App.getContext());
                openPDFView(pdf.getUrl());
                //if (!customSharedPreferences.getPdfName().equals("")) {
                //    customSharedPreferences.deletePdfName();
                //}
                //customSharedPreferences.setPdfName(pdf.getName());
            });
        }
        return view;
    }
    //-..
    private void openPDFView(String path){
        //Intent intent = new Intent(context, PDFActivity.class);
        //intent.putExtra("PATH",path);
        //context.startActivity(intent);
    }
}