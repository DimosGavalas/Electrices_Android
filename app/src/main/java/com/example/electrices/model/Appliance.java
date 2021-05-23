package com.example.electrices.model;

import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * model class for getters and setters for all the buttons from row.xml
 * it was all set folowing this tutorial:
 * https://www.youtube.com/watch?v=pGi02uJre4M&ab_channel=AndroidWorldClub
 */

public class Appliance {

    int appliance_image;
    private String appliance_name, start, stop, warmWater, coldWater;
    private ArrayList<ApplianceMode> appliance_modes;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    // Constructor must be empty to be used with firestore method .toObject().
    public Appliance(String appliance_name, String start, String stop, String warmWater, String coldWater, int appliance_image) {
        this.appliance_name = appliance_name;
        this.start = start;
        this.stop = stop;
        this.warmWater = warmWater;
        this.coldWater = coldWater;
        this.expandable = false;
        this.appliance_image = appliance_image;
        appliance_modes = new ArrayList<>();

        // Making appliance modes for testing.
        for(int i=1; i<=4; i++){
            ApplianceMode applianceMode = new ApplianceMode();
            switch (i){
                case 1:
                    applianceMode.setmModeName("Mode "+String.valueOf(i));
                    applianceMode.setmWorkingCycle("2h 15m");
                    break;
                case 2:
                    applianceMode.setmModeName("Mode "+String.valueOf(i));
                    applianceMode.setmWorkingCycle("3h");
                    break;
                case 3:
                    applianceMode.setmModeName("Mode "+String.valueOf(i));
                    applianceMode.setmWorkingCycle("1h 40m");
                    break;
                case 4:
                    applianceMode.setmModeName("Mode "+String.valueOf(i));
                    applianceMode.setmWorkingCycle("1h 5m");

                    break;
            }
            appliance_modes.add(applianceMode);
        }
    }

    public String getAppliance_name() {
        return appliance_name;
    }

    public void setAppliance_name(String appliance_name) {
        this.appliance_name = appliance_name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getWarmWater() {
        return warmWater;
    }

    public void setWarmWater(String warmWater) {
        this.warmWater = warmWater;
    }

    public String getColdWater() {
        return coldWater;
    }

    public void setColdWater(String coldWater) {
        this.coldWater = coldWater;
    }

    public int getAppliance_image(){
        return appliance_image;
    }

    public ArrayList<ApplianceMode> getAppliance_modes(){
        return appliance_modes;
    }

    @Override
    public String toString() {
        return "DeviceControl{" +
                "device='" + appliance_name + '\'' +
                ", start='" + start + '\'' +
                ", stop='" + stop + '\'' +
                ", warmWater='" + warmWater + '\'' +
                ", coldWater='" + coldWater + '\'' +
                '}';
    }
}
