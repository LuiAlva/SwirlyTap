package com.gmail.dianaupham.swirlytap;


import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.spencer.swirlytap.R;

public class DatabaseMainActivity extends Activity
{
    //public final static String MESS_AGE = "MESSAGE IN A BOTTLE";
   // private ListView ListObj;
    private Button buttonLogin,buttonRegister,buttonUpdate,buttonDelete,buttonAllUsers;

    DatabaseTableActivity myTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);
        buttonLogin = (Button) findViewById(R.id.LogIn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
        buttonRegister = (Button) findViewById(R.id.Register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);

            }
        });
        buttonUpdate = (Button) findViewById(R.id.UpDate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

        myTable = new DatabaseTableActivity(this);
    }
}
