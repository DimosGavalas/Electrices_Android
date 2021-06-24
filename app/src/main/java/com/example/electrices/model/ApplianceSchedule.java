package com.example.electrices.model;

public class ApplianceSchedule {

    private String applianceName;
    private String timeScheduled;
    private Integer mode;
    private String workingCycle;

    public ApplianceSchedule(String applianceName, String timeScheduled, Integer mode){
        this.applianceName = applianceName;
        this.timeScheduled = timeScheduled;
        this.mode = mode;
        switch (mode){
            case 1:
                this.workingCycle = "2h 15m";
                break;
            case 2:
                this.workingCycle = "3h";
                break;
            case 3:
                this.workingCycle = "1h 40m";
                break;
            case 4:
                this.workingCycle = "1h 5m";
                break;
        }
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

    public String getWorkingCycle(){
        return workingCycle;
    }
}
