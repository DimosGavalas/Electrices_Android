package com.example.electrices.model;

public class ApplianceSchedule {

    private String applianceName;
    private String timeScheduled;
    private Integer mode;

    public ApplianceSchedule(String applianceName, String timeScheduled, Integer mode){
        this.applianceName = applianceName;
        this.timeScheduled = timeScheduled;
        this.mode = mode;
    }

    public String getApplianceName() {
        return applianceName;
    }

    public String getTimeScheduled() {
        return timeScheduled;
    }

    public Integer getMode() {
        return mode;
    }
}
