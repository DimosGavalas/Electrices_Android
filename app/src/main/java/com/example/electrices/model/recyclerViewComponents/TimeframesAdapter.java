package com.example.electrices.model.recyclerViewComponents;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electrices.R;
import com.example.electrices.model.StatisticsDocument;

import java.util.ArrayList;
import java.util.Map;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class TimeframesAdapter extends RecyclerView.Adapter<TimeframesAdapter.ViewHolder>{

    // Provide a direct reference to each of the views within a data item.
    // Used to cache the views within the item layout for fast access.
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView timeframeTV;
        public TextView avgPriceTV;
        public TextView priceLevelTV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(@NonNull View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            timeframeTV = (TextView) itemView.findViewById(R.id.timeframe);
            avgPriceTV = (TextView) itemView.findViewById(R.id.average_pirce);
            priceLevelTV = (TextView) itemView.findViewById(R.id.price_level);
        }
    }

//################# End of ViewHolder Class ###########################################


    // Store a member variable for the timeframes.
//    private ArrayList<Map<String, Object>> mTimeframes;
    private ArrayList<StatisticsDocument.Timeframe> mTimeframes;
    private Resources resources;

    // Pass in the timeframes array into the constructor.
    public TimeframesAdapter(ArrayList<StatisticsDocument.Timeframe> timeframes) {
        mTimeframes = timeframes;
    }

    // Usually involves inflating a layout from XML and returning the holder.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        resources = context.getResources();

        // Inflate the custom layout
        View timeframeView = inflater.inflate(R.layout.recycler_view_timeframe_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(timeframeView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Get the data model based on position.
       StatisticsDocument.Timeframe timeframeItem = mTimeframes.get(position);

        // Set item views based on your views and data model
        viewHolder.timeframeTV.setText(timeframeItem.getTimeframe());
        viewHolder.avgPriceTV.setText(String.valueOf(timeframeItem.getAverage()));
        String priceLevel = timeframeItem.getPrice_level();


        viewHolder.priceLevelTV.setText(priceLevel );
        if(priceLevel.equals("low")){
            viewHolder.priceLevelTV.setBackground(resources.getDrawable(R.drawable.bg_label_low_prices));
        }
        if(priceLevel.equals("medium")){
            viewHolder.priceLevelTV.setBackground(resources.getDrawable(R.drawable.bg_label_medium_prices));
        }
        if(priceLevel.equals("high")){
            viewHolder.priceLevelTV.setBackground(resources.getDrawable(R.drawable.bg_label_high_prices));
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTimeframes.size();
    }
}
