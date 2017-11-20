package stepboom.familycare.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import stepboom.familycare.R;
import stepboom.familycare.view.state.BundleSavedState;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SearchBluetoothItem extends BaseCustomViewGroup {

    private TextView name;
    private TextView mac;


    public SearchBluetoothItem(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public SearchBluetoothItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public SearchBluetoothItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public SearchBluetoothItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.search_bluetooth_item, this);
    }

    private void initInstances() {
        // findViewById here
        name = findViewById(R.id.search_bluetooth_name);
        mac = findViewById(R.id.search_bluetooth_mac);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    public void setName(String n){
        if(n.equals("No Result"))
            name.setTextColor(getResources().getColor(R.color.grey));
        name.setText(n);
    }

    public void setMac(String m){
        if(m != null) {
            String text = getResources().getText(R.string.scan_activity_mac_adr) + " " + m;
            mac.setText(text);
        } else mac.setText("");
    }

    public String getName(){
        return name.getText().toString();
    }

    public String getMac(){
        return mac.getText().toString();
    }

}
