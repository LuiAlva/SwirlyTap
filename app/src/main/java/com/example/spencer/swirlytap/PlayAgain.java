package com.example.spencer.swirlytap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PlayAgain extends ActionBarActivity implements View.OnClickListener {
    Button buttonAgain;  //create type button for 'Play Again'
    Button buttonHome;   //create type button for 'Home'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again);
        getSupportActionBar().hide();
        Intent game = getIntent(); // Grab the the intent of game that ended
        int score = game.getIntExtra("score", 0); //Grab score from game
        buttonAgain = (Button)findViewById(R.id.PlayAgain);
        buttonHome = (Button) findViewById(R.id.returnHome);
        buttonAgain.setOnClickListener(this);  //sets an onClickListener on buttonAgain
        buttonHome.setOnClickListener(this); //sets an onClickListener on buttonAgain
        TextView Score= (TextView) findViewById(R.id.textView);
        Score.setText("" + score + " points!"); // Set text to show score
    }

    private void PlayAgainClick()
    {   //start single player activity once "Play Again" button clicked
        Intent intentAgain2 = new Intent(PlayAgain.this, SinglePlayer.class);
        startActivity(intentAgain2);//goes to singlePlayer activity
        finish();
    }
    private void HomeClick()
    {   //sgo back to MainActivity (Home) once "Home" button clicked
        Intent intentReturnHome = new Intent(PlayAgain.this, MainActivity.class);
        startActivity(intentReturnHome);//returns to Home screen
        finish();
    }
    public void onClick(View v)
    {//when "Play Again" button is clicked on the Play Again activity menu
        switch(v.getId())
        {
            case R.id.PlayAgain: //if "Play Again" is clicked
                PlayAgainClick();  //re-start Single Player
                break;
            case R.id.returnHome: //if "Home" is clicked
                HomeClick();      //return to Home screen (MainActivity)
                break;
        }
    }

}//end public class PlayAgain
