package com.example.electrices.utilities;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.example.electrices.R;

import java.lang.reflect.Field;

/**
 * Used to stylize the DatePicker dialog.
 * Used mostly for HOLO themes that provide a spinner layout.
 * - AlertDialog.THEME_HOLO_LIGHT
 * - AlertDialog.THEME_HOLO_DARK
 *
 * Tutorial: https://stackoverflow.com/questions/51545752/android-change-lines-color-from-datepickerdialog
 **/

public class DatePickerStyler {

    private static final String TAG = "DatePickerStyler";

    public static void colorizeDatePicker(DatePicker datePicker) {
        Resources system = Resources.getSystem();
        int dayId = system.getIdentifier("day", "id", "android");
        int monthId = system.getIdentifier("month", "id", "android");
        int yearId = system.getIdentifier("year", "id", "android");

        NumberPicker dayPicker = datePicker.findViewById(dayId);
        NumberPicker monthPicker = datePicker.findViewById(monthId);
        NumberPicker yearPicker = datePicker.findViewById(yearId);

        setDividerColor(dayPicker);
        setDividerColor(monthPicker);
        setDividerColor(yearPicker);
    }

    private static void setDividerColor(NumberPicker picker) {
        Log.i(TAG, " => " + String.valueOf(picker));
        if (picker == null)
            return;

        final int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(picker.getResources().getColor(R.color.pgreen));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (Exception e) {
                Log.w("setDividerColor", e);
            }
        }

    }

}
