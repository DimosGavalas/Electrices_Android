package com.example.electrices.model;

/**
 * model class for getters and setters for all the buttons from row.xml
 * it was all set folowing this tutorial:
 * https://www.youtube.com/watch?v=pGi02uJre4M&ab_channel=AndroidWorldClub
 */

public class DeviceControl {

    private String device, start, stop, warmWater, coldWater;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public DeviceControl(String device, String start, String stop, String warmWater, String coldWater) {
        this.device = device;
        this.start = start;
        this.stop = stop;
        this.warmWater = warmWater;
        this.coldWater = coldWater;
        this.expandable = false;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
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

    @Override
    public String toString() {
        return "DeviceControl{" +
                "device='" + device + '\'' +
                ", start='" + start + '\'' +
                ", stop='" + stop + '\'' +
                ", warmWater='" + warmWater + '\'' +
                ", coldWater='" + coldWater + '\'' +
                '}';
    }
}
