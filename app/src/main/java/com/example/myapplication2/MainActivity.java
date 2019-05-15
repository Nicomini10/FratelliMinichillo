package com.example.myapplication2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private TextView tv;
    private String pdfUrl = "http://fratelliminichillows.altervista.org/catalogo.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        context=this;
        tv=(TextView)findViewById(R.id.txtmessage);
        Button btDownload=(Button)findViewById(R.id.btdownload);
        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent=new Intent(context,DownloadService.class);
                newIntent.setAction(DownloadService.ACTION_DOWNLOAD);
                newIntent.putExtra(DownloadService.EXTRA_URL, pdfUrl);
                // Start Download Service
                tv.setText("Downloading...");
                context.startService(newIntent);

            }
        });

    }

    private BroadcastReceiver DownloadReceiver=new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent){
            // Display message from DownloadService
            Bundle b=intent.getExtras();
            if(b!=null){

                tv.setText(b.getString(DownloadService.EXTRA_MESSAGE));
            }
        }
    };

    protected void onResume(){
        super.onResume();
        // Register receiver to get message from DownloadService
        registerReceiver(DownloadReceiver, new IntentFilter(DownloadService.ACTION_DOWNLOAD));

    }

    protected void onPause(){
        super.onPause();
        // Unregister the receiver
        unregisterReceiver(DownloadReceiver);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_visualizza_prodotti) {

            Intent intent = new Intent(getApplicationContext(), ProdottiActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_dove_siamo) {

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contatti) {

            Intent intent = new Intent(getApplicationContext(), ContattiActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_info_azienda) {

            Intent intent = new Intent(getApplicationContext(),InfoAziendaActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
