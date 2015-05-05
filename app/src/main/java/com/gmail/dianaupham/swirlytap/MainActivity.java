package com.gmail.dianaupham.swirlytap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.gmail.dianaupham.swirlytap.swirlytap.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mopub.common.MoPub;
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
    Button HIGHSCORES; //create type button for PlayAgainTest
    Button buttonLevel;
    Button buttonLogIn;
    Button buttonProfile;
    Button LOG_IN, REG, NoThanks;      // For Login screen
    CheckBox StopLogin;
    MediaPlayer mediaPlayer; //for music
    boolean NotLogged;
    PopupWindow popupWindow;
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
        Fabric.with(this, new TweetComposer(), new Crashlytics(), new MoPub());
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
        HIGHSCORES = (Button)findViewById(R.id.highscores);
        HIGHSCORES.setOnClickListener(this); //sets an onClickListener on HIGHSCORES
        buttonLevel = (Button) findViewById(R.id.levelMode);
        buttonLevel.setOnClickListener(this);
        mediaPlayer.setLooping(true);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean firstTime = prefs.getBoolean("FirstTimeSetup", true);
        if (firstTime) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("TimeHighScore1", 0);         // For Time Mode Local HighScores
            editor.putInt("TimeHighScore2", 0);
            editor.putInt("TimeHighScore3", 0);
            editor.putInt("TimeHighScore4", 0);
            editor.putInt("TimeHighScore5", 0);
            editor.putInt("TimeHighScore6", 0);
            editor.putInt("TimeHighScore7", 0);
            editor.putInt("TimeHighScore8", 0);
            editor.putInt("TimeHighScore9", 0);
            editor.putInt("TimeHighScore10", 0);
            editor.putString("TimeHighName1", "");
            editor.putString("TimeHighName2", "");
            editor.putString("TimeHighName3", "");
            editor.putString("TimeHighName4", "");
            editor.putString("TimeHighName5", "");
            editor.putString("TimeHighName6", "");
            editor.putString("TimeHighName7", "");
            editor.putString("TimeHighName8", "");
            editor.putString("TimeHighName9", "");
            editor.putString("TimeHighName10", "");
            editor.putInt("LevelHighScore1", 0);         // For Level Mode Local HighScores
            editor.putInt("LevelHighScore2", 0);
            editor.putInt("LevelHighScore3", 0);
            editor.putInt("LevelHighScore4", 0);
            editor.putInt("LevelHighScore5", 0);
            editor.putInt("LevelHighScore6", 0);
            editor.putInt("LevelHighScore7", 0);
            editor.putInt("LevelHighScore8", 0);
            editor.putInt("LevelHighScore9", 0);
            editor.putInt("LevelHighScore10", 0);
            editor.putString("LevelHighName1", "");
            editor.putString("LevelHighName2", "");
            editor.putString("LevelHighName3", "");
            editor.putString("LevelHighName4", "");
            editor.putString("LevelHighName5", "");
            editor.putString("LevelHighName6", "");
            editor.putString("LevelHighName7", "");
            editor.putString("LevelHighName8", "");
            editor.putString("LevelHighName9", "");
            editor.putString("LevelHighName10", "");
            editor.putString("PlayerName", "Player");// For player name
            editor.putInt("HighScore", 0);           // For player High Score
            editor.putBoolean("FirstTimeSetup", false);
            editor.commit();

        }

        // If not logged in send out login popup action
        boolean NotLoggedIn = prefs.getBoolean("NotLoggedIn", true);
        boolean AskLogin = prefs.getBoolean("AskLogin", true);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("NotLoggedIn",NotLoggedIn);
        editor.putBoolean("AskLogin", AskLogin);

        buttonLogIn = (Button)findViewById(R.id.LogIn);
        buttonLogIn.setOnClickListener(this); //sets an onClickListener on buttonLogIn
        buttonProfile = (Button)findViewById(R.id.Profile);
        buttonProfile.setOnClickListener(this); //sets an onClickListener on buttonProfile
        if (prefs.getBoolean("NotLoggedIn", true))
            buttonProfile.setVisibility(View.GONE); //removes this button from view, and makes space for others
        else
            buttonLogIn.setVisibility(View.GONE); //removes this button from view, and makes space for others
        editor.commit();
        new CountDownTimer(800,800) {

            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1000 == 0)
                {
                    onFinish();
                }
                else
                {}
            }
            public void onFinish()
            {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                if(prefs.getBoolean("NotLoggedIn", true) && prefs.getBoolean("AskLogin", true) ) {
                    LoginScreen();
                }
            }
        }.start();

        TextView login_message = (TextView)findViewById(R.id.LoginMessage);
        NotLogged = prefs.getBoolean("NotLoggedIn", true);
        if(NotLogged){ login_message.setText("Not logged in");}
        else { login_message.setText("Welcome " + prefs.getString("PlayerName", "Player")); }
    }

    private void singlePlayerClick()
    {
        //start single player activity once singlePlayer button clicked
        startActivity(new Intent(MainActivity.this, SinglePlayer.class));
        finish();
    }
    private void HighScores()
    {
        //test button for PlayAgain activity (for quicker access)
        Intent intentAgain2 = new Intent(MainActivity.this, HighScoreActivity.class);
        intentAgain2.putExtra("LoadTimedScores", true);
        startActivity(intentAgain2);//goes to HighScore activity
    }
    private void playLevelClick()
    {
        startActivity(new Intent (MainActivity.this, levelPlay.class));
        finish();
    }
    private void LogInClick() {   //LogIn method
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        NotLogged = prefs.getBoolean("NotLoggedIn", true);
        if( NotLogged ) {
            Intent login = new Intent(MainActivity.this, WelcomeScreen.class);
            startActivity(login);
            finish();
        } else {
            Intent profile = new Intent(MainActivity.this, Profile.class);
            startActivity(profile);
            finish();
        }
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.singlePlayer:
                mediaPlayer.pause(); //stop song
                singlePlayerClick();
                break;
            case R.id.highscores:
                mediaPlayer.pause(); //stop song
                mediaPlayer.release();
                HighScores();
                break;
            case R.id.levelMode:
                mediaPlayer.pause(); //stop song
                playLevelClick();
                break;
            case R.id.LogIn:
                mediaPlayer.pause(); //stop song
                LogInClick();
                break;
            case R.id.Profile:
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

    // * For login screen +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    public void LoginScreen() {
        try {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.welcome_screen, (ViewGroup)findViewById(R.id.login_Layout));
            popupWindow = new PopupWindow(layout, getWindow().getAttributes().width, getWindow().getAttributes().height, true);
            popupWindow.setAnimationStyle(-1);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            LOG_IN = (Button)findViewById(R.id.login1);
            REG = (Button)findViewById(R.id.register1);
            NoThanks = (Button)findViewById(R.id.stop_asking);
            LOG_IN.setOnClickListener(this);
            REG.setOnClickListener(this);
            NoThanks.setOnClickListener(this);
            StopLogin = (CheckBox)findViewById(R.id.checkBox1);
            StopLogin.setEnabled(true);
            StopLogin.setOnClickListener(this);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void register(View v) {
        Intent i = new Intent(getApplicationContext(),Register.class);
        startActivity(i);
        popupWindow.dismiss();
    }

    public  void login(View v) {
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        popupWindow.dismiss();
    }

    public  void no_thanks(View v) {
        popupWindow.dismiss();
    }

    public  void stop_asking(View v) {
        CheckBox StopLogin = (CheckBox) v.findViewById(R.id.checkBox1);
        //StopLogin.toggle();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (StopLogin.isChecked() == true) {
            editor.putBoolean("AskLogin", false);
            editor.commit();
        }
        else {
            editor.putBoolean("AskLogin", true);
            editor.commit();
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
}//end public class Main Activity