
package com.example.spencer.swirlytap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class mySQLActivity extends ActionBarActivity implements View.OnClickListener{

    Button buttonLogIn;
    Button buttonRegister;
    Button buttonUpdate;
    Button buttonDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);
        buttonLogIn = (Button)findViewById(R.id.LogIn);
        buttonLogIn.setOnClickListener(this);     //sets an onClickListener on buttonLogIn
        buttonRegister = (Button)findViewById(R.id.Register);
        buttonRegister.setOnClickListener(this);      //sets an onClickListener on buttonRegister
        buttonUpdate = (Button)findViewById(R.id.UpDate);
        buttonUpdate.setOnClickListener(this);     //sets an onClickListener on buttonUpdate
        buttonDelete = (Button)findViewById(R.id.Delete);
        buttonDelete.setOnClickListener(this);

    }
    private void LogInClick()
    {

    }
    private void RegisterClick()
    {
        startActivity(new Intent(mySQLActivity.this,RegisterActivity.class));
        finish();

    }
    private void UpDateClick()
    {

    }
    private void DeleteClick()
    {

    }




    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.LogIn:
                LogInClick();
                break;
            case R.id.Register:
                RegisterClick();
                break;
            case R.id.UpDate:
                UpDateClick();
                break;
            case R.id.Delete:
                DeleteClick();
                break;

        }
    }
    }

