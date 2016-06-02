package com.dev.alt.devand.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class Daemon extends IntentService {

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
    }
}
