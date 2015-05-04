package com.gmail.dianaupham.swirlytap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.dianaupham.swirlytap.swirlytap.R;


public class Profile extends Activity {
    public static final String PREFS_NAME = "PREFS_FILE";
    TextView username, highscore, goodswirltotal;
    Button Sign_Out, BACK;

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.gmail.dianaupham.swirlytap.SystemUiHider#getInstance}.
    private com.gmail.dianaupham.swirlytap.SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        username = (TextView) findViewById(R.id.UserNameBox);
        highscore = (TextView) findViewById(R.id.HighScoreBox);
        goodswirltotal = (TextView) findViewById(R.id.GoodSwirlTotalBox);
        String player = prefs.getString("PlayerName", "Player");
        int Highscore = prefs.getInt("HighScore", 0);
        int GoodSwirlTotal = prefs.getInt("GoodSwirlTotal", 0);
        username.setText("" + player );
        highscore.setText("" + Highscore );
        goodswirltotal.setText("" + GoodSwirlTotal);
        Sign_Out = (Button)findViewById(R.id.sign_out);
        BACK = (Button)findViewById(R.id.Back);
        Sign_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Sign out
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("NotLoggedIn", true);
                editor.putString("PlayerName", "Player");
                editor.commit();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

}
