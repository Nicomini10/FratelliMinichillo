package com.example.myapplication2;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ContattiActivity extends AppCompatActivity {


    private FloatingActionButton email;
    private FloatingActionButton telefono;
    private FloatingActionButton telefonoMobile;
    private FloatingActionButton fax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatti);

        email = (FloatingActionButton) findViewById(R.id.buttonEmail);
        telefono = (FloatingActionButton) findViewById(R.id.buttontelefono);
        telefonoMobile = (FloatingActionButton) findViewById(R.id.buttontelefonomobile);
        fax = (FloatingActionButton) findViewById(R.id.buttonfax);



        telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0874873298"));
                startActivity(intent);

            }


        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
                startActivity(intent);

            }
        });



        telefonoMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:3358261662"));
                startActivity(intent);

            }
        });


        fax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0874873678"));
                startActivity(intent);

            }
        });


    }
}
