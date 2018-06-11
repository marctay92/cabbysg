package com.example.android.cabbysg;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;

    public arrayAdapter (Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Log.d(TAG,"Calling getView from arrayAdapter" + position );
        cards card_item = getItem(position);

        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);
        }
        TextView location = convertView.findViewById(R.id.location);
        location.setText(card_item.getLocation());
        Log.d(TAG,"Getting location: "+card_item.getLocation());
        TextView destination = convertView.findViewById(R.id.destination);
        destination.setText(card_item.getDestination());

        TextView fare = convertView.findViewById(R.id.fare);
        fare.setText(card_item.getFare());

        TextView routeSelected = convertView.findViewById(R.id.routeSelected);
        routeSelected.setText(card_item.getSelectedRoute());

        TextView fareType = convertView.findViewById(R.id.fareType);
        fareType.setText(card_item.getFareType());

        TextView dateTime = convertView.findViewById(R.id.dateTime);
        dateTime.setText(card_item.getDateTime());

        return convertView;
    }
}
