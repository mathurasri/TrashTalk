package com.example.mathu.trashtalk;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class UserActivity extends ActionBarActivity {

    EditText comment;
    TextView viewComment;
    ProgressDialog pDialog;
    int position;
    String txt;
    ArrayList<String> uploadKeywordsList = null;
    ArrayList<String> keywordsList = null;
    JSONArray commentsArray = null, keywordsArray = null;
    JSONParser jsonParser = new JSONParser();
    String transformedSentence = null;
    String untransformedSentence = null;
    String[][]commentInfo = null;
    String keyword = null;
    private static String url_store_comments = "http://192.168.1.2:8080/series/StoreComments.php";
    private static String url_download_comments = "http://192.168.1.2:8080/series/DownloadCommentInfo.php";
    private static String url_download_keywords = "http://192.168.1.2:8080/series/DownloadKeywords.php";
    private static String url_store_keywords = "http://192.168.1.2:8080/series/StoreKeywords.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ImageView img = (ImageView)findViewById(R.id.imageView);
            Intent intent =  getIntent();
            if(intent.getExtras().get("avatarPosition") != null) {
                //String imgposition = (String)intent.getExtras().get("avatarPosition");
                //if(imgposition != null) {
                    position = (Integer) intent.getExtras().get("avatarPosition");
                    img.setImageResource(position);
                //}
            }
        if(intent.getExtras().get("keywords")!= null)
        {
            keywordsList = (ArrayList<String>)intent.getExtras().get("keywords");
        }
            comment = (EditText)findViewById(R.id.comment);
            viewComment = (TextView)findViewById(R.id.commentsText);
            //viewComment.setText(Integer.toString(keywordsList.size()));
            new DownloadCommentInfo().execute();
    }
    public void PostComment(View view)
    {
        uploadKeywordsList = new ArrayList<String>();
        /*txt = comment.getText().toString();
        LinearLayout l1 = (LinearLayout) findViewById(R.id.linear1);
        l1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout l2 = new LinearLayout(this);
        l1.addView(l2);

        ImageView i2 = new ImageView(this);
        i2.setImageResource(position);
        l2.addView(i2);
        TextView t2 = new TextView(this);
        t2.setText(txt);
        l2.addView(t2);*/
        untransformedSentence = comment.getText().toString();
        comment.setText("");
        transformedSentence = untransformedSentence;
        BuildKeywordList();
        TransformComment();
        LinearLayout l1 = (LinearLayout) findViewById(R.id.linear1);
        l1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout l2 = new LinearLayout(this);
        l1.addView(l2);

        ImageView i2 = new ImageView(this);
        i2.setImageResource(position);
        l2.addView(i2);
        TextView t2 = new TextView(this);
        t2.setText(transformedSentence);
        l2.addView(t2);
        new UploadKeywords().execute();
        //TransformComment();
        /*String str = "-Jas #Java; String to String Array Example";
        String strArray[] = str.split("[, '' ; -]");
        viewComment.setText(strArray[2]);*/
        //*/
        //new AddComment().execute();

    }
    public void BuildKeywordList()
    {
        String[] strArray = untransformedSentence.split("[, ' ' ; - . \\t \\n \\r \\f]");
        for(int i = 0; i < strArray.length; i++)
        {
            if(strArray[i].charAt(0) == '#')
            {
                char[] charArray = strArray[i].toCharArray();
                StringBuilder sb = new StringBuilder();
                for(int j = 1; j < charArray.length; j++)
                {
                    sb.append(charArray[j]);
                }
                uploadKeywordsList.add(sb.toString());
                keywordsList.add(sb.toString());
            }
        }
    }
    public void TransformComment()
    {
        String[] strArray = untransformedSentence.split("[, ' ' ; - . \\t \\n \\r \\f]");
        for(int index = 0; index < strArray.length; index++)
        {
            if(strArray[index].charAt(0) != '#')
            {
                if(keywordsList.contains(strArray[index]))
                {
                    //strArray[index] = "#" + strArray[index];
                    String target = " " + strArray[index] + " ";
                    String replacement = " " + "#" + strArray[index] + " ";
                    //transformedSentence = transformedSentence.replace(target, replacement);
                    transformedSentence = transformedSentence.replaceAll(target, replacement);
                    target = "^"+strArray[index] + " ";
                    replacement = "#" + strArray[index] + " ";
                    transformedSentence = transformedSentence.replaceAll(target, replacement);
                    target = " " + strArray[index] + " ";
                    replacement = " " + "#" + strArray[index] + " ";
                    transformedSentence = transformedSentence.replaceAll(target, replacement);
                    target = " " + strArray[index] + "$";
                    replacement = " " + "#" + strArray[index] + ".";
                    transformedSentence = transformedSentence.replaceAll(target, replacement);
                    target = "," + strArray[index] + " ";
                    replacement = ","+"#" + strArray[index] + " ";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = " " + strArray[index] + ",";
                    replacement = " "+"#" + strArray[index] + ",";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = "," + strArray[index] + ",";
                    replacement = ","+"#" + strArray[index] + ",";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = ";" + strArray[index] + " ";
                    replacement = ";"+"#" + strArray[index] + " ";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = " " + strArray[index] + ";";
                    replacement = " "+"#" + strArray[index] + ";";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = ";" + strArray[index] + ";";
                    replacement = ";"+"#" + strArray[index] + ";";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = " " + strArray[index] + "-";
                    replacement = " "+"#" + strArray[index] + "-";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = "-" + strArray[index] + " ";
                    replacement = "-"+"#" + strArray[index] + " ";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = "-" + strArray[index] + "-";
                    replacement = "-"+"#" + strArray[index] + "-";
                    transformedSentence = transformedSentence.replace(target, replacement);
                    target = "." + strArray[index] + " ";
                    replacement = "."+"#" + strArray[index] + " ";
                    transformedSentence = transformedSentence.replace(target, replacement);
                }
            }
        }
    }
    public void PopulateComments()
    {
        for(int index = 0; index < commentInfo.length; index++) {
            String comment = commentInfo[index][0];
            int avaPos = Integer.parseInt(commentInfo[index][1]);
            LinearLayout l1 = (LinearLayout) findViewById(R.id.linear1);
            //l1.setOrientation(LinearLayout.VERTICAL);
            LinearLayout l2 = new LinearLayout(this);
            l1.addView(l2);

            ImageView i2 = new ImageView(this);
            i2.setImageResource(avaPos);
            l2.addView(i2);
            TextView t2 = new TextView(this);
            t2.setText(comment);
            l2.addView(t2);
        }
    }

    class UploadKeywords extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserActivity.this);
            pDialog.setMessage("Downloading comment info..");
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
            for(int i = 0; i < uploadKeywordsList.size(); i++) {
                params.add(new BasicNameValuePair("keyword[]", uploadKeywordsList.get(i)));
               // params.add(new BasicNameValuePair("keyword[]", dummyKeywordsArray[1]));
            }
            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("Before: ", "HttpRequest");
            JSONObject json = jsonParser.makeHttpRequest(url_store_keywords,"POST", params);

            // check log cat fro response
            Log.d("After: ", "HttpRequest");

            // check for success tag
            try {
                int success = json.getInt("success");
                if(success == 1)
                {
                    Log.d("Success", "keywords inserted successfully");
                    status = "Inserted";
                }
                else
                {
                    // failed to create product
                    Log.d("Fail", "could not log insert keywords successfully");
                    ///Log.d("imageUri", imageUri);
                    status = "Not Inserted";
                }
            }
            catch (Exception e)
            {
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
                Toast toast = Toast.makeText(getApplicationContext(), "Inserted Keywords successfully", Toast.LENGTH_LONG);
                toast.show();
                new AddComment().execute();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Could not insert the keywords", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

    class AddComment extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserActivity.this);
            pDialog.setMessage("Adding User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args)  {
            String status = null;
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("avatar", Integer.toString(position)));
            params.add(new BasicNameValuePair("comment", transformedSentence));

            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("Before: ", "HttpRequest");
            JSONObject json = jsonParser.makeHttpRequest(url_store_comments,"POST", params);

            // check log cat fro response
            Log.d("After: ", "HttpRequest");

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Log.d("Success", "comment added");
                    status = "Inserted";
                    // closing this screen
                    // finish();
                } else {
                    // failed to create product
                    Log.d("Fail", "could not add comment");
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
                Toast toast = Toast.makeText(getApplicationContext(), "Comment added", Toast.LENGTH_LONG);
                toast.show();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Could not add comment", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

    class DownloadCommentInfo extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserActivity.this);
            pDialog.setMessage("Downloading comment info..");
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
            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("Before: ", "HttpRequest");
            JSONObject json = jsonParser.makeHttpRequest(url_download_comments,"POST", params);

            // check log cat fro response
            Log.d("After: ", "HttpRequest");

            // check for success tag
            try {
                int success = json.getInt("success");
                if(success == 1)
                {
                    Log.d("Success", "Downloaded comments successfully");
                    try {
                        commentsArray = json.getJSONArray("usercomments");
                        commentInfo = new String[commentsArray.length()][2];
                        for (int index = 0; index < commentsArray.length(); index++) {
                            JSONObject obj = commentsArray.getJSONObject(index);
                            String comment = obj.getString("comment");
                            String avatarPos = obj.getString("avatar");
                            int avaPos = Integer.parseInt(avatarPos);
                            commentInfo[index][0] = comment;
                            commentInfo[index][1] = avatarPos;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                        status = "Inserted";
                }
                else
                {
                    // failed to create product
                    Log.d("Fail", "could not log in user activity");
                    ///Log.d("imageUri", imageUri);
                    status = "Not Inserted";
                }
            }
            catch (Exception e)
            {
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
                PopulateComments();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Could not populate the comment screen", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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
}
