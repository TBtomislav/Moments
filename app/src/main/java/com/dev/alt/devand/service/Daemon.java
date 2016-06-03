package com.dev.alt.devand.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.widget.Toast;

import com.dev.alt.devand.R;

import java.io.FileOutputStream;

public class Daemon extends IntentService {

    VolumeContentObserver vco;

    public Daemon() {
        super("Daemon Free");
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("service", "service opérationnelle");

        vco = new VolumeContentObserver(this, Daemon.this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, vco );
    }

    @Override
    public void onDestroy() {
        getApplicationContext().getContentResolver().unregisterContentObserver(vco);
    }

    public void sayIt() {
        Log.d("vco", "Winter is coming");
    }
}
