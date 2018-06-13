package com.example.android.cabbysg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.android.cabbysg.nav_lostandfound.isEmailValid;
import static com.example.android.cabbysg.nav_lostandfound.isNameValid;


public class riderRegister extends AppCompatActivity {
    EditText firstName, lastName, email, mobileNb, password, rePassword;
    de.hdodenhof.circleimageview.CircleImageView registerProfile_image;
    private Uri resultUri;
    Button registerButton;
    Map<String, Object> newPost = new HashMap<String, Object>();

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(riderRegister.this, MenuBar.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        mobileNb = findViewById(R.id.mobileNb);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.rePassword);
        registerProfile_image = findViewById(R.id.registerProfile_image);

        registerButton = findViewById(R.id.registerButton);

        registerProfile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str_firstName = firstName.getText().toString();
                final String str_lastName = lastName.getText().toString();
                final String str_email = email.getText().toString();
                final String str_mobileNb = mobileNb.getText().toString();
                final String str_password = password.getText().toString();
                final String str_rePassword = rePassword.getText().toString();

                boolean validFirstName = false;
                boolean validLastName = false;
                boolean validEmail = false;
                boolean validNum = false;
                boolean validPW = false;
                boolean validRePW = false;

                if(TextUtils.isEmpty(str_firstName)) {
                    firstName.setError("Please enter your first name");
                } else if(!isNameValid(str_firstName)){
                    firstName.setError("Invalid First Name");
                }else validFirstName = true;

                if (TextUtils.isEmpty(str_lastName)) {
                    lastName.setError("Please enter your last name");
                } else if(!isNameValid(str_lastName)){
                    lastName.setError("Invalid Last Name");
                } else validLastName = true;

                if (TextUtils.isEmpty(str_email)) {
                    email.setError("Please enter your email");
                } else if(!isEmailValid(str_email)){
                    email.setError("Please enter a valid email");
                } else validEmail = true;

                if (TextUtils.isEmpty(str_mobileNb)) {
                    mobileNb.setError("Please enter your phone number");
                }else if (!TextUtils.isDigitsOnly(str_mobileNb)){
                    mobileNb.setError("Please enter only numbers");
                }else if(str_mobileNb.length() > 8 || str_mobileNb.length()<8){
                    mobileNb.setError("Please enter only 8 digits");
                }else validNum = true;

                if (TextUtils.isEmpty(str_password)) {
                    password.setError("Please enter your password");
                } else if (!(isValidPassword(str_password))){
                    password.setError("Min 8 characters with at least 1 Upper and 1 number");
                } else validPW = true;

                if (TextUtils.isEmpty(str_rePassword)) {
                    rePassword.setError("Please enter your password");
                } else if (!(TextUtils.equals(str_password,str_rePassword))) {
                    rePassword.setError("Password does not match");
                    password.setError("Password does not match");
                } else validRePW = true;

                if (validFirstName && validLastName && validEmail && validNum && validPW && validRePW){
                    mAuth.createUserWithEmailAndPassword(str_email,str_password).addOnCompleteListener(riderRegister.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(riderRegister.this, "sign up error", Toast.LENGTH_SHORT).show();
                            }else{
                                String user_id = mAuth.getCurrentUser().getUid();
                                final DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(user_id);

                                //newPost.put("email",str_email);
                                newPost.put("firstName",str_firstName);
                                newPost.put("lastName",str_lastName);
                                newPost.put("mobileNum",str_mobileNb);
                                newPost.put("email",mAuth.getCurrentUser().getEmail());
                                newPost.put("rating",0);
                                current_user_db.updateChildren(newPost);

                                if(resultUri!=null){
                                    StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(user_id);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
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
                                                    newPost.put("profileImageUrl",profileImageUrl.toString());
                                                    System.out.println(newPost.get("profileImageUrl"));
                                                    current_user_db.updateChildren(newPost);
                                                }
                                            });
                                            return;
                                        }
                                    });
                                }

                                current_user_db.updateChildren(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(riderRegister.this,"Registration is successful",Toast.LENGTH_SHORT);

                                        }else{
                                            Toast.makeText(riderRegister.this,"Registration is unsuccessful",Toast.LENGTH_SHORT);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            registerProfile_image.setImageURI(resultUri);
        }
    }
}
