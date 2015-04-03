
package com.gmail.dianaupham.swirlytap;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.example.spencer.swirlytap.R;


public class mySQLActivity extends ActionBarActivity implements View.OnClickListener {

    Button buttonLogIn;
    Button buttonRegister;
    Button buttonUpdate;
    Button buttonDelete;
   // int status = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);
        buttonRegister = (Button)findViewById(R.id.Register);
        buttonRegister.setOnClickListener(this);      //sets an onClickListener on buttonRegister
        buttonUpdate = (Button) findViewById(R.id.UpDate);
        buttonUpdate.setOnClickListener(this);     //sets an onClickListener on buttonUpdate
        buttonDelete = (Button) findViewById(R.id.Delete);
        buttonDelete.setOnClickListener(this);
        buttonLogIn = (Button) findViewById(R.id.LogIn);
        buttonLogIn.setOnClickListener(this);
        //sets an onClickListener on buttonLogIn


    }



    private void LogInClick()
    {

    }

    private void RegisterClick()
    {

        startActivity(new Intent(mySQLActivity.this, RegisterActivity.class));
        finish();

    }

    private void UpDateClick()
    {

    }

    private void DeleteClick()
    {

    }


    @Override
    public void onClick(View a1) {

        switch (a1.getId()) {
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


