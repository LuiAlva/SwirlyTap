package com.example.spencer.swirlytap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;




public class mySQLActivity extends Activity
  {
   // EditText etUsername,etScore;
    Button bSubmit;
    //@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);
        bSubmit = (Button)findViewById(R.id. Submit);
        // bSubmit.setOnClickListener(this);


    }

}


