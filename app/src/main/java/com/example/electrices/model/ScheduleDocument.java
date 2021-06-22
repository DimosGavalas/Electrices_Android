package com.example.electrices.model;

import java.util.HashMap;

public class ScheduleDocument {

    private HashMap<String, HashMap<String, HashMap<String, Integer>>> daySchedule;

    public ScheduleDocument(){ }

    public ScheduleDocument(String time, String deviceName, Integer modeNumber){
        HashMap<String, HashMap<String, Integer>> appliance = new HashMap<>();
        HashMap<String, Integer> applianceMode = new HashMap<>();
        applianceMode.put("Mode", modeNumber);
        appliance.put(deviceName, applianceMode);
        this.daySchedule = new HashMap<>();
        this.daySchedule.put(time, appliance);
    }


    public HashMap<String, HashMap<String, HashMap<String, Integer>>> getDaySchedule() {
        return daySchedule;
    }
}
