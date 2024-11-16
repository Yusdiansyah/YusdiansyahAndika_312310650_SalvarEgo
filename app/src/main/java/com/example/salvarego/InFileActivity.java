package com.example.salvarego;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InFileActivity extends AppCompatActivity {


    Button homebutton;
    Button addbutton;
    Button profilebutton;
    Button viewfile;
    Button backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_in_file);
        homebutton = (Button) findViewById(R.id.homebtn);
        addbutton = (Button) findViewById(R.id.addbtn);
        profilebutton = (Button) findViewById(R.id.profilebtn);
        viewfile = (Button) findViewById(R.id.tofilebtn);
        backbutton = (Button) findViewById(R.id.backbtn);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(gotoMain);
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAdd = new Intent(getApplicationContext(), AddFileActivity.class);
                startActivity(gotoAdd);
            }
        });

        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(gotoProfile);
            }
        });
        viewfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewthefile = new Intent(getApplicationContext(), ViewFileActivity.class);
                startActivity(viewthefile);
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goback);
            }
        });
    }
}