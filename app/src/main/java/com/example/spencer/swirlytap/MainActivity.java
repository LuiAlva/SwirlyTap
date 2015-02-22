package com.example.spencer.swirlytap;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    Button buttonSinglePlayer; //create type button
    MediaPlayer mediaPlayer; //for music

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.title_song); //get song
        //mediaPlayer.start(); //start song
        buttonSinglePlayer = (Button)findViewById(R.id.singlePlayer);
        buttonSinglePlayer.setOnClickListener(this); //sets an onClickListener on buttonSinglePlayer
        getSupportActionBar().hide();
    }
    private void singlePlayerClick()
    {
        //start single player activity once singlePlayer button clicked
        startActivity(new Intent(MainActivity.this, SinglePlayer.class)); //startActivity(new Intent("android.intent.action.SINGLE"));
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.singlePlayer:
                //mediaPlayer.stop(); //stop song
                singlePlayerClick();
                break;
        }
    }

}//end public class Main Activity
