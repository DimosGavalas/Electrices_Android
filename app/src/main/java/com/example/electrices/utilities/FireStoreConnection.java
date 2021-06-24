package com.example.electrices.utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.electrices.model.firestoreModel.PricesDocument;
import com.example.electrices.model.firestoreModel.ScheduleDocument;
import com.example.electrices.model.firestoreModel.StatisticsDocument;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
    private static DocumentListener staticDocumentListener;

    public interface DocumentUploadListener{
        void onDocumentUploaded(boolean isUploaded);
    }

    private DocumentUploadListener documentUploadListener;

// #################### END of Interfaces #######################################

    // Following a Singleton pattern
    private static FireStoreConnection INSTANCE = null;

    private static final String TAG = "FIRESTORE-CONNECTION";
    private static final String PRICES_COLLECTION = "electricityPrices";
    private static final String STATISTICS_COLLECTION = "electricityPrices-stats";
    private static final String APPLIANCE_SCHEDULE = "applianceSchedule";

    private FirebaseFirestore ffDatabase;

    // Private constructor for singleton pattern
    private FireStoreConnection(){
        documentUploadListener = null;
        documentUploadListener = null;
        openDBConnection();
    }

    // Singleton pattern beacause we need only one instance of the FireStoreConnection class.
    public static FireStoreConnection getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FireStoreConnection();
        }
        return INSTANCE;
    }

    private void openDBConnection(){
        // Access a Cloud Firestore instance from your Activity
        ffDatabase = FirebaseFirestore.getInstance();
//        Log.i(TAG, String.valueOf(ffDatabase.getApp()));
        Log.i(TAG, String.valueOf(ffDatabase));
    }


    // ############## Reading Methods #####################
    public void getPricesDocumentForDate(String date){
        DocumentListener documentListener = this.staticDocumentListener;
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
        DocumentListener documentListener = this.staticDocumentListener;
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
                                Log.i(TAG, "Statistics: " + String.valueOf(document.toObject(ScheduleDocument.class)));
                            }
                        } else {
                            Log.w(TAG, "Error getting STATISTICS documents.", task.getException());
                        }
                    }
                });
    }


    // Creates a compound prices document where it relates every hourly price with a price level (low, medium, high).
    public void getCompoundPricesDocumentForDate(String date){
        DocumentListener documentListener = this.staticDocumentListener;
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


    public void getAppliancesScheduleDocumentForDate(String date){
        final DocumentListener documentListener = staticDocumentListener;
        // Use of Snapshot listener for real time data listening.
        ffDatabase.collection(APPLIANCE_SCHEDULE).document(date)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }

                        ScheduleDocument scheduleDocument;
                        if (snapshot != null && snapshot.exists()) {
                            scheduleDocument = snapshot.toObject(ScheduleDocument.class);
                            if (documentListener != null) {
                                documentListener.onDocumentReady(scheduleDocument);
                                Log.i(TAG, "LISTENER: " + String.valueOf(documentListener));
                            }
                            Log.i(TAG, "Scheduled Appliances: " + String.valueOf(snapshot.toObject(ScheduleDocument.class)));
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
    }


    // ############## Input Methods #####################
    public void insertApplianceScheduleDocument(ScheduleDocument scheduleDocument, String dateId){
        ffDatabase.collection(APPLIANCE_SCHEDULE).document(dateId).set(scheduleDocument, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(documentUploadListener != null){
                        documentUploadListener.onDocumentUploaded(true);
                    }
                }
            }
        });
    }

    public void setDocumentListener(DocumentListener documentListener) {
        staticDocumentListener = documentListener;
    }

    public void setDocumentUploadListener(DocumentUploadListener documentUploadListener){
        this.documentUploadListener = documentUploadListener;
    }

}
