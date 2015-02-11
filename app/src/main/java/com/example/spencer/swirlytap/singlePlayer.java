package com.example.spencer.swirlytap;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class singlePlayer extends ActionBarActivity  implements View.OnClickListener
{
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        TextView totalScore= (TextView) findViewById(R.id.totalScore);
        totalScore.setText("Score: " + count);

        levelOne();
        //countdown from 60 seconds ...
        CountDownTimer start = new CountDownTimer(60000, 1000)
        {
            //create new type textview and relate it to countdown textbox in activity_single_player.xml
            TextView mTextField = (TextView) findViewById(R.id.countdown);

            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1000 == 0)
                {
                    onFinish();
                }
                else
                    //display seconds left in text field
                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

            }

            //stop time/game when time is up
            public void onFinish()
            {
                mTextField.setText("done!");
            }

            //player clicks on swirl add point

        }.start();


    }

    ImageButton goodSwirl; //create image button
    public void levelOne()//display level one on screen
    {

        //after all buttons are clicked by user call levelTwo function
        goodSwirl = (ImageButton) findViewById(R.id.levelOne);
        goodSwirl.setOnClickListener(this); //sets an onClickListener on button1

    }
    @Override
    public void onClick(View v) //if clicked item id matches button then call swirlClick function
    {
        switch(v.getId())
        {
            case R.id.levelOne:
                swirlClick();
                break;
        }
    }

    public void swirlClick()
    {
        count++;//add point to total points
        //display new score
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_player, menu);
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
