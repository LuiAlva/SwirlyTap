package com.example.spencer.swirlytap;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.example.spencer.swirlytap.util.SystemUiHider;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "TtVaQegijoQY8zOHSgAX0kvEv";
    private static final String TWITTER_SECRET = "RcSbTspCQY3atAkXFYmqRoOOZrJK7ZBiBEhpMsroVbJXrwM70G";
    Button buttonSinglePlayer; //create type button
    Button buttonPlayAgainTest; //create type button for PlayAgainTest
    Button buttonLevel;
    Button buttonLogIn;
    MediaPlayer mediaPlayer; //for music

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 2000; // Hide system UI after 2000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.example.spencer.swirlytap.util.SystemUiHider#getInstance}.
    private SystemUiHider mSystemUiHider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//hide the ActionBar (full screen)

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
        buttonSinglePlayer = (Button)findViewById(R.id.singlePlayer);
        buttonSinglePlayer.setOnClickListener(this); //sets an onClickListener on buttonSinglePlayer
        buttonPlayAgainTest = (Button)findViewById(R.id.PlayAgainMain);
        buttonPlayAgainTest.setOnClickListener(this); //sets an onClickListener on buttonPlayAgainTest
        buttonLevel = (Button) findViewById(R.id.levelMode);
        buttonLevel.setOnClickListener(this);
        buttonLogIn = (Button)findViewById(R.id.LogIn);
        buttonLogIn.setOnClickListener(this); //sets an onClickListener on buttonHighScore
        mediaPlayer.isLooping();
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
    private void playLevelClick()
    {
        startActivity(new Intent (MainActivity.this, levelPlay.class));
        finish();
    }
    private void LogInClick() {   //LogIn method
        Intent login = new Intent(MainActivity.this, mySQLActivity.class);
        startActivity(login);
        finish();
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.singlePlayer:
                mediaPlayer.pause(); //stop song
                singlePlayerClick();
                break;
            case R.id.PlayAgainMain:
                mediaPlayer.pause(); //stop song
                PlayAgainTestClick();
                break;
            case R.id.levelMode:
                mediaPlayer.pause(); //stop song
                playLevelClick();
                break;
            case R.id.LogIn:
                mediaPlayer.pause(); //stop song
                LogInClick();
                break;
        }
    }

    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(this, R.raw.title_song); //get song
        mediaPlayer.setLooping(true);    //make background song loop
        mediaPlayer.start();
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
}//end public class Main Activity
