package com.example.spencer.swirlytap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import com.example.spencer.swirlytap.util.SystemUiHider;
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

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.example.spencer.swirlytap.util.SystemUiHider#getInstance}.
    private SystemUiHider mSystemUiHider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Hide Actionbar ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again);
        getSupportActionBar().hide();
        MediaPlayer mediaPlayer; //for sound
        mediaPlayer = MediaPlayer.create(this, R.raw.game_success); //get success sound

        Intent game = getIntent(); // Grab the the intent of game that ended
        int score = game.getIntExtra("score", 0); //Grab score from game
        TextView Score= (TextView) findViewById(R.id.textView);
        Score.setText("" + score + " points!");   //Set text to show score

        buttonAgain = (Button)findViewById(R.id.PlayAgain);
        buttonAgain.setOnClickListener(this);     //sets an onClickListener on buttonAgain
        buttonHome = (Button)findViewById(R.id.returnHome);
        buttonHome.setOnClickListener(this);      //sets an onClickListener on buttonHome
        buttonShare = (Button)findViewById(R.id.Share);
        buttonShare.setOnClickListener(this);     //sets an onClickListener on buttonShare

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
    private void ShareClick2()
    {   //use Implicit Intent to share promotional text/image of application
        Uri imageUri = Uri.parse("android.resource://" + getPackageName()
                + "/drawable/" + "goodswirl.png");                  //temp. use goodswirl
        Intent shareIntent = new Intent(Intent.ACTION_SEND); //allows delivery of image and text
        shareIntent.setType("*/*");                                 //send any generic data
        shareIntent.putExtra(Intent.EXTRA_TEXT, "SwirlyTap :)");    //text shared: "@SwirlyTap :)"
        String fileTemp = "file://" + Environment.getExternalStorageDirectory()
                + File.separator + "goodswirl.png";
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileTemp));
        //shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);        //includes image
        //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//read URI
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));  //choose sharing app
    }
    public void ShareClick(View view){
        //sharing implementation
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Uri imageUri = Uri.parse("android.resource://" + getPackageName()
                + "/drawable/" + "goodswirl.png");                  //temp. use goodswirl
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); //allows delivery of image and text
        sharingIntent.setType("*/*");                                 //send any generic data
        String shareBody = "SwirlyTap :) https://twitter.com/SwirlyTap";//text+URL

        PackageManager pm = view.getContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for(final ResolveInfo app : activityList) {
            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
            targetedShareIntent.setType("*/*");                                 //send any generic data
            targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out SwirlyTap!");
            String fileTemp = "file://" + Environment.getExternalStorageDirectory()
                    + File.separator + "goodswirl.png";
            targetedShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileTemp));
            if(TextUtils.equals(packageName, "com.facebook.katana")){
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://twitter.com/SwirlyTap"); //will only accept URL
            } else {
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);//share text+URL
            }
            targetedShareIntent.setPackage(packageName);
            targetedShareIntents.add(targetedShareIntent);
        }
        Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Share Idea");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
        startActivity(chooserIntent);
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
            case R.id.Share:      //if "Share" is clicked
                final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                        .findViewById(android.R.id.content)).getChildAt(0);     //set view
                ShareClick(viewGroup);//share text+URL and image
                //ShareClick2();     //share text and image
                break;

            /*if High Score is clicked... it will take you to a different screen
            to display the "Leader Board" */
        }
    }

////    TweetComposer.Builder builder = new TweetComposer.Builder(this)
////            .text("just setting up my Fabric.")
//            .image(R.drawable.goodswirl);
////            .image(Uri.parse(new File("/drawable/goodswirl.png").toString()));
//    builder.show();

}//end public class PlayAgain
