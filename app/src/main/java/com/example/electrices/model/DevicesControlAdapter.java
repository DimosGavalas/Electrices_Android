package com.example.electrices.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electrices.R;

import org.json.JSONObject;

import java.util.List;

/**
 * An adapter to set the functionality for the device control
 */

public class DevicesControlAdapter extends RecyclerView.Adapter<DevicesControlAdapter.VersionVH> {

    List<DeviceControl> deviceControlListList;

    public DevicesControlAdapter(List<DeviceControl> deviceControlListList) {
        this.deviceControlListList = deviceControlListList;
    }

    @NonNull
    @Override
    public VersionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new VersionVH(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull VersionVH holder, int position) {
        DeviceControl devices = deviceControlListList.get(position);
        holder.deviceTxt.setText(devices.getDevice());
        holder.startDeviceTxt.setText(devices.getStart());
        holder.stopDeviceTxt.setText(devices.getStop());
        holder.warmWaterTxt.setText(devices.getWarmWater());
        holder.coldWaterTxt.setText(devices.getColdWater());

        boolean isExpandable = deviceControlListList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return deviceControlListList.size();
    }

    //view holder class
    public class VersionVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView deviceTxt;    // to select the device from the recyclerView list, in this case - teh coffee machine
        Button startDeviceTxt, stopDeviceTxt, warmWaterTxt, coldWaterTxt;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;


        public VersionVH(final Context context, @NonNull View itemView) {
            super(itemView);

            deviceTxt = itemView.findViewById(R.id.device_item_from_list);


            // the buttons
            startDeviceTxt = itemView.findViewById(R.id.start_device_button);
            startDeviceTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    String urlStartCoffee = "http://83.95.169.116:5000/api/startMachine";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlStartCoffee, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(context, "Turned ON", Toast.LENGTH_SHORT).show();
                        }
                    } , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                            Log.i("adapter", String.valueOf(error));

                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            });

            stopDeviceTxt = itemView.findViewById(R.id.stop_device_button);
            stopDeviceTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    String urlStopCoffee = "http://83.95.169.116:5000/api/stopMachine";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlStopCoffee, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(context, "Turned OFF", Toast.LENGTH_SHORT).show();
                        }
                    } , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                            Log.i("adapter", String.valueOf(error));

                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            });

            warmWaterTxt = itemView.findViewById(R.id.warm_water_button);
            warmWaterTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    String urlWarmWater = "http://83.95.169.116:5000/api/hotWater";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlWarmWater, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(context, "Pouring hot water", Toast.LENGTH_SHORT).show();
                        }
                    } , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                            Log.i("adapter", String.valueOf(error));

                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            });

            coldWaterTxt = itemView.findViewById(R.id.cold_water_button);
            coldWaterTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    String urlColdWater = "http://83.95.169.116:5000/api/coldWater";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlColdWater, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(context, "Pouring cold water", Toast.LENGTH_SHORT).show();
                        }
                    } , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                            Log.i("adapter", String.valueOf(error));

                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            });

            //the layouts
            linearLayout = itemView.findViewById(R.id.row_linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            //clickListener on the LinearLayout
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeviceControl deviceControl = deviceControlListList.get(getAdapterPosition());
                    deviceControl.setExpandable(!deviceControl.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

        @Override
        public void onClick(View v) {
        }
    }
}
