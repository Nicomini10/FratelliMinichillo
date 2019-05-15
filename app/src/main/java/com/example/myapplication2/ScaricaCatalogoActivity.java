package com.example.myapplication2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

public class ScaricaCatalogoActivity extends AppCompatActivity {

    private Context context;
    private TextView tv;
    private String pdfUrl = "http://fratelliminichillows.altervista.org/catalogo.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scarica_catalogo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                Toast.makeText(getBaseContext(), "Catalogo scaricato, lo troverai nei documenti del tuo smartphone!", Toast.LENGTH_LONG).show();

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

}
