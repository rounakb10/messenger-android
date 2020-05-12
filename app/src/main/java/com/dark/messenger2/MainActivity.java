package com.dark.messenger2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    Button btnlLogin, btnlRegister;
    EditText etlMail;
    EditText etlPwd;
    EditText etlName;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser firebaseUser;
    TextView tvRegister;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etlMail = findViewById(R.id.etlMail);
        etlPwd = findViewById(R.id.etlPwd);
        btnlLogin = findViewById(R.id.btnlLogin);
        tvRegister = findViewById(R.id.tvRegister);
        etlName = findViewById(R.id.etlName);
        btnlRegister = findViewById(R.id.btnlRegister);

        etlName.setVisibility(View.GONE);
        btnlRegister.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

       /* authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mFirebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    //Toast.makeText(MainActivity.this , "Successfully logged in" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this , com.dark.messenger2.HomeActivity.class);

                    //intent.putExtra("EMAIL" ,etlMail.getText().toString());
                    startActivity(intent);
                    finish();
                }
                //Toast.makeText(MainActivity.this , "Please Login" , Toast.LENGTH_SHORT).show();

            }
        };*/

        btnlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etlMail.getText().toString().isEmpty()){
                    //Toast.makeText(RegisterActivity.this , "Enter all fields!" , Toast.LENGTH_SHORT).show();
                    etlMail.setError("Enter valid email ID");
                    etlMail.requestFocus();
                }
                else if(etlPwd.getText().toString().isEmpty()) {
                    etlPwd.setError("Enter the password");
                    etlPwd.requestFocus();
                }
                else if(etlMail.getText().toString().isEmpty() && etlPwd.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this , "Enter all fields" , Toast.LENGTH_SHORT).show();
                }

                else {
                    firebaseAuth.signInWithEmailAndPassword(etlMail.getText().toString() , etlPwd.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!(task.isSuccessful())){
                                Toast.makeText(MainActivity.this , "Wrong login details!" , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intent = new Intent(MainActivity.this , HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LayoutInflater inflater=getLayoutInflater();
                //View view=inflater.inflate(R.layout.activity_main, null, false);
                //view.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right));
                //setContentView(view);

                etlName.setVisibility(View.VISIBLE);
                tvRegister.setVisibility(View.GONE);
                btnlLogin.setVisibility(View.GONE);
                btnlRegister.setVisibility(View.VISIBLE);

            }
        });

        btnlRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etlMail.getText().toString().isEmpty()){
                    //Toast.makeText(RegisterActivity.this , "Enter all fields!" , Toast.LENGTH_SHORT).show();
                    etlMail.setError("Enter valid email ID");
                    etlMail.requestFocus();
                }
                else if(etlName.getText().toString().isEmpty()){
                    etlName.setError("Enter valid name");
                    etlName.requestFocus();
                }
                else if(etlPwd.getText().toString().isEmpty()) {
                    etlPwd.setError("Enter the password");
                    etlPwd.requestFocus();
                }
                else if(etlPwd.getText().toString().length() < 6) {
                    Toast.makeText(MainActivity.this , "Password is too short" , Toast.LENGTH_SHORT).show();
                    etlPwd.requestFocus();
                }
                else if(etlMail.getText().toString().isEmpty() && etlPwd.getText().toString().isEmpty() && etlName.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this , "Enter all fields" , Toast.LENGTH_SHORT).show();
                }
                else {
                    register();
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //firebaseAuth.addAuthStateListener(authStateListener);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            //Toast.makeText(MainActivity.this , "Successfully logged in" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, com.dark.messenger2.HomeActivity.class);

            //intent.putExtra("EMAIL" ,etlMail.getText().toString());
            startActivity(intent);
            finish();
        }

    }

    private void register(){
        firebaseAuth.createUserWithEmailAndPassword(etlMail.getText().toString() , etlPwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userID = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("User").child(userID);

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id" , userID);
                    hashMap.put("username" , etlName.getText().toString());
                    hashMap.put("imageURL" , "default");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this , HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this , "Try using a different ID/Password" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
