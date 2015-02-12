package com.example.spencer.swirlytap;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class singlePlayer extends ActionBarActivity  implements View.OnClickListener
{
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        // This Timer updates every 30 milliseconds, used for updating changing texts and
        // Images that constantly change
        new CountDownTimer(60000, 30) {

            //Get access to totalScore Textbox
            TextView totalScore= (TextView) findViewById(R.id.totalScore);

            public void onTick(long millisUntilFinished) {

                //Update totalScore Textbox with current score, end at 60 seconds
                if (millisUntilFinished / 30 == 0) {
                    onFinish();
                } else {
                    // Update Textfield
                    totalScore.setText("Score: " + count);
                }
            }

            // Show text at end of timer
            public void onFinish() {
                totalScore.setText(count + " Congratulations!");
            }
        }.start();

        /*CountDownTimer start = (<-Doesn't needs this)*/new CountDownTimer(60000, 1000)
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
                    mTextField.setText("Time: " + millisUntilFinished / 1000);
            }

            //stop time/game when time is up
            public void onFinish()
            {
                mTextField.setText("Finished!");
            }

            //player clicks on swirl add point

        }.start();

        levelOne();

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
/* updating file - me too*/