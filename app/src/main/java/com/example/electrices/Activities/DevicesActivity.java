package com.example.electrices.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electrices.R;
import com.example.electrices.model.Appliance;
import com.example.electrices.model.recyclerViewComponents.AppliancesAdapter;

import java.util.ArrayList;
import java.util.List;

/** In this activity:
 *  - add each device on the list (for now each device will be added manually as well) "initData()"
 * FUTURE IMPLEMENTATION: get each device with its functions from Firebase
 */

public class DevicesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Appliance> devicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_screen);

        recyclerView = findViewById(R.id.devices_recycler_list);

        initData();
        setRecyclerView();

     }

    private void setRecyclerView() {
        AppliancesAdapter appliancesAdapter = new AppliancesAdapter(devicesList);
        recyclerView.setAdapter(appliancesAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {

        devicesList = new ArrayList<>();

        devicesList.add(new Appliance("Coffee Machine", "START", "STOP", "Warm water", "Cold water", R.drawable.appliance_image_coffee_machine_254));
        devicesList.add(new Appliance("Washing Machine", "ON", "OFF", "Schedule washing", "Cancel schedule", R.drawable.appliance_image_washing_machine_254));
        devicesList.add(new Appliance("Robot Vacuum cleaner", "START", "STOP", "Schedule cleaning", "Cancel schedule", R.drawable.appliance_image_general_appliances_254));
    }

}
