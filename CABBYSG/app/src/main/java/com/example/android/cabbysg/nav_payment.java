package com.example.android.cabbysg;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_payment extends android.support.v4.app.Fragment {
    private String userId;
    private addCardAdapter adapter;
    String cardTypeStr;

    public nav_payment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_nav_payment, container, false);

        ListView listView= view.findViewById(R.id.cardList);
        adapter = new addCardAdapter(getContext(),getDataSetHistory());
        listView.setAdapter(adapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserCreditCard();

        Button button = view.findViewById(R.id.addpaymentmethod);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CardEditActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getUserCreditCard(){
        DatabaseReference userCreditCard_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(userId).child("creditCard");
        userCreditCard_db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    //for(DataSnapshot creditCard : dataSnapshot.getChildren()){
                        FetchCardInfo(dataSnapshot.getKey());
                    //}
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    //for(DataSnapshot creditCard : dataSnapshot.getChildren()){
                        FetchCardInfo(dataSnapshot.getKey());
                    //}
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //arrayOfDetails.clear();
                    FetchCardInfo(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*DatabaseReference userCreditCard_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(userId).child("creditCard");
        userCreditCard_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot creditCard : dataSnapshot.getChildren()){
                        FetchCardInfo(creditCard.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void FetchCardInfo(String cardKey){
        DatabaseReference creditCard_db = FirebaseDatabase.getInstance().getReference().child("creditCard").child(cardKey);
        arrayOfDetails.clear();
        creditCard_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String cardNum =dataSnapshot.getKey();
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        if(child.getKey().equals("cardType")){
                            cardTypeStr = child.getValue().toString();
                        }
                    }
                    cardDetailsContainer newDetails = new cardDetailsContainer(cardNum,cardTypeStr);
                    arrayOfDetails.add(newDetails);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private ArrayList<cardDetailsContainer> arrayOfDetails = new ArrayList<cardDetailsContainer>();
    private ArrayList<cardDetailsContainer> getDataSetHistory(){
        return arrayOfDetails;
    }

}
