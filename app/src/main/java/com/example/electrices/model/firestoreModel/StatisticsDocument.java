package com.example.electrices.model.firestoreModel;


import java.util.ArrayList;
import java.util.HashMap;


public class StatisticsDocument extends FirestoreDocument {

    // Inner classes must be static to be used with firestore .toObject method.
    public static class Timeframe {

        private String timeframe;
        private String price_level;
        private Double average;

        public Timeframe(){
        }

        public void setTimeframe(String timeframe){
            this.timeframe = timeframe;
        }

        public String getTimeframe() {
            return timeframe;
        }

        public void setPrice_level(String price_level){
            this.price_level = price_level;
        }

        public String getPrice_level() {
            return price_level;
        }

        public void setAverage(Double average){
            this.average = average;
        }

        public Double getAverage() {
            return average;
        }
    }

    // Note! to save a document directly from firestore as an instance of this class, the names of
    // the class variables must be the same as in the firestore document.
    private String date;
    private String day;
    private HashMap<String, Float> prices_percent_distribution;
    private ArrayList<Timeframe> timeframes;
    private HashMap<String, HashMap<String, Double>> price_levels_min_max;


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

    public ArrayList<Timeframe> getTimeframes(){
        return timeframes;
    }

    public HashMap<String, HashMap<String, Double>> getPrice_levels_min_max(){
        return price_levels_min_max;
    }
}
