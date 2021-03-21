package com.example.electrices;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class RadarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);

        RadarChart radarChart = findViewById(R.id.radarChart);

        ArrayList<RadarEntry> visitorsFirst = new ArrayList<>();
        visitorsFirst.add(new RadarEntry(398));
        visitorsFirst.add(new RadarEntry(428));
        visitorsFirst.add(new RadarEntry(450));
        visitorsFirst.add(new RadarEntry(612));
        visitorsFirst.add(new RadarEntry(520));
        visitorsFirst.add(new RadarEntry(583));

        RadarDataSet radarDataSetFirst = new RadarDataSet(visitorsFirst, "Radar 1");
        radarDataSetFirst.setColor(Color.RED);
        radarDataSetFirst.setLineWidth(2f);
        radarDataSetFirst.setValueTextColor(Color.RED);
        radarDataSetFirst.setValueTextSize(14f);

        ArrayList<RadarEntry> visitorsSecond = new ArrayList<>();
        visitorsSecond.add(new RadarEntry(350));
        visitorsSecond.add(new RadarEntry(435));
        visitorsSecond.add(new RadarEntry(428));
        visitorsSecond.add(new RadarEntry(614));
        visitorsSecond.add(new RadarEntry(580));
        visitorsSecond.add(new RadarEntry(550));

        RadarDataSet radarDataSetSecond = new RadarDataSet(visitorsSecond, "Radar 2");
        radarDataSetSecond.setColor(Color.BLUE);
        radarDataSetSecond.setLineWidth(2f);
        radarDataSetSecond.setValueTextColor(Color.BLUE);
        radarDataSetSecond.setValueTextSize(14f);

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSetFirst);
        radarData.addDataSet(radarDataSetSecond);

        String[] labels = {"2016", "2017", "2018", "2019", "2020", "2021"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.getDescription().setText("Radar Chart Example");
        radarChart.setData(radarData);
    }
}