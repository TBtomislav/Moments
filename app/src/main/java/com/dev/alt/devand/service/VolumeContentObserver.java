package com.dev.alt.devand.service;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import java.util.logging.Logger;

public class VolumeContentObserver extends ContentObserver {
    Context context;
    boolean b;
    Daemon d;

    public VolumeContentObserver(Context c, Daemon d, Handler handler) {
        super(handler);
        context=c;
        b=true;
        this.d = d;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // intercepte juste le press_down
        if(b) {
            d.sayIt();

        }
        b = !b;

    }
}
