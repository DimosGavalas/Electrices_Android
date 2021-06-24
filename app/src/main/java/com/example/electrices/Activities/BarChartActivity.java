package com.example.electrices.Activities;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.electrices.MPchartsCustom.CustomMarkerView;
import com.example.electrices.R;
import com.example.electrices.model.firestoreModel.PricesDocument;
import com.example.electrices.utilities.CustomDateUtility;
import com.example.electrices.utilities.DatePickerStyler;
import com.example.electrices.utilities.FireStoreConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;

public class BarChartActivity extends AppCompatActivity {

    private static final String TAG = "BARCHART";

    // Array used to provide our own labels in the X axis.
    static final String[] timeOfDayLabels = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00",
            "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private FireStoreConnection fireStoreConnection;
    private CustomDateUtility customDateUtility;
    public PricesDocument pricesDocument;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    private ArrayList<BarEntry> barEntries_prices;
    private BarDataSet barDataSet;
    private BarData barData;
    private BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.barChart);
        dateButton = findViewById(R.id.datePickerButton);

        customDateUtility = new CustomDateUtility();
        initDatePickerDialog();
        dateButtonSetup();

        initFirestoreConnection();
        setFirestoreOnDocumentReadyListeners();
        // getting firestore data
        fireStoreConnection.getPricesDocumentForDate(customDateUtility.getTodaysDateForFirestoreQuery());
    }
    /* On Create END */

    private ArrayList<BarEntry> barEntriesSetup(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        HashMap<String,Double> pricesPerHour =  pricesDocument.getPricesPerHour();
//        Log.i(TAG, String.valueOf(pricesDocument.getDate()));
//        Log.i(TAG, String.valueOf(pricesDocument.getDay()));
//        Log.i(TAG, String.valueOf(pricesPerHour));
        int noOfBarEntry = 0;
        for (String timeOfDay : timeOfDayLabels){
            double priceValue = pricesPerHour.get(timeOfDay);
            barEntries.add(new BarEntry(noOfBarEntry, (float) priceValue));
            noOfBarEntry += 1;
        }
//        Log.i(TAG, String.valueOf(noOfBarEntry));
        return barEntries;
    }

    private void barDataSetConfig(BarDataSet barDataSet){
        //barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setColor(getResources().getColor(R.color.primary_light));
        barDataSet.setValueTextColor(Color.BLACK);
//        barDataSet.setValueTextSize(12f);
        barDataSet.setDrawValues(false);
//        barDataSet.setFormSize(5f); // size of the legend icon
    }

    private void barChartConfig(BarChart barChart){
        barChartAxesConfig(barChart);
        barChartMarkerConfig(barChart);
        barChart.setFitBars(true);
//        barChart.getDescription().setPosition(200f, 150f);
        barChart.getDescription().setYOffset(-30);
        barChart.getDescription().setText("Hourly Prices");
        barChart.animateY(1000);
    }

    private void barChartAxesConfig(BarChart barChart){
        // Y axis styling
        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();

        // Removing the Y axis lines
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);

        // Removing the left Y axis labels
        leftAxis.setDrawLabels(false);

        // Changing the color of the Y axis grid lines
        leftAxis.setGridColor(getResources().getColor(R.color.gray_50));
        rightAxis.setGridColor(getResources().getColor(R.color.gray_50));

        leftAxis.setGridLineWidth(0.7f);
        rightAxis.setGridLineWidth(0.7f);

        rightAxis.setTextColor(getResources().getColor(R.color.gray_200));


        // X axis styling
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(2f); // Show labels for every second bar. (Used also for avoiding duplicate or off-positioned labels)
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(timeOfDayLabels)); // Set the given string labels to the X axis. The number of labels must me equal to the number of barentries given to the barchart.
        xAxis.setLabelCount(24);
        xAxis.setTextColor(getResources().getColor(R.color.gray_200));
    }

    private void barChartMarkerConfig(BarChart barChart){
        IMarker marker = new CustomMarkerView(this.getApplicationContext(), R.layout.marker_bar_chart);
        barChart.setMarker(marker);
    }

    private void initDatePickerDialog(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int monthDay) {
                month = month + 1; // The date picker returns always months that start from zero.
                String date = customDateUtility.makeDateString(year, month, monthDay);
                dateButton.setText(date);
                String firestoreDateQuery = customDateUtility.makeDateStringForFirestoreQuery(year, month, monthDay);

                //##############################################
                Log.i(TAG, "onDataSet => " + firestoreDateQuery);
                barEntries_prices.clear();
                barChart.clear();
                fireStoreConnection.getPricesDocumentForDate(firestoreDateQuery);
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;
        int year = customDateUtility.getYear();
        int month = customDateUtility.getMonth();
        Log.i(TAG , "Month => " + month);
        int monthDay = customDateUtility.getMonthDay();

        datePickerDialog = new DatePickerDialog(this, style , dateSetListener, year, month, monthDay);
        // Styling the DatePicker
        DatePickerStyler.colorizeDatePicker(datePickerDialog.getDatePicker());
        datePickerDialog.getDatePicker().setMinDate(customDateUtility.getDateInMillis( new int[]{2021, 2, 14})); // 2021 March 10
        // Max date of the date picker is always the next day from today.
        datePickerDialog.getDatePicker().setMaxDate(customDateUtility.getDateInMillis(customDateUtility.getTodaysDateWithOffset(0,0,1)));
    }

    private void dateButtonSetup(){
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        dateButton.setText(customDateUtility.getTodaysDate());
    }

    private void initFirestoreConnection(){
        fireStoreConnection = FireStoreConnection.getInstance();
    }

    private void setFirestoreOnDocumentReadyListeners(){
        // When the above method finishes the below listener will fire up.
        // This means that the barchart will be constructed and show up when the data has been downloaded.
        fireStoreConnection.setDocumentListener(new FireStoreConnection.DocumentListener(){
            @Override
            public <D> void onDocumentReady(D document) {
                pricesDocument = (PricesDocument) document;
                Log.i(TAG, "onDocumentReady => " + String.valueOf(document));

                barEntries_prices = barEntriesSetup();

                barDataSet = new BarDataSet(barEntries_prices, "Øre / kWh");
                barDataSetConfig(barDataSet);

                barData = new BarData(barDataSet);
                barChart.setData(barData);
                barChartConfig(barChart);
            }
        });
    }
}