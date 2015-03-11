package com.example.spencer.swirlytap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.spencer.swirlytap.util.SystemUiHider;
import java.io.IOException;
import java.util.Random;

public class SinglePlayer extends Activity implements View.OnClickListener {
    int Score = 0; //this is total score
    boolean addTime = false;    //Allows Time button to appear
    PopupWindow popupWindow;//Popup Window for Countdown, Pause menu, and Time over
    ImageButton PauseButton; //create image button for pause
    //Grid & Storage ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    private static final int NUM_ROWS = 6; //instantiated size of grid
    private static final int NUM_COLS = 4;
    private static final int ARRAY_ROWS = NUM_ROWS * 5;
    private static final int ARRAY_COLS = NUM_COLS * 5;
    private static int FINAL_COL; //set col and row location to pass to gridButton
    private static int FINAL_ROW; //this sends location of button
    Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];   //created total number of grid buttons
    String[][] luckArray = new String[ARRAY_ROWS][ARRAY_COLS]; //array containing good and bad buttons
    //Time & Speed ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    int StartTime = 61000;       //Set start time, 60000 = 60 seconds temporary set to 20 seconds
    int Current_Time = 61000;    //Current in-game time
    int Game_Speed = 400;        //Speed of the game
    int Speed_Limit = 200;       //Highest Speed
    int Game_Speed_Add = 15;     //Add speed every increment
    int Speed_Increment = 8;     //Points needed to increment speed
    int Speed_Increment_Add = 4; //Add to Speed_Increment to make it harder to speed up
    //Music & Sounds ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    Uri tapGood;              // Sound when Good swirl is tapped
    Uri tapBad;               // Sound for Bad swirl
    Uri tapTimeAdd;           // Sound for Time add swirl
    MediaPlayer GoodSound;    // MediaPlayer for playing good sound
    MediaPlayer GoodSound2;   // MediaPlayer for playing good sound - for faster sound
    MediaPlayer BadSound;     // MediaPlayer for playing Bad sound
    MediaPlayer SpecialSound; // MediaPlayer for playing Special button sounds
    MediaPlayer gameBG;       // For Background music
    MediaPlayer CountdownSound; //Sound for CountDown
    //Timers ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    CountDownTimer Updater;        //Timer that updates test and values at 30 frames/ms(millisecond)
    CountDownTimer TimeCountdown;  //Timer that updates the timer every second
    CountDownTimer SwirlEngine;    //Timer that lets the swirls appear. Update depends on Game_Speed
    CountDownTimer CountdownTimer; //Timer for the countdown at the beginning
    //Swirl Disappear System ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    buttonDisappear[] GoodArray = new buttonDisappear[10];
    buttonDisappear[] BadArray = new buttonDisappear[10];
    buttonDisappear[] SpecialArray = new buttonDisappear[10];
    int ArrayLocation;
    int GoodCount;
    int BadCount;
    int SpecialCount;
    Vibrator vibration;

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.example.spencer.swirlytap.util.SystemUiHider#getInstance}.
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes Title Bar
        //View decorView = getWindow().getDecorView();   // Hide action Bar
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        //Hide Actionbar ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_single_player); //show res/layout/activity_single_player.xml
        vibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //set up device vibration control
        //Buttons +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        PauseButton= (ImageButton)findViewById(R.id.pause_button);
        PauseButton.setOnClickListener(this); //sets an onClickListener on PauseButton
        //Sounds ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        gameBG = MediaPlayer.create(this, R.raw.game_song); //get background song
        gameBG.setLooping(true);    //make background song loop
        CountdownSound = MediaPlayer.create(this, R.raw.countdown); //get countdown sound
        GoodSound = new MediaPlayer();     // Setup MediaPlayer for good sound
        GoodSound2 = new MediaPlayer();    // Setup MediaPlayer for good sound
        GoodSound.setVolume(16,16);
        GoodSound2.setVolume(16,16);
        BadSound = new MediaPlayer();      // Setup MediaPlayer for bad sound
        SpecialSound = new MediaPlayer();  // Setup MediaPlayer for Special button sounds
        tapGood = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_good);    //Setup sound for Good swirl
        tapBad = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_bad);      //Setup sound for Bad swirl
        tapTimeAdd = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_time); //Setup sound for Time up swirl

        populateButtons(); //add buttons to grid

        ///////////////////////////////////////POPULATE LUCK ARRAY///////////////////////////////////

        //POPULATE luck array with different types of buttons
        Random rand = new Random(); //randomly select location in luck array

        for(int row = 0; row<ARRAY_ROWS; row++)
        {
            for(int col = 0; col<ARRAY_COLS; col++)
            {
                //bad button should display in a more random fashion...please test
                int randCell = rand.nextInt(ARRAY_COLS*ARRAY_ROWS); //random selection of numbers
                if(randCell%5 ==0) //much less chance to receive bad button
                {
                    luckArray[row][col] = "bad"; //place bad button in array
                }
                else if(randCell%6 == 0)
                {
                    luckArray[row][col] = "twicePoints"; //place 2x button in array
                }
                else if(randCell%21 == 0)
                {
                    luckArray[row][col] = "addTime"; //place add time button in array
                }
                else
                    luckArray[row][col] = "good"; //much higher chance to receive good button
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////
        // Null Disappear timers array
        for(int i = 0; i < 10; i++){
            GoodArray[i] = null;
            BadArray[i] = null;
            SpecialArray[i] = null;
        }

        //Wait for game to load
        new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 == 0) {
                    onFinish();
                } else {
                }
            }

            @Override
            public void onFinish() {
                GameStart();       //Start beginning Countdown
            }
        }.start();

    }

    private void populateButtons() //creating grid of buttons. These buttons are initialized as disabled and invisible
    {
        TableLayout table = (TableLayout)findViewById(R.id.tableForButtons); //make new table
        for(int row = 0; row < NUM_ROWS; row++) //rows instantiated at top
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT,1.0f)); //make layout look nice
            table.addView(tableRow);
            for(int col = 0; col < NUM_COLS; col++) //cols instantiated at top
            {
                FINAL_COL = col; //set col and row location to pass to gridButton
                FINAL_ROW = row; //this sends location of button
                Button Swirl = new Button(this); //create button to display correctly
                Swirl.setBackgroundResource(R.drawable.goodswirl); //make this grid block location have the image of goodswirl
                Swirl.setVisibility(View.INVISIBLE);               //Start Swirl Invisible
                Swirl.setEnabled(false);                           //Start Swirl button Disabled
                Swirl.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,1.0f));

                //This sets what the swirl will do when clicked
                Swirl.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.INVISIBLE);         // Make Swirl disappear
                        v.setEnabled(false);                     // Disable button
                        Score++;                                 // Add one to score
                    }
                });
                tableRow.addView(Swirl);
                buttons[row][col] = Swirl;
            }
        }//end 'for'
    }//end private void populateButtons

    void GameTimers(int Time) { // contains the timers of the game

        TimeCountdown = new CountDownTimer(Time, 1000)
        {
            //create new type textview and relate it to countdown textbox in activity_single_player.xml
            TextView mTextField = (TextView) findViewById(R.id.countdown);

            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1000 == 0)
                {
                    onFinish();
                }
                else
                    Current_Time = (int) millisUntilFinished;         //Updates current time
                mTextField.setText("" + millisUntilFinished / 1000);  //display seconds left in text field
            }
            //stop time/game when time is up
            public void onFinish() {
                mTextField.setText("0");                              //Set end of timer
                GameOver();
            }

        }.start();

        Updater = new CountDownTimer(Time, 30)
        {
            //Get access to totalScore Textbox
            TextView totalScore= (TextView) findViewById(R.id.totalScore);

            public void onTick(long millisUntilFinished)
            {
                //Update totalScore Textbox with current score, end at 60 seconds
                if (millisUntilFinished / 30 == 0)
                {
                    onFinish();
                }
                else
                {
                    totalScore.setText("" + Score); //Update Score Counter
                    Speed_Engine(Score);           //Update the Speed
                }
            }
            // Show text at end of timer
            public void onFinish() {
                totalScore.setText("" + Score); //Update Score Counter
            }
        }.start();

        SwirlEngine = new CountDownTimer(Time, Game_Speed) {
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 30 == 0)
                {
                    onFinish();
                }
                else
                {
                    displayButton();
                }
            }
            public void onFinish()
            {

            }

        }.start();

    } // End of GameTimers

    class buttonDisappear{
        public Button ButtonId;
        CountDownTimer TimerId;
        public buttonDisappear(Button button, CountDownTimer timer, int location){
            ButtonId = button;
            TimerId = timer;
            ArrayLocation = location;
        }
    }

    public void displayButton() //will call when button needs to be displayed
    {
        final Runnable buttonRunnable;
        final Handler buttonHandler = new Handler();
        Random r = new Random(); //randomly select location in luck array
        int randRow = r.nextInt(NUM_ROWS);
        int randCol = r.nextInt(NUM_COLS);
        int i; //For array location for disappear

        if(luckArray[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
        {
            for(i = 0; i < 10; i++) {        // Find empty spot in GoodArray
                if(GoodArray[i] == null){break;}
                if(i == 9) {i = 0;}
            }
            final Button goodButton = buttons[randRow][randCol];     //Button in this location
            goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
            goodButton.setEnabled(true);                            //Enable Swirl
            goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
            /*buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                public void run()
                {

                    goodButton.setVisibility(View.INVISIBLE);
                    goodButton.setEnabled(false);

                }
            };
            if(goodButton.isEnabled() == true) {
                buttonHandler.postDelayed(buttonRunnable, 1600); //will disappear after 2 seconds
            } */
            final int finalI = i;
            CountDownTimer temp = new CountDownTimer(1600,1600) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1600 == 0)
                {
                    onFinish();
                }
                else{}
            }

            @Override
            public void onFinish() {
                GoodArray[finalI] = null;
                goodButton.setVisibility(View.INVISIBLE);
                goodButton.setEnabled(false);
            }
        }.start();
            GoodArray[i] =  new buttonDisappear(goodButton, temp, i);
            goodButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {

                    {
                        playGood(tapGood);
                        GoodArray[finalI] = null;                // Remove from array
                        v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                        v.setEnabled(false);                     // Disable button
                        Score++;                                 // Add one to score
                    }
                }
            });


            //set 1 second timer... if timer reached then make button disappear
        }
        else if(luckArray[randRow][randCol] == "bad")
    {
        for(i = 0; i < 10; i++) {        // Find empty spot in BadArray
            if(BadArray[i] == null){break;}
            if(i == 9) {i = 0;}
        }
        final Button badButton = buttons[randRow][randCol];     //Button in this location
        badButton.setBackgroundResource(R.drawable.badswirl); //Set image to goodswirl
        badButton.setEnabled(true);                            //Enable Swirl
        badButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        /*buttonRunnable = new Runnable() { //what will be called if button has not been clicked
            public void run()
            {

                badButton.setVisibility(View.INVISIBLE);
                badButton.setEnabled(false);

            }
        };
        if(badButton.isEnabled() == true) {
            buttonHandler.postDelayed(buttonRunnable, 1700); //will disappear after 2 seconds
        }*/
        final int finalI = i;
        CountDownTimer temp = new CountDownTimer(1700,1700) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1700 == 0)
                {
                    onFinish();
                }
                else{}
            }
            @Override
            public void onFinish() {
                BadArray[finalI] = null;
                badButton.setVisibility(View.INVISIBLE);
                badButton.setEnabled(false);
            }
        }.start();
        BadArray[i] =  new buttonDisappear(badButton, temp, i);
        badButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
            {
                playBad(tapBad);                        // Play tapBad
                vibration.vibrate(300);                 // Vibrate device for 300 milliseconds
                BadArray[finalI] = null;                // Remove from array
                v.setVisibility(View.INVISIBLE);        // Make Swirl disappear when clicked
                v.setEnabled(false);                    // Disable button
                Score -= 5;                             // Add one to score
            }
            }
        });
    }
    else if(luckArray[randRow][randCol] == "twicePoints")
    {
        for(i = 0; i < 10; i++) {        // Find empty spot in SpecialArray
            if(SpecialArray[i] == null){break;}
            if(i == 9) {i = 0;}
        }
        final Button twiceButton = buttons[randRow][randCol];     //Button in this location
        twiceButton.setBackgroundResource(R.drawable.twiceswirl); //Set image to goodswirl
        twiceButton.setEnabled(true);                            //Enable Swirl
        twiceButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        /*buttonRunnable = new Runnable() { //what will be called if button has not been clicked
            public void run()
            {
                twiceButton.setVisibility(View.INVISIBLE);
                twiceButton.setEnabled(false);
            }
        };
        if(twiceButton.isEnabled() == true) {
            buttonHandler.postDelayed(buttonRunnable, 1600); //will disappear after 2 seconds
        }*/
        final int finalI = i;
        CountDownTimer temp = new CountDownTimer(1600,1600) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1600 == 0)
                {
                    onFinish();
                }
                else{}
            }

            @Override
            public void onFinish() {
                SpecialArray[finalI] = null;
                twiceButton.setVisibility(View.INVISIBLE);
                twiceButton.setEnabled(false);
            }
        }.start();
        SpecialArray[i] =  new buttonDisappear(twiceButton, temp, i);
        twiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                {
                    playGood2(tapGood);
                    SpecialArray[finalI] = null;                // Remove from array
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    v.setEnabled(false);                     // Disable button
                    Score+=2;                                 // Add 2 to score
                }
            }
        });

    }
    else if(luckArray[randRow][randCol] == "addTime") {
            if (addTime == true) {
                for(i = 0; i < 10; i++) {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 9) {i = 0;}
                }
                final Button timeButton = buttons[randRow][randCol];     //Button in this location
                timeButton.setBackgroundResource(R.drawable.fivetime); //Set image to goodswirl
                timeButton.setEnabled(true);                            //Enable Swirl
                timeButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                /*buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run() {

                        timeButton.setVisibility(View.INVISIBLE);
                        timeButton.setEnabled(false);

                    }
                };
                if (timeButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1800); //will disappear after 2 seconds
                }*/
                final int finalI = i;
                CountDownTimer temp = new CountDownTimer(1800,1800) { // Set timer for disappearance
                    public void onTick(long millisUntilFinished)
                    {
                        if (millisUntilFinished / 1800 == 0)
                        {
                            onFinish();
                        }
                        else{}
                    }

                    @Override
                    public void onFinish() {
                        SpecialArray[finalI] = null;
                        timeButton.setVisibility(View.INVISIBLE);
                        timeButton.setEnabled(false);
                    }
                }.start();
                SpecialArray[i] =  new buttonDisappear(timeButton, temp, i);
                timeButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        {
                            addTime = false;                        // Stop more time buttons from popping up
                            playSpecial(tapTimeAdd);                // Play time up sound
                            SpecialArray[finalI] = null;            // Remove from array
                            v.setVisibility(View.INVISIBLE);        // Make Swirl disappear when clicked
                            v.setEnabled(false);                    // Disable button
                            SwirlEngine.cancel();                   // Cancel old Timers
                            Updater.cancel();
                            TimeCountdown.cancel();
                            Current_Time += 5000;                             // Add 5 seconds to time
                            GameTimers(Current_Time);                          // Add one to score
                        }
                    }
                });

            } else if (addTime == false) {
                for(i = 0; i < 10; i++) {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 9) {i = 0;}
                }
                final Button goodButton = buttons[randRow][randCol];     //Button in this location
                goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
                goodButton.setEnabled(true);                            //Enable Swirl
                goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                /*buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run() {

                        goodButton.setVisibility(View.INVISIBLE);
                        goodButton.setEnabled(false);

                    }
                };
                if (goodButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1500); //will disappear after 2 seconds
                }*/
                final int finalI = i;
                CountDownTimer temp = new CountDownTimer(1600,1600) { // Set timer for disappearance
                    public void onTick(long millisUntilFinished)
                    {
                        if (millisUntilFinished / 1600 == 0)
                        {
                            onFinish();
                        }
                        else{}
                    }

                    @Override
                    public void onFinish() {
                        GoodArray[finalI] = null;
                        goodButton.setVisibility(View.INVISIBLE);
                        goodButton.setEnabled(false);
                    }
                }.start();
                GoodArray[i] =  new buttonDisappear(goodButton, temp, i);
                goodButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        {
                            playGood2(tapGood);                      // Play good swirl sound
                            GoodArray[finalI] = null;            // Remove from array
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            Score++;                                 // Add one to score
                        }
                    }
                });

            }
        }
    } //End of displaybutton

    void Speed_Engine(int Score) {

        if ((Score % Speed_Increment) == 0 && Score != 0 && Game_Speed >= Speed_Limit) //every (Speed_Increment) points, and the speed isn't going past the speed limit
        {
            Game_Speed -= Game_Speed_Add;           //Speed up the game by 50 frames/second
            Speed_Increment += Speed_Increment_Add; //Add more points to increment to make it harder to speed up
            addTime = true;                         //Allows adding one add time button
            SwirlEngine.cancel();                   // cancel the old timer with the old speed
            SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                @Override
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished / 30 == 0) {
                        onFinish();
                    } else {
                        displayButton();
                    }
                }
                @Override
                public void onFinish() {

                }
            }.start();
        } else {
        }
    } //End Speed_Engine

    //Sound Players+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void playGood(Uri uri) {
        try{
            GoodSound.reset();
            GoodSound.setDataSource(this, uri);
            GoodSound.prepare();
            GoodSound.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playGood2(Uri uri) {
        try{
            GoodSound2.reset();
            GoodSound2.setDataSource(this, uri);
            GoodSound2.prepare();
            GoodSound2.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playBad(Uri uri) {
        try{
            BadSound.reset();
            BadSound.setDataSource(this, uri);
            BadSound.prepare();
            BadSound.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playSpecial(Uri uri) {
        try{
            SpecialSound.reset();
            SpecialSound.setDataSource(this, uri);
            SpecialSound.prepare();
            SpecialSound.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.pause_button:
                gameBG.pause();                      // Pause the game music
                for(int i = 0; i < 10; i++) {        // cancel all the timers in disappear array
                    if(GoodArray[i] != null){
                        GoodArray[i].TimerId.cancel();
                    }
                    if (BadArray[i] != null){
                        BadArray[i].TimerId.cancel();
                    }
                    if (SpecialArray[i] != null){
                        SpecialArray[i].TimerId.cancel();
                    }
                }
                SwirlEngine.cancel();               // cancel the rest of the timers
                Updater.cancel();
                TimeCountdown.cancel();
                PopupPauseMenu();                   //Popup the Pause menu
            break;
        }
    }

    //Pause Menu Options +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //Continues game from Pause Menu
    public void Continue(View v) {
        new CountDownTimer(800,800) {
            int i;
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 800 == 0)
                {
                    onFinish();
                }
                else
                {
                    if(i != 10) {
                        for (i = 0; i < 10; i++) {        // starts all the timers in disappear array
                            if (GoodArray[i] != null) {
                                GoodArray[i].TimerId.start();
                            }
                            if (BadArray[i] != null) {
                                BadArray[i].TimerId.start();
                            }
                            if (SpecialArray[i] != null) {
                                SpecialArray[i].TimerId.start();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                popupWindow.dismiss();
                GameTimers(Current_Time);
                gameBG.start();
            }
        }.start();
    }

    //Restarts game from Pause Menu
    public void Restart(View v) {
        popupWindow.dismiss();
        startActivity(new Intent(SinglePlayer.this, SinglePlayer.class));
        finish();
    }

    //Quits game from Pause Menu. Sends back to Title menu.
    public void Quit(View v) {
        popupWindow.dismiss();
        startActivity(new Intent(SinglePlayer.this, MainActivity.class));
        finish();
    }

    //Inflates Popup menu for Pause Button
    public void PopupPauseMenu() {
        try {
            LayoutInflater inflater = (LayoutInflater) SinglePlayer.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_pause_menu, (ViewGroup)findViewById(R.id.pause_layout));
            popupWindow = new PopupWindow(layout, getWindow().getAttributes().width, getWindow().getAttributes().height, true);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            ImageButton Continue = (ImageButton)findViewById(R.id.Paused);
            Continue.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }});
            Button Continue2 = (Button)findViewById(R.id.Continue);
            Continue2.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }});
            Button Restart = (Button) findViewById(R.id.Restart);
            Restart.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }});
            Button Quit = (Button)findViewById(R.id.Quit);
            Quit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }});
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void GameStart() {
        try {
            LayoutInflater inflater = (LayoutInflater) SinglePlayer.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.game_start_screen, (ViewGroup)findViewById(R.id.countdown_layout));
            popupWindow = new PopupWindow(layout, getWindow().getAttributes().width, getWindow().getAttributes().height, true);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        }catch(Exception e) {
            e.printStackTrace();
        }

        CountdownTimer = new CountDownTimer(4000,1000) {

            TextView CountdownText = (TextView) popupWindow.getContentView().findViewById (R.id.count_down_text);

            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1000 == 0)
                {
                    onFinish();
                }
                else
                {
                    CountdownSound.start();                                  // Play sound
                    CountdownText.setText("" + millisUntilFinished / 1000 ); // Display Countdown
                }
            }
            public void onFinish()
            {
                popupWindow.dismiss();      // Dismiss Countdown
                gameBG.start();             // Start background song
                GameTimers(StartTime);      // Start game timers
            }
        }.start();
    }

    public void GameOver() {
        try {
            LayoutInflater inflater = (LayoutInflater) SinglePlayer.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.game_over_screen, (ViewGroup)findViewById(R.id.game_over_layout));
            popupWindow = new PopupWindow(layout, getWindow().getAttributes().width, getWindow().getAttributes().height, true);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        }catch(Exception e) {
            e.printStackTrace();
        }
        //Wait x seconds before going to results screen
        new CountDownTimer(3000,1000) {
            int i = 0;
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1000 == 0)
                {
                    onFinish();
                }
                else{
                    // Cancel the timers and clear the arrays
                    if (i != 10) {
                        for (i = 0; i < 10; i++) {
                            if (GoodArray[i] != null){
                                GoodArray[i].TimerId.cancel();
                            }
                            if (BadArray[i] != null){
                                BadArray[i].TimerId.cancel();
                            }
                            if (SpecialArray[i] != null){
                                SpecialArray[i].TimerId.cancel();
                            }
                            GoodArray[i] = null;
                            BadArray[i] = null;
                            SpecialArray[i] = null;
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                //* End the Game*\\
                popupWindow.dismiss();                                                //Dismiss Time's up popup
                gameBG.stop();                                                        //stop song
                Intent intentAgain = new Intent(SinglePlayer.this, PlayAgain.class);  //create intent (to go to PlayAgain menu)
                intentAgain.putExtra("score", Score);                                 //Send variable Score (score) to new intent (PlayAgain)
                startActivity(intentAgain);                                           //go to PlayAgain activity/menu
                finish();
            }
        }.start();
    }

    protected void onResume() {
        super.onResume();

    }

}
