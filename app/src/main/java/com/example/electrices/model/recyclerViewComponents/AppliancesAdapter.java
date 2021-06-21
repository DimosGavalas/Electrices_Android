package com.example.electrices.model.recyclerViewComponents;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.electrices.R;
import com.example.electrices.model.Appliance;
import com.example.electrices.model.customPopupWindows.PopupWindowApplianceScheduler;

import org.json.JSONObject;

import java.util.List;

/**
 * An adapter to set the functionality for the device control
 */

public class AppliancesAdapter extends RecyclerView.Adapter<AppliancesAdapter.ViewHolder> {

    //view holder class
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appliance_name;    // to select the device from the recyclerView list, in this case - teh coffee machine
        ImageView appliance_image;
        TextView txtView_mode_start;
        TextView txtView_mode_stop;
        TextView txtView_mode_3;
        TextView txtView_mode_4;
        ImageButton button_mode_start, button_mode_stop, button_mode_3, button_mode_4;
        ImageButton card_toggler, button_schedule_appliance;
        RelativeLayout expandableLayout;


        public ViewHolder(final Context applicationContext, @NonNull View itemView) {
            super(itemView);

            appliance_name = itemView.findViewById(R.id.device_item_from_list);
            appliance_image = itemView.findViewById(R.id.appliance_image);

            // the buttons
            txtView_mode_start = itemView.findViewById(R.id.label_operational_mode_start);
            button_mode_start = itemView.findViewById(R.id.button_start);
            button_mode_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
                    String urlStartCoffee = "http://83.95.169.116/startappliance";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlStartCoffee, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(applicationContext, "Turned ON", Toast.LENGTH_SHORT).show();
                        }
                    } , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show();
                            Log.i("adapter", String.valueOf(error));

                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            });

            txtView_mode_stop = itemView.findViewById(R.id.label_operational_mode_stop);
            button_mode_stop = itemView.findViewById(R.id.button_stop);

            txtView_mode_3 = itemView.findViewById(R.id.label_operational_mode_3);
            button_mode_3 = itemView.findViewById(R.id.button_mode3); // Warm water - for the coffee machine

            txtView_mode_4 = itemView.findViewById(R.id.label_operational_mode_4);
            button_mode_4 = itemView.findViewById(R.id.button_mode4); // Cold water - for the coffee machine

            // Image button for expanding-contracting the cards.
            card_toggler = itemView.findViewById(R.id.card_toggler);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            card_toggler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Appliance appliance = applianceListList.get(getAbsoluteAdapterPosition());
                    appliance.setExpandable(!appliance.isExpandable());
                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            });

            // Button for the popup window of scheduling an appliance.
            button_schedule_appliance = itemView.findViewById(R.id.imageButton_schedule_appliance);
            button_schedule_appliance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Appliance appliance = applianceListList.get(getAbsoluteAdapterPosition());
                    PopupWindowApplianceScheduler popupWindowApplianceScheduler = new PopupWindowApplianceScheduler(v);
                    popupWindowApplianceScheduler.setAppliance(appliance);
                    popupWindowApplianceScheduler.showPopupWindow();
                }
            });
        }
    }

//################# End of ViewHolder Class ###########################################


    private final String TAG = "DevicesControlAdapter";
    List<Appliance> applianceListList;

    public AppliancesAdapter(List<Appliance> applianceListList) {
        this.applianceListList = applianceListList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context applicationContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_appliance_item, parent, false);
        return new ViewHolder(applicationContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appliance devices = applianceListList.get(position);
        holder.appliance_image.setImageResource(devices.getAppliance_image());
        holder.appliance_name.setText(devices.getAppliance_name());
        holder.txtView_mode_start.setText(devices.getStart());
        holder.txtView_mode_stop.setText(devices.getStop());
        holder.txtView_mode_3.setText(devices.getWarmWater());
        holder.txtView_mode_4.setText(devices.getColdWater());

        boolean isExpandable = applianceListList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        holder.card_toggler.setRotation(isExpandable ? 180f : 0f);
    }

    @Override
    public int getItemCount() {
        return applianceListList.size();
    }
}
