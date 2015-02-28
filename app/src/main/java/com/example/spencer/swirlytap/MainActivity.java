package com.example.spencer.swirlytap;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import io.fabric.sdk.android.Fabric;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "TtVaQegijoQY8zOHSgAX0kvEv";
    private static final String TWITTER_SECRET = "RcSbTspCQY3atAkXFYmqRoOOZrJK7ZBiBEhpMsroVbJXrwM70G";
    Button buttonSinglePlayer; //create type button
    Button buttonPlayAgainTest; //create type button for PlayAgainTest
    MediaPlayer mediaPlayer; //for music

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        if (!Fabric.isInitialized()) {
            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics(), new Twitter(authConfig), new TweetComposer())
                    .debuggable(true)
                    .build();
        Fabric.with(fabric);
        }
        Fabric.with(this, new TweetComposer());

        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.title_song); //get song
        //mediaPlayer.start(); //start song
        buttonSinglePlayer = (Button)findViewById(R.id.singlePlayer);
        buttonSinglePlayer.setOnClickListener(this); //sets an onClickListener on buttonSinglePlayer
        buttonPlayAgainTest = (Button)findViewById(R.id.PlayAgainMain);
        buttonPlayAgainTest.setOnClickListener(this); //sets an onClickListener on buttonPlayAgainTest
        getSupportActionBar().hide();//hide the ActionBar (full screen)
    }
    private void singlePlayerClick()
    {
        //start single player activity once singlePlayer button clicked
        startActivity(new Intent(MainActivity.this, SinglePlayer.class));
        finish();
    }
    private void PlayAgainTestClick()
    {
        //test button for PlayAgain activity (for quicker access)
        startActivity(new Intent(MainActivity.this, PlayAgain.class));
        finish();
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.singlePlayer:
                //mediaPlayer.stop(); //stop song
                singlePlayerClick();
                break;
            case R.id.PlayAgainMain:
                PlayAgainTestClick();
                break;
        }
    }
    //@Override
    //protect void on
}//end public class Main Activity
