package com.example.electrices.model.recyclerViewComponents;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electrices.R;
import com.example.electrices.model.ApplianceSchedule;
import com.example.electrices.model.firestoreModel.ScheduleDocument;

import java.util.ArrayList;

public class ScheduledAppliancesAdapter extends RecyclerView.Adapter<ScheduledAppliancesAdapter.ViewHolder> {

    public static final String TAG = "ScheduledItemsAdapter";

    // Provide a direct reference to each of the views within a data item.
    // Used to cache the views within the item layout for fast access.
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView applianceNameTV;
        public TextView startTimeTV;
        public TextView workCycleTV;
        public TextView modeTV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(@NonNull View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            applianceNameTV = itemView.findViewById(R.id.appliance_name);
            startTimeTV = itemView.findViewById(R.id.start_time);
            workCycleTV = itemView.findViewById(R.id.work_cycle);
            modeTV = itemView.findViewById(R.id.mode);
        }
    }

//    ########################## END OF VIEW HOLDER CLASS ###########################


    // Store a member variable for the timeframes.
    private ArrayList<ApplianceSchedule> mScheduledAppliancesList;
    private Resources resources;

    // Pass in the timeframes array into the constructor.
    public ScheduledAppliancesAdapter(ArrayList<ApplianceSchedule> scheduledAppliances) {
        this.mScheduledAppliancesList = scheduledAppliances;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @NonNull
    @Override
    public ScheduledAppliancesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        resources = context.getResources();

        // Inflate the custom layout
        View scheduledItemView = inflater.inflate(R.layout.recycler_view_scheduled_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(scheduledItemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Get the data model based on position.
        ApplianceSchedule applianceSchedule = mScheduledAppliancesList.get(position);

        viewHolder.applianceNameTV.setText(applianceSchedule.getApplianceName());
        viewHolder.startTimeTV.setText(applianceSchedule.getTimeScheduled());
        viewHolder.modeTV.setText(String.valueOf(applianceSchedule.getMode()));
        viewHolder.workCycleTV.setText("2h 15m");
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mScheduledAppliancesList.size();
    }

}
