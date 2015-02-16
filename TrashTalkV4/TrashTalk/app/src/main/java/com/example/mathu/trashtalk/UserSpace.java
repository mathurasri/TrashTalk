package com.example.mathu.trashtalk;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.facebook.android.Facebook;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class UserSpace extends ActionBarActivity {

    private Facebook facebook;
    LinearLayout popUpLayout;
    PopupWindow popupWindow;
    ImageView avatarPopup;
    GridView avatarsGrid;
    ProgressDialog pDialog;
    int avatarPosition;
    int avatarPath;
    JSONParser jsonParser = new JSONParser();
    String name, imageUri;
    ArrayList<String> keywordsList = null;
    JSONArray keywordsArray = null;
    private static String url_add_loginUserActivityInfo = "http://192.168.1.2:8080/series/LoginUserInfo.php";
    private static String url_download_loginUserActivityInfo = "http://192.168.1.2:8080/series/DownloadLoginUserInfo.php";
    private static String url_download_keywords = "http://192.168.1.2:8080/series/DownloadKeywords.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_space);
        facebook = new Facebook(getString(R.string.facebook_app_id));
        Intent intent =  getIntent();
        if(intent.getExtras() != null) {
            name = (String)intent.getExtras().get("username");
        }
        new DownloadKeyword().execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_space, menu);
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

    public void AvatarPopup(View view)
    {
        //s
        // etContentView(R.layout.popup_layout);
        //Toast.makeText(getApplicationContext(), "Image view button works", Toast.LENGTH_LONG).show();
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupview = inflater.inflate(R.layout.popup_layout, null);
        popupWindow = new PopupWindow(popupview, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        Button popupButton = (Button) findViewById(R.id.avatarSelect);
        //GridView avatarsGrid = (GridView) findViewById(R.id.avatarsView);
        GridView avatarsGrid = (GridView) popupview.findViewById(R.id.avatarsView);
        final ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext());
        avatarsGrid.setAdapter(new ImageAdapter(getApplicationContext()));
        popupWindow.showAsDropDown(popupButton, 50, 30);
        avatarsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               // Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                avatarPosition = ImageAdapter.avatars[position];
                new AddLoginUserActivityInfo().execute();
            }
        });
    }
    public void Continue(View view)
    {
        /*try {
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
        new DownloadLoginUserActivityInfo().execute();
    }
    public void Logout(View view)
    {
        /*if(facebook.isSessionValid()) {
            try {
                facebook.logout(this);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        //else
        //{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        //}
    }
        class AddLoginUserActivityInfo extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserSpace.this);
            pDialog.setMessage("Adding LoginUserActivity info..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args)  {

            //String name = loginUser.getText().toString();
            String status = null;
            avatarPath = avatarPosition;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(avatarPath);
            String imgUri = sb.toString();
            Log.d("name", name);
            Log.d("URI", imgUri);
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("imagePath", imgUri));

            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("Before: ", "HttpRequest");
            JSONObject json = jsonParser.makeHttpRequest(url_add_loginUserActivityInfo,"POST", params);

            // check log cat fro response
            Log.d("After: ", "HttpRequest");

            // check for success tag
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    // successfully created product
                    Log.d("Success", "Login user activity info added");
                    status = "Inserted";
                    // closing this screen
                    // finish();
                } else {
                    // failed to create product
                    Log.d("Fail", "could not log in user activity");
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
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("avatarPosition", avatarPosition);
                intent.putExtra("keywords", keywordsList);
                startActivity(intent);
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "User activity info alread exists", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }
    class DownloadLoginUserActivityInfo extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserSpace.this);
            pDialog.setMessage("Downloading LoginUserActivity info..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args)  {

            //String name = loginUser.getText().toString();
            String status = null;
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            Log.d("name",name);
            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("Before: ", "HttpRequest");
            JSONObject json = jsonParser.makeHttpRequest(url_download_loginUserActivityInfo,"POST", params);

            // check log cat fro response
            Log.d("After: ", "HttpRequest");

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Log.d("Success", "Downloaded Login userActivity");

                    imageUri = json.getString("imageUri");
                    status = "Inserted";
                    // closing this screen
                    // finish();
                } else {
                    // failed to create product
                    Log.d("Fail", "could not log in user activity");
                    ///Log.d("imageUri", imageUri);
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
                Toast toast = Toast.makeText(getApplicationContext(), "Downloaded Image", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                int imagePosition = Integer.parseInt(imageUri);
                intent.putExtra("avatarPosition", imagePosition);
                intent.putExtra("keywords", keywordsList);
                startActivity(intent);
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "User has not chosen image", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                //intent.putExtra("avatarPosition", imageUri);
                intent.putExtra("keywords", keywordsList);
                startActivity(intent);
            }
        }

    }
    class DownloadKeyword extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserSpace.this);
            pDialog.setMessage("Downloading Keywords...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            //String name = loginUser.getText().toString();
            String status = null;
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("Before: ", "HttpRequest");
            JSONObject json = jsonParser.makeHttpRequest(url_download_keywords, "POST", params);

            // check log cat fro response
            Log.d("After: ", "HttpRequest");

            // check for success tag
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    Log.d("Success", "Downloaded comments successfully");
                    try {
                        try {
                            keywordsArray = json.getJSONArray("keywords");
                            keywordsList = new ArrayList<String>();
                            for (int index = 0; index < keywordsArray.length(); index++) {
                                JSONObject obj = keywordsArray.getJSONObject(index);
                                String keyword = obj.getString("keyword");
                                keywordsList.add(keyword);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    status = "Inserted";
                } else {
                    // failed to create product
                    Log.d("Fail", "could not log in user activity");
                    ///Log.d("imageUri", imageUri);
                    status = "Not Inserted";
                }
            } catch (Exception e) {
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
            pDialog.dismiss();
            if (status.equals("Inserted")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Downloaded Keywords", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Could not get the keywords", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    }
