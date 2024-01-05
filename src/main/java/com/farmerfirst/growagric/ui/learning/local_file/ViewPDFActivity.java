package com.farmerfirst.growagric.ui.learning.local_file;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Environment;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityViewPdfBinding;
import com.farmerfirst.growagric.ui.learning.pdf.PDF;
import com.farmerfirst.growagric.ui.learning.pdf.PDFAdapter;
import com.farmerfirst.growagric.utils.ComponentUtils;

import java.io.File;
import java.util.ArrayList;

public class ViewPDFActivity extends AppCompatActivity {
    private ActivityViewPdfBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_pdf);

        ComponentUtils.customActionBar("Learning materials", ViewPDFActivity.this);

        binding.gv.setAdapter(new PDFAdapter(ViewPDFActivity.this,getPDFs()));

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //-.routine to get pdf.
    private ArrayList<PDF> getPDFs() {
        ArrayList<PDF> pdfList = new ArrayList<>();
        //-.target folder.
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        PDF pdf;
        if (downloadsFolder.exists()) {
            //-.get all downloaded files.
            File[] files = downloadsFolder.listFiles();
            if (files != null) {
                //-.get name & url through looping.
                for (int k = 0; k < files.length; k++) {
                    File file = files[k];
                    if (file.getPath().endsWith("pdf")) {
                        pdf = new PDF();
                        pdf.setName(file.getName());
                        pdf.setUrl(file.getAbsolutePath());
                        pdfList.add(pdf);
                    }
                }
                return pdfList;
            }else{
                return pdfList;
            }
        }else{
            return pdfList;
        }
    }
}