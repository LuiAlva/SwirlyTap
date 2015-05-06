package com.gmail.dianaupham.swirlytap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.dianaupham.swirlytap.swirlytap.R;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class PlayAgain extends ActionBarActivity implements View.OnClickListener  {
    Button buttonAgain;     //create type button for 'Play Again'
    Button buttonHome;      //create type button for 'Home'
    Button buttonShare;     //create type button for 'Share'
    Button buttonHighScore; //create type button for 'High Score'
    MediaPlayer mediaPlayer;// For sounds
    int HighScore;          // For HighScore
    public static final String PREFS_NAME = "PREFS_FILE";  // Name of Preference file

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.gmail.dianaupham.swirlytap.SystemUiHider#getInstance}.
    private SystemUiHider mSystemUiHider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Hide Actionbar ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again);
        getSupportActionBar().hide();
        mediaPlayer = MediaPlayer.create(this, R.raw.game_success); //get success sound
        // Get HighScore
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        HighScore = prefs.getInt("TimeHighScore1", 0);

        Intent game = getIntent(); // Grab the the intent of game that ended
        int score = game.getIntExtra("score", 0); //Grab score from game
        int GoodSwirls = game.getIntExtra("GoodSwirls", 0);
        int BadSwirls = game.getIntExtra("BadSwirls", 0);
        int Good2Swirls = game.getIntExtra("Good2Swirls", 0);
        int TimeSwirls = game.getIntExtra("TimeSwirls", 0);

        TextView Score= (TextView) findViewById(R.id.ScoreView);
        Score.setText("" + score + " points!");   //Set text to show score
        TextView BEST_SCORE= (TextView) findViewById(R.id.BestScore);
        BEST_SCORE.setText("Best: " + HighScore);   //Set text to show score
        TextView GoodCount= (TextView) findViewById(R.id.Good_Swirl_Counter);
        GoodCount.setText(""+ GoodSwirls);   //Set text to show count
        TextView BadCount= (TextView) findViewById(R.id.Bad_Swirl_Counter);
        BadCount.setText(""+ BadSwirls);   //Set text to show count
        TextView Good2Counter= (TextView) findViewById(R.id.Good2_Swirl_Counter);
        Good2Counter.setText(""+ Good2Swirls);   //Set text to show count
        TextView TimeCounter= (TextView) findViewById(R.id.Time_Swirl_Counter);
        TimeCounter.setText(""+ TimeSwirls);   //Set text to show count

        buttonAgain = (Button)findViewById(R.id.PlayAgain);
        buttonAgain.setOnClickListener(this);     //sets an onClickListener on buttonAgain
        buttonHome = (Button)findViewById(R.id.returnHome);
        buttonHome.setOnClickListener(this);      //sets an onClickListener on buttonHome
        buttonShare = (Button)findViewById(R.id.Share);
        buttonShare.setOnClickListener(this);     //sets an onClickListener on buttonShare
        buttonHighScore = (Button)findViewById(R.id.HighScore);
        buttonHighScore.setOnClickListener(this);     //sets an onClickListener on buttonHighScore

        Fabric.with(this, new TweetComposer());
        mediaPlayer.start(); //start success sound
    }//end 'onCreate'

    private void PlayAgainClick()
    {   //start single player activity once "Play Again" button clicked
        Intent intentAgain2 = new Intent(PlayAgain.this, SinglePlayer.class);
        startActivity(intentAgain2);//goes to singlePlayer activity
        finish();
    }
    private void HomeClick()
    {   //go back to MainActivity (Home) once "Home" button clicked
        Intent intentReturnHome = new Intent(PlayAgain.this, MainActivity.class);
        startActivity(intentReturnHome);//returns to Home screen
        finish();
    }
    public void ShareClick(View view){
        //sharing implementation
        Resources resources = getResources();

        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Uri imageUri = Uri.parse("android.resource://" + getPackageName()
                + "/drawable/" + "goodswirl.png");                  //temp. use goodswirl
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); //allows delivery of image and text
        sharingIntent.setType("*/*");                               //send any generic data
        String shareBodyTwitter = "Check out my score on @SwirlyTap! https://twitter.com/SwirlyTap";//text+URL
        String shareBodyText = "Check out my score on SwirlyTap! https://twitter.com/SwirlyTap"; //TODO: replace twitter link with GooplePlay URL
        String shareBody = "Check out my score on SwirlyTap! https://twitter.com/SwirlyTap";//text+URL

        PackageManager pm = view.getContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for(final ResolveInfo app : activityList) {
            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
            targetedShareIntent.setType("*/*");                                         //send any generic data
            targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out SwirlyTap!"); //subject line on emails
            String fileTemp = "file://" + Environment.getExternalStorageDirectory()     //image location
                    + File.separator + "screenshot.png";
            targetedShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileTemp));     //share image
            if(TextUtils.equals(packageName, "com.facebook.katana")){                   //share message specific to Facebook
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://twitter.com/SwirlyTap"); //will only accept URL
            }else if(packageName.contains("mms")) {                                     //if sharing via Text Message
                targetedShareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            }else if(packageName.contains("twitter")){                                  //if sharing via Twitter
                targetedShareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyTwitter);      //Twitter-specific message
            }else {                                                                     //message for other sharing apps
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);//share text+URL along with image
            }
            targetedShareIntent.setPackage(packageName);
            targetedShareIntents.add(targetedShareIntent);
        }
        Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Share Idea");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
        startActivity(chooserIntent);
    }

    private void HighScoreClick()
    {   //start single player activity once "Play Again" button clicked
        Intent intentAgain2 = new Intent(PlayAgain.this, HighScoreActivity.class);
        intentAgain2.putExtra("LoadTimedScores", true);
        startActivity(intentAgain2);//goes to HighScore activity
    }

    public void onClick(View v)
    {//when "Play Again" button is clicked on the Play Again activity menu
        switch(v.getId())
        {
            case R.id.PlayAgain:  //if "Play Again" is clicked
                PlayAgainClick(); //re-start Single Player
                break;
            case R.id.returnHome: //if "Home" is clicked
                HomeClick();      //return to Home screen (MainActivity)
                break;
            case R.id.HighScore:  //if "HighScore" is clicked
                HighScoreClick(); // Go to high score activity (HighScoreActivity)
                break;
            case R.id.Share:      //if "Share" is clicked
                final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                        .findViewById(android.R.id.content)).getChildAt(0);     //set view
                ShareClick(viewGroup);//share text+URL and image
                break;
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();

        mediaPlayer.release();
    }

    public void onBackPressed() {
        HomeClick();      //return to Home screen (MainActivity)
    }

}//end public class PlayAgain
