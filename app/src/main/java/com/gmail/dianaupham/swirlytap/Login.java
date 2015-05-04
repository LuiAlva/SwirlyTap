package com.gmail.dianaupham.swirlytap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.dianaupham.swirlytap.swirlytap.R;

public class Login extends Activity
{
    private DatabaseTableActivity myTable;
    EditText USER_NAME;
    EditText PASS_WORD;
    Button SUBMIT;
    public static final String PREFS_NAME = "PREFS_FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        myTable = new DatabaseTableActivity(this);
        USER_NAME = (EditText)findViewById(R.id.editUsername1);
        PASS_WORD = (EditText)findViewById(R.id.editText2);
        SUBMIT = (Button)findViewById(R.id.button);
        SUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(myTable.Login(USER_NAME.getText().toString(), PASS_WORD.getText().toString()))
                {
                    myTable.setlogin(USER_NAME.getText().toString(),PASS_WORD.getText().toString(),1);
                    Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("PlayerName", USER_NAME.getText().toString() );
                    editor.putBoolean("NotLoggedIn", false);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), DatabaseMainActivity.class);
                    Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("USERNAME",USER_NAME.getText().toString());
                    bundle.putString("PASSWORD", PASS_WORD.getText().toString());
                    i.putExtras(bundle);
                    //startActivity(i);
                    startActivity(i2);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Incorrect Login - Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
