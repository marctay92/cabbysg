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
public class nav_help extends Fragment{



    public nav_help() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_nav_help, container, false);


        /*Call*/
        ImageView goCall = view.findViewById(R.id.helpCall);
        goCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+6584449912"));
                startActivity(callIntent);
            }
        });


        /*Website*/
        ImageView goEmail = view.findViewById(R.id.helpEmail);
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
        ImageView goManual = view.findViewById(R.id.helpManual);
        goManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManual newFragment=null;
                newFragment= new userManual();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.nav_help,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        /*FAQ*/
        ImageView goFaq = view.findViewById(R.id.helpFaq);
        goFaq.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                goFaq newFragment=null;
                newFragment= new goFaq();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.nav_help,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return view;
    }
}
