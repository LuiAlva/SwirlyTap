package com.gmail.dianaupham.swirlytap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.spencer.swirlytap.R;



public class HighScoreActivity extends Activity {

    public static final String PREFS_NAME = "PREFS_FILE";
    TextView NameBox;
    TextView ScoreBox;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_high_score);
        //Load Table +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        NameBox = (TextView) findViewById(R.id.Name1);                  // Set High Score 1
        ScoreBox = (TextView) findViewById(R.id.Score1);
        NameBox.setText("" + prefs.getInt("HighScore1", 0));
        ScoreBox.setText("" + prefs.getString("HighName1", "Player"));
        NameBox = (TextView) findViewById(R.id.Name2);                  // Set High Score 2
        ScoreBox = (TextView) findViewById(R.id.Score2);
        NameBox.setText("" + prefs.getInt("HighScore2", 0));
        ScoreBox.setText("" + prefs.getString("HighName2", "Player"));
        NameBox = (TextView) findViewById(R.id.Name3);                  // Set High Score 3
        ScoreBox = (TextView) findViewById(R.id.Score3);
        NameBox.setText("" + prefs.getInt("HighScore3", 0));
        ScoreBox.setText("" + prefs.getString("HighName3", "Player"));
        NameBox = (TextView) findViewById(R.id.Name4);                  // Set High Score 4
        ScoreBox = (TextView) findViewById(R.id.Score4);
        NameBox.setText("" + prefs.getInt("HighScore4", 0));
        ScoreBox.setText("" + prefs.getString("HighName4", "Player"));
        NameBox = (TextView) findViewById(R.id.Name5);                  // Set High Score 5
        ScoreBox = (TextView) findViewById(R.id.Score5);
        NameBox.setText("" + prefs.getInt("HighScore5", 0));
        ScoreBox.setText("" + prefs.getString("HighName5", "Player"));
        NameBox = (TextView) findViewById(R.id.Name6);                  // Set High Score 6
        ScoreBox = (TextView) findViewById(R.id.Score6);
        NameBox.setText("" + prefs.getInt("HighScore6", 0));
        ScoreBox.setText("" + prefs.getString("HighName6", "Player"));
        NameBox = (TextView) findViewById(R.id.Name7);                  // Set High Score 7
        ScoreBox = (TextView) findViewById(R.id.Score7);
        NameBox.setText("" + prefs.getInt("HighScore7", 0));
        ScoreBox.setText("" + prefs.getString("HighName7", "Player"));
        NameBox = (TextView) findViewById(R.id.Name8);                  // Set High Score 8
        ScoreBox = (TextView) findViewById(R.id.Score8);
        NameBox.setText("" + prefs.getInt("HighScore8", 0));
        ScoreBox.setText("" + prefs.getString("HighName8", "Player"));
        NameBox = (TextView) findViewById(R.id.Name9);                  // Set High Score 9
        ScoreBox = (TextView) findViewById(R.id.Score9);
        NameBox.setText("" + prefs.getInt("HighScore9", 0));
        ScoreBox.setText("" + prefs.getString("HighName9", "Player"));
        NameBox = (TextView) findViewById(R.id.Name10);                  // Set High Score 10
        ScoreBox = (TextView) findViewById(R.id.Score10);
        NameBox.setText("" + prefs.getInt("HighScore10", 0));
        ScoreBox.setText("" + prefs.getString("HighName10", "Player"));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    public void onBackPressed() {
        finish();
    }

}
