package com.example.electrices.model.firestoreModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * This object is structured in a way so we can get a document from firestore and store it directly as a java object.
 * It holds the electricity prices of a SPECIFIC date.
 * Tutorial: https://www.youtube.com/watch?v=jJnm3YKfAUI&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=8
 **/
public class PricesDocument extends FirestoreDocument{

    /*
    Model for use in the ApplianceTimeSelectionAdapter.
    Because the pricesPerHour HashMap of the PricesDocument contains only the hours (as keys) and
    the prices (as values), the below created inner class helps to link these hours and prices with
    the price level (low, medium, high). This way a modeled view can be achieved for use inside
    any recycler view adapter.
    */
    public static class PriceLevelHour {
        private String time;
        private String price_level;
        private Double price;

        private boolean checked = false;

        public PriceLevelHour(){}

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPrice_level() {
            return price_level;
        }

        public void setPrice_level(String price_level) {
            if(price_level.equals("low_price_level"))
                this.price_level = "low";
            if(price_level.equals("medium_price_level"))
                this.price_level = "medium";
            if(price_level.equals("high_price_level"))
                this.price_level = "high";
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public boolean isChecked(){
            return checked;
        }
        public void setChecked(boolean checked){
            this.checked = checked;
        }
    }


    public static final String TAG = "PRICES_DOCUMENT";

    // Note! to save a document directly from firestore as an instance of this class, the names of
    // the class variables must be the same as in the firestore document.
    private String date;
    private String day;
    private String data_type;
    private HashMap<String, Double> pricesPerHour;

    private ArrayList<PriceLevelHour> priceLevelHourObjects;


    public PricesDocument(){
        // !! Don't Remove !!
        // Empty constructor used for the firestore document.toObject() method.
    }

    public void constructPriceLevelHourObjects(HashMap<String, HashMap<String, Double>> priceLevels_min_max){
        priceLevelHourObjects = new ArrayList<>();

        Set<String> priceLevels = priceLevels_min_max.keySet();

        // Converting Set to List so it can be sorted.
        ArrayList<String> hours_sorted = new ArrayList<>(pricesPerHour.keySet());
        Collections.sort(hours_sorted);

        // Getting each hourly price and creating a new PriceLevelHour object with
        // a corresponding price level.
        for(String hour : hours_sorted){
            PriceLevelHour priceLevelHour = new PriceLevelHour();
            Double priceValue = pricesPerHour.get(hour);
            for(String priceLevel : priceLevels){
                Double minPrice = priceLevels_min_max.get(priceLevel).get("min");
                Double maxPrice = priceLevels_min_max.get(priceLevel).get("max");
                if(( priceValue >= minPrice) && (priceValue <= maxPrice)){
                    priceLevelHour.setPrice_level(priceLevel);
                    priceLevelHour.setTime(hour);
                    priceLevelHour.setPrice(pricesPerHour.get(hour));
                    priceLevelHourObjects.add(priceLevelHour);
                }
            }
        }

//        for(PriceLevelHour priceLevelHour : priceLevelHours){
//        Log.i(TAG, "Hour: " + String.valueOf(priceLevelHour.getTime()) +
//                ", Level : " + String.valueOf(priceLevelHour.getPrice_level()) +
//                ", Price: " + String.valueOf(priceLevelHour.getPrice()));
//        }
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getDay() {
        return day;
    }


    public String getData_type() {
        return data_type;
    }

    public HashMap<String, Double> getPricesPerHour() {
        return pricesPerHour;
    }

    public ArrayList<PriceLevelHour> getPriceLevelHourObjects(){
        return priceLevelHourObjects;
    }
}
