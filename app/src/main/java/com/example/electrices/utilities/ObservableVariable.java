package com.example.electrices.utilities;

/**
 * Solution Found in: https://stackoverflow.com/questions/14457711/android-listening-for-variable-changes/39008281
 *
 * This class represents variables of every type, for which we can monitor if their values have been changed.
 * */

public class ObservableVariable<T> {

    public interface OnChangeListener{
        void onChange();
    }

    private OnChangeListener listener = null;

    public void setOnChangeListener(OnChangeListener listener){
        this.listener = listener;
    }

    public OnChangeListener getListener(){
        return this.listener;
    }
//########### End of Interface #############################

    private T value;
    private T oldValue = null;

    public void setValue(T value){
        this.value = value;
        if(isValueChanged()){
            if(listener != null){
                listener.onChange();
            }
        }
        oldValue = this.value;
    }

    private boolean isValueChanged(){
        if(this.value != oldValue){
            return true;
        }
        return false;
    }

    public T getValue(){
        return value;
    }
}
