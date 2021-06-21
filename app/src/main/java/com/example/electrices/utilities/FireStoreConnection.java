package com.example.electrices.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.electrices.model.PricesDocument;
import com.example.electrices.model.StatisticsDocument;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


public class FireStoreConnection {

    /** Custom listener that monitors when a document's data are downloaded from firestore and
     * pass the created (from that data) ElectricityPricesDocument object to the appropriate activity.
     * Tutorial: https://guides.codepath.com/android/Creating-Custom-Listeners
     *
     * In this specific occasion we set-up the DocumentListener inside the BarChartActivity with purpose
     * to pass the downloaded data from firestore, in the form of a ElectricityPricesDocument object.
     *
     * Inside this class (see below) the getDocumentForDate() contains a onComplete listener. Inside this
     * listener we receive the data from firestore and create an ElectricityPricesDocument object that
     * contains these data. However, if we pass our ElectricityPricesDocument object with a return
     * statement of the getDocumentForDate() method, the object will always be null. This is because the
     * onComplete listener does not receive the callback immediately but only after the execution of the
     * return statement of the getDocumentForDate() method. This means that the downloaded data are
     * passed and saved to the ElectricityPricesDocument object after it has been returned by the getDocumentForDate()
     * method as null.
     * To solve this problem ....
     **/
    public interface DocumentListener{
        <D> void onDocumentReady(D document);
    }
    private DocumentListener documentListener;

    // Following a Singleton pattern
//    private static FireStoreConnection INSTANCE = null;

    private static final String TAG = "FIRESTORE-CONNECTION";
    private static final String PRICES_COLLECTION = "electricityPrices";
    private static final String STATISTICS_COLLECTION = "electricityPrices-stats";

    private FirebaseFirestore ffDatabase;

    // Private constructor for singleton pattern
    public FireStoreConnection(){
        documentListener = null;
        openDBConnection();
    }

    // Singleton pattern
//    public static FireStoreConnection getInstance(){
//        if(INSTANCE == null){
//            INSTANCE = new FireStoreConnection();
//        }
//        return INSTANCE;
//    }

    private void openDBConnection(){
        // Access a Cloud Firestore instance from your Activity
        ffDatabase = FirebaseFirestore.getInstance();
        Log.i(TAG, String.valueOf(ffDatabase.getApp()));
    }


    // Two ways of reading data from firestore.
    // ReadData version 1 implements querying in a direct way, applying method over method.
    // ReadData version 2 starts with creating separetly a Query object and after this it pass it to the get method as in version 1.
    // This way query functions can be handled easier and reduce rewriting the same code for querying different collections and documents.
    public void readDataV1(FirebaseFirestore db){
        db.collection("electricityPrices")
                .whereEqualTo("Date", "2021-03-10")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getPricesDocumentForDate(String date){
        Query querySelectedDatePrices = ffDatabase.collection(PRICES_COLLECTION).whereEqualTo("date", date);
        querySelectedDatePrices
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    PricesDocument pricesDocument = new PricesDocument();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                pricesDocument = document.toObject(PricesDocument.class); // Can return this object and pass it to another class.
                                if (documentListener != null){
                                    documentListener.onDocumentReady(pricesDocument);
                                }
//                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting PRICES documents.", task.getException());
                        }
                    }
                });
    }

    public void getStatisticsDocumentForDate(String date){
        Query querySelectedDateStats = ffDatabase.collection(STATISTICS_COLLECTION).whereEqualTo("date", date);
        querySelectedDateStats
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    StatisticsDocument statisticsDocument = new StatisticsDocument();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                statisticsDocument = document.toObject(StatisticsDocument.class); // Can return this object and pass it to another class.

                                if (documentListener != null){
                                    documentListener.onDocumentReady(statisticsDocument);
                                }
//                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting STATISTICS documents.", task.getException());
                        }
                    }
                });
    }

    // Creates a compound prices document where it relates every hourly price with a price level (low, medium, high).
    public void getCompoundPricesDocumentForDate(String date){
        final PricesDocument[] compoundPricesDocument = {new PricesDocument()};
        Query querySelectedDatePrices = ffDatabase.collection(PRICES_COLLECTION).whereEqualTo("date", date);
        querySelectedDatePrices
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                compoundPricesDocument[0] = document.toObject(PricesDocument.class); // Can return this object and pass it to another class.

                                // New nested query for the creation of a compound PricesDocument that contains also data from the statistics collection.
                                Query querySelectedDateStatsMinMax = ffDatabase.collection(STATISTICS_COLLECTION).whereEqualTo("date", date);
                                querySelectedDateStatsMinMax
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        // Can return this object and pass it to another class.
                                                        HashMap<String, HashMap<String, Double>> price_levels_min_max = (HashMap<String, HashMap<String, Double>>) document.getData().get("price_levels_min_max");
                                                        compoundPricesDocument[0].constructPriceLevelHourObjects(price_levels_min_max);

                                                        if (documentListener != null){
                                                            documentListener.onDocumentReady(compoundPricesDocument[0]);
                                                        }
//                                                        Log.i(TAG, document.getId() + " => " + price_levels_min_max);
                                                    }
                                                } else {
                                                    Log.w(TAG, "Error getting STATISTICS documents.", task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting PRICES documents.", task.getException());
                        }
                    }
                });
    }

    public void setDocumentListener(DocumentListener documentListener) {
        this.documentListener = documentListener;
    }


}
