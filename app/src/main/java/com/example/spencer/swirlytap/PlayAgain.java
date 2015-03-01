package com.example.spencer.swirlytap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class PlayAgain extends ActionBarActivity implements View.OnClickListener  {
    Button buttonAgain;     //create type button for 'Play Again'
    Button buttonHome;      //create type button for 'Home'
    Button buttonShare;     //create type button for 'Share'
    Button buttonHighScore; //create type button for 'High Score'

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again);
        getSupportActionBar().hide();

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
        buttonHighScore = (Button)findViewById(R.id.HighScore);
        buttonHighScore.setOnClickListener(this); //sets an onClickListener on buttonHighScore

        Fabric.with(this, new TweetComposer());
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
    private void ShareClick()
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
    private void HighScoreClick()
    {   //High Score method
        Intent HStext = new Intent(PlayAgain.this,mySQLActivity.class);
        startActivity(HStext);
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
                ShareClick();     //share text and image
                break;
            case R.id.HighScore:
                HighScoreClick();
                break;
            /*if High Score is clicked... it will take you to a different screen
            to display the "Leader Board" */
        }
    }

////    TweetComposer.Builder builder = new TweetComposer.Builder(this)
////            .text("just setting up my Fabric.")
//            .image(R.drawable.goodswirl);
////            .image(Uri.parse(new File("/drawable/cosby.png").toString()));
//    builder.show();

}//end public class PlayAgain
