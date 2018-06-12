package com.example.android.cabbysg;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
//import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.text.format.DateFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class nav_driverhome extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private static final String TAG = "Driver Home Fragment";
    private GoogleMap mMap;
    private Boolean mLocationPermissionsGranted = false;
    private Boolean scheduledRide = false;

    //PLAY_SERVICES
    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    private String MAP_FRAGMENT = "map_fragment";

    private EditText mFinalFare, mTollFees;
    private LinearLayout mLinearLayout1, mLinearLayout2, mLinearLayout3, mLinearLayout4, mLinearLayout5;
    private TextView mCustomerLocation, mRiderName, mDestination, mFare, fLocation, fDestination, fRiderName, fFare,
            mTotalFare, mEndTripDateTime, mStartingLocation, mEndingDestination, mTotalTripFare, mRatingRiderName,
            mNoOfTrips, mRating, mCancellation;
    private Long timestamp;
    private Button mArrivedButton, mStartTripButton, mEndTripButton, mConfirmReceipt, mSubmit;
    private ImageView mCallDriver, mTextDriver, mCancelRequest, mPaymentMethodImageView, mRatingRiderImage;
    private CircleImageView mRiderPhoto, mRiderPhoto1;
    private LatLng customerLatLng, destinationLatLng;
    private Marker mCurrent;
    private String customerId = "";
    private String riderPhotoUrl = "";
    private Boolean onTrip = false, routingToCustomer = false;
    private String destination, location, fare, selectedRoute, phoneNo, fareType, dateTime, endTripDateTime;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Switch location_switch;
    private float riderRating;
    private RatingBar mRatingBar;

    //arrayAdapter
    private cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;
    private SwipeFlingAdapterView flingContainer;
    ListView listView;
    List<cards> rowItems;

    public nav_driverhome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();

        getLocationPermission();

        final View v =  inflater.inflate(R.layout.fragment_nav_driverhome, container, false);

        location_switch = v.findViewById(R.id.location_switch);
        mCustomerLocation = v.findViewById(R.id.reqLocation);
        mDestination = v.findViewById(R.id.reqDestination);
        mFare = v.findViewById(R.id.reqFare);
        mRiderName = v.findViewById(R.id.riderName);
        mLinearLayout1 = v.findViewById(R.id.linearLayout1);
        mLinearLayout2 = v.findViewById(R.id.linearLayout2);
        mLinearLayout3 = v.findViewById(R.id.linearLayout3);
        mArrivedButton = v.findViewById(R.id.arrivedBtn);
        mStartTripButton = v.findViewById(R.id.startTripBtn);
        mCallDriver = v.findViewById(R.id.callDriver);
        mTextDriver = v.findViewById(R.id.textDriver);
        fLocation = v.findViewById(R.id.custLocation);
        fDestination = v.findViewById(R.id.custDestination);
        fRiderName = v.findViewById(R.id.custName);
        fFare = v.findViewById(R.id.custFare);
        mRiderPhoto = v.findViewById(R.id.riderReqPhoto);
        mRiderPhoto1 = v.findViewById(R.id.riderPhoto);
        mCancelRequest = v.findViewById(R.id.cancelRequest);
        mEndTripButton = v.findViewById(R.id.endTripBtn);
        mEndTripDateTime = v.findViewById(R.id.tripDateTime);
        mLinearLayout4 = v.findViewById(R.id.linearlayout4);
        mLinearLayout5 = v.findViewById(R.id.linearLayout5);
        mFinalFare = v.findViewById(R.id.finalFare);
        mTollFees = v.findViewById(R.id.tollFees);
        mTotalFare = v.findViewById(R.id.totalFare);
        mConfirmReceipt = v.findViewById(R.id.confirmReceipt);
        mPaymentMethodImageView = v.findViewById(R.id.paymentMethodImageView);
        mRatingRiderImage = v.findViewById(R.id.ratingRiderImage);
        mStartingLocation = v.findViewById(R.id.startingLocation);
        mEndingDestination = v.findViewById(R.id.endingDestination);
        mTotalTripFare = v.findViewById(R.id.totalTripFare);
        mRatingRiderName = v.findViewById(R.id.ratingRiderName);
        mRatingBar = v.findViewById(R.id.ratingBar);
        mSubmit = v.findViewById(R.id.submitRating);
        mNoOfTrips = v.findViewById(R.id.noOfTrips);
        mRating = v.findViewById(R.id.rating);
        mCancellation = v.findViewById(R.id.cancellation);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                riderRating = rating;
            }
        });

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(getActivity(), R.layout.items, rowItems);
        flingContainer = (SwipeFlingAdapterView) v.findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.bringToFront();
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                assignedCustomerRef.removeValue();
                driverAccepted=false;
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d(TAG,"scheduled ride: "+scheduledRide);
                if (!scheduledRide) {
                    reqRef.child("driverFound").setValue("true");
                    driverAccepted = true;
                    if(getActivity()!= null) {
                        Toast.makeText(getActivity(), "Rider Found!", Toast.LENGTH_SHORT).show();
                    }
                    getAssignedCustomerPickupLocation();
                    changeDriverStatus();

                    if(getActivity()!= null){
                        Toasty.success(getActivity(), "You've accepted the ride! Routing to your rider now..", Toast.LENGTH_SHORT, true).show();
                    }
                }else{
                    //do scheduled ride
                    DatabaseReference scheduledRideRef = FirebaseDatabase.getInstance().getReference().child("scheduledRides");
                    String scheduledRideID = scheduledRideRef.push().getKey();
                    HashMap map = new HashMap();
                    map.put("startLocation", location);
                    map.put("destination", destination);
                    map.put("driverID", userID);
                    map.put("riderID", customerId);
                    map.put("fare", fare);
                    map.put("selectedRoute", selectedRoute);
                    map.put("timestamp", dateTime);
                    scheduledRideRef.child(scheduledRideID).updateChildren(map);

                    DatabaseReference riderScheduledRef = FirebaseDatabase.getInstance().getReference().child("Rider").child(customerId);
                    DatabaseReference driverScheduledRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID);
                    riderScheduledRef.child("scheduledRides").child(scheduledRideID).setValue(true);
                    driverScheduledRef.child("scheduledRides").child(scheduledRideID).setValue(true);
                    endTrip();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                return;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateResult();
            }
        };

        mFinalFare.addTextChangedListener(textWatcher);
        mTollFees.addTextChangedListener(textWatcher);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        createLocationRequest();

        return v;
    }

    private void calculateResult() {
        Editable editableText1 = mFinalFare.getText(),
                editableText2 = mTollFees.getText();

        double  value1 = 0.0, value2 = 0.0, result;

        if (editableText1 != null && editableText1.length() >= 1)
            value1 = Double.parseDouble(editableText1.toString());

        if (editableText2 != null && editableText2.length() >= 1)
            value2 = Double.parseDouble(editableText2.toString());

        // Whatever your magic formula is
        result = value1 + value2;

        mTotalFare.setText(String.format("%1.2f", result));
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        MY_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    MY_PERMISSION_REQUEST_CODE);
        }
    }


    private void createLocationRequest() {
        Log.d(TAG,"createLocationRequest: Creating location request");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void displayLocation() {
        if (getActivity()!=null){
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            Log.d("Location: ",mLastLocation.toString());
            if (location_switch.isChecked()){
                final double latitude = mLastLocation.getLatitude();
                final double longitude = mLastLocation.getLongitude();

                if (mCurrent != null)
                    mCurrent.remove();
                if (!onTrip) {
                    mCurrent = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("You"));
                    //Move Camera
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                    }
                changeDriverStatus();
                }

            } else {
            Log.d("ERROR","Cannot get your location");
        }

    }

    private void changeDriverStatus() {
        String userId = userID;
        //FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference availableRef = FirebaseDatabase.getInstance().getReference("driversAvailable");
        DatabaseReference workingRef = FirebaseDatabase.getInstance().getReference("driversWorking");

        GeoFire geoFireAvailable = new GeoFire(availableRef);
        GeoFire geoFireWorking = new GeoFire(workingRef);

        if(customerId!=""){
            geoFireAvailable.removeLocation(userId);
            geoFireWorking.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
        } else {
            geoFireWorking.removeLocation(userId);
            geoFireAvailable.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
        }
    }

    private void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userID);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();

        if(driverAccepted){
            getDirections(customerLatLng);
            mMap.addMarker(new MarkerOptions().position(customerLatLng));
        }
        if (driverAccepted&&onTrip){
            getDirections(destinationLatLng);
            mMap.addMarker(new MarkerOptions().position(destinationLatLng));
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,"MAP IS READY");
        if(getActivity()!= null){
            Toast.makeText(getActivity(), "Map is ready!", Toast.LENGTH_SHORT).show();
        }
        mMap = googleMap;
        if (mLocationPermissionsGranted) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            init();
        }

    }
    DatabaseReference historyRef;
    ValueEventListener historyRefListener;
    double count = 0.0;
    double noOfTrip = 0.0;
    private void init() {
        Log.d(TAG,"init: Initialing!");

        DatabaseReference initialRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID);
        initialRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Double douRating = Double.parseDouble(dataSnapshot.child("rating").getValue().toString());
                    DecimalFormat df = new DecimalFormat("#.00");
                    String ratingFormatted = df.format(douRating);
                    mRating.setText(ratingFormatted);
                    for (DataSnapshot snap: dataSnapshot.child("History").getChildren()){
                        Log.d(TAG,"No of Trips: "+count);
                        long child = snap.getChildrenCount();
                        count = count + child;
                        mNoOfTrips.setText(Double.toString(count));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         DatabaseReference cancellationRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID);
         cancellationRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){
                     for (DataSnapshot snap: dataSnapshot.child("History").getChildren()){
                         long child = snap.getChildrenCount();
                         noOfTrip = noOfTrip+ child;
                     }
                     if (dataSnapshot.child("cancellation").getValue().toString()!=null){
                         DecimalFormat df = new DecimalFormat("#.00");
                         Double cancellationNum = Double.parseDouble(dataSnapshot.child("cancellation").getValue().toString());
                         Double cancellationRate = (cancellationNum/(cancellationNum+noOfTrip))*100;
                         String cancellationRateFormatted = df.format(cancellationRate);
                         mCancellation.setText(cancellationRateFormatted+"%");
                     } else{
                         mCancellation.setText("0%");
                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

        location_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    startLocationUpdates();
                    displayLocation();
                    if(getActivity()!=null){
                        Toast.makeText(getActivity(), "You are online!", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    stopLocationUpdates();
                    mCurrent.remove();
                    if(getActivity()!=null){
                        Toast.makeText(getActivity(), "You are offline!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        mArrivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Location loc1 = new Location("");
                loc1.setLatitude(mLastLocation.getLatitude());
                loc1.setLongitude(mLastLocation.getLongitude());

                Location loc2 = new Location("");
                loc2.setLatitude(customerLatLng.latitude);
                loc2.setLongitude(customerLatLng.longitude);

                float distance = loc1.distanceTo(loc2);
                 if (distance<5000){
                     DatabaseReference arrivalRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("Details").child("ongoingTrip");
                     arrivalRef.setValue("true");
                     if (getActivity()!=null){
                         Toast toast = Toast.makeText(getActivity(),"Informing your Rider!", Toast.LENGTH_LONG);
                         toast.setGravity(Gravity.CENTER, 0, 0);
                         toast.show();
                     }
                 } else if(getActivity()!= null) {
                         Toast.makeText(getActivity(), "You are too far from the pick up location! Please drive nearer in order to indicate arrival.", Toast.LENGTH_SHORT).show();

                 }
            }
        });
        mStartTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrip = true;
                if(getActivity()!= null) {
                    //Toasty.success(getActivity(), "Trip has begun!", Toast.LENGTH_LONG, true).show();
                }
                DatabaseReference setOngoingTripRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("Details").child("ongoingTrip");
                setOngoingTripRef.setValue("started");
                mLinearLayout2.setVisibility(View.INVISIBLE);
                mLinearLayout3.setVisibility(View.VISIBLE);
                fLocation.setText(mCustomerLocation.getText());
                fDestination.setText(mDestination.getText());
                fRiderName.setText(mRiderName.getText());
                fFare.setText(mFare.getText());
                if(riderPhotoUrl!=null){
                    Glide.with(getActivity()).load(riderPhotoUrl).into(mRiderPhoto1);
                }
                getDirections(destinationLatLng);

            }
        });

        mCallDriver.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Rider").child(customerId).child("mobileNum");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        phoneNo = dataSnapshot.getValue().toString();
                        String callRiderStr = String.format("tel: %s",phoneNo);
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse(callRiderStr));
                        if (dialIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(dialIntent);
                        } else {
                            Log.e(TAG, "Can't resolve app for ACTION_DIAL Intent.");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    });
        mTextDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Rider").child(customerId).child("mobileNum");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            phoneNo = dataSnapshot.getValue().toString();
                            String smsNumber = String.format("smsto: %s", phoneNo);
                            Intent dialIntent = new Intent(Intent.ACTION_SENDTO);
                            dialIntent.setData(Uri.parse(smsNumber));
                            if (dialIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(dialIntent);
                            } else {
                                Log.e(TAG, "Can't resolve app for ACTION_SENDTO Intent.");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        mEndTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayout3.setVisibility(View.INVISIBLE);
                mLinearLayout4.setVisibility(View.VISIBLE);
                if(fareType.equals("Fare")){
                    mFinalFare.setFocusable(false);
                    Log.d(TAG,"fare is equal to: "+fare);
                    String finalFare = fare.replaceAll("\\$","");
                    Log.d(TAG,"fare is equal to: after regex "+finalFare);
                    mFinalFare.setText(finalFare);
                    timestamp = System.currentTimeMillis()/1000;
                    mEndTripDateTime.setText(getDate(timestamp));
                }
            }
        });

        mConfirmReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tollFees = mTollFees.getText().toString();
                String finalFare = mTotalFare.getText().toString();
                DatabaseReference endTripRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("Details");
                endTripRef.child("ongoingTrip").setValue("ended");
                endTripRef.child("fare").setValue(finalFare);
                endTripRef.child("tollFees").setValue(tollFees);
                //display rating screen
                getRating();

            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordTrip();
                endTrip();
            }
        });
        mCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                historyRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID).child("cancellation");
                historyRefListener = historyRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if (dataSnapshot.getValue().toString()!=null){
                                Double cancelledTrips = Double.parseDouble(dataSnapshot.getValue().toString());
                                Double newCancelledTrips = cancelledTrips+1;
                                historyRef.setValue(newCancelledTrips);
                                historyRef.removeEventListener(this);
                                endTrip();
                            }
                        } else{
                            historyRef.setValue("1");
                            historyRef.removeEventListener(this);
                            endTrip();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        DatabaseReference noOfTripsRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID).child("History");
        noOfTripsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long noOfTrips = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Long snap = ds.getChildrenCount();
                        noOfTrips = noOfTrips + snap;
                        mNoOfTrips.setText(Long.toString(noOfTrips));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getAssignedCustomer();
    }

    private void getRating() {
        mLinearLayout4.setVisibility(View.GONE);
        mLinearLayout5.setVisibility(View.VISIBLE);
        mStartingLocation.setText(fLocation.getText());
        mEndingDestination.setText(fDestination.getText());
        mTotalTripFare.setText(mTotalFare.getText());
        mRatingRiderName.setText(mRiderName.getText());
        Glide.with(getActivity()).load(riderPhotoUrl).into(mRatingRiderImage);
    }

    private String getDate(Long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }


    private DatabaseReference assignedCustomerRef;
    private ValueEventListener assignedCustomerRefListener;
    private Boolean driverAccepted = false;
    private DatabaseReference reqRef;
    private void getAssignedCustomer() {
        String driverId = userID;
        //FirebaseAuth.getInstance().getCurrentUser().getUid();
        assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(driverId).child("customerRiderId");

        assignedCustomerRefListener = assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   customerId = dataSnapshot.getValue().toString();
                   Log.d(TAG,"Customer Rider ID: "+customerId);
                   reqRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("Details");
                   reqRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                           if (dataSnapshot.exists()) {
                               if (dataSnapshot.child("driverFound").getValue().equals("false") && !driverAccepted) {
                                   Log.d(TAG, "driverFound: " + dataSnapshot.child("driverFound").getValue() + " " + driverAccepted);
                                   location = dataSnapshot.child("currentLocation").getValue().toString();
                                   destination = dataSnapshot.child("destination").getValue().toString();
                                   fare = dataSnapshot.child("fare").getValue().toString();
                                   selectedRoute = dataSnapshot.child("selectedRoute").getValue().toString();
                                   fareType = dataSnapshot.child("fareType").getValue().toString();

                                   if (dataSnapshot.child("timestamp").getValue().toString().equals("now")) {
                                       if (dataSnapshot.child("timestamp").getValue().toString().equals("")) {
                                           dateTime = "Now";
                                       } else {
                                           dateTime = dataSnapshot.child("timestamp").getValue().toString();
                                           scheduledRide = true;
                                       }
                                       Double destinationLat = Double.parseDouble(dataSnapshot.child("destinationLat").getValue().toString());
                                       Double destinationLng = Double.parseDouble(dataSnapshot.child("destinationLng").getValue().toString());
                                       destinationLatLng = new LatLng(destinationLat, destinationLng);

                                       cards item = new cards(location, destination, fare, selectedRoute, fareType, dateTime);
                                       if (rowItems.size() == 0) {
                                           rowItems.add(item);
                                           arrayAdapter.notifyDataSetChanged();
                                       }
                                       Log.d(TAG, "Row item size: " + rowItems.size());

                                   }
                               }
                           }
                       }


                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    Marker pickUpMarker;
    private DatabaseReference assignedCustomerPickupLocationRef, workingRef;
    private ValueEventListener assignedCustomerPickupLocationRefListener;
    private void getAssignedCustomerPickupLocation() {
        Log.d(TAG,"ASSIGNEDCUSTOMERPICKUPLOCATION RUNNING");
        assignedCustomerPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("l");
        assignedCustomerPickupLocationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"ASSIGNEDCUSTOMERPICKUPLOCATION customerrequest latlng "+dataSnapshot.getValue());
                if (dataSnapshot.exists() && !customerId.equals("")) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double custLat = 0;
                    double custLng = 0;
                    if (map.get(0) != null) {
                        custLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        custLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng custLatLng = new LatLng(custLat, custLng);
                    customerLatLng = custLatLng;
                    pickUpMarker = mMap.addMarker(new MarkerOptions().position(custLatLng).title("Pickup Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(custLatLng, 15.0f));

                    getAssignedCustomerDetails();
                    getDirections(custLatLng);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getAssignedCustomerDetails() {
        mLinearLayout1.setVisibility(View.INVISIBLE);
        mLinearLayout2.setVisibility(View.VISIBLE);
        mCustomerLocation.setText(location);
        mDestination.setText(destination);
        mFare.setText(fare);

        DatabaseReference customerDetailsRef = FirebaseDatabase.getInstance().getReference().child("Rider").child(customerId);
        customerDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String custFirstName = dataSnapshot.child("firstName").getValue().toString();
                    String custLastName = dataSnapshot.child("lastName").getValue().toString();
                    mRiderName.setText(custFirstName+" "+custLastName);
                    if(dataSnapshot.child("profileImageUrl")!=null){
                        riderPhotoUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        Glide.with(getActivity()).load(riderPhotoUrl).into(mRiderPhoto);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference ongoingRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("Details");
        ongoingRef.child("ongoingTrip").setValue("false");
    }

    private String requestDirections(String reqUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(reqUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private void getDirections(LatLng custLatLng) {
        LatLng locLatLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());

        String url = getRequestUrl(locLatLng, custLatLng);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirections(strings[0]);
            } catch (IOException e) {
                Log.d("Background Task", e.toString());
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here

            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);

        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsJSONParser directionsJSONParser = new DirectionsJSONParser();
                routes = directionsJSONParser.parse(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            String distance = "";
            String duration = "";
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            int count = 0;
            int shortestRouteDis = 0;
            int shortestRouteDura = 0;
            Float shortestDis, shortestDura;

            // use for loop to run through List/List/HashMap to compare distance and duration
            for (int i = 1; i <= lists.size() - 1; i++) {
                //run through all available routes to compare distance and duration
                List<HashMap<String, String>> test = lists.get(count);
                List<HashMap<String, String>> test1 = lists.get(i);

                String firstDist = (test.get(0).get("distance")).replaceAll("km", "");
                String secondDist = (test1.get(0).get("distance")).replaceAll("km", "");
                String firstDura = (test.get(1).get("duration")).replaceAll("mins", "");
                String secondDura = (test1.get(1).get("duration")).replaceAll("mins", "");
                Log.d(TAG, "Route 1 Distance: " + firstDist + " Route 2 Distance: " + secondDist);
                Float testDou = Float.parseFloat(firstDist);
                Float test1Dou = Float.parseFloat(secondDist);
                Float testDura = Float.parseFloat(firstDura);
                Float test1Dura = Float.parseFloat(secondDura);
                Log.d(TAG, "Route 1 Duration: " + testDura + " Route 2 Duration: " + test1Dura);
                if (testDou > test1Dou) {
                    shortestDis = test1Dou;
                    shortestRouteDis = count;
                } else {
                    shortestDis = testDou;
                }
                if (testDura > test1Dura) {
                    shortestDura = test1Dura;
                    shortestRouteDura = count;
                } else {
                    shortestDura = testDura;
                }
                Log.d(TAG, "Shortest route and Distance: " + shortestRouteDis + " " + shortestDis);
                count++;
                Log.d(TAG, "Shortest route and duration: " + shortestRouteDura + " " + shortestDura);
            }


            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();
                List<HashMap<String, String>> path;

                if (selectedRoute.equals("Shortest")) {
                    path = lists.get(shortestRouteDis);

                } else if (selectedRoute.equals("Fastest")) {
                    path = lists.get(shortestRouteDura);
                } else {
                    path = lists.get(i);
                }
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) { // Get distance from the list
                        Log.d(TAG, "Distance [0]: " + path.get(0).get("distance"));
                        distance = point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        Log.d(TAG, "Duration [1]: " + path.get(1).get("duration"));
                        duration = point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                    boundsBuilder.include(position);
                }

                polylineOptions.addAll(points);
                polylineOptions.width(12);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                mMap.clear();
                mMap.addPolyline(polylineOptions);
                if (driverAccepted){
                    addMarker(customerLatLng, mLastLocation);
                }
                if (driverAccepted&&onTrip){
                    addMarker(destinationLatLng, mLastLocation);
                }
                LatLngBounds routeBounds = boundsBuilder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 12));

            } else {
                Log.e(TAG,"Directions not found!");
                //Toast.makeText(getActivity(), "Directions not found!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void addMarker(LatLng customerLatLng, Location mLastLocation) {
        LatLng currentLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        MarkerOptions locMarker = new MarkerOptions()
                .position(customerLatLng);
        mMap.addMarker(new MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.working_taxi)));
        mMap.addMarker(locMarker);

    }

    private String getRequestUrl(LatLng origin, LatLng destination) {
        //value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        //Value of destination
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode-driving";
        //Alternate routes
        String alternatives = "alternatives=true";
        //Build full param
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode + "&" + alternatives;
        //Output format
        String output = "json";

        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void endTrip() {
        Log.d(TAG,"END TRIP TRIGGERED DAMMIT");
        //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        onTrip = false;
        driverAccepted = false;

        if (customerId!= null){
            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID);
            driverRef.child("customerRiderId").removeValue();
        }
        //connect to DB to check if ride is scheduled

        //code if ride is NOT scheduled
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
        ref.child(customerId).removeValue();
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(customerId);
        customerId = "";
        //code if ride is scheduled
        mRatingBar.setRating(0f);
        mMap.clear();
        if(mLinearLayout4.isShown()){
            mLinearLayout4.setVisibility(View.INVISIBLE);
        } else if(mLinearLayout3.isShown()){
            mLinearLayout3.setVisibility(View.INVISIBLE);
        } else if(mLinearLayout2.isShown()){
            mLinearLayout2.setVisibility(View.INVISIBLE);
        } else if (mLinearLayout5.isShown()){
            mLinearLayout5.setVisibility(View.INVISIBLE);
        }
        mLinearLayout1.setVisibility(View.VISIBLE);

        if (assignedCustomerPickupLocationRefListener!=null){
            assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefListener);
        }
        if(assignedCustomerRefListener!=null){
            assignedCustomerRef.removeEventListener(assignedCustomerRefListener);
        }
        getAssignedCustomer();
        displayLocation();
    }

    private Long getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis()/1000;
        return timestamp;
    }
    DatabaseReference riderRatingRef;
    private void recordTrip() {
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthInt = cal.get(Calendar.MONTH);
        String month = Integer.toString(monthInt);

        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID).child("History");
        DatabaseReference riderRef = FirebaseDatabase.getInstance().getReference().child("Rider").child(customerId).child("History");
        final DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("History");
        String historyId = historyRef.push().getKey();
        driverRef.child(month).child(historyId).child("fare").setValue(mTotalFare.getText());
        riderRef.child(historyId).setValue(true);

        HashMap map = new HashMap();
        map.put("driverID", userID);
        map.put("riderID", customerId);
        map.put("startLocation", location);
        map.put("destination", destination);
        map.put("fare", mTotalFare.getText());
        map.put("selectedRoute", selectedRoute);
        map.put("timestamp", timestamp);
        historyRef.child(historyId).updateChildren(map);

        riderRatingRef = FirebaseDatabase.getInstance().getReference().child("Rider").child(customerId);
        riderRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    long count = dataSnapshot.child("History").getChildrenCount();
                    float previousRating = Float.parseFloat(dataSnapshot.child("rating").getValue().toString());
                    float finalRating = ((previousRating*count)+riderRating)/count;
                    riderRatingRef.child("rating").setValue(Float.toString(finalRating));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
