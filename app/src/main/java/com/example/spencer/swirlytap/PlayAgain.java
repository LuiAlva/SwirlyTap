package com.example.spencer.swirlytap;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class PlayAgain extends ActionBarActivity implements View.OnClickListener {
    Button buttonAgain;  //create type button for 'Play Again'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again);
        buttonAgain = (Button)findViewById(R.id.PlayAgain);
        buttonAgain.setOnClickListener(this);  //sets an onClickListener on buttonAgain
    }

    private void PlayAgainClick()
    {   //start single player activity once "Play Again" button clicked
        Intent intentAgain2 = new Intent(PlayAgain.this, singlePlayer.class);
        startActivity(intentAgain2);
    }
    public void onClick(View v)
    {//when "Play Again" button is clicked on the Play Again activity menu
        switch(v.getId())
        {
            case R.id.PlayAgain:
                PlayAgainClick();
                break;
        }
    }

}//end public class PlayAgain
