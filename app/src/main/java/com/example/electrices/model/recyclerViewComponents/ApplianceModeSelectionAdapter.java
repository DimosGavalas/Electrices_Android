package com.example.electrices.model.recyclerViewComponents;

import android.content.Context;
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
import com.example.electrices.model.ApplianceMode;

import java.util.ArrayList;

public class ApplianceModeSelectionAdapter extends RecyclerView.Adapter<ApplianceModeSelectionAdapter.ViewHolder> {

    // A listener used to know when a recycler item is pressed and pass it to the popup window.
    public interface ModeSelectionListener{
        void onModeSelected(ApplianceMode mode);
    }

    private ModeSelectionListener modeSelectionListener = null;

    public void setModeSelectionListener(ModeSelectionListener modeSelectionListener){
        this.modeSelectionListener = modeSelectionListener;
    }


//################# End of Interface ###########################################


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvModeName;
        public TextView tvWorkingCycle;
        public RadioButton radioButton;
        public LinearLayout modeBaseLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvModeName = itemView.findViewById(R.id.operational_mode);
            tvWorkingCycle = itemView.findViewById(R.id.working_cycle);
            modeBaseLayout = itemView.findViewById(R.id.mode_base_layout);

            radioButton = itemView.findViewById(R.id.selection_button);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Changing background color according to the checked status of the button.
                    if(isChecked){
                        modeBaseLayout.setBackgroundResource(R.color.selected_background);
                    }else {
                        modeBaseLayout.setBackgroundResource(R.color.card_background);
                    }
                }
            });

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mPreviouslySelectedPosition != -1){
                        appliance_modes.get(mPreviouslySelectedPosition).setChecked(false);
                        notifyItemChanged(mPreviouslySelectedPosition);
                    }
                    mSelectedPosition = getLayoutPosition();
                    appliance_modes.get(mSelectedPosition).setChecked(true);

                    mPreviouslySelectedPosition = mSelectedPosition;

//                    Log.i(TAG, "Position " + String.valueOf(mSelectedItemPosition));
                    if(modeSelectionListener != null){
                        modeSelectionListener.onModeSelected(appliance_modes.get(mSelectedPosition));
                    }
                }
            });
        }
    }

//################# End of ViewHolder Class ###########################################

    private final String TAG = "MODE_SELECTION_ADAPTER";

    // The incoming data that will populate the view's elements.
    private ArrayList<ApplianceMode> appliance_modes;
    public int mPreviouslySelectedPosition = -1;
    public int mSelectedPosition = -1;

    public ApplianceModeSelectionAdapter(ArrayList<ApplianceMode> appliance_modes){
        this.appliance_modes = appliance_modes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View modeSelectionView = layoutInflater.inflate(R.layout.recycler_view_schedule_appliance_mode_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(modeSelectionView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplianceMode operationalMode = appliance_modes.get(position);

        holder.tvModeName.setText(operationalMode.getmModeName());
        holder.tvWorkingCycle.setText(operationalMode.getmWorkingCycle());

        holder.radioButton.setChecked(operationalMode.isChecked());

    }

    @Override
    public int getItemCount() {
        return appliance_modes.size();
    }

}
