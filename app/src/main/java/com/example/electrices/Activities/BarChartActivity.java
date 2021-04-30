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
import com.example.electrices.model.ElectricityPricesDocument;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BarChartActivity extends AppCompatActivity {

    private static final String TAG = "BARCHART";

    // Array used to provide our own labels in the X axis.
    static final String[] timeOfDayLabels = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00",
            "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private FireStoreConnection fireStoreConnection;
    private FirebaseFirestore ffdatabase;
    public ElectricityPricesDocument electricityPricesDocument;
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

        initDatePickerDialog();
        dateButtonSetup();

//        Log.i(TAG, "onCreate => " + getTodaysDateForFirestoreQuery());

        fireStoreConnection = new FireStoreConnection();
        fireStoreConnection.getDocumentForDate(getTodaysDateForFirestoreQuery());

        // When the above method finishes the below listener will fire up.
        // This means that the barchart will be constructed and show up when the data has been downloaded.
        fireStoreConnection.setDocumentListener(new FireStoreConnection.DocumentListener(){
            @Override
            public void onDocumentReady(ElectricityPricesDocument document) {
                electricityPricesDocument = document;
                Log.i(TAG, "onDocumentReady => " + String.valueOf(document));

                barEntries_prices = barEntriesSetup();

                barDataSet = new BarDataSet(barEntries_prices, "Øre / kWh");
                barDataSetConfig(barDataSet);

                barData = new BarData(barDataSet);
                barChart.setData(barData);
//                barChart.clear();
                barChartConfig(barChart);
            }
        });
    }
    /* On Create END */

    private ArrayList<BarEntry> barEntriesSetup(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        HashMap<String,Float> pricesPerHour = electricityPricesDocument.getPricesPerHour();
//        Log.i(TAG, String.valueOf(electricityPricesDocument.getDate()));
//        Log.i(TAG, String.valueOf(pricesPerHour));
        int noOfBarEntry = 0;
        for (String timeOfDay : timeOfDayLabels){
            barEntries.add(new BarEntry(noOfBarEntry, pricesPerHour.get(timeOfDay)));
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
                month = month +1;
                String date = makeDateString(year, month, monthDay);
                dateButton.setText(date);
                String firestoreDateQuery = makeDateStringForFirestoreQuery(monthDay, month, year);

                //##############################################
//                Log.i(TAG, "onDataSet => " + firestoreDateQuery);
                barEntries_prices.clear();
                barChart.clear();
                fireStoreConnection.getDocumentForDate(firestoreDateQuery);
            }
        };


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int monthDay = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style , dateSetListener, year, month, monthDay);
        // Styling the DatePicker
        DatePickerStyler.colorizeDatePicker(datePickerDialog.getDatePicker());
        datePickerDialog.getDatePicker().setMinDate(getDateInMillis( new int[]{2021, 2, 14})); // 2021 March 10
//        datePickerDialog.getDatePicker().setMinDate(getDateInMillis(getTodaysDateWithOffset(0,0,-3)));
        datePickerDialog.getDatePicker().setMaxDate(getDateInMillis(getTodaysDateWithOffset(0,0,1)));

    }

    private String makeDateString(int year, int month, int monthDay) {
        return getWeekDayFromDate(year, month, monthDay) + ",  " + monthDay + " " + getMonthFormat(month) + ", " + year;
    }

    private String getWeekDayFromDate(int year, int month, int monthDay){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, monthDay);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return getWeekDayFormat(dayOfWeek);
    }

    private String makeDateStringForFirestoreQuery(int day, int month, int year){
        String monthString = String.valueOf(month);
        String dayString = String.valueOf(day);
        if(day < 10 && month >= 10){ // Only day is below ten
            dayString = String.format("0%s", dayString);
        }
        if(month < 10 && day >= 10){ // Only month is below ten
            monthString = String.format("0%s", monthString);
        }
        if(day < 10 && month < 10){ // Both day and month are below ten
            dayString = String.format("0%s", dayString);
            monthString = String.format("0%s", monthString);
        }
        return String.format(Locale.ENGLISH,"%d-%s-%s", year, monthString, dayString);
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        // Default should never happen
        return "NaN";
    }

    private String getWeekDayFormat(int dayOfWeek){
        if(dayOfWeek == 1)
            return "Sunday";
        if(dayOfWeek == 2)
            return "Monday";
        if(dayOfWeek == 3)
            return "Tuesday";
        if(dayOfWeek == 4)
            return "Wednesday";
        if(dayOfWeek == 5)
            return "Thursday";
        if(dayOfWeek == 6)
            return "Friday";
        if (dayOfWeek == 7)
            return "Saturday";

        // Default should never happen
        return "NaN";
    }

    private void dateButtonSetup(){
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        dateButton.setText(getTodaysDate());
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int monthDay = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(year, month, monthDay);
    }

    private String getTodaysDateForFirestoreQuery(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateStringForFirestoreQuery(day, month, year);
    }

    private int[] getTodaysDateWithOffset(int yearsOffset, int monthsOffset, int daysOffset){
        Calendar cal = Calendar.getInstance();
        int[] dateArray = new int[3];
        int year = cal.get(Calendar.YEAR) + yearsOffset;
        int month = cal.get(Calendar.MONTH) + monthsOffset;
        int day = cal.get(Calendar.DAY_OF_MONTH) + daysOffset;
        dateArray[0] = year;
        dateArray[1] = month;
        dateArray[2] = day;
        return dateArray;
    }

    private long getDateInMillis(int[] date){
        Calendar cal = Calendar.getInstance();
        cal.set(date[0], date[1], date[2]);
        return cal.getTimeInMillis();
    }
}