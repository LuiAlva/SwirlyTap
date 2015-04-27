package com.gmail.dianaupham.swirlytap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.gmail.dianaupham.swirlytap.swirlytap.R;

public class LoginScreen extends Activity
{
   private DatabaseTableActivity myTable;
    Button LOG_IN, REG, Back;
    CheckBox StopLogin;

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.gmail.dianaupham.swirlytap.SystemUiHider#getInstance}.
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_layout);
        myTable = new DatabaseTableActivity(this);
        LOG_IN = (Button)findViewById(R.id.login1);
        REG = (Button)findViewById(R.id.register1);
        Back = (Button)findViewById(R.id.stop_asking);
        StopLogin = (CheckBox)findViewById(R.id.checkBox1);
        StopLogin.setEnabled(false);
        LOG_IN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();

            }
        });
        REG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);
                finish();

            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
