package com.dev.alt.devand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Menu extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mail = "";

        setContentView(R.layout.menu);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            //---------Récupère informations------------
            mail = extras.getString("mail");


        }

        TextView login = (TextView) findViewById(R.id.disconnect);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logAction = new Intent(Menu.this, Connection.class);
                startActivity(logAction);
            }
        });
    }
}
