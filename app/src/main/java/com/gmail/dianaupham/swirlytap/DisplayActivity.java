package com.gmail.dianaupham.swirlytap;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spencer.swirlytap.R;

public class DisplayActivity extends Activity
{
    private DatabaseTableActivity dta;
    TextView user_name;
    TextView pass_word;
    //TextView Score = (TextView) findViewById(R.)

    @Override
    protected void  onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_layout);
        //user_name = (TextView) findViewById(R.)
    }


}
