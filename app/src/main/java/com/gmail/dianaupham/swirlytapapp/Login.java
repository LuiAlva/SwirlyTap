package com.gmail.dianaupham.swirlytapapp;
//package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/*import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;
*/
import com.gmail.dianaupham.swirlytapapp.swirlytap.R;

public class Login extends Activity
{
    private DatabaseTableActivity myTable;
    EditText USER_NAME, PASS_WORD;
    Button SUBMIT, BACK;
    public static final String PREFS_NAME = "PREFS_FILE";

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.gmail.dianaupham.swirlytap.SystemUiHider#getInstance}.
    private com.gmail.dianaupham.swirlytapapp.SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        myTable = new DatabaseTableActivity(this);
        USER_NAME = (EditText)findViewById(R.id.editUsername1);
        PASS_WORD = (EditText)findViewById(R.id.editText2);
        SUBMIT = (Button)findViewById(R.id.login);
        SUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(myTable.Login(USER_NAME.getText().toString(), PASS_WORD.getText().toString()))
                {
                    // After an account is registered, log in
                    myTable.setlogin(USER_NAME.getText().toString(),PASS_WORD.getText().toString(),1);
                    Toast.makeText(getApplicationContext(), "Welcome " + USER_NAME.getText() + "!", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("PlayerName", USER_NAME.getText().toString() );
                    editor.putBoolean("NotLoggedIn", false);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), DatabaseMainActivity.class);
                    Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("USERNAME",USER_NAME.getText().toString());
                    bundle.putString("PASSWORD", PASS_WORD.getText().toString());
                    i.putExtras(bundle);
                    //startActivity(i);
                    startActivity(i2);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Incorrect Login - Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
        BACK = (Button)findViewById(R.id.Back);
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WelcomeScreen.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), WelcomeScreen.class);
        startActivity(i);
        finish();
    }
}
