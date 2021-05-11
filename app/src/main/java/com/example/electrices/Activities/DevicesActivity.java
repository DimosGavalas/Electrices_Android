package com.example.electrices.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electrices.R;
import com.example.electrices.model.DeviceControl;
import com.example.electrices.model.DevicesControlAdapter;

import java.util.ArrayList;
import java.util.List;

/** In this activity:
 *  - add each device on the list (for now each device will be added manually as well) "initData()"
 * FUTURE IMPLEMENTATION: get each device with its functions from Firebase
 */

public class DevicesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DeviceControl> devicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_screen);

        recyclerView = findViewById(R.id.devices_recycler_list);

        initData();
        setRecyclerView();

     }

    private void setRecyclerView() {
        DevicesControlAdapter devicesControlAdapter = new DevicesControlAdapter(devicesList);
        recyclerView.setAdapter(devicesControlAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {

        devicesList = new ArrayList<>();

        devicesList.add(new DeviceControl("Coffee Machine", "START", "STOP", "Warm water", "Cold water"));
        devicesList.add(new DeviceControl("Washing Machine", "ON", "OFF", "Schedule washing", "Cancel schedule"));
        devicesList.add(new DeviceControl("Robot Vacuum cleaner", "START", "STOP", "Schedule cleaning", "Cancel schedule"));
    }

}
