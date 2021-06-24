package com.example.electrices.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electrices.R;
import com.example.electrices.model.ApplianceSchedule;
import com.example.electrices.model.firestoreModel.FirestoreDocument;
import com.example.electrices.model.firestoreModel.ScheduleDocument;
import com.example.electrices.model.firestoreModel.StatisticsDocument;
import com.example.electrices.model.recyclerViewComponents.ScheduledAppliancesAdapter;
import com.example.electrices.utilities.CustomDateUtility;
import com.example.electrices.utilities.FireStoreConnection;
import com.example.electrices.model.recyclerViewComponents.TimeframesAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    final String TAG = "HOME_ACTIVITY";
    final String PRICE_LEVEL_ALL = "all";
    final String PRICE_LEVEL_LOW = "low";
    final String PRICE_LEVEL_MEDIUM = "medium";
    final String PRICE_LEVEL_HIGH = "high";

    private FireStoreConnection fireStoreConnection;
    private StatisticsDocument statisticsDocument;

    // Our Views
    private TextView statsTableDate;
    private PieChart pieChart;
    private TextView pieChartLabelPercentLow;
    private TextView pieChartLabelPercentMedium;
    private TextView pieChartLabelPercentHigh;
    private RecyclerView rvTimeframes, rvScheduledAppliances;
    private TimeframesAdapter timeframesAdapter;

//    private ArrayList<Map<String, Object>> statsDocumentTimeframesList;
    private ArrayList<StatisticsDocument.Timeframe> statsDocumentTimeframesList;
    private HashMap<String, Float> statsDocumentPriceLevelDestributionMap;
//    private ArrayList<Map<String, Object>> adapterTimeframesList;
    private ArrayList<StatisticsDocument.Timeframe> adapterTimeframesList;
    private CountDownTimer popupDismissTimer;
    private CustomDateUtility customDateUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.button_prices_barChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BarChartActivity.class));
            }
        });

        findViewById(R.id.button_appliances).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DevicesActivity.class));
            }
        });

        customDateUtility = new CustomDateUtility();
        fireStoreConnection = FireStoreConnection.getInstance();

        MaterialButton button_drop_down = findViewById(R.id.button_dropdown_price_levels);
        dropDownSetup(button_drop_down);

        statsTableDate = findViewById(R.id.statistics_table_date);
        statsTableDate.setText(customDateUtility.getTodaysDate());

        pieChart = findViewById(R.id.pie_chart_statistics);
        pieChartLabelPercentLow = findViewById(R.id.label_percent_low);
        pieChartLabelPercentMedium = findViewById(R.id.label_percent_medium);
        pieChartLabelPercentHigh = findViewById(R.id.label_percent_high);
        pieChartSetup(pieChart);

        rvTimeframes = findViewById(R.id.recycler_view_statistics_home_activity);
        rvTimeframes.setLayoutManager(new LinearLayoutManager(this));

        rvScheduledAppliances = findViewById(R.id.recycler_view_scheduled_appliances);
        rvScheduledAppliances.setLayoutManager(new LinearLayoutManager(this));

//        new FireStoreConnection();
//        FireStoreConnection ffStaticInstance = FireStoreConnection.getInstance();

        // When the above method gets the document the below listener will fire up.
        fireStoreConnection.setDocumentListener(new FireStoreConnection.DocumentListener(){
            @Override
            public <D> void onDocumentReady(D document) {
                // here check if document is of type statistics document.
                // In barchart activity check id the returning document id of simple prices document.
                if(document instanceof StatisticsDocument){
                    statisticsDocument = (StatisticsDocument) document;
                    statsDocumentTimeframesList = statisticsDocument.getTimeframes();
                    statsDocumentPriceLevelDestributionMap = statisticsDocument.getPrices_percent_distribution();

                    HashMap<String, Float> pieChartData = statsDocumentPriceLevelDestributionMap;
                    adapterTimeframesList = new ArrayList<>();
//                    Log.i(TAG, String.valueOf(statsDocumentTimeframesList));
                    adapterTimeframesList.addAll(statsDocumentTimeframesList);

                    setPiechartData(pieChart, pieChartData);
                    timeframesAdapter = new TimeframesAdapter(adapterTimeframesList);
                    rvTimeframes.setAdapter(timeframesAdapter);
                }
            }
        });
        fireStoreConnection.getStatisticsDocumentForDate(customDateUtility.getTodaysDateForFirestoreQuery());

       /*
       @TODO
       Database structure needs to be fixed first because creates a chaos in the code implementation.
       */
        fireStoreConnection.setDocumentListener(new FireStoreConnection.DocumentListener() {
            @Override
            public <D> void onDocumentReady(D document) {
                if (document instanceof ScheduleDocument) {
                    ScheduleDocument scheduleDocument = (ScheduleDocument) document;
                    Log.i(TAG, String.valueOf(scheduleDocument.getDaySchedule()));

                    ArrayList<ApplianceSchedule> applianceScheduleList = new ArrayList<>();

                    // Iterating through the schedule hashmap
                    for (Map.Entry mapItemTime : scheduleDocument.getDaySchedule().entrySet()) {
                        String scheduledTime = (String) mapItemTime.getKey();
                        HashMap<String, HashMap<String, Integer>> scheduledAppliances = (HashMap<String, HashMap<String, Integer>>) mapItemTime.getValue();
                        for (Map.Entry mapItemAppliance : scheduledAppliances.entrySet()) {
                            String applianceName = (String) mapItemAppliance.getKey();
                            HashMap<String, Integer> mode = (HashMap<String, Integer>) mapItemAppliance.getValue();

                            ApplianceSchedule applianceSchedule = new ApplianceSchedule(applianceName, scheduledTime, mode.get("Mode"));
                            applianceScheduleList.add(applianceSchedule);
//                            Log.i(TAG, applianceSchedule.getTimeScheduled() + " " + applianceSchedule.getApplianceName() + " " + applianceSchedule.getMode());
                        }
                    }

                    ScheduledAppliancesAdapter scheduledAppliancesAdapter = new ScheduledAppliancesAdapter(applianceScheduleList);
                    rvScheduledAppliances.setAdapter(scheduledAppliancesAdapter);
                }
            }
        });
        fireStoreConnection.getAppliancesScheduleDocumentForDate(customDateUtility.getTodaysDateForFirestoreQuery());
    }


    // Main method for the construction of the pie chart.
    private void pieChartSetup(PieChart pieChart){
        pieChartConfig(pieChart);
    }

    private void dataSetConfig(PieDataSet pieDataSet){
        pieDataSet.setDrawValues(false);
        pieDataSet.setColors(new int[] {R.color.low_prices, R.color.medium_prices, R.color.high_prices}, this.getApplicationContext());
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setSliceSpace(5f);
        pieDataSet.setSelectionShift(8f);
    }

    private  void pieChartConfig(PieChart pieChart){
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.animate();
        pieChart.setDescription(null);

        // enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(68);
        pieChart.setTransparentCircleRadius(75);

//        pieChart.setDrawRoundedSlices(true);
//        pieChart.setDrawSlicesUnderHole(true);
//        pieChart.setEntryLabelColor(R.color.letters_dark);
        pieChart.setUsePercentValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        chartLegendConfig(pieChart);
    }

    private void chartLegendConfig(PieChart chart){
        Legend legend = chart.getLegend();
        legend.setFormSize(16f); // set the size of the legend forms/shapes
        legend.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextColor(getResources().getColor(R.color.letters_dark));
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setYOffset(-22f);
        legend.setXEntrySpace(15f); // set the space between the legend entries on the x-axis
        legend.setYEntrySpace(10f); // set the space between the legend entries on the y-axis
        // and many more...
    }

    private void setPiechartData(PieChart pieChart, Map<String, Float> pieChartData){
        float percent_low = pieChartData.get("low_prices_percent");
        float percent_medium = pieChartData.get("medium_prices_percent");
        float percent_high = pieChartData.get("high_prices_percent");

        ArrayList<PieEntry> price_frequencies = new ArrayList<>();
        price_frequencies.add(new PieEntry(percent_low, "LOW"));
        price_frequencies.add(new PieEntry(percent_medium, "MEDIUM"));
        price_frequencies.add(new PieEntry(percent_high, "HIGH"));

        PieDataSet pieDataSet = new PieDataSet(price_frequencies, null);
        dataSetConfig(pieDataSet);

        PieData pieData = new PieData(pieDataSet);
        pieChart.clear();
        pieChart.setData(pieData);

        pieChartLabelPercentLow.setText(String.valueOf(percent_low + "%"));
        pieChartLabelPercentMedium.setText(String.valueOf(percent_medium + "%"));
        pieChartLabelPercentHigh.setText(String.valueOf(percent_high + "%"));
    }


    // Statistics Card drop down button
    private void dropDownSetup(final MaterialButton dropDownButton){

        // Inflating the view for the popup window.
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_price_levels, null);
        final PopupWindow popup_window_price_levels = new PopupWindow(view,280, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

        // Getting the clickable views of the popup window.
        final LinearLayout viewItemAllPrices = view.findViewById(R.id.all_prices);
        final LinearLayout viewItemLowPrices = view.findViewById(R.id.low_prices);
        final LinearLayout viewItemMediumPrices = view.findViewById(R.id.medium_prices);
        final LinearLayout viewItemHighPrices = view.findViewById(R.id.high_prices);

        // Setting on click listeners to the clickable views of the popup window.
        setOnClickListenerForPopupWindowItem(viewItemAllPrices, dropDownButton, popup_window_price_levels, "all");
        setOnClickListenerForPopupWindowItem(viewItemLowPrices, dropDownButton, popup_window_price_levels, "low");
        setOnClickListenerForPopupWindowItem(viewItemMediumPrices, dropDownButton, popup_window_price_levels, "medium");
        setOnClickListenerForPopupWindowItem(viewItemHighPrices, dropDownButton, popup_window_price_levels, "high");

        // Setting up the drop down button to show the popup window.
        dropDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAsDropDown(below which view you want to show as dropdown,horizontal position, vertical position)
                popup_window_price_levels.showAsDropDown(v,-40,0);
            }
        });
    }

    private void setOnClickListenerForPopupWindowItem(final LinearLayout item, final MaterialButton ddButton, final PopupWindow popupWindow, final String priceLevel){
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is the default for PRICE LEVEL ALL.
                int itemColor = R.color.all_prices;
                int itemText = R.string.all_prices_label;
                switch (priceLevel){
                    case PRICE_LEVEL_LOW:
                        itemColor = R.color.low_prices;
                        itemText = R.string.low_prices_label;
                        break;
                    case PRICE_LEVEL_MEDIUM:
                        itemColor = R.color.medium_prices;
                        itemText = R.string.medium_prices_label;
                        break;
                    case PRICE_LEVEL_HIGH:
                        itemColor = R.color.high_prices;
                        itemText = R.string.high_prices_label;
                        break;
                }
                ddButton.setBackgroundColor(getResources().getColor(itemColor));
                ddButton.setText(itemText);
                dismissPopupWindowOnTime(popupWindow, 150); // Dismissing popup after 150 milliseconds.
                setPriceLevelTimeframesToRecyclerView(priceLevel);
//                Toast.makeText(HomeActivity.this, "You Clicked : " + ((TextView) item.getChildAt(1)).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPriceLevelTimeframesToRecyclerView(String priceLevel){
        if(statisticsDocument != null){
//            Log.i(TAG, "Statistics Doc NOT NULL ");
//            ArrayList<HashMap<String, Object>> allPriceLevelTimeframes = statisticsDocument.getTimeframes();
//            Log.i(TAG, "ALL Pr. LVL Timeframes => " + String.valueOf(allPriceLevelTimeframes));
            ArrayList<StatisticsDocument.Timeframe> priceLevelTimeframes = new ArrayList<>();

            if(priceLevel.equals(PRICE_LEVEL_ALL)){
                priceLevelTimeframes.addAll(statsDocumentTimeframesList);
            }

            if(priceLevel.equals(PRICE_LEVEL_LOW)){
                for(StatisticsDocument.Timeframe timeframe : statsDocumentTimeframesList){
                    if(timeframe.getPrice_level().equals("low")) {
                        priceLevelTimeframes.add(timeframe);
                    }
                }
            }

            if(priceLevel.equals(PRICE_LEVEL_MEDIUM)){
                for(StatisticsDocument.Timeframe timeframe : statsDocumentTimeframesList){
                    if(timeframe.getPrice_level().equals("medium")) {
                        priceLevelTimeframes.add(timeframe);
                    }
                }
            }

            if(priceLevel.equals(PRICE_LEVEL_HIGH)){
                for(StatisticsDocument.Timeframe timeframe : statsDocumentTimeframesList){
                    if(timeframe.getPrice_level().equals("high")) {
                        priceLevelTimeframes.add(timeframe);
                    }
                }
            }

            // Clearing adapter's data-list and importing new.
            adapterTimeframesList.clear();
            adapterTimeframesList.addAll(priceLevelTimeframes);
            timeframesAdapter.notifyDataSetChanged();
//            Log.i(TAG, "Adapter Timeframes => " + String.valueOf(adapterTimeframesList));
//            Log.i(TAG, "Pr. LVL Timeframes => " + String.valueOf(priceLevelTimeframes));
//            Log.i(TAG, "ALL Pr. LVL Timeframes => " + String.valueOf(statsDocumentTimeframesList));
//            Log.i(TAG, "ALL Pr. LVL Timeframes => " + String.valueOf(statisticsDocument.getTimeframes()) + "\n");
        }
    }

    private void dismissPopupWindowOnTime(final PopupWindow popupWindow, long millisInFuture){
        popupDismissTimer = new CountDownTimer(millisInFuture, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                popupWindow.dismiss();
            }
        };
        popupDismissTimer.start();
    }

}
