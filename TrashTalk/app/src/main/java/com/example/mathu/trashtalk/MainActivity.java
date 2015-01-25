package com.example.mathu.trashtalk;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.android.Facebook;

import java.io.Console;


public class MainActivity extends ActionBarActivity {

    private static String app_ID = "1598150887070304";
    private Facebook facebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    public  void Test(View view)
    {
        String name = "test2";
        String accountType = "com.example.mathu.trashtalk.account";
        Account account = new Account(name, accountType);
        Bundle extraData = new Bundle();
        extraData.putString("email", "test1@trashtalk.com");
        boolean accountCreated = false;
        try {
            AccountManager am = AccountManager.get(getApplicationContext());
            AuthenticatorDescription[] authenticators = am.getAuthenticatorTypes();
            for(int index=0; index < authenticators.length; index++)
            {
               String authVal = authenticators[index].type;
            }
            accountCreated = am.addAccountExplicitly(account, "password", null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //Toast toast = Toast.makeText(getApplicationContext(), "No error", Toast.LENGTH_LONG);
        //toast.show();
        if(accountCreated)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void SignUp(View view)
    {
        Intent formRegistration = new Intent(this, Register.class);
        startActivity(formRegistration);
    }
}
