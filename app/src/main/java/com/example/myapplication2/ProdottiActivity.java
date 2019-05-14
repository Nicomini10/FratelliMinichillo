package com.example.myapplication2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ProdottiActivity extends AppCompatActivity {

    private Button buttonPorte;
    private Button buttonFinestre;
    private Button buttonBalconi;
    private Button buttonCencelli;

    private FloatingActionButton reverseButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodotti);

        buttonPorte = (Button) findViewById(R.id.button2);
        buttonFinestre = (Button) findViewById(R.id.button3);
        buttonBalconi = (Button) findViewById(R.id.button4);
        buttonCencelli = (Button) findViewById(R.id.button5);

        buttonPorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ListaPorte.class);
                startActivity(intent);

            }
        });

        buttonFinestre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ListaFinestre.class);
                startActivity(intent);

            }
        });

        buttonBalconi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ListaBalconi.class);
                startActivity(intent);

            }
        });

        buttonCencelli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ListaCancelli.class);
                startActivity(intent);

            }
        });



    }
}
