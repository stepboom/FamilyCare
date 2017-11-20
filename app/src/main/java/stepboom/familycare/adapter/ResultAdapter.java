package stepboom.familycare.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import stepboom.familycare.R;
import stepboom.familycare.view.SearchBluetoothItem;

/**
 * Created by Stepboom on 11/11/2017.
 */

public class ResultAdapter extends BaseAdapter {

    private ArrayList<String> data;
    private boolean isDiscovering;

    public ResultAdapter(){
        data = new ArrayList<>();
        isDiscovering = false;
    }

    public void add(String name, String mac){
       data.add(name + "/" + mac);
    }

    @Override
    public int getCount() {
        if(isDiscovering)
            return data.size();
        else if(data.size()==0)
            return 1;
        else return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void clear(){
        data.clear();
    }

    public void setDiscovering(boolean isDiscovering){
        this.isDiscovering = isDiscovering;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SearchBluetoothItem searchBluetoothItem;
        if(view != null){
            searchBluetoothItem = (SearchBluetoothItem) view;
        } else{
            searchBluetoothItem = new SearchBluetoothItem(viewGroup.getContext());
        }
        if(!data.isEmpty()) {
            searchBluetoothItem.setName(data.get(i).substring(0, data.get(i).indexOf('/')));
            searchBluetoothItem.setMac(data.get(i).substring(data.get(i).indexOf('/') + 1));
        } else if(!isDiscovering) {
            searchBluetoothItem.setName(viewGroup.getContext().getResources().getString(R.string.scan_activity_no_result));
            searchBluetoothItem.setMac(null);
        }
        return searchBluetoothItem;
    }
}
