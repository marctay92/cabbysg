package com.example.android.cabbysg;


        import android.app.AlertDialog;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.ArrayList;

public class driverschedule_adaptor extends ArrayAdapter<driverschedule_details> {
    public driverschedule_adaptor(Context context, ArrayList<driverschedule_details> users){
        super(context,0,users);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        driverschedule_details user = getItem(position);

        if (convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.driverschedule_template,parent,false);
        }

        TextView ds_date = (TextView) convertView.findViewById(R.id.ds_date);
        TextView ds_time = (TextView) convertView.findViewById(R.id.ds_time);
        TextView ds_name = (TextView) convertView.findViewById(R.id.ds_name);
        TextView ds_rating = (TextView) convertView.findViewById(R.id.ds_rating);
        TextView ds_destination = (TextView) convertView.findViewById(R.id.ds_destination);
        TextView ds_location = (TextView) convertView.findViewById(R.id.ds_location);
        TextView ds_type = (TextView) convertView.findViewById(R.id.ds_type);
        TextView ds_price = (TextView) convertView.findViewById(R.id.ds_price);
        TextView ds_note = (TextView) convertView.findViewById(R.id.ds_note);
        ImageView ds_cancel=(ImageView)convertView.findViewById(R.id.ds_cancel);



        ds_date.setText(user.driverschedule_date);
        ds_time.setText(user.driverschedule_time);
        ds_name.setText(user.driverschedule_name);
        ds_rating.setText(user.driverschedule_rating);
        ds_destination.setText(user.driverschedule_destination);
        ds_location.setText(user.driverschedule_location);
        ds_type.setText(user.driverschedule_type);
        ds_price.setText(user.driverschedule_price);
        ds_note.setText(user.driverschedule_notes);

        ds_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   int position = (Integer) view.getTag();
                //  schedule_details user = getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm Cancel Booking?");
                builder.setMessage("Booking will be cancelled");

                /*MARCUS SEE THIS*/                    builder.setPositiveButton("Continue", null/*set to delete from database*/);
                builder.setNegativeButton("Exit",null);

                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });

        return convertView;

    }

}
