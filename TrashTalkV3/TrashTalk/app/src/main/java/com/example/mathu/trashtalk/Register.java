package com.example.mathu.trashtalk;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;


public class Register extends ActionBarActivity {

    private ProgressDialog pDialog;
    EditText username;
    EditText password;
    EditText email;
    JSONParser jsonParser = new JSONParser();
    private static String url_add_user = "http://192.168.1.2:8080/series/file.php";
    private static final String TAG_SUCCESS = "success";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d("Log", "onCreate");
        username = (EditText) findViewById(R.id.enterRegisterUserName);
        password = (EditText) findViewById(R.id.enterSignInPwd);
        email = (EditText) findViewById(R.id.enterEmail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
    public void AddUsers(View view) throws UnsupportedEncodingException, MalformedURLException, IOException
    {
        Log.d("AddUsers", "USers ADd");
        boolean userNameFlag = ValidateUserName();
        boolean passwordFlag = ValidatePassword();
        boolean emailFlag = ValidateEmail();
       /* if(userNameFlag && passwordFlag && emailFlag)
        {
            //new AddNewProduct().execute();
            new UserAddTest(this, 1).execute("ms");
        }*/
        new AddNewProduct().execute();
    }

    public boolean ValidateEmail()
    {
        boolean emailFlag = false;
        EditText etEmail;
        String email;
        etEmail = (EditText)findViewById(R.id.enterEmail);
        email = etEmail.getText().toString();
        TextView emailText;
        emailText = (TextView) findViewById(R.id.emailSetText);
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        emailFlag = Pattern.matches(emailPattern, email);
        if(emailFlag) {
            emailFlag = true;
        }
        return emailFlag;
    }
    public boolean ValidateUserName()
    {
        boolean userNameFlag = false;
        EditText etUserName;
        String userName;
        etUserName = (EditText)findViewById(R.id.enterRegisterUserName);
        userName = etUserName.getText().toString();
        TextView tvUserName;
        tvUserName = (TextView)findViewById(R.id.userNameSetText);

        if(userName.length() == 0)
        {
            tvUserName.setText("*User name should be more than 3 in length");
            userNameFlag = false;
        }
        else if(userName.length() < 3)
        {
            tvUserName.setText("*User name should be more than 3 in length");
            userNameFlag = false;
        }
        else if(userName.length() > 8)
        {
            tvUserName.setText("*User name should not be more than 8 in length");
            userNameFlag = false;
        }
        else if((Pattern.matches("^[a-zA-Z]+([A-Za-z0-9])+", userName))== false)
        {
            tvUserName.setText("*Username should have alpha numeric characters only starting with alphabets");
            userNameFlag = false;
        }
        else
        {
            userNameFlag=true;
        }
        return userNameFlag;
    }
    public boolean ValidatePassword()
    {
        boolean passwordFlag = false;
        EditText etPassword;
        String password;
        EditText etConfirmPassword;
        String confirmPassword;
        etPassword = (EditText) findViewById(R.id.enterSignInPwd);
        password = etPassword.getText().toString();
        etConfirmPassword = (EditText) findViewById(R.id.confirmEnterSignInPwd);
        confirmPassword = etConfirmPassword.getText().toString();
        TextView tvPassword = (TextView)findViewById(R.id.passwordSetText);
        TextView tvConfirmPassword = (TextView) findViewById(R.id.confirmPwdSetText);
        if(password.length() == 0)
        {
            tvPassword.setText("*Password cannot be blank and its length should be greater >= 5");
        }
        else if(password.length() < 5)
        {
            tvPassword.setText("Password should be more than length 5");
        }
        else if(!password.equals(confirmPassword))
        {
            tvPassword.setText("Password and confirm password should be equal");
        }
        else
        {
            passwordFlag =  true;
        }
        return passwordFlag;
    }

    public void Test() throws UnsupportedEncodingException, MalformedURLException, IOException
    {
            /*String name = username.getText().toString();
            String Password = password.getText().toString();
            String Email = email.getText().toString();*/

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", "success"));
            /*params.add(new BasicNameValuePair("password", Password));
            params.add(new BasicNameValuePair("email", Email));*/

        // getting JSON Object
        // Note that create product url accepts POST method
        Log.d("Before: ", "HttpRequest");
        JSONObject json = jsonParser.makeHttpRequest(url_add_user,"POST", params);

        // check log cat fro response
        Log.d("After: ", "HttpRequest");
        //Log.d("Create Response", json.toString());

        // check for success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created product
                Log.d("Success", "user name added");
                // closing this screen
                // finish();
            } else {
                // failed to create product
                Log.d("Fail", "could not add user name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class AddNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Adding User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args)  {
            String name = username.getText().toString();
            String Password = password.getText().toString();
            String Email = email.getText().toString();
            String status = null;
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("password", Password));
            params.add(new BasicNameValuePair("email", Email));

            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("Before: ", "HttpRequest");
            JSONObject json = jsonParser.makeHttpRequest(url_add_user,"POST", params);

            // check log cat fro response
            Log.d("After: ", "HttpRequest");

            // check for success tag
           try {
                  int success = json.getInt(TAG_SUCCESS);

                   if (success == 1) {
                    // successfully created product
                        Log.d("Success", "user name added");
                       status = "Inserted";
                    // closing this screen
                        // finish();
                } else {
                    // failed to create product
                  Log.d("Fail", "could not add user name");
                       status = "Not Inserted";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String status) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if(status.equals("Inserted"))
            {
                Toast toast = Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG);
                toast.show();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Username, email or both already exists", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }
}
