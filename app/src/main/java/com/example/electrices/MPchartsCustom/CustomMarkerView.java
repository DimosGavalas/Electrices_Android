package com.example.electrices.MPchartsCustom;

import android.content.Context;
import android.widget.TextView;

import com.example.electrices.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {

    static final String[] timeOfDayLabels = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00",
            "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private TextView value;
    private TextView description;
    private MPPointF mOffset;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        value = findViewById(R.id.marker_value);
        description = findViewById(R.id.marker_description);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        value.setText(String.format("%s", e.getY()));
        description.setText(String.format("%s", timeOfDayLabels[(int) e.getX()]));

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
//        return super.getOffset();
        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2f), -(1.05f * getHeight()));
        }
        return mOffset;
    }
}
