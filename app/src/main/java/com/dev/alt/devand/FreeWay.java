package com.dev.alt.devand;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.dev.alt.devand.helper.PersonEntity;
import com.dev.alt.devand.helper.PersonRepository;
import com.dev.alt.devand.service.Daemon;
import com.dev.alt.devand.service.VolumeContentObserver;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FreeWay extends AppCompatActivity implements SurfaceHolder.Callback {

    Intent servDaemon;
    private PersonEntity pe;
    PersonRepository pr;
    private FileOutputStream stream;

    private Camera camera;
    private SurfaceView surfaceCamera;
    private Boolean isPreview;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_way);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pr = new PersonRepository(getApplicationContext());
        pe = null;

        Bundle extras = getIntent().getExtras();

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
        }

        // Preview and camera
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        isPreview = false;
        surfaceCamera = (SurfaceView) findViewById(R.id.surfaceViewCamera);
        InitializeCamera();

        // Daemon
        servDaemon = new Intent(FreeWay.this, Daemon.class);
        startService(servDaemon);


        Log.d("freeway", "service lancé");
        Button login = (Button) findViewById(R.id.btn_stopFree);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(servDaemon);
                Log.d("freeway", "service arrété");
                Intent i = new Intent(FreeWay.this, MainMenu.class);
                i.putExtra("login", pe.getLogin());
                startActivity(i);
            }
        });
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

    // méhode pour caméra
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Nous prenons le contrôle de la camera
        if (camera == null)
            camera = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // Si le mode preview est lancé alors nous le stoppons
        if (isPreview) {
            camera.stopPreview();
        }
        // Nous récupérons les paramètres de la caméra
        Camera.Parameters parameters = camera.getParameters();

        // Nous changeons la taille
        parameters.setPreviewSize(width, height);

        // Nous appliquons nos nouveaux paramètres
        camera.setParameters(parameters);

        try {
            // Nous attachons notre prévisualisation de la caméra au holder de la
            // surface
            camera.setPreviewDisplay(surfaceCamera.getHolder());
        } catch (IOException e) {
            Log.d("FreeWay", "Erreur à la mise en place du preview" + e.getMessage());
        }

        // Nous lançons la preview
        camera.startPreview();

        isPreview = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Nous arrêtons la camera et nous rendons la main
        if (camera != null) {
            camera.stopPreview();
            isPreview = false;
            camera.release();
        }
    }

    public void InitializeCamera() {
        // Nous attachons nos retours du holder à notre activité
        surfaceCamera.getHolder().addCallback(this);
        // Nous spécifiions le type du holder en mode SURFACE_TYPE_PUSH_BUFFERS
        surfaceCamera.getHolder().setType(
                SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    // Retour sur l'application
    @Override
    public void onResume() {
        super.onResume();
        if (camera == null)
            camera = Camera.open();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        isPreview = false;
        Log.d("camera", surfaceCamera.toString());
        surfaceCamera = (SurfaceView) findViewById(R.id.surfaceViewCamera);
        InitializeCamera();
    }

    // Mise en pause de l'application
    @Override
    public void onPause() {
        super.onPause();
    }

    private void SavePicture() {
        try {

            Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

                public void onPictureTaken(byte[] data, Camera camera) {
                    if (data != null) {
                        // Enregistrement de votre image
                        try {
                            if (stream != null) {
                                stream.write(data);
                                stream.flush();
                                stream.close();
                            }
                        } catch (Exception e) {
                            Log.d("FreeWay", "erreur en sauvegardant la photo :" + e.getMessage());
                        }

                        // Nous redémarrons la prévisualisation
                        camera.startPreview();
                    }
                }
            };

            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy-MM-dd-HH.mm.ss");
            String fileName = "photo_" + timeStampFormat.format(new Date())
                    + ".jpg";

            // Metadata pour la photo
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image prise par FormationCamera");
            values.put(MediaStore.Images.Media.DATE_TAKEN, new Date().getTime());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            // Support de stockage
            Uri taken = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);

            // Ouverture du flux pour la sauvegarde
            stream = (FileOutputStream) getContentResolver().openOutputStream(
                    taken);

            camera.takePicture(null, pictureCallback, pictureCallback);
        } catch (Exception e) {
            Log.d("FreeWay", "erreur en prenant la photo :" + e.getMessage());
        }
    }

    public Camera getCamera() {
        return this.camera;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (camera != null) {
            isPreview = false;
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        stopService(servDaemon);
    }
}
