package com.gmail.dianaupham.swirlytap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.dianaupham.swirlytap.swirlytap.R;

import static com.gmail.dianaupham.swirlytap.swirlytap.R.drawable;
import static com.gmail.dianaupham.swirlytap.swirlytap.R.id;
import static com.gmail.dianaupham.swirlytap.swirlytap.R.layout;


public class HighScoreActivity extends Activity {

    public static final String PREFS_NAME = "PREFS_FILE";
    TextView SCOREBOX, NAMEBOX;
    ImageView TITLE;
    Button BACK, LEVEL, TIMED, LOCAL, OVERALL;
    Boolean Level, Timed;
    MediaPlayer mediaPlayer; //for music

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.gmail.dianaupham.swirlytap.SystemUiHider#getInstance}.
    private com.gmail.dianaupham.swirlytap.SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide Actionbar ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(layout.activity_high_score);
        TITLE = (ImageView)findViewById(id.Title);
        mediaPlayer = MediaPlayer.create(this, R.raw.highscore_song); //get song
        mediaPlayer.setLooping(true);
        LOCAL = (Button) findViewById(id.LocalTab);
        LOCAL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalScores(Level, Timed);
            }});
        OVERALL = (Button) findViewById(id.LocalTab);
        OVERALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseScores(Level, Timed);
            }
        });
        BACK = (Button) findViewById(id.Back);
        BACK.setOnClickListener( new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         finish();
                                     }});
        LEVEL =(Button)findViewById(id.Level);
        LEVEL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Level = true; Timed = false;
                TITLE.setImageResource(drawable.highscore_level_title);
                LocalScores(Level, Timed);
            }});
        TIMED =(Button)findViewById(id.Timed);
        TIMED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Level = false;
                Timed = true;
                TITLE.setImageResource(drawable.highscore_timed_title);
                LocalScores(Level, Timed);
            }
        });
        Intent game = getIntent(); // Grab the the intent of game that ended
        Level  = game.getBooleanExtra("LoadLevelScores", false); // Load Level highscores?
        Timed = game.getBooleanExtra("LoadTimedScores", false);  // Load Timed highscores?
        if (Level == true){ TITLE.setImageResource(drawable.highscore_level_title); }
        if (Timed == true){ TITLE.setImageResource(drawable.highscore_timed_title); }
        //Load Table +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        LocalScores(Level, Timed);
    }

    public void LocalScores(Boolean Level, Boolean Timed) {
        if(Level == true) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SCOREBOX = (TextView) findViewById(id.Name1);                  // Set High Score 1
            NAMEBOX = (TextView) findViewById(id.Score1);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore1", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName1", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name2);                  // Set High Score 2
            NAMEBOX = (TextView) findViewById(id.Score2);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore2", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName2", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name3);                  // Set High Score 3
            NAMEBOX = (TextView) findViewById(id.Score3);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore3", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName3", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name4);                  // Set High Score 4
            NAMEBOX = (TextView) findViewById(id.Score4);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore4", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName4", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name5);                  // Set High Score 5
            NAMEBOX = (TextView) findViewById(id.Score5);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore5", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName5", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name6);                  // Set High Score 6
            NAMEBOX = (TextView) findViewById(id.Score6);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore6", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName6", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name7);                  // Set High Score 7
            NAMEBOX = (TextView) findViewById(id.Score7);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore7", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName7", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name8);                  // Set High Score 8
            NAMEBOX = (TextView) findViewById(id.Score8);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore8", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName8", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name9);                  // Set High Score 9
            NAMEBOX = (TextView) findViewById(id.Score9);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore9", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName9", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name10);                  // Set High Score 10
            NAMEBOX = (TextView) findViewById(id.Score10);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore10", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName10", "Player"));
        }
        if(Timed == true) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SCOREBOX = (TextView) findViewById(id.Name1);                  // Set High Score 1
            NAMEBOX = (TextView) findViewById(id.Score1);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore1", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName1", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name2);                  // Set High Score 2
            NAMEBOX = (TextView) findViewById(id.Score2);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore2", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName2", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name3);                  // Set High Score 3
            NAMEBOX = (TextView) findViewById(id.Score3);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore3", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName3", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name4);                  // Set High Score 4
            NAMEBOX = (TextView) findViewById(id.Score4);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore4", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName4", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name5);                  // Set High Score 5
            NAMEBOX = (TextView) findViewById(id.Score5);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore5", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName5", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name6);                  // Set High Score 6
            NAMEBOX = (TextView) findViewById(id.Score6);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore6", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName6", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name7);                  // Set High Score 7
            NAMEBOX = (TextView) findViewById(id.Score7);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore7", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName7", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name8);                  // Set High Score 8
            NAMEBOX = (TextView) findViewById(id.Score8);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore8", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName8", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name9);                  // Set High Score 9
            NAMEBOX = (TextView) findViewById(id.Score9);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore9", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName9", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name10);                  // Set High Score 10
            NAMEBOX = (TextView) findViewById(id.Score10);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore10", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName10", "Player"));
        }
    }

    public void DatabaseScores(Boolean Level, Boolean Timed) {
        if(Level == true) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SCOREBOX = (TextView) findViewById(id.Name1);                  // Set High Score 1
            NAMEBOX = (TextView) findViewById(id.Score1);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore1", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName1", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name2);                  // Set High Score 2
            NAMEBOX = (TextView) findViewById(id.Score2);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore2", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName2", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name3);                  // Set High Score 3
            NAMEBOX = (TextView) findViewById(id.Score3);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore3", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName3", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name4);                  // Set High Score 4
            NAMEBOX = (TextView) findViewById(id.Score4);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore4", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName4", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name5);                  // Set High Score 5
            NAMEBOX = (TextView) findViewById(id.Score5);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore5", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName5", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name6);                  // Set High Score 6
            NAMEBOX = (TextView) findViewById(id.Score6);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore6", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName6", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name7);                  // Set High Score 7
            NAMEBOX = (TextView) findViewById(id.Score7);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore7", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName7", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name8);                  // Set High Score 8
            NAMEBOX = (TextView) findViewById(id.Score8);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore8", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName8", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name9);                  // Set High Score 9
            NAMEBOX = (TextView) findViewById(id.Score9);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore9", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName9", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name10);                  // Set High Score 10
            NAMEBOX = (TextView) findViewById(id.Score10);
            SCOREBOX.setText("" + prefs.getInt("LevelHighScore10", 0));
            NAMEBOX.setText("" + prefs.getString("LevelHighName10", "Player"));
        }
        if(Timed == true) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SCOREBOX = (TextView) findViewById(id.Name1);                  // Set High Score 1
            NAMEBOX = (TextView) findViewById(id.Score1);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore1", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName1", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name2);                  // Set High Score 2
            NAMEBOX = (TextView) findViewById(id.Score2);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore2", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName2", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name3);                  // Set High Score 3
            NAMEBOX = (TextView) findViewById(id.Score3);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore3", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName3", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name4);                  // Set High Score 4
            NAMEBOX = (TextView) findViewById(id.Score4);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore4", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName4", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name5);                  // Set High Score 5
            NAMEBOX = (TextView) findViewById(id.Score5);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore5", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName5", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name6);                  // Set High Score 6
            NAMEBOX = (TextView) findViewById(id.Score6);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore6", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName6", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name7);                  // Set High Score 7
            NAMEBOX = (TextView) findViewById(id.Score7);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore7", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName7", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name8);                  // Set High Score 8
            NAMEBOX = (TextView) findViewById(id.Score8);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore8", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName8", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name9);                  // Set High Score 9
            NAMEBOX = (TextView) findViewById(id.Score9);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore9", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName9", "Player"));
            SCOREBOX = (TextView) findViewById(id.Name10);                  // Set High Score 10
            NAMEBOX = (TextView) findViewById(id.Score10);
            SCOREBOX.setText("" + prefs.getInt("TimeHighScore10", 0));
            NAMEBOX.setText("" + prefs.getString("TimeHighName10", "Player"));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void onBackPressed() {
        finish();
    }

    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(this, R.raw.highscore_song); //get song
        mediaPlayer.setLooping(true);    //make background song loop
        if (!mediaPlayer.isPlaying()) { mediaPlayer.start(); }
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

}
