package com.farmerfirst.growagric.ui.learning.chart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityLearningChartBinding;
import com.farmerfirst.growagric.ui.learning.LearnModuleActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class LearningChartActivity extends AppCompatActivity {
    private ActivityLearningChartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(LearningChartActivity.this,R.layout.activity_learning_chart);

        ComponentUtils.customActionBar("Learning activity", LearningChartActivity.this);

        setupChart();

    }

    private void setupChart(){
        LineChart chart = binding.lineChart;

        // Sample data: days in milliseconds and hours spent on each day
        //long[] daysMillis = {1629244800000L, 1629331200000L, 1629417600000L, 1629504000000L};
        //long[] daysMillis = {1695234768000L,1695321168000L,1695407568000L,1695493968000L,1695580368000L,1695666768000L,1695839568000L};
        long[] daysMillis = {1695234768000L,1695839568000L};
        float[] hoursSpent = {3.37f,0f,0f,0f,0f,0f,88.43f};

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < daysMillis.length; i++) {
            entries.add(new Entry(daysMillis[i], hoursSpent[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Hours");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        dataSet.setValueTextSize(12f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);

        chart.setData(lineData);
        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();

        xAxis.setValueFormatter(new DateAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(4); // Adjust the number of labels as needed

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setLabelCount(5); // Adjust the number of labels as needed

        chart.invalidate(); // Refresh the chart
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

    private class DateAxisValueFormatter extends ValueFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return dateFormat.format(new Date((long) value));
        }
    }

}