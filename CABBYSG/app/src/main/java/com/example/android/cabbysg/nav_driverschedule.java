package com.example.android.cabbysg;


        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v4.app.Fragment;
        import android.text.format.DateFormat;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ListView;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_driverschedule extends Fragment {

    FirebaseUser user;
    String userId;
    String startLocationStr, riderIdStr,destinationStr,selectedRouteStr,fareStr,scheduleID;
    ArrayList<driverschedule_details>arrayOfSchedule= new ArrayList<>();
    ListView listView;
    driverschedule_adaptor adapter;

    public nav_driverschedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nav_driverschedule, container, false);

//MARCUS CHANGE TO DATABASE

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        listView = rootView.findViewById(R.id.listOfDetails);

        adapter = new driverschedule_adaptor(getActivity(), getDataSetHistory());

        listView = rootView.findViewById(R.id.driverschedulelist);
        listView.setAdapter(adapter);

        getUserScheduledIds();

        return rootView;
    }
    private void getUserScheduledIds(){
        DatabaseReference userScheduleDatabase = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userId).child("scheduledRides");
        userScheduleDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot scheduledRide : dataSnapshot.getChildren()){
                        scheduleID = scheduledRide.getKey();
                        FetchScheduledInfo(scheduledRide.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void FetchScheduledInfo(String rideKey){
        DatabaseReference scheduledRidesDatabase = FirebaseDatabase.getInstance().getReference().child("scheduledRides").child(rideKey);
        arrayOfSchedule.clear();
        scheduledRidesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long timestamp = 0L;
                if(dataSnapshot.exists()){
                    for(DataSnapshot child: dataSnapshot.getChildren()) {
                        if (child.getKey().equals("riderID")) {
                            riderIdStr = dataSnapshot.child("riderID").getValue().toString();
                        }
                        if (child.getKey().equals("startLocation")) {
                            startLocationStr = dataSnapshot.child("startLocation").getValue().toString();
                        }
                        if (child.getKey().equals("destination")) {
                            destinationStr = dataSnapshot.child("destination").getValue().toString();
                        }
                        if (child.getKey().equals("selectedRoute")) {
                            selectedRouteStr = dataSnapshot.child("selectedRoute").getValue().toString();
                        }

                        if (child.getKey().equals("fare")) {
                            fareStr = dataSnapshot.child("fare").getValue().toString();
                        }
                        if (child.getKey().equals("timestamp")) {
                            timestamp = Long.valueOf(dataSnapshot.child("timestamp").getValue().toString());
                        }
                    }
                    driverschedule_details obj = new driverschedule_details(scheduleID,getDate(timestamp),getTime(timestamp), riderIdStr,destinationStr, startLocationStr,selectedRouteStr,fareStr);
                    arrayOfSchedule.add(obj);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private ArrayList<driverschedule_details> getDataSetHistory(){
        return arrayOfSchedule;
    }

    private String getDate(Long timestamp){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String date = DateFormat.format("dd-MM-yyyy",cal).toString();
        return date;
    }
    private String getTime(Long timestamp){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String time = DateFormat.format("hh:mm",cal).toString();
        return time;
    }
}
