package com.example.mathu.trashtalk;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;


public class Register extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
    public void AddUsers(View view)
    {
        boolean userNameFlag = ValidateUserName();
        boolean passwordFlag = ValidatePassword();
        boolean emailFlag = ValidateEmail();
        // Intent intent = new Intent(this, UserArea.class);
        //startActivity(intent);
        if(userNameFlag && passwordFlag && emailFlag) {
            TextView tvUserName;
            tvUserName = (TextView) findViewById(R.id.userNameSetText);
            EditText etUserName;
            etUserName = (EditText)findViewById(R.id.enterRegisterUserName);
            String userName;
            userName = etUserName.getText().toString();
            EditText etPassword;
            etPassword = (EditText)findViewById(R.id.enterSignInPwd);
            String password;
            password = etPassword.getText().toString();
            EditText etEmail;
            etEmail = (EditText) findViewById(R.id.enterEmail);
            String email;
            email = etEmail.getText().toString();

            String accountType = "TrashTalk";
            Account account = new Account(userName, accountType);
            Bundle extraData = new Bundle();
            extraData.putString("email", email);
            boolean accountCreated = false;
            try {
                AccountManager am = AccountManager.get(this);
                accountCreated = am.addAccountExplicitly(account, password, extraData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Toast toast = Toast.makeText(getApplicationContext(), "No error", Toast.LENGTH_LONG);
            //toast.show();
            if (accountCreated) {
                Toast toast = Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_LONG);
                toast.show();
            } else {
                tvUserName.setText("Username already exists");
            }
        }
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
}
