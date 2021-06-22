package com.example.electrices.model.customPopupWindows;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electrices.model.Appliance;
import com.example.electrices.model.ApplianceMode;
import com.example.electrices.model.PricesDocument;
import com.example.electrices.model.ScheduleDocument;
import com.example.electrices.model.recyclerViewComponents.ApplianceModeSelectionAdapter;
import com.example.electrices.model.recyclerViewComponents.ApplianceTimeSelectionAdapter;
import com.example.electrices.utilities.CustomDateUtility;
import com.example.electrices.utilities.FireStoreConnection;
import com.example.electrices.utilities.ObservableVariable;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.electrices.R.*;

public class PopupWindowApplianceScheduler {

    public static final String TAG = "POPUP_WINDOW";

    private LayoutInflater inflater;
    private View popupView;
    private PopupWindow popupWindow;
    private Context popupViewContext;
    private Resources resources;
    private FireStoreConnection fireStoreConnection;
    private CustomDateUtility customDateUtility;

    private ImageView applianceImage;
    private TextView applianceName;
    private Button buttonCancel, buttonConfirm;
    private RecyclerView rvModes, rvTime;
    private TextView mTextViewSelectedMode, mTextViewSelectedTime;

    private ApplianceModeSelectionAdapter modeSelectionAdapter;
    private ApplianceTimeSelectionAdapter timeSelectionAdapter;
    private Appliance appliance;

    // Expandable layouts and components
    private CardView mCardViewModeSelectionContainer, mCardViewTimeSelectionContainer;
    private LinearLayout mExpandableModeSelection, mExpandableTimeSelection;
    private ImageView mTogglerModeSelection, mTogglerTimeSelection;
    private CardView mCardSelectionReview;
    private ObservableVariable<Boolean> isModeSelectionExpanded;
    private ObservableVariable<Boolean> isTimeSelectionExpanded;
    private Boolean isReviewCardVisible;

    // These two variables are used to get the selected Mode and Time from the inner recycler views.
    private ApplianceMode mModeSelectionObject;
    private PricesDocument.PriceLevelHour mTimeSelectionObject;

    private ObservableVariable<Boolean> obv;


    public PopupWindowApplianceScheduler(final View view){
        this.fireStoreConnection = new FireStoreConnection();
        this.customDateUtility = new CustomDateUtility();
        constructPopupWindow(view);
    }

    //PopupWindow display method
    public void showPopupWindow() {
        //Initialize the elements of our window, install the handler
        initializeTitles();
        initializeExpandableViews();
        initializeRecyclerViews();
        initializeButtons();
        onDismissClearCheckedRecyclerItemsListener();
        onVariablesChagnedListener();
//        Log.i(TAG, "Mode: " + mSelectedModeObject);
//        Log.i(TAG, "Time: " + mSelectedTimeObject);
//        Log.i(TAG, String.valueOf(isModeSelectionExpanded instanceof Object));

    }

    public void setAppliance(Appliance appliance){
        this.appliance = appliance;
    }

    private void constructPopupWindow(View view){

        //Create a View object yourself through inflater
        inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(layout.popup_schedule_appliance, null);
        popupViewContext = popupView.getContext();
        resources = popupViewContext.getResources();

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.showAsDropDown(view, 0, 0, Gravity.CENTER);
    }

    private void initializeTitles(){
        // Image and name of the appliance.
        applianceImage = popupView.findViewById(id.appliance_image);
        applianceImage.setImageResource(appliance.getAppliance_image());

        applianceName = popupView.findViewById(id.appliance_name);
        applianceName.setText(appliance.getAppliance_name());
    }

