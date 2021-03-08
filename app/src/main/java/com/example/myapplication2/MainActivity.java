package com.example.myapplication2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button modificaCredenziali;
    private Button logout;
    @BindView(R.id.userText) TextView _usernameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Bundle data = getIntent().getExtras();
        _usernameText.setText("Benvenuto " + data.getString("username") + "!");


        modificaCredenziali = (Button) findViewById(R.id.buttonModificaCredenziali);
        logout = (Button) findViewById(R.id.buttonLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });

        modificaCredenziali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ModificaCredenzialiActivity.class);
                intent.putExtra("username", data.getString("username"));
                startActivity(intent);

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sito) {

            Uri uri = Uri.parse("https://fratelliminichillosnc.wordpress.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);


        } else if (id == R.id.nav_visualizza_catalogo) {

            Intent intent = new Intent(getApplicationContext(), ScaricaCatalogoActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_dove_siamo) {

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_social) {

            Intent intent = new Intent(getApplicationContext(), SocialActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contatti) {

            Intent intent = new Intent(getApplicationContext(), ContattiActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_orari) {

            Intent intent = new Intent(getApplicationContext(), OrariActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_info_azienda) {

            Intent intent = new Intent(getApplicationContext(), InfoAziendaActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
