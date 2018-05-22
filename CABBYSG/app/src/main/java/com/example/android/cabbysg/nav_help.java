package com.example.android.cabbysg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class nav_help extends Fragment {



    public nav_help() {
        // Required empty public constructor
    }
    public void goCall(View view){
    }
    public void goEmail (View view){

    }

    public void goFaq(View view){
    }
    public void goManual (View view){
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
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:84449912"));
                startActivity(callIntent);
            }
        });

        /*Email*//*
        ImageView goEmail = view.findViewById(R.id.helpEmail);
        goEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","joshuakwekk@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });*/
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


        /*FAQ*/
        ImageView goFaq = view.findViewById(R.id.helpFaq);
        goFaq.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
             Intent intent = new Intent(getActivity(), faq.class);
             startActivity(intent);
            }
        });

        /*User Manual*/
        ImageView goManual = view.findViewById(R.id.helpManual);
        goManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), userManual.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
