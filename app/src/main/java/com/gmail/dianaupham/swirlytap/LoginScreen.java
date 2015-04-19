package com.gmail.dianaupham.swirlytap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.spencer.swirlytap.R;

public class LoginScreen extends Activity
{
   private DatabaseTableActivity myTable;
    Button LOG_IN, REG;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_layout);
        myTable = new DatabaseTableActivity(this);
        LOG_IN = (Button)findViewById(R.id.login1);
        REG = (Button)findViewById(R.id.register1);
        LOG_IN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);

                //setContentView(R.layout.login_layout);

            }
        });
        REG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);

            }
        });
    }




}
