package com.example.electrices.model.recyclerViewComponents;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electrices.R;
import com.example.electrices.model.PricesDocument;

import java.util.ArrayList;

public class ApplianceTimeSelectionAdapter extends RecyclerView.Adapter<ApplianceTimeSelectionAdapter.ViewHolder> {

    // A listener used to know when a recycler item is pressed and pass it to the popup window.
    public interface TimeSelectionListener{
        void onTimeSelected(PricesDocument.PriceLevelHour priceLevelHourObject);
    }
    private TimeSelectionListener timeSelectionListener = null;

    public void setTimeSelectionListener(TimeSelectionListener timeSelectionListener){
        this.timeSelectionListener = timeSelectionListener;
    }



//################# End of Interface ###########################################


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTime;
        public TextView tvPrice;
        public TextView tvPriceLevel;
        public RadioButton mRadioButton;
        public LinearLayout mViewBaseLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.time);
            tvPrice = itemView.findViewById(R.id.hourly_price);
            tvPriceLevel = itemView.findViewById(R.id.price_level);

            mViewBaseLayout = itemView.findViewById(R.id.base_layout);
            mRadioButton = itemView.findViewById(R.id.selection_button);
            mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Changing background color according to the checked status of the button.
                    if(isChecked){
                        mViewBaseLayout.setBackgroundResource(R.color.selected_background);
                    }else {
                        mViewBaseLayout.setBackgroundResource(R.color.card_background);
                    }
                }
            });

            mRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mPreviouslySelectedPosition != -1){
                        mPriceLevelHours.get(mPreviouslySelectedPosition).setChecked(false);
                        notifyItemChanged(mPreviouslySelectedPosition);
                    }
                    mSelectedPosition = getLayoutPosition();
                    mPriceLevelHours.get(mSelectedPosition).setChecked(true);

                    mPreviouslySelectedPosition = mSelectedPosition;

//                    Log.i(TAG, "Position " + String.valueOf(mSelectedItemPosition));
                    if(timeSelectionListener != null){
                        timeSelectionListener.onTimeSelected(mPriceLevelHours.get(mSelectedPosition));
                    }
                }
            });
        }
    }

//################# End of ViewHolder Class ###########################################


    private final String TAG = "TIME_SELECTION_ADAPTER";

    // The incoming data that will populate the view's elements.
    private ArrayList<PricesDocument.PriceLevelHour> mPriceLevelHours;
    public int mPreviouslySelectedPosition = -1;
    public int mSelectedPosition = -1;

    public ApplianceTimeSelectionAdapter(ArrayList<PricesDocument.PriceLevelHour> priceLevelHours){
        this.mPriceLevelHours = priceLevelHours;
//        this.lastClickedRadioButton = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View timeSelectionView = layoutInflater.inflate(R.layout.recycler_view_schedule_appliance_time_item, parent, false);

        ViewHolder viewHolder = new ApplianceTimeSelectionAdapter.ViewHolder(timeSelectionView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PricesDocument.PriceLevelHour priceLevelHour = mPriceLevelHours.get(position);
//        Log.i(TAG, String.valueOf(mPriceLevelHours.get(position).hashCode()));
//        Log.i(TAG, String.valueOf(position));
//        Log.i(TAG, String.valueOf(holder.getItemId()));

        holder.tvTime.setText(priceLevelHour.getTime());
        holder.tvPrice.setText(String.valueOf(priceLevelHour.getPrice()));

        String priceLevel = priceLevelHour.getPrice_level();
        holder.tvPriceLevel.setText(priceLevel);
//        Log.i(TAG, priceLevel);
        Resources resources = holder.tvPriceLevel.getContext().getResources();
        if(priceLevel.equals("low")){
            holder.tvPriceLevel.setBackground(resources.getDrawable(R.drawable.bg_label_low_prices));
        }
        if(priceLevel.equals("medium")){
            holder.tvPriceLevel.setBackground(resources.getDrawable(R.drawable.bg_label_medium_prices));
        }
        if(priceLevel.equals("high")){
            holder.tvPriceLevel.setBackground(resources.getDrawable(R.drawable.bg_label_high_prices));
        }

        // At the start if any item is checked, uncheck it.
        if(mSelectedPosition == -1){
            priceLevelHour.setChecked(false);
        }
        holder.mRadioButton.setChecked(priceLevelHour.isChecked());

    }

    @Override
    public int getItemCount() {
        return mPriceLevelHours.size();
    }



}
