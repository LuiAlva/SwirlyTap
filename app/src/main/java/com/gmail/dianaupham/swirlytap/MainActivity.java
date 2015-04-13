package com.gmail.dianaupham.swirlytap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.example.spencer.swirlytap.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends ActionBarActivity implements View.OnClickListener/*,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener*/ {

    private static final String TWITTER_KEY = "TtVaQegijoQY8zOHSgAX0kvEv";
    private static final String TWITTER_SECRET = "RcSbTspCQY3atAkXFYmqRoOOZrJK7ZBiBEhpMsroVbJXrwM70G";
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    Button buttonSinglePlayer; //create type button
    Button buttonPlayAgainTest; //create type button for PlayAgainTest
    Button buttonLevel;
    Button buttonLogIn;
    MediaPlayer mediaPlayer; //for music
    public static final String PREFS_NAME = "PREFS_FILE";

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 2000; // Hide system UI after 2000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.gmail.dianaupham.swirlytap.SystemUiHider#getInstance}.
    private SystemUiHider mSystemUiHider;

    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

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
        Fabric.with(this, new TweetComposer(), new Crashlytics());
/*
        // Create the Google Api Client with access to the Play Game services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                        // add other APIs and scopes here as needed
                .build();

        // ...
*/
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
        mediaPlayer.setLooping(true);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean firstTime = prefs.getBoolean("FirstTimeSetup", true);
        if (firstTime) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FirstTimeSetup", false);
            editor.putInt("HighScore1", 0);
            editor.putInt("HighScore2", 0);
            editor.putInt("HighScore3", 0);
            editor.putInt("HighScore4", 0);
            editor.putInt("HighScore5", 0);
            editor.commit();
        }
        //if(!mediaPlayer.isPlaying()) { mediaPlayer.start(); }
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
        Intent login = new Intent(MainActivity.this, DatabaseMainActivity.class);
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
        if (!mediaPlayer.isPlaying()) { mediaPlayer.start(); }
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // The player is signed in. Hide the sign-in button and allow the
        // player to proceed.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // Message: "There was an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, "Error")) {  //change from "Error" from signin_other_error to signin_failure
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. Message: "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }
    }

    // Call when the sign-in button is clicked
    private void signInClicked() {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    // Call when the sign-out button is clicked
    private void signOutclicked() {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
    }
    */
}//end public class Main Activity