package com.example.electrices.model;

import com.example.electrices.utilities.FireStoreConnection;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsDocument extends FirestoreDocument {

    // Note! to save a document directly from firestore as an instance of this class, the names of
    // the class variables must be the same as in the firestore document.
    private String date;
    private String day;
    private HashMap<String, Float> prices_percent_distribution;
    private ArrayList<Map<String, Object>> timeframes;

    public StatisticsDocument() {
        // !! Don't Remove !!
        // Empty constructor used for the firestore document.toObject() method.
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getDay() {
        return day;
    }

    public HashMap<String, Float> getPrices_percent_distribution() {
        return prices_percent_distribution;
    }

    public ArrayList<Map<String, Object>> getTimeframes() {
        return timeframes;
    }
}
