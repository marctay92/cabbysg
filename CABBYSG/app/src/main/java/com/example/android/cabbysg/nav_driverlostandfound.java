package com.example.android.cabbysg;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.HashMap;
import java.util.Map;


public class nav_driverlostandfound extends Fragment {

    private EditText d_descriptionfield;
    private Button d_button;
    private ImageView d_lostandfoundphoto;

    private String d_description;
    private String d_lostandfoundPhotoUrl;
    private String userID;

    private FirebaseAuth d_Auth;
    private DatabaseReference d_CustomerDatabase;

    private Uri resultUri;

    public nav_driverlostandfound(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=  super.onCreateView(inflater, container, savedInstanceState);

        d_descriptionfield = (EditText) v.findViewById(R.id.description);

        d_button = (Button)v.findViewById(R.id.button);

        d_lostandfoundphoto = (ImageView) v.findViewById(R.id.lostandfoundphoto);

        d_Auth = FirebaseAuth.getInstance();
        userID = d_Auth.getCurrentUser().getUid();


        getUserInfo();

        d_lostandfoundphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        d_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }


        });



        return v;
    }

    private void getUserInfo(){
        d_CustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Description")!=null) {
                        d_description = map.get("Description").toString();
                        d_descriptionfield.setText(d_description);
                    }
                    if (map.get("lostandfoundPhotoUrl")!=null) {
                        d_lostandfoundPhotoUrl = map.get("lostandfoundPhotoUrl").toString();
                        Glide.with(getActivity().getApplicationContext()).load(d_lostandfoundPhotoUrl).into(d_lostandfoundphoto);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveUserInformation() {
        d_description = d_descriptionfield.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("Lost&FoundDescription", d_descriptionfield);
        d_CustomerDatabase.updateChildren(userInfo);

        //Saving image in Firebase storage

        if(resultUri != null) {

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("lostandfound_photo").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                 //   finish();
                    return;
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map newImage = new HashMap();
                    newImage.put("lostandfoundPhotoUrl", downloadUrl.toString());
                    d_CustomerDatabase.updateChildren(newImage);

                 //   finish();
                    return;

                }
            });
        }
      //  finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            d_lostandfoundphoto.setImageURI(resultUri);
        }
    }
}