package com.example.android.cabbysg;

import android.content.Intent;
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

public class MenuBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Home");
        nav_home fragment = new nav_home();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment,"Home");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //NOTICE FOR LATER
            super.onBackPressed();
        }
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
            //Intent intent=new Intent(this,CreditCardDesign.class);
            //startActivity(intent);


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
            nav_driverhelp fragment = new nav_driverhelp();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment,"Help");
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

/*
   public class addPaymentMethod extends Activity {
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.add_creditcard);

            final Button button = findViewById(R.id.addpaymentmethod);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(MenuBar.this,add_creditcard.class));
                }
            });
        }
    }
 /*
    private void addPaymentMethod(){
        Button addPaymentMethod = findViewById(R.id.addpaymentmethod);
        addPaymentMethod.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(MenuBar.this,add_creditcard.class));
            }
        });
    }
*/
/*}*/

