package com.dev.alt.devand;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.dev.alt.devand.helper.PersonEntity;
import com.dev.alt.devand.helper.PersonRepository;
import com.dev.alt.devand.service.Daemon;
import com.dev.alt.devand.service.VolumeContentObserver;

public class FreeWay extends AppCompatActivity {

    Intent servDaemon;
    private PersonEntity pe;
    PersonRepository pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_way);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pr = new PersonRepository(getApplicationContext());
        pe = null;

        Bundle extras = getIntent().getExtras();
/*
        // Check if the user is connected
        if (extras != null) {
            String login = extras.getString("login");

            if (pr.existPerson(login)) {
                pe = pr.getPerson(login);
            } else {
                Intent returnConnection = new Intent(FreeWay.this, Connection.class);
                returnConnection.putExtra("err", "Try to connect first");
                startActivity(returnConnection);
            }
        } else {
            Intent returnConnection = new Intent(FreeWay.this, Connection.class);
            returnConnection.putExtra("err", "Try to connect first");
            startActivity(returnConnection);
        }

        // If user is not connected
        if (pe == null || pe.getLoggedIn() == 0) {
            Intent returnConnection = new Intent(FreeWay.this, Connection.class);
            returnConnection.putExtra("err", "Try to connect first");
            startActivity(returnConnection);
        }*/

        // Daemon

        servDaemon = new Intent(this, Daemon.class);
        startService(servDaemon);

        Log.d("freeway", "service lancé");
        Button login = (Button) findViewById(R.id.btn_stopFree);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(servDaemon);
                Log.d("freeway", "service arrété");
                Intent i = new Intent(FreeWay.this, MainMenu.class);
                //i.putExtra("login", pe.getLogin());
                startActivity(i);
            }
        });

        //IntentFilter filter = new IntentFilter();
        //filter.addAction("android.media.EXTRA_VOLUME_STREAM_VALUE");
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            Toast.makeText(getApplicationContext(), "touche appuyé",
                    Toast.LENGTH_SHORT).show();
            Log.d("toast", "titi et gros minet");
            //CameraEntity ce = new CameraEntity();
            new Thread(new Runnable() {
                public void run() {
                    // Nous prenons une photo
                    if (camera != null) {
                        SavePicture();
                    }
                }
            }).start();
        }
        return true;
    }*/

    // Retour sur l'application
    @Override
    public void onResume() {
        super.onResume();
    }

    // Mise en pause de l'application
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopService(servDaemon);
    }
}
