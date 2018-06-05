package com.example.android.cabbysg;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_driverhelp extends Fragment {


    public nav_driverhelp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_driverhelp, container, false);

        ImageView goCall = view.findViewById(R.id.driverHelpCall);
        goCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+6596532136"));
                startActivity(callIntent);
            }
        });

        /*Website*/
        ImageView goEmail = view.findViewById(R.id.driverHelpEmail);
        goEmail.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://lihwan95.wixsite.com/cabbysg"));
                startActivity(intent);
            }
        });
        /*User Manual*/
        ImageView goManual = view.findViewById(R.id.driverHelpManual);
        goManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                driverManual newFragment=null;
                newFragment= new driverManual();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.nav_driverHelp,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        /*FAQ*/
        ImageView goFaq = view.findViewById(R.id.driverHelpFaq);
        goFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                driverFaq newFragment=null;
                newFragment= new driverFaq();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.nav_driverHelp,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return view;
    }

}
