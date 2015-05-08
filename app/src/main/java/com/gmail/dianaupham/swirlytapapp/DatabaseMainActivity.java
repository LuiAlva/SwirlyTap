package com.gmail.dianaupham.swirlytapapp;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gmail.dianaupham.swirlytapapp.swirlytap.R;

public class DatabaseMainActivity extends Activity
{
    //public final static String MESS_AGE = "MESSAGE IN A BOTTLE";
   // private ListView ListObj;
    private Button buttonUpdate,buttonDelete,buttonAllUsers,buttonGame;

    DatabaseTableActivity myTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);
        /*buttonLogin = (Button) findViewById(R.id.LogIn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
        buttonRegister = (Button) findViewById(R.id.register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);

            }
        });*/
        buttonUpdate = (Button) findViewById(R.id.UpDate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Update.class);
                startActivity(i);


            }
        });
        buttonUpdate.setVisibility(View.GONE); //removes this button from view, and makes space for others
        buttonDelete = (Button) findViewById(R.id.Delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonAllUsers = (Button) findViewById(R.id.Users);
        buttonAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonAllUsers.setVisibility(View.GONE); //removes this button from view, and makes space for others
        buttonGame = (Button) findViewById(R.id.starts_game);
        buttonGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

            }
        });

        myTable = new DatabaseTableActivity(this);
    }
}
