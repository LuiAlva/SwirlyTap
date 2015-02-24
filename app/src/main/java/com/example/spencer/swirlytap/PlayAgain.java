package com.example.spencer.swirlytap;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import java.io.File;
import java.io.FileOutputStream;
import io.fabric.sdk.android.Fabric;


public class PlayAgain extends ActionBarActivity implements View.OnClickListener  {
    Button buttonAgain;  //create type button for 'Play Again'
    Button buttonHome;  //create type button for 'Home'
    Button buttonShare;  //create type button for 'Share'
    Button buttonHighScore; // crete type button for ' High Score'

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_again);
        getSupportActionBar().hide();
        Intent game = getIntent(); // Grab the the intent of game that ended
        int score = game.getIntExtra("score", 0); //Grab score from game
        buttonAgain = (Button)findViewById(R.id.PlayAgain);
        buttonHome = (Button) findViewById(R.id.returnHome);
        buttonAgain.setOnClickListener(this);  //sets an onClickListener on buttonAgain
        buttonHome.setOnClickListener(this); //sets an onClickListener on buttonAgain
        TextView Score= (TextView) findViewById(R.id.textView);
        Score.setText("" + score + " points!"); // Set text to show score
        buttonHome = (Button)findViewById(R.id.returnHome);
        buttonHome.setOnClickListener(this);  //sets an onClickListener on buttonHome
        buttonShare = (Button)findViewById(R.id.Share);
        buttonShare.setOnClickListener(this);  //sets an onClickListener on buttonShare
        buttonHighScore = (Button)findViewById(R.id.HighScore);
        buttonHighScore.setOnClickListener(this);
        Fabric.with(this, new TweetComposer());

        //TwitterAuthConfig authConfig = new TwitterAuthConfig("consumerKey","consumerSecret");
        //Fabric.with(this, new Twitter(authConfig));
        // Example: multiple kits
        // Fabric.with(this, new Twitter(authConfig),
        //                  new Crashlytics());
    }//end 'onCreate'

    private void PlayAgainClick()
    {   //start single player activity once "Play Again" button clicked
        Intent intentAgain2 = new Intent(PlayAgain.this, GameTest.class);
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
    {   //use Implicit Intent to share promotional text of application
        Intent shareText = new Intent("android.intent.action.SEND");
        shareText.setType("text/plain");
        shareText.putExtra("android.intent.extra.TEXT", "@SwirlyTap :)");
        startActivity(Intent.createChooser(shareText, getResources().getString(R.string.share)));
    }
    private void HighScoreClick()
    {
        Intent HStext = new Intent(PlayAgain.this,PrettyScreen.class);
        startActivity(HStext);

    }

    public void onClick(View v)
    {//when "Play Again" button is clicked on the Play Again activity menu
        switch(v.getId())
        {
            case R.id.PlayAgain: //if "Play Again" is clicked
                PlayAgainClick();  //re-start Single Player
                break;
            case R.id.returnHome: //if "Home" is clicked
                HomeClick();      //return to Home screen (MainActivity)
                break;
            case R.id.Share:      //if "Share" is clicked
                ShareClick();     //share screenshot
                Bitmap bitmap = takeScreenshot();
                break;
            case R.id.HighScore:
                 HighScoreClick();
                 break;

        }
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public Bitmap Share(View v) {
        // Image
        v.setDrawingCacheEnabled(true);
        v.setLayerType(View.LAYER_TYPE_NONE, null);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "temporary_file.jpg");
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Share
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        String filel = "file://" + Environment.getExternalStorageDirectory()
                + File.separator + "temporary_file.png";
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filel));

        startActivity(Intent.createChooser(share, "Share Image"));
        return bitmap;
    }

    TweetComposer.Builder builder = new TweetComposer.Builder(this)
            .text("just setting up my Fabric.")
//            .image(R.drawable.goodswirl);
            .image(Uri.parse(new File("/drawable/cosby.png").toString()));
//    builder.show();

//    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//    sharingIntent.getType("text/plain"); //sharingIntent.setType("text/html");
//    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is the text that will be shared.");
//    startActivity(Intent.createChooser(sharingIntent,"Share using"));

}//end public class PlayAgain
