package com.example.android.cabbysg;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;
import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.cabbysg.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

public class MenuBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MapActivity";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bar);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);


 /*      getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.app_logo); //also displays wide logo
        getSupportActionBar().setDisplayShowTitleEnabled(false); //optional
*/
       /* getSupportActionBar().setLogo(R.drawable.app_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.drawable.app_logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Home");
        nav_home fragment = new nav_home();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment,"Home");
        fragmentTransaction.commit();

        mDestination = findViewById(R.id.input_search1);
        mCurrentLocation = findViewById(R.id.input_search);


        mDisTextView = findViewById(R.id.disTextView);
        mDuraTextView = findViewById(R.id.duraTextView);
        mFareTextView = findViewById(R.id.fareTextView);

        mRouteOptions = findViewById(R.id.mRouteOptions);
        mFareType = findViewById(R.id.mFareType);
        mServiceType = findViewById(R.id.mServiceType);

        mBookingTime = findViewById(R.id.mBookingTime);

        mGps = findViewById(R.id.ic_gps);
        mSubmit = findViewById(R.id.submitBtn);

        getLocationPermission();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setTitle("Home");
            nav_home fragment = new nav_home();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment,"Home");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_profile) {
            setTitle("Profile");
            nav_profile fragment = new nav_profile();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment,"Profile");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_payment) {
            setTitle("Payment");
            nav_payment fragment = new nav_payment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment,"Payment");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_schedule) {
            setTitle("Schedule");
            nav_schedule fragment = new nav_schedule();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment,"Schedule");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_history) {
            setTitle("History");
            nav_history fragment = new nav_history();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment,"History");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_lostandfound) {
            setTitle("Lost & Found");
            nav_lostandfound fragment = new nav_lostandfound();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment,"LostandFound");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_help) {
            setTitle("Help");
            nav_help fragment = new nav_help();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment,"Help");
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
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

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mDestination.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, Places.getGeoDataClient(this, null),
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

        mPlaceAutocompleteAdapter1 = new PlaceAutocompleteAdapter(this, Places.getGeoDataClient(this, null),
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
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Avoid Tolls") && (mDestination.getText().toString().length() > 0) && (mCurrentLocation.getText().toString().length() > 0)) {
                    getDirections(mCurrentLocation, mDestination);
                } else if ((mDestination.getText().toString().length() > 0) && (mCurrentLocation.getText().toString().length() > 0)) {
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
                if (selectedItem.equals("Metered") && (mDestination.getText().toString().length() > 0) && (mCurrentLocation.getText().toString().length() > 0)) {
                    calculateFare(mDisTextView.getText().toString(), mDuraTextView.getText().toString());
                } else if ((mDestination.getText().toString().length() > 0) && (mCurrentLocation.getText().toString().length() > 0)) {
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
                if ((mDestination.getText().toString().length() > 0) && (mCurrentLocation.getText().toString().length() > 0)) {
                    //save current location and destination
                } else {
                    Toast.makeText(MenuBar.this, "Please enter a valid location and/or destination!", Toast.LENGTH_SHORT).show();
                }
                if (mRouteOptions.getSelectedItem().toString().equals("Shortest")) {
                    //save shortest option
                } else if (mRouteOptions.getSelectedItem().toString().equals("Fastest")) {
                    //save fastest
                } else {
                    //save avoid tolls
                }
                if (mServiceType.getSelectedItem().toString().equals("4-Seater")) {
                    //save 4-seater
                } else {
                    //save 6-seater
                }
                if (mFareType.getSelectedItem().toString().equals("Fare")) {
                    fare = mFareTextView.toString();
                    //save final fare and fare type
                } else {
                    //save metered
                }
                if (mBookingTime.toString().equals("Now")) {
                    //get current time and save
                } else {
                    //get selected time
                }

            }
        });

        hideSoftKeyboard();
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mDestination.getText().toString();

        Geocoder geocoder = new Geocoder(MenuBar.this);
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

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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
                            Toast.makeText(MenuBar.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void setEditText(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null) {
                mCurrentLocation.setText(addresses.get(0).getAddressLine(0));
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
        LatLng locLatLng = null;
        LatLng desLatLng = null;
        //geocoding location to latlng
        Geocoder geocoder = new Geocoder(MenuBar.this);
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
        Geocoder geocoder1 = new Geocoder(MenuBar.this);
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
        LatLng locLatLng = null;
        LatLng desLatLng = null;
        //geocoding location to latlng
        Geocoder geocoder = new Geocoder(MenuBar.this);
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
        Geocoder geocoder1 = new Geocoder(MenuBar.this);
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
        if (locLatLng != null && desLatLng != null) {
            MarkerOptions locMarker = new MarkerOptions()
                    .position(locLatLng);
            MarkerOptions desMarker = new MarkerOptions()
                    .position(desLatLng);
            mMap.addMarker(locMarker);
            mMap.addMarker(desMarker);


            String url = getRequestUrl(locLatLng, desLatLng);

            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
        } else{
            Log.e(TAG,"Error getting directions: LocLatLng and DesLatLng is null");
        }

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MenuBar.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mDestination.getWindowToken(), 0);
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
                Toast.makeText(MenuBar.this, "Directions not found!", Toast.LENGTH_SHORT).show();
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
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
