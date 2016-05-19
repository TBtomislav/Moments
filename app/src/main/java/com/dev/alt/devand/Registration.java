package com.dev.alt.devand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Hachiman on 13/05/2016.
 */
public class Registration extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration);

//test
        Button login = (Button) findViewById(R.id.butCancel);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logAction = new Intent(Registration.this, MyActivity.class);
                startActivity(logAction);
            }
        });
    }
}
