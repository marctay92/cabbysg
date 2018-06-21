package com.example.android.cabbysg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.ProgressDialog.STYLE_SPINNER;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_driverlostandfound extends Fragment {

    EditText itemDescription,date;
    ImageView photo1,photo2,photo3;
    String itemDescriptionStr = "",dateStr;
    private Uri photo1Uri, photo2Uri, photo3Uri;
    Map<String, Object> newPost = new HashMap<String, Object>();
    Button foundItemSubmit;
    boolean validDescription = false,validDate = false;
    DatabaseReference current_user_db,found_item_db;
    int index = 1;
    //Create progress dialog
    ProgressDialog pd;

    FirebaseUser user;

    public nav_driverlostandfound() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nav_driverlostandfound, container, false);

        //Init pd
        pd = new ProgressDialog(getContext(), STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");

        //Init Edit Text
        itemDescription = rootView.findViewById(R.id.foundItemDescription);
        date = rootView.findViewById(R.id.foundDate);
        //Init Button
        foundItemSubmit = rootView.findViewById(R.id.foundItemSubmit);
        //Init photo view
        photo1 = rootView.findViewById(R.id.lostandfoundphoto);
        photo2 = rootView.findViewById(R.id.lostandfoundphoto2);
        photo3 = rootView.findViewById(R.id.lostandfoundphoto3);

        user = FirebaseAuth.getInstance().getCurrentUser();
        current_user_db = FirebaseDatabase.getInstance().getReference().child("Drivers");
        found_item_db = FirebaseDatabase.getInstance().getReference().child("FoundItem").push();

        final String foundItemID = found_item_db.getKey();

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            index=1;
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,1);
            }
        });
        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 2;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 3;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        foundItemSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Extract String from EditText
                itemDescriptionStr = itemDescription.getText().toString();
                dateStr = date.getText().toString();

                //Checks
                if (TextUtils.isEmpty(itemDescriptionStr)) {
                    itemDescription.setError("Please enter your item description");
                } else validDescription = true;

                if (TextUtils.isEmpty(dateStr)) {
                    date.setError("Please enter your date found");
                } else if (!isValidDate(dateStr)) {
                    date.setError("Please enter your date in dd-MM-yyyy");
                }else validDate = true;

                if (photo1Uri==null || photo2Uri==null||photo3Uri==null){
                    Toast.makeText(getActivity(),"Please upload a picture for reference",Toast.LENGTH_SHORT).show();
                }

                if (validDescription && validDate){
                    newPost.put("foundDate", dateStr);
                    newPost.put("itemDescription", itemDescriptionStr);
                    newPost.put("driverID",user.getUid());
                    found_item_db.updateChildren(newPost);

                    if (photo1Uri != null) {
                        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("found_items").child(foundItemID).child("photo1");
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photo1Uri);
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
                                return;
                            }
                        });
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri profileImageUrl = uri;
                                        newPost.put("photo1", profileImageUrl.toString());
                                        System.out.println(newPost.get("photo1"));
                                        found_item_db.updateChildren(newPost);
                                    }
                                });
                                return;
                            }
                        });
                    }
                    if (photo2Uri != null) {
                        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("found_items").child(foundItemID).child("photo2");
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photo2Uri);
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
                                return;
                            }
                        });
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri profileImageUrl = uri;
                                        newPost.put("photo2", profileImageUrl.toString());
                                        System.out.println(newPost.get("photo2"));
                                        found_item_db.updateChildren(newPost);
                                    }
                                });
                                return;
                            }
                        });
                    }
                    if (photo3Uri != null) {
                        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("found_items").child(foundItemID).child("photo3");
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photo3Uri);
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
                                return;
                            }
                        });
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri profileImageUrl = uri;
                                        newPost.put("photo3", profileImageUrl.toString());
                                        System.out.println(newPost.get("photo3"));
                                        found_item_db.updateChildren(newPost);
                                    }
                                });
                                return;
                            }
                        });
                    }
                    current_user_db.child(user.getUid()).child("FoundItem").child(foundItemID).setValue(true);
                    Toast.makeText(getActivity(),"Found Item Report submitted.",Toast.LENGTH_SHORT).show();
                    Fragment newFragment=new nav_driverhome();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.driverLostandFound,newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    pd.dismiss();
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;

    }
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
            Date today = Calendar.getInstance().getTime();
            String formattedDate = dateFormat.format(today);
            return dateFormat.parse(inDate.trim()).before(dateFormat.parse(formattedDate));
        } catch (ParseException pe) {
            return false;
        }
        //return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && index == 1){
            final Uri imageUri = data.getData();
            photo1Uri = imageUri;
            photo1.setImageURI(photo1Uri);
        }
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && index == 2){
            final Uri imageUri = data.getData();
            photo2Uri = imageUri;
            photo2.setImageURI(photo2Uri);
        }
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && index == 3){
            final Uri imageUri = data.getData();
            photo3Uri = imageUri;
            photo3.setImageURI(photo3Uri);
        }
    }
}
