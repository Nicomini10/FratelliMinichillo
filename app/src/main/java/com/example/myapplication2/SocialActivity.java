package com.example.myapplication2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SocialActivity extends AppCompatActivity {


    private ImageView facebook;
    private ImageView instagram;
    private ImageView twitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        facebook = (ImageView) findViewById(R.id.imageFacebook);
        instagram = (ImageView) findViewById(R.id.imageInstagram);
        twitter = (ImageView) findViewById(R.id.imageTwitter);

        facebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.facebook.com/fratelli.minichillosnc");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.instagram.com/fratelli_minichillo_s.n.c/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://twitter.com/fratelli_mini");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



    }
}