package com.example.android.cabbysg;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.example.android.cabbysg.models.PlaceInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */

public class nav_home extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

 private static final String TAG = "Rider Home";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    //widgets
    private AutoCompleteTextView mDestination, mCurrentLocation;
    private ImageView mGps, mSubmit;
    private Spinner mServiceType, mRouteOptions, mFareType;
    private TextView mDisTextView, mDuraTextView, mFareTextView;
    private static EditText mBookingTime;
    private String fare;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter, mPlaceAutocompleteAdapter1;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private int mLastSpinnerPosition;
    private LatLng locLatLng = null;
    private LatLng desLatLng = null;
    private ProgressBar mProgressBar;
    private LinearLayout mLinearLayout1, mLinearLayout2;
    private String userID = "ywO5uyDNM0eQ0aOpLSQD0qj9zxO2";

    public nav_home() {
        // Required empty public constructor
    }
 /*
       -------------------------------- Google Places API autocomplete suggestions--------------------
        */
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setId(place.getId());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setRating(place.getRating());
                mPlace.setWebsiteUri(place.getWebsiteUri());

                Log.d(TAG, "onResult: place " + mPlace.toString());
            } catch (NullPointerException e) {
                Log.e(TAG, "onResult: NullPointerException " + e.getMessage());
            }
            mMap.clear();
            mDisTextView.setText("");
            mDuraTextView.setText("");
            mFareTextView.setText("");
            if ((mDestination.getText().toString().length() > 0) && (mCurrentLocation.getText().toString().length() > 0)) {
                getDirections(mCurrentLocation, mDestination);
            } else {
                moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                        place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());
            }


            places.release();
        }
    };

    /*
   -------------------------------- Google Places API autocomplete suggestions--------------------
    */
    private AdapterView.OnItemClickListener mAutocompleteClickListener1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter1.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback1);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback1 = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setId(place.getId());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setRating(place.getRating());
                mPlace.setWebsiteUri(place.getWebsiteUri());

                Log.d(TAG, "onResult: place " + mPlace.toString());
            } catch (NullPointerException e) {
                Log.e(TAG, "onResult: NullPointerException " + e.getMessage());
            }
            mMap.clear();
            mFareTextView.setText("");
            mDisTextView.setText("");
            mDuraTextView.setText("");
            if ((mDestination.getText().toString().length() > 0) && (mCurrentLocation.getText().toString().length() > 0)) {
                getDirections(mCurrentLocation, mDestination);
            } else {
                moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                        place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());
            }
            places.release();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_nav_home, container, false);

        mDestination = v.findViewById(R.id.input_search1);
        mCurrentLocation = v.findViewById(R.id.input_search);


        mDisTextView = v.findViewById(R.id.disTextView);
        mDuraTextView = v.findViewById(R.id.duraTextView);
        mFareTextView = v.findViewById(R.id.fareTextView);

        mRouteOptions = v.findViewById(R.id.mRouteOptions);
        mFareType = v.findViewById(R.id.mFareType);
        mServiceType = v.findViewById(R.id.mServiceType);

        mBookingTime = v.findViewById(R.id.mBookingTime);

        mGps = v.findViewById(R.id.ic_gps);
        mSubmit = v.findViewById(R.id.submitBtn);
        mProgressBar = v.findViewById(R.id.progressBar);
        mLinearLayout1 = v.findViewById(R.id.linearLayout1);
        mLinearLayout2 = v.findViewById(R.id.linearLayout2);

        getLocationPermission();
        return v;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getActivity(), "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }

    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mDestination.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), Places.getGeoDataClient(getActivity(), null),
                LAT_LNG_BOUNDS, null);

        mDestination.setAdapter(mPlaceAutocompleteAdapter);

        mDestination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
        mCurrentLocation.setOnItemClickListener(mAutocompleteClickListener1);

        mPlaceAutocompleteAdapter1 = new PlaceAutocompleteAdapter(getActivity(), Places.getGeoDataClient(getActivity(), null),
                LAT_LNG_BOUNDS, null);

        mCurrentLocation.setAdapter(mPlaceAutocompleteAdapter1);

        mCurrentLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        mRouteOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mLastSpinnerPosition == position) {
                    return;
                } else {
                    getDirections(mCurrentLocation, mDestination);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //change fare type/amt
        mFareType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (mLastSpinnerPosition == position) {

                } else {
                    calculateFare(mDisTextView.getText().toString(), mDuraTextView.getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBookingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
                showDatePickerDialog(v);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customerRequest");
                DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(userID).child("Details");
                GeoFire geoFire = new GeoFire(ref);

                String destination = mDestination.getText().toString();
                String currentLocation = mCurrentLocation.getText().toString();
                String selectedRoute = mRouteOptions.getSelectedItem().toString();
                String serviceType = mServiceType.getSelectedItem().toString();
                String fare = mFareTextView.getText().toString();
                String originLatLng = "origin=" + locLatLng.latitude + "," + locLatLng.longitude;
                String destinationLatLng = "destination=" + desLatLng.latitude + "," + desLatLng.longitude;

                System.out.println("GeoFire Location LatLng " + locLatLng.toString());

                geoFire.setLocation(userID, new GeoLocation(locLatLng.latitude,locLatLng.longitude));

                Map newRequest = new HashMap();

                if ((destination.length() > 0) && (currentLocation.length() > 0)) {
                    newRequest.put("currentLocation",currentLocation);
                    newRequest.put("destination",destination);
                } else {
                    Toast.makeText(getActivity(), "Please enter a valid location and/or destination!", Toast.LENGTH_SHORT).show();
                }
                if (selectedRoute.equals("Shortest")) {
                    //save shortest option
                    newRequest.put("selectedRoute",selectedRoute);
                } else if (selectedRoute.equals("Fastest")) {
                    //save fastest
                    newRequest.put("selectedRoute",selectedRoute);
                } else {
                    //save avoid tolls
                    newRequest.put("selectedRoute",selectedRoute);
                }
                if (serviceType.equals("4-Seater")) {
                    //save 4-seater
                    newRequest.put("serviceType",serviceType);
                } else {
                    //save 6-seater
                    newRequest.put("serviceType",serviceType);
                }
                if (mBookingTime.toString().equals("Now")) {
                    //get current time and save

                } else {
                    //get selected time
                }
                newRequest.put("fare",fare);
                reqRef.setValue(newRequest);

                mProgressBar.setVisibility(View.VISIBLE);
                getClosestDriver();

            }
        });

        hideSoftKeyboard();
    }
    private int radius = 1;
    private boolean driverFound = false;
    private String driverFoundID;
    private void getClosestDriver() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
        GeoFire geoFire = new GeoFire(driverLocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(locLatLng.latitude, locLatLng.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {
                if (!driverFound) {
                    DatabaseReference driverFoundRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(userID).child("driverFound");
                    driverFoundRef.addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          if(dataSnapshot.getValue()=="true"){
                              driverFound = true;
                              driverFoundID = key;
                              DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(driverFoundID);
                              //String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                              HashMap map = new HashMap();
                              map.put("customerRiderId", userID);
                              driverRef.updateChildren(map);


                              System.out.println("Radius is " + radius);

                              getDriverLocation();
                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });


                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {
                    radius++;
                    System.out.println("Radius is " + radius);

                    getClosestDriver();
                }
            }
            @Override
            public void onGeoQueryError (DatabaseError error){

            }
        });
    }
    private Marker mDriverMarker;
    private void getDriverLocation(){

        mProgressBar.setVisibility(View.INVISIBLE);
        mLinearLayout1.setVisibility(View.INVISIBLE);
        mLinearLayout2.setVisibility(View.VISIBLE);

        DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference().child("driversWorking").child(driverFoundID).child("l");
        driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if(map.get(0)!=null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLng = new LatLng(locationLat,locationLng);
                    if(mDriverMarker!=null){
                        mDriverMarker.remove();
                    }
                    Location loc1 = new Location("");
                    loc1.setLatitude(locLatLng.latitude);
                    loc1.setLongitude(locLatLng.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(driverLatLng.latitude);
                    loc2.setLongitude(driverLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mDestination.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));

            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");
                            setEditText(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void setEditText(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null) {
                mCurrentLocation.setText(addresses.get(0).getAddressLine(0));
                if(!getDriversAroundStarted) {
                    getDriversAround(latLng.latitude, latLng.longitude);
                }

            } else {
                Log.e(TAG, "No addresses returned!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to get current address!");
        }
    }

    private void addMarker(AutoCompleteTextView mCurrentLocation, AutoCompleteTextView mDestination) {
        String locSearchString = mCurrentLocation.getText().toString();
        String desSearchString = mDestination.getText().toString();
        //geocoding location to latlng
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(locSearchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address loc = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + loc.toString());

            locLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            Log.d(TAG, "Your location latlng is " + locLatLng);
        }
        Geocoder geocoder1 = new Geocoder(getActivity());
        List<Address> list1 = new ArrayList<>();
        try {
            //Log.d(TAG,"des search string is "+desSearchString );
            list1 = geocoder1.getFromLocationName(desSearchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list1.size() > 0) {
            Address des = list1.get(0);

            Log.d(TAG, "geoLocate: found a destination: " + des.toString());

            desLatLng = new LatLng(des.getLatitude(), des.getLongitude());

            Log.d(TAG, "Your destination latlng is " + desLatLng);
        } else {
            Log.e(TAG, "Error: Destination not found");
        }
        MarkerOptions locMarker = new MarkerOptions()
                .position(locLatLng);
        MarkerOptions desMarker = new MarkerOptions()
                .position(desLatLng);
        mMap.addMarker(locMarker);
        mMap.addMarker(desMarker);
    }

    private void getDirections(AutoCompleteTextView mCurrentLocation, AutoCompleteTextView mDestination) {
        String locSearchString = mCurrentLocation.getText().toString();
        String desSearchString = mDestination.getText().toString();

        //geocoding location to latlng
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(locSearchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address loc = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + loc.toString());

            locLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            Log.d(TAG, "Your location latlng is " + locLatLng);
        }
        Geocoder geocoder1 = new Geocoder(getActivity());
        List<Address> list1 = new ArrayList<>();
        try {
            //Log.d(TAG,"des search string is "+desSearchString );
            list1 = geocoder1.getFromLocationName(desSearchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list1.size() > 0) {
            Address des = list1.get(0);

            Log.d(TAG, "geoLocate: found a destination: " + des.toString());

            desLatLng = new LatLng(des.getLatitude(), des.getLongitude());

            Log.d(TAG, "Your destination latlng is " + desLatLng);
        } else {
            Log.e(TAG, "Error: Destination not found");
        }
        MarkerOptions locMarker = new MarkerOptions()
                .position(locLatLng);
        MarkerOptions desMarker = new MarkerOptions()
                .position(desLatLng);
        mMap.addMarker(locMarker);
        mMap.addMarker(desMarker);
        String url = getRequestUrl(locLatLng, desLatLng);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);

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
        //avoid tolls

        if (mRouteOptions.getSelectedItem().toString().equals("Avoid Tolls")) {
            String avoidTolls = "avoid=tolls";
            param = param + "&" + avoidTolls;

        }
        Log.d(TAG, "wtheck is this" + param);
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
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

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    boolean getDriversAroundStarted = false;
    List<Marker> availableMarkerList = new ArrayList<Marker>();
    List<Marker> workingMarkerList = new ArrayList<Marker>();
    private void getDriversAround(double lat, double lng){
        getDriversAroundStarted = true;
        DatabaseReference availableDriversLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");

        GeoFire geoFire = new GeoFire(availableDriversLocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lat,lng), 10000);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                for (Marker markerIt : availableMarkerList){
                    if(markerIt.getTag().equals(key))
                        return;
                }

                LatLng driverLocation = new LatLng(location.latitude,location.longitude);
                Marker mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLocation).title(key).icon(BitmapDescriptorFactory.fromResource(R.mipmap.available_taxi)));
                mDriverMarker.setTag(key);
                availableMarkerList.add(mDriverMarker);
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker markerIt : availableMarkerList){
                    if(markerIt.getTag().equals(key)) {
                        markerIt.remove();
                        availableMarkerList.remove(markerIt);
                        return;
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker markerIt : availableMarkerList) {
                    if (markerIt.getTag().equals(key)){
                        markerIt.setPosition(new LatLng(location.latitude,location.longitude));
                    }

                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        DatabaseReference workingDriversLocation = FirebaseDatabase.getInstance().getReference().child("driversWorking");

        GeoFire geoFire1 = new GeoFire(workingDriversLocation);
        GeoQuery geoQuery1 = geoFire1.queryAtLocation(new GeoLocation(lat,lng), 10000);
        geoQuery1.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                for (Marker markerIt : workingMarkerList){
                    if(markerIt.getTag().equals(key))
                        return;
                }

                LatLng workingDriverLocation = new LatLng(location.latitude,location.longitude);
                Marker mWorkingDriverMarker = mMap.addMarker(new MarkerOptions().position(workingDriverLocation).title(key).icon(BitmapDescriptorFactory.fromResource(R.mipmap.working_taxi)));
                mWorkingDriverMarker.setTag(key);
                workingMarkerList.add(mWorkingDriverMarker);
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker markerIt : workingMarkerList){
                    if(markerIt.getTag().equals(key)) {
                        markerIt.remove();
                        workingMarkerList.remove(markerIt);
                        return;
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker markerIt : workingMarkerList) {
                    if (markerIt.getTag().equals(key)){
                        markerIt.setPosition(new LatLng(location.latitude,location.longitude));
                    }

                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
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
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mDestination.getWindowToken(), 0);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                if (mRouteOptions.getSelectedItem().toString().equals("Shortest")) {
                    path = lists.get(shortestRouteDis);

                } else if (mRouteOptions.getSelectedItem().toString().equals("Fastest")) {
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
                addMarker(mCurrentLocation, mDestination);
                LatLngBounds routeBounds = boundsBuilder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 12));
                mDisTextView.setText(distance);
                mDuraTextView.setText(duration);
                calculateFare(distance, duration);
            } else {
                Toast.makeText(getActivity(), "Directions not found!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void calculateFare(String distance, String duration) {
        double baseFare = 3;
        double perKmFare = 0.45;
        double perMinFare = 0.22;
        String stringDura = duration.replaceAll("mins", "");
        String stringDist = distance.replaceAll("km", "");
        double douDist = Double.parseDouble(stringDist);
        double douDura = Double.parseDouble(stringDura);
        double finalFare = baseFare + douDist * perKmFare + douDura * perMinFare;
        double meterLower, meterUpper;


        if (mFareType.getSelectedItem().toString().equals("Metered")) {
            meterLower = finalFare - 3;
            meterUpper = finalFare + 3;
            mFareTextView.setText("$" + (int) meterLower + " - " + (int) meterUpper);
        } else {
            mFareTextView.setText("$" + Double.toString((int) finalFare));
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            mBookingTime.setText(mBookingTime.getText() + ", " + hourOfDay + ":" + minute);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(new Date().getTime());
            c.add(Calendar.DATE, +7);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            mBookingTime.setText(day + "/" + (month + 1));
        }
    }
}