package com.example.android.cabbysg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class spinner_history extends ArrayAdapter<String>
{
    Context context;
    String[] names;
    int[] images;

    public spinner_history(Context context, String[] names, int[] images)
    {
        super(context, R.layout.activity_spinner_history, names);
        this.context = context;
        this.names = names;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_spinner_history, null);
        TextView t1 = (TextView) row.findViewById(R.id.textView);
        ImageView i1 = (ImageView) row.findViewById(R.id.imageView);

        t1.setText(names[position]);
        i1.setImageResource(images[position]);

        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_spinner_history, null);
        TextView t1 = (TextView) row.findViewById(R.id.textView);
        ImageView i1 = (ImageView) row.findViewById(R.id.imageView);

        t1.setText(names[position]);
        i1.setImageResource(images[position]);

        return row;

    }
}