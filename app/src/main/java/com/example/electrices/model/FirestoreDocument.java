package com.example.electrices.model;


public abstract class FirestoreDocument {

    public FirestoreDocument(){
        // !! Don't Remove !!
        // Empty constructor used for the firestore document.toObject() method.
    }

    public abstract String getDate();
    public abstract  String getDay();
}
