package com.gmail.dianaupham.swirlytap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.Toast;

import com.example.spencer.swirlytap.R;


public class RegisterActivity extends ActionBarActivity
{
    EditText  USER_NAME, USER_PASS, CON_PASS;
    String user_name,user_pass,con_pass;
    Button buttonREG;
    Context newctx = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        USER_NAME = (EditText) findViewById(R.id.editUserName);
        USER_PASS = (EditText) findViewById(R.id.editpassword);
        CON_PASS = (EditText) findViewById(R.id.editpassword1);
        buttonREG = (Button) findViewById(R.id.register);
        buttonREG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                user_name = USER_NAME.getText().toString();
                user_pass = USER_PASS.getText().toString();
                con_pass = CON_PASS.getText().toString();

                    if(!(user_pass.equals(con_pass)))
                    {
                        Toast.makeText(getBaseContext(),"Error! Password does not match! Please Try Again",Toast.LENGTH_LONG).show();
                        USER_NAME.setText("");
                        USER_PASS.setText("");
                        CON_PASS.setText("");


                    }
                    else
                    {
                        DatabaseOperations database = new DatabaseOperations(newctx);
                        database.InsertInfo(database, user_name, user_pass);
                        Toast.makeText(getBaseContext(), "You have Successfully registered!",Toast.LENGTH_LONG).show();
                        finish();


                    }

            }


        });

    }




}
