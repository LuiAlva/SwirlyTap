package com.gmail.dianaupham.swirlytap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.dianaupham.swirlytap.swirlytap.R;

public class Register extends Activity
{
    private DatabaseTableActivity myTable;
    EditText USER_NAME;
    EditText PASS_WORD;
    EditText CON_PASS;
    Button SUBMIT;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        myTable = new DatabaseTableActivity(this);
        USER_NAME = (EditText)findViewById(R.id.editUserName);
        PASS_WORD = (EditText)findViewById(R.id.editpassword);
        CON_PASS = (EditText)findViewById(R.id.editpassword1);
        SUBMIT = (Button)findViewById(R.id.register);
        SUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(myTable.InsertInfo(USER_NAME.getText().toString(), PASS_WORD.getText().toString())
                        && PASS_WORD.getText().toString().equals(CON_PASS.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),LoginScreen.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
