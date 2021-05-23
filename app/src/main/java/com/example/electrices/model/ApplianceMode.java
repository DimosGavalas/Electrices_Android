package com.example.electrices.model;

public class ApplianceMode {

    private String mModeName;
    private String mWorkingCycle;
    private boolean checked = false;
    private boolean expandable = false;


    public ApplianceMode(){
    }

    public String getmModeName() {
        return mModeName;
    }

    public void setmModeName(String mModeName) {
        this.mModeName = mModeName;
    }

    public String getmWorkingCycle() {
        return mWorkingCycle;
    }

    public void setmWorkingCycle(String mWorkingCycle) {
        this.mWorkingCycle = mWorkingCycle;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}
