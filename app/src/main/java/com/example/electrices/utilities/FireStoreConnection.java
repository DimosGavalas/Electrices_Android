package com.example.electrices.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.electrices.model.ElectricityPricesDocument;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


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
        void onDocumentReady(ElectricityPricesDocument document);
    }
    private DocumentListener documentListener;

    private static final String TAG = "FIRESTORE-CONNECTION";
    private static final String COLLECTION = "electricityPrices";

    private FirebaseFirestore ffDatabase;

    public FireStoreConnection(){
        documentListener = null;
        openDBConnection();
    }

    private void openDBConnection(){
        // Access a Cloud Firestore instance from your Activity
        ffDatabase = FirebaseFirestore.getInstance();
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

    public void getDocumentForDate(String date){
        Query querySelectedDatePrices = ffDatabase.collection(COLLECTION).whereEqualTo("date", date);
        querySelectedDatePrices
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    ElectricityPricesDocument electricityPricesDocument = new ElectricityPricesDocument();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                electricityPricesDocument = document.toObject(ElectricityPricesDocument.class); // Can return this object and pass it to another class.
                                if (documentListener != null){
                                    documentListener.onDocumentReady(electricityPricesDocument);
                                }
//                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void setDocumentListener(DocumentListener documentListener) {
        this.documentListener = documentListener;
    }
}