    private void initializeExpandableViews(){
        mCardViewModeSelectionContainer = popupView.findViewById(id.card_mode_selection);
        mCardViewTimeSelectionContainer = popupView.findViewById(id.card_time_selection);

        mCardSelectionReview = popupView.findViewById(id.card_review_selection);
        mTextViewSelectedMode = popupView.findViewById(id.tv_selected_mode);
        mTextViewSelectedTime = popupView.findViewById(id.tv_selected_time);
        isReviewCardVisible = false;

        mExpandableModeSelection = popupView.findViewById(id.expandable_layout_mode_selection);
        mExpandableModeSelection.setVisibility(View.GONE);
        isModeSelectionExpanded = new ObservableVariable<Boolean>();
        isModeSelectionExpanded.setValue(false);
        mTogglerModeSelection = popupView.findViewById(id.mode_selection_toggler);

        mExpandableTimeSelection = popupView.findViewById(id.expandable_layout_time_selection);
        mExpandableTimeSelection.setVisibility(View.GONE);
        isTimeSelectionExpanded = new ObservableVariable<Boolean>();
        isTimeSelectionExpanded.setValue(false);
        mTogglerTimeSelection = popupView.findViewById(id.time_selection_toggler);

        mTogglerModeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                obv.setValue(true);

                if(!isModeSelectionExpanded.getValue()){
                    expandLayout(mExpandableModeSelection, mCardViewModeSelectionContainer, mTogglerModeSelection);
                    isModeSelectionExpanded.setValue(true);
                    if(isTimeSelectionExpanded.getValue()) {
                        retractLayout(mExpandableTimeSelection, mCardViewTimeSelectionContainer, mTogglerTimeSelection);
                        isTimeSelectionExpanded.setValue(false);
                    }
                }else {
                    retractLayout(mExpandableModeSelection, mCardViewModeSelectionContainer, mTogglerModeSelection);
                    isModeSelectionExpanded.setValue(false);
                }
            }
        });

        mTogglerTimeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                obv.setValue(false);

                if(!isTimeSelectionExpanded.getValue()){
                    expandLayout(mExpandableTimeSelection, mCardViewTimeSelectionContainer, mTogglerTimeSelection);
                    isTimeSelectionExpanded.setValue(true);
                    if(isModeSelectionExpanded.getValue()){
                        retractLayout(mExpandableModeSelection, mCardViewModeSelectionContainer, mTogglerModeSelection);
                        isModeSelectionExpanded.setValue(false);
                    }
                }else{
                    retractLayout(mExpandableTimeSelection, mCardViewTimeSelectionContainer, mTogglerTimeSelection);
                    isTimeSelectionExpanded.setValue(false);
                }
            }
        });
    }

    private void initializeRecyclerViews(){
        // The recycler views for the modes and time selection for the appliance.
        rvModes = popupView.findViewById(id.recycler_view_schedule_appliance_modes);
        rvModes.setLayoutManager(new LinearLayoutManager(popupViewContext));
        modeSelectionAdapter = new ApplianceModeSelectionAdapter(appliance.getAppliance_modes());
        rvModes.setAdapter(modeSelectionAdapter);
        modeSelectionAdapter.setModeSelectionListener(new ApplianceModeSelectionAdapter.ModeSelectionListener() {
            @Override
            public void onModeSelected(ApplianceMode mode) {
                mModeSelectionObject = mode;
//                Log.i(TAG, "Mode: " + mSelectedModeObject.getmModeName());
//                Log.i(TAG, "Work Cycle: " + mSelectedModeObject.getmWorkingCycle());
            }
        });


        rvTime = popupView.findViewById(id.recycler_view_schedule_appliance_hours);
        rvTime.setLayoutManager(new LinearLayoutManager(popupViewContext));
        fireStoreConnection.getCompoundPricesDocumentForDate(customDateUtility.getTodaysDateForFirestoreQuery());
        fireStoreConnection.setDocumentListener(new FireStoreConnection.DocumentListener() {
            @Override
            public <D> void onDocumentReady(D document) {
                if(document instanceof PricesDocument){
                    PricesDocument compoundPricesDocument = (PricesDocument) document;
                    timeSelectionAdapter = new ApplianceTimeSelectionAdapter(compoundPricesDocument.getPriceLevelHourObjects());
                    rvTime.setAdapter(timeSelectionAdapter);

                    timeSelectionAdapter.setTimeSelectionListener(new ApplianceTimeSelectionAdapter.TimeSelectionListener() {
                        @Override
                        public void onTimeSelected(PricesDocument.PriceLevelHour priceLevelHourObject) {
                            mTimeSelectionObject = priceLevelHourObject;
//                            Log.i(TAG, "Time: " + mSelectedTimeObject.getTime());
//                            Log.i(TAG, "Price: " + mSelectedTimeObject.getPrice());
//                            Log.i(TAG, "Level: " + mSelectedTimeObject.getPrice_level());
                        }
                    });
                }
            }
        });

    }

    private void initializeButtons(){

        buttonCancel = popupView.findViewById(id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // Disabling Confirmation button until mode and time is selected.
        buttonConfirm = popupView.findViewById(id.button_confirm);
        disableButton(buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here make the call to firestore to save the scheduled device.
                ScheduleDocument scheduleDocument = new ScheduleDocument(mTimeSelectionObject.getTime(), appliance.getAppliance_name(), mModeSelectionObject.getmModeNumber());
                fireStoreConnection.setDocumentUploadListener(new FireStoreConnection.DocumentUploadListener() {
                    @Override
                    public void onDocumentUploaded(boolean isUploaded) {
                        if(isUploaded){
                            popupWindow.dismiss();
                            Toast.makeText(popupViewContext, "Schedule Uploaded", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                fireStoreConnection.insertApplianceScheduleDocument(scheduleDocument, customDateUtility.getTodaysDateForFirestoreQuery());
            }
        });
    }

    private void expandLayout(LinearLayout expandableLayout, CardView expandableLayoutContainer, ImageView toggler){
        expandableLayout.setVisibility(View.VISIBLE);
        setCardViewHeight(expandableLayoutContainer, 0);
        toggler.animate()
                .rotation(180)
                .setDuration(300);
    }

    private void retractLayout(LinearLayout expandableLayout, CardView expandableLayoutContainer, ImageView toggler){
        setCardViewHeight(expandableLayoutContainer, ViewGroup.LayoutParams.WRAP_CONTENT);
        expandableLayout.setVisibility(View.GONE);
        toggler.animate()
                .rotation(0)
                .setDuration(300);
    }

    /**
     * Method to programmatically change the height of the CardView.
     * Our CardViews contain a constraint_height_percent parameter
     * defined in the xml layout.
     * When the height of the card is set at 0, then the CardView uses
     * the constraint percent value.
     * When the height is set at WRAP_CONTENT, then the CardView discards
     * the constraint percent value.
     * When the view is expanded we need the use of the percentage value so the
     * elements do not overflow out of the layout.
     * When the view is retracted we need to wrap the view so it does not
     * occupy empty space.
     * */
    private void setCardViewHeight(CardView cardView, int height){
        ViewGroup.LayoutParams params = cardView.getLayoutParams();
        params.height = height;
        cardView.setLayoutParams(params);
    }

    private void disableButton(Button button){
        button.setEnabled(false);
        button.setBackgroundColor(resources.getColor(color.gray_50));
    }

    private void enableButton(Button button){
        button.setEnabled(true);
        button.setBackgroundColor(resources.getColor(color.low_prices));
    }

    private void onDismissClearCheckedRecyclerItemsListener(){
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ArrayList<ApplianceMode> applianceModes = appliance.getAppliance_modes();
                for(ApplianceMode applianceMode : applianceModes){
                    if(applianceMode.isChecked()){
                        applianceMode.setChecked(false);
                    }
                }
            }
        });
    }

    // For testing observable variables
    private void onVariablesChagnedListener(){
//        obv = new ObservableVariable<Boolean>();
//        obv.setOnChangeListener(new ObservableVariable.OnChangeListener() {
//            @Override
//            public void onChange() {
//                Log.i(TAG, "Variable Changed!!!!  " + obv.getValue());
//            }
//        });

        isModeSelectionExpanded.setOnChangeListener(new ObservableVariable.OnChangeListener() {
            @Override
            public void onChange() {
                Log.i(TAG, "Mode Expanded !  " + isModeSelectionExpanded.getValue());
                Log.i(TAG, "Time Expanded !  " + isTimeSelectionExpanded.getValue());
                Log.i(TAG, "Mode Object !  " + mModeSelectionObject);
                Log.i(TAG, "Time Object !  " + mTimeSelectionObject);
                if(areSchedulingItemsSelected() && !isModeSelectionExpanded.getValue() && !isTimeSelectionExpanded.getValue()){
                    mCardSelectionReview.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(400)
                            .setStartDelay(400)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    mCardSelectionReview.setVisibility(View.VISIBLE);
                                }
                            });

                    mTextViewSelectedMode.setText(mModeSelectionObject.getmModeName());
                    mTextViewSelectedTime.setText(mTimeSelectionObject.getTime());
                    Log.i(TAG, "Visibility ON  " );
                    if(!buttonConfirm.isEnabled()){
                        enableButton(buttonConfirm);
                    }
                }else{
                    mCardSelectionReview.setVisibility(View.GONE);
                    mCardSelectionReview.setScaleX(0f);
                    mCardSelectionReview.setScaleY(0f);
                    Log.i(TAG, "Visibility OFF " );
                    if(buttonConfirm.isEnabled()){
                        disableButton(buttonConfirm);
                    }
                }
            }
        });

        isTimeSelectionExpanded.setOnChangeListener(new ObservableVariable.OnChangeListener() {
            @Override
            public void onChange() {
                Log.i(TAG, "Mode Expanded !  " + isModeSelectionExpanded.getValue());
                Log.i(TAG, "Time Expanded !  " + isTimeSelectionExpanded.getValue());
                Log.i(TAG, "Mode Object !  " + mModeSelectionObject);
                Log.i(TAG, "Time Object !  " + mTimeSelectionObject);
                if(areSchedulingItemsSelected() && !isModeSelectionExpanded.getValue() && !isTimeSelectionExpanded.getValue()){
                    mCardSelectionReview.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(400)
                            .setStartDelay(400)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    mCardSelectionReview.setVisibility(View.VISIBLE);
                                }
                            });
                    mTextViewSelectedMode.setText(mModeSelectionObject.getmModeName());
                    mTextViewSelectedTime.setText(mTimeSelectionObject.getTime());
                    Log.i(TAG, "Visibility ON  " );
                    if(!buttonConfirm.isEnabled()){
                        enableButton(buttonConfirm);
                    }
                }else{
                    mCardSelectionReview.setVisibility(View.GONE);
                    mCardSelectionReview.setScaleX(0f);
                    mCardSelectionReview.setScaleY(0f);
                    Log.i(TAG, "Visibility OFF  " );
                    if(buttonConfirm.isEnabled()){
                        disableButton(buttonConfirm);
                    }
                }
            }
        });
    }

    // Checking if Mode and Time has been selected.
    private boolean areSchedulingItemsSelected(){
        if(mModeSelectionObject != null && mTimeSelectionObject != null){
            return true;
        }
        return false;
    }
}
