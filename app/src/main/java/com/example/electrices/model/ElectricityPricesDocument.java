package com.example.electrices.model;

import java.util.HashMap;

/**
 * This object is structured in a way so we can get a document from firestore and store it directly as a java object.
 * It holds the electricity prices of a SPECIFIC date.
 * Tutorial: https://www.youtube.com/watch?v=jJnm3YKfAUI&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=8
 **/
public class ElectricityPricesDocument {

    // Note! to save a document directly from firestore as an instance of this class, the names of
    // the class variables must be the same as in the firestore document.
    private String date;
    private String data_type;
    private HashMap<String, Float> pricesPerHour;


    public ElectricityPricesDocument(){
        // !! Don't Remove !!
        // Empty constructor used for the firestore document.toObject() method.
    }



    public String getDate() {
        return date;
    }

    public String getData_type() {
        return data_type;
    }

    public HashMap<String, Float> getPricesPerHour() {
        return pricesPerHour;
    }

}
