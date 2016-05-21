package com.dev.alt.devand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dev.alt.devand.helper.PersonEntity;
import com.dev.alt.devand.helper.PersonRepository;

public class MainMenu extends Activity {

    private PersonEntity pe;
    PersonRepository pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        pr = new PersonRepository(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String login = extras.getString("login");

            if(pr.existPerson(login)) {
                pe = pr.getPerson(login);
            } else {
                Intent returnConnection = new Intent(MainMenu.this, Connection.class);
                returnConnection.putExtra("err", "Try to connect first");
                startActivity(returnConnection);
            }
        } else {
            Intent returnConnection = new Intent(MainMenu.this, Connection.class);
            returnConnection.putExtra("err", "Try to connect first");
            startActivity(returnConnection);
        }

        // If user is not connected
        if(pe.getLoggedIn() == 0) {
            Intent returnConnection = new Intent(MainMenu.this, Connection.class);
            returnConnection.putExtra("err", "Try to connect first");
            startActivity(returnConnection);
        }

        TextView name = (TextView) findViewById(R.id.tv_name);
        name.setText(pe.getLogin());

        TextView login = (TextView) findViewById(R.id.disconnect);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pe.setLoggedIn(0);
                pr.updatePerson(pe);

                Intent logAction = new Intent(MainMenu.this, Connection.class);
                startActivity(logAction);
            }
        });
    }
}
