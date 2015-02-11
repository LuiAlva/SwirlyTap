package com.example.spencer.swirlytap;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

//test test
public class MainActivity extends ActionBarActivity implements View.OnClickListener {
Button button1; //create type button

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)findViewById(R.id.singlePlayer);
        button1.setOnClickListener(this); //sets an onClickListener on button1
    }
    private void singlePlayerClick()
    {
        //start single player activity once singlePlayer button clicked
        startActivity(new Intent("android.intent.action.SINGLE"));
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.singlePlayer:
                singlePlayerClick();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
