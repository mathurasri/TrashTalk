package com.example.mathu.trashtalk;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static String app_ID = "1598150887070304";
    private EditText userName;
    private EditText password;
    String name;
    String Password;
    JSONParser jsonParser = new JSONParser();
    private static String url_add_user = "http://192.168.1.2:8080/series/SelectUsers.php"; //
    private static final String TAG_SUCCESS = "success";
    int success;
    private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    Button fbLogin, fbLogout;
    private SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebook = new Facebook(app_ID);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        fbLogin = (Button) findViewById(R.id.facebookLoginBtn);
        fbLogout = (Button) findViewById(R.id.facebookLoginBtn);
        UpdateButton();
    }

    private void UpdateButton()
    {
        if(facebook.isSessionValid())
        {
            fbLogin.setText("Logout from facebook");
        }
        else
        {
            fbLogin.setText(getString(R.string.facebook_login));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void FBLogin(View view)
    {
        mPrefs = getPreferences(MODE_PRIVATE);
        /*String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }*/
        //mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.commit();
        if(facebook.isSessionValid())
        {
            Intent intent = new Intent(getApplicationContext(), UserSpace.class);
            startActivity(intent);
        }
        else
        {
            facebook.authorize(this, new String[]{"email"},new Facebook.DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    //UpdateButton();
                   /* SharedPreferences.Editor editor = mPrefs.edit();
                    editor.clear();
                    editor.commit();*/
                    Intent intent = new Intent(getApplicationContext(), UserSpace.class);
                    startActivity(intent);
                }

                @Override
                public void onFacebookError(FacebookError e) {
                    Toast.makeText(getApplicationContext(), "Facebookerror", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(DialogError e) {
                    Toast.makeText(getApplicationContext(), "Facebook on error", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Facebook cancel", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }

    public void Login(View view)
    {
        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.Password);
        name = userName.getText().toString();
        Password = password.getText().toString();
        new CredentialCheck().execute();

    }
    public void SignUp(View view)
    {
        Intent formRegistration = new Intent(this, Register.class);
        startActivity(formRegistration);
    }
    class CredentialCheck extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Pre", "execute");
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            Log.d("USer name", name);
            String status = null;
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("password", Password));

            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("Before: ", "HttpRequest");
            JSONObject json = jsonParser.makeHttpRequest(url_add_user, "POST", params);
            //Log.d("JSON", json.toString());
            // check log cat fro response
            Log.d("After: ", "HttpRequest");

            // check for success tag
            try {
                success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Log.d("Success", "Row found");
                    status = "success";
                    // closing this screen
                    // finish();
                } else {
                    // failed to create product
                    Log.d("Fail", "Row not found");
                    status="fail";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String status) {
            // dismiss the dialog once done
            if(status.equals("success"))
            {
                Log.d("Login", "Success");
                Intent intent = new Intent(getApplicationContext(), UserSpace.class);
                intent.putExtra("username", name);
                startActivity(intent);
            }
            else
            {
                Log.d("Login", "Error");
                Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_LONG).show();
            }
        }
    }

}
