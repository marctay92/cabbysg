package com.example.android.cabbysg;

import android.content.Intent;
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
    public void goFaq(View view){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_nav_help, container, false);
        ImageView goFaq = view.findViewById(R.id.helpFaq);

        goFaq.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
             Intent intent = new Intent(getActivity(), faq.class);
             startActivity(intent);
            }
        });
        return view;
    }


}



