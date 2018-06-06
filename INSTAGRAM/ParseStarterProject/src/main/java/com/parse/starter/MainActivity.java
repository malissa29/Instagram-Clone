/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signupmodeactive = true;

  TextView changesignupmodetextView;

  EditText passwordeditText;

  public void showUserList()
  {
    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intent);
  }


  @Override
  public void onClick(View view) {

    if (view.getId() == R.id.changesignupmodetextView)
    {
      Button Signupbutton = (Button)findViewById(R.id.Signupbutton);

      if (signupmodeactive)
      {
          signupmodeactive = false;
          Signupbutton.setText("LOGIN");
          changesignupmodetextView.setText("or, SIGNUP");
      }
      else
      {
        signupmodeactive = true;
        Signupbutton.setText("SIGNUP");
        changesignupmodetextView.setText("or, LOGIN");
      }
    }
    else if (view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoimageView)
    {
      InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

  }

  public void signup (View view)
  {
    EditText usernameeditText = (EditText)findViewById(R.id.usernameeditText);



    if(usernameeditText.getText().toString().matches("") || passwordeditText.getText().toString().matches(""))
    {
      Toast.makeText(this, "A Username and Password are required", Toast.LENGTH_SHORT).show();
    }

    else
    {

      if (signupmodeactive) {

        ParseUser user = new ParseUser();

        user.setUsername(usernameeditText.getText().toString());

        user.setPassword(passwordeditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {

            if (e == null) {
              Log.i("Signup", "Successful");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
      else
      {
        ParseUser.logInInBackground(usernameeditText.getText().toString(), passwordeditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user != null )
            {
              Log.i("Signup", "Login Successful");
              showUserList();
            }
            else
            {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instagram");

    changesignupmodetextView = (TextView) findViewById(R.id.changesignupmodetextView);

    changesignupmodetextView.setOnClickListener(this);


    RelativeLayout backgroundRelativeLayout =(RelativeLayout) findViewById(R.id.backgroundRelativeLayout);

    ImageView logoimageView= (ImageView) findViewById(R.id.logoimageView);

    backgroundRelativeLayout.setOnClickListener(this);

    logoimageView.setOnClickListener(this);




    passwordeditText = (EditText)findViewById(R.id.passwordeditText);

    passwordeditText.setOnKeyListener(this);

    if (ParseUser.getCurrentUser() != null)
    {
      showUserList();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
    {
      signup(view);
    }
    return false;
  }
}