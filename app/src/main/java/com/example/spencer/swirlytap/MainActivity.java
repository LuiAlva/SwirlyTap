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

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "TtVaQegijoQY8zOHSgAX0kvEv";  //old: ehTIivAXeXu2UdBfeA5hVsdcr
    private static final String TWITTER_SECRET = "RcSbTspCQY3atAkXFYmqRoOOZrJK7ZBiBEhpMsroVbJXrwM70G";  //old: kyVa6p8fty5D4T2UYkmb9fdzFzK14uflxZobpYZzmEidnGj3RW
    Button buttonSinglePlayer; //create type button
    MediaPlayer mediaPlayer; //for music

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        if (!Fabric.isInitialized()) {
            //TwitterAuthConfig authConfig
            //        = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.TWITTER_SECRET);
            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics(), new Twitter(authConfig), new TweetComposer())
                    .debuggable(true)
                    .build();

        Fabric.with(fabric);
        }
        //Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        Fabric.with(this, new TweetComposer());

        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.title_song); //get song
        //mediaPlayer.start(); //start song
        buttonSinglePlayer = (Button)findViewById(R.id.singlePlayer);
        buttonSinglePlayer.setOnClickListener(this); //sets an onClickListener on buttonSinglePlayer
        getSupportActionBar().hide();//hide the ActionBar (full screen)
    }
    private void singlePlayerClick()
    {
        //start single player activity once singlePlayer button clicked
        startActivity(new Intent(MainActivity.this, GameTest.class));
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
        }
    }


}//end public class Main Activity
