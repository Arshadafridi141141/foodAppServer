package com.example.admin_server.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin_server.Current.Current;
import com.example.admin_server.Home;
import com.example.admin_server.Model.User;
import com.example.admin_server.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signin extends AppCompatActivity {

    EditText phone,password;
    Button Signin;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        phone=(EditText)findViewById(R.id.login_number);
        password=(EditText)findViewById(R.id.login_password);
        Signin=(Button)findViewById(R.id.admin_login_button);
        db=FirebaseDatabase.getInstance();
        users=db.getReference("users");

        Signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(Validation(phone.getText().toString(),password.getText().toString())){
                    Log.e("checking","Validation done");
                    final String str_number=phone.getText().toString();
                    final String str_password=password.getText().toString();
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(str_number).exists()){
                                User user=dataSnapshot.child(str_number).getValue(User.class);
                                user.setPhone(str_number);
                                if(Boolean.parseBoolean(user.getIsStaff())){
                                    if(user.getPassword().equals(str_password)){
                                        Toast.makeText(Signin.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(Signin.this, Home.class);
                                        Current.currentUser=user;
                                        startActivity(intent);

                                    }
                                    else
                                        Toast.makeText(Signin.this,"Password wrong",Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(Signin.this,"Please Login with staff account",Toast.LENGTH_SHORT).show();


                            }
                            else
                                Toast.makeText(Signin.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    
                }
                else
                    Toast.makeText(Signin.this,"Validation doesnt match",Toast.LENGTH_SHORT).show();

            }


        });
    }
    private boolean Validation(String str_name, String str_passwrd) {
        Boolean isValud=true;
        if(TextUtils.isEmpty(str_name)){
            isValud=false;
            phone.setError("Enter Number");
        }
        if(TextUtils.isEmpty(str_passwrd)){
            isValud=false;
            password.setError("Enter Password");
        }

        return isValud;

    }
}
