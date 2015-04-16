package com.gmail.dianaupham.swirlytap;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.spencer.swirlytap.R;

public class Update extends Activity
{
    private DatabaseTableActivity myTable;
    EditText NEW_USER;
    EditText NEW_PASS;
    Button bsubmit;

    @Override
    protected void OnCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);
        myTable = new DatabaseTableActivity(this);
        NEW_USER = (EditText)findViewById(R.id.username2);
        NEW_PASS = (EditText)findViewById(R.id.password2);
        bsubmit = (Button) findViewById(R.id.UpDateButton);
        bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(myTable.updateInfo(id,NEW_USER.getText().toString(),NEW_PASS.getText().toString(),score) )
                {

                }


            }
        });





    }


}
