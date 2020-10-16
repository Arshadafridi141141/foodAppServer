package com.example.admin_server;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.admin_server.Admin.Signin;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void Signinin(View view) {
        Intent intent=new Intent(MainActivity.this, Signin.class) ;

        startActivity(intent);
    }

    public void signup(View view) {
    }
}
