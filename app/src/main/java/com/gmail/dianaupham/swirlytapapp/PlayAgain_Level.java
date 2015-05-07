package com.gmail.dianaupham.swirlytapapp;

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

import com.gmail.dianaupham.swirlytapapp.swirlytap.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayAgain_Level extends ActionBarActivity implements View.OnClickListener  {
    Button buttonAgainLevel;     //create type button for 'Play Again'
    Button buttonHomeLevel;      //create type button for 'Home'
    Button buttonShareLevel;     //create type button for 'Share'
    Button buttonHighScoreLevel; //create type button for 'High Score'
    MediaPlayer mediaPlayer;// For sounds
    int HighScore;          // For HighScore
    public static final String PREFS_NAME = "PREFS_FILE";  // Name of Preference file

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Hide Actionbar ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again__level);
        getSupportActionBar().hide();
        mediaPlayer = MediaPlayer.create(this, R.raw.game_success); //get success sound
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        HighScore = prefs.getInt("LevelHighScore1", 0);

        Intent game = getIntent(); // Grab the the intent of game that ended
        int score = game.getIntExtra("score", 0); //Grab score from game
        int good_buttons = game.getIntExtra("good_swirl", 0); //grab number of good buttons clicked
        int bad_buttons = game.getIntExtra("bad_swirl", 0);
        int twice_buttons = game.getIntExtra("twice_swirl", 0);
        int double_buttons = game.getIntExtra("double_points", 0);
        int lightning_buttons = game.getIntExtra("lightning_bolt", 0);
        int heart_buttons = game.getIntExtra("add_life", 0);
        int nuke = game.getIntExtra("nuke", 0);
        TextView Score= (TextView) findViewById(R.id.ScoreViewLevel);
        TextView goodButtons = (TextView)findViewById(R.id.good_swirl_count);
        TextView badButtons = (TextView)findViewById(R.id.bad_swirl_count);
        TextView twiceButtons = (TextView)findViewById(R.id.twice_swirl_count);
        TextView doubleButtons = (TextView)findViewById(R.id.double_point_count);
        TextView lightningButtons = (TextView)findViewById(R.id.lightning_bolt_count);
        TextView lifeButtons = (TextView)findViewById(R.id.life_count);
        TextView nukeButtons = (TextView)findViewById(R.id.nukeCount);

        Score.setText("" + score + " points!");   //Set text to show recent score
        goodButtons.setText(""+good_buttons);
        badButtons.setText(""+bad_buttons);
        twiceButtons.setText(""+twice_buttons);
        doubleButtons.setText(""+double_buttons);
        lightningButtons.setText(""+lightning_buttons);
        lifeButtons.setText(""+heart_buttons);
        nukeButtons.setText(""+nuke);

        TextView BEST_SCORE= (TextView) findViewById(R.id.BestScoreLevel);
        BEST_SCORE.setText("Best: " + HighScore);   //Set text to show best score

        buttonAgainLevel = (Button)findViewById(R.id.PlayAgainLevel);
        buttonAgainLevel.setOnClickListener(this);     //sets an onClickListener on buttonAgain
        buttonHomeLevel = (Button)findViewById(R.id.returnHomeLevel);
        buttonHomeLevel.setOnClickListener(this);      //sets an onClickListener on buttonHome
        buttonShareLevel = (Button)findViewById(R.id.ShareLevel);
        buttonShareLevel.setOnClickListener(this);     //sets an onClickListener on buttonShare
        buttonHighScoreLevel = (Button)findViewById(R.id.HighScoreLevel);
        buttonHighScoreLevel.setOnClickListener(this);     //sets an onClickListener on buttonHighScore

        mediaPlayer.start(); //start success sound
    }//end 'onCreate'

    private void PlayAgainLevelClick()
    {   //start Level Play activity once "Play Again" button clicked
        Intent intentAgain2 = new Intent(PlayAgain_Level.this, levelPlay.class);
        startActivity(intentAgain2);//goes to Level Play activity
        finish();
    }
    private void HomeClick()
    {   //go back to MainActivity (Home) once "Home" button clicked
        Intent intentReturnHome = new Intent(PlayAgain_Level.this, MainActivity.class);
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
        String shareBodyTwitter = "Check out my score on Level Mode for @SwirlyTap! https://twitter.com/SwirlyTap";//text+URL
        String shareBodyText = "Check out my score on SwirlyTap's Level Mode! https://twitter.com/SwirlyTap"; //TODO: replace twitter link with GooplePlay URL
        String shareBody = "Check out my score on SwirlyTap's Level Mode! https://twitter.com/SwirlyTap";//text+URL

        PackageManager pm = view.getContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for(final ResolveInfo app : activityList) {
            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
            targetedShareIntent.setType("*/*");                                         //send any generic data
            targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out SwirlyTap!"); //subject line on emails
            String fileTemp = "file://" + Environment.getExternalStorageDirectory()     //image location
                    + File.separator + "screenshotLevel.png";
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
    {   //start HighScore activity once "Play Again" button clicked
        Intent intentAgain2 = new Intent(PlayAgain_Level.this, HighScoreActivity.class);
        intentAgain2.putExtra("LoadLevelScores", true);
        startActivity(intentAgain2);//goes to HighScore activity
    }

    public void onClick(View v)
    {//when "Play Again" button is clicked on the Play Again activity menu
        switch(v.getId())
        {
            case R.id.PlayAgainLevel:  //if "Play Again" is clicked
                PlayAgainLevelClick(); //re-start Level Play
                break;
            case R.id.returnHomeLevel: //if "Home" is clicked
                HomeClick();      //return to Home screen (MainActivity)
                break;
            case R.id.HighScoreLevel: //if "HighScore" is clicked
                HighScoreClick();      // Go to high score activity (HighScoreActivity)
                break;
            case R.id.ShareLevel:      //if "Share" is clicked
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
