package com.dev.alt.devand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // url to get all products list
    private static String url_connection = "http://alt.moments.free.fr/requests/identify_user.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MAIL = "mail";
    private static final String SALAGE = "alt";

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.login);

        Button login = (Button) findViewById(R.id.tryLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectUser cu = new ConnectUser(((EditText) findViewById(R.id.login)).getText().toString(), ((EditText) findViewById(R.id.password)).getText().toString());
                cu.execute();
            }
        });

        TextView registration = (TextView) findViewById(R.id.Registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyActivity.this, Registration.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class ConnectUser extends AsyncTask<String, String, String> {
        private String login;
        private String password;

        protected ConnectUser(String l, String p) {
            this.login = l;
            this.password = p;
        }

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyActivity.this);
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("login", login));
            params.add(new BasicNameValuePair("pass", get_SHA_512_SecurePassword(password)));

            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_connection, "POST", params);

            // Check your log cat for JSON response
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), Connected.class);
                    i.putExtra("mail", json.getString(TAG_MAIL));
                    startActivity(i);
                } else {
                    // no products found
                    // Launch Add New product Activity
                    /*Intent i = new Intent(getApplicationContext(), NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
        }

        public String get_SHA_512_SecurePassword(String passwordToHash){
            String generatedPassword = null;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(SALAGE.getBytes("UTF-8"));
                byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
                StringBuilder sb = new StringBuilder();
                for(int i=0; i< bytes.length ;i++){
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                generatedPassword = sb.toString();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e){
                e.printStackTrace();
            }
            return generatedPassword;
        }
    }
}
