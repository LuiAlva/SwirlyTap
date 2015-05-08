package com.gmail.dianaupham.swirlytapapp;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.dianaupham.swirlytapapp.swirlytap.R;

public class Update extends Activity
{
    private DatabaseTableActivity myTable;
    EditText NEW_USER;
    EditText NEW_PASS;
    Button bsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);
        myTable = new DatabaseTableActivity(this);
        NEW_USER = (EditText)findViewById(R.id.username2);
        NEW_PASS = (EditText)findViewById(R.id.password2);
        bsubmit = (Button) findViewById(R.id.button3);
       bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
           int score = 0;
           if(myTable.updateScore(NEW_USER.getText().toString(), NEW_PASS.getText().toString(),score)){
               Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
               //Intent i = new Intent(getApplicationContext(),.class);
               //startActivity(i);
           } else {
               Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
           }
           }
       });
    }
}
