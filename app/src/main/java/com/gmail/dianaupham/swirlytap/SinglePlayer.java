package com.gmail.dianaupham.swirlytap;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.spencer.swirlytap.R;

import java.io.IOException;
import java.util.Random;

public class SinglePlayer extends Activity implements View.OnClickListener {
    int Score = 0; //this is total score
    boolean addTime = false;    //Allows Time button to appear
    boolean paused = false;
    boolean Countdown_active = false; //DisablePause
    PopupWindow popupWindow; // Popup Window for Countdown, Pause menu, and Time over
    TableLayout GameWindow;  // For SwirlTable background
    ImageButton PauseButton; // create image button for pause
    ImageButton Nuke;        // Create Image button for Nuke
    ProgressBar Speed_Bar;   // Speed bar
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
    int Current_Speed = 290;     //Current in-game speed
    int Start_Speed = 290;        //Speed at the start of the game
    int Speed_Limit = 90;       //Highest Speed
    int Game_Speed_Add = 10;     //Add speed every increment
    int Speed_Increment = 6;     //Points needed to increment speed
    int Speed_Increment_Add = 0; //Add to Speed_Increment to make it harder to speed up
    int Speed_Increment_Set = 6; //Equal to Speed_Increment
    //Music & Sounds ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    Uri tapGood;              // Sound when Good swirl is tapped
    Uri tapBad;               // Sound for Bad swirl
    Uri tapTimeAdd;           // Sound for Time add swirl
    Uri CountdownSound;       // Sound for Countdown at the beginning of the game
    Uri CountdownGoSound;     // Sound for Start after Countdown
    MediaPlayer GoodSound;    // MediaPlayer for playing good sound
    MediaPlayer GoodSound2;   // MediaPlayer for playing good sound - for faster sound
    MediaPlayer BadSound;     // MediaPlayer for playing Bad sound
    MediaPlayer SpecialSound; // MediaPlayer for playing Special button sounds
    MediaPlayer gameBG;       // For Background music
    MediaPlayer NukeBoom;     // For Nuke Explosion
    //Timers ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    CountDownTimer Updater;        //Timer that updates test and values at 30 frames/ms(millisecond)
    CountDownTimer TimeCountdown;  //Timer that updates the timer every second
    CountDownTimer SwirlEngine;    //Timer that lets the swirls appear. Update depends on Start_Speed
    CountDownTimer CountdownTimer; //Timer for the countdown at the beginning
    //Swirl Disappear System ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    buttonDisappear[] GoodArray = new buttonDisappear[10];
    buttonDisappear[] BadArray = new buttonDisappear[10];
    buttonDisappear[] SpecialArray = new buttonDisappear[10];
    int Good_Pressed = 0;       // GoodSwirls pressed
    int Bad_Pressed = 0;        // BadSwirls pressed
    int Time_Pressed = 0;       // TimeSwirls pressed
    int Good2_Pressed = 0;      // 2xGoodSwirls pressed
    int OnScreenGood = 0;       // GoodSwirls On Screen
    int OnScreenBad = 0;        // BadSwirls On Screen
    int OnScreenTime = 0;       // TimeSwirls On Screen
    int OnScreenGood2 = 0;      // 2xGoodSwirls On Screen
    int Extra_Time_Max = 4;     // Set limit for amount of time added (20 seconds)
    int Extra_Time_Counter = 0; // Count amount of ExtraTime added
    Vibrator vibration;

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.gmail.dianaupham.swirlytap.SystemUiHider#getInstance}.
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
        PauseButton = (ImageButton)findViewById(R.id.pause_button);
        PauseButton.setOnClickListener(this); //sets an onClickListener on PauseButton
        Nuke = (ImageButton)findViewById(R.id.Nuke);
        Nuke.setOnClickListener(this);         //sets an onClickListener on PauseButton
        Nuke.setEnabled(false);               //Start Nuke Disabled
        Speed_Bar = (ProgressBar)findViewById(R.id.SpeedBar);
        GameWindow = (TableLayout)findViewById(R.id.tableForButtons);
        //Sounds ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        gameBG = MediaPlayer.create(this, R.raw.game_song); //get background song
        gameBG.setLooping(true);    //make background song loop
        NukeBoom =  MediaPlayer.create(this, R.raw.nuke_explosion); //get nuke explosion
        GoodSound = new MediaPlayer();     // Setup MediaPlayer for good sound
        GoodSound2 = new MediaPlayer();    // Setup MediaPlayer for good sound
        BadSound = new MediaPlayer();      // Setup MediaPlayer for bad sound
        SpecialSound = new MediaPlayer();  // Setup MediaPlayer for Special button sounds
        GoodSound.setVolume(12,12);
        GoodSound2.setVolume(12,12);
        CountdownSound = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.countdown);    //Setup sound for Countdown
        CountdownGoSound = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.countdown_go);//Setup sound for Start sound
        tapGood = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_good);    //Setup sound for Good swirl
        tapBad = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_bad);      //Setup sound for Bad swirl
        tapTimeAdd = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_time); //Setup sound for Time up swirl

        Nuke.setImageResource(R.drawable.nuke_button_active); // Activate Nuke for now
        Nuke.setEnabled(true);


        populateButtons(); //add buttons to grid

        ///////////////////////////////////////POPULATE LUCK ARRAY///////////////////////////////////

        //POPULATE luck array with different types of buttons
        Random rand = new Random(); //randomly select location in luck array
        rand.setSeed(System.currentTimeMillis());
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
                SwirlEngine.cancel();
                Updater.cancel();
                DestroySwirls();
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
                    if (Current_Speed != Speed_Limit)
                       Speed_Engine(Score);           //Update the Speed
                }
            }
            // Show text at end of timer
            public void onFinish() {
                totalScore.setText("" + Score); //Update Score Counter
            }
        }.start();

        SwirlEngine = new CountDownTimer(Time, Current_Speed) {
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
        public buttonDisappear(Button button, CountDownTimer timer){
            ButtonId = button;
            TimerId = timer;
        }
    }

    public void displayButton() //will call when button needs to be displayed
    {
        final Runnable buttonRunnable;
        final Handler buttonHandler = new Handler();
        Random r = new Random(); //randomly select location in luck array
        r.setSeed(System.currentTimeMillis());
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
            OnScreenGood++;                                         //Count on Screen
            goodButton.setEnabled(true);                            //Enable Swirl
            goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
            final int finalI = i;
            CountDownTimer temp = new CountDownTimer(700,700) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 700 == 0)
                {
                    onFinish();
                }
                else{}
            }

            @Override
            public void onFinish() {
                GoodArray[finalI].ButtonId = null;       // Remove Button ID
                GoodArray[finalI] = null;
                OnScreenGood--;
                goodButton.setVisibility(View.INVISIBLE);
                goodButton.setEnabled(false);
            }
        }.start();
            GoodArray[i] =  new buttonDisappear(goodButton, temp);
            goodButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {

                    {
                        Animation FadeAnim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                        FadeAnim.setDuration(200);
                        playGood(tapGood);
                        GoodArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                        GoodArray[finalI].ButtonId = null;       // Remove Button ID
                        GoodArray[finalI] = null;                // Remove from array
                        Good_Pressed++;
                        OnScreenGood--;
                        goodButton.setBackgroundResource(R.drawable.goodswirl_break); //Set image to goodswirl
                        v.startAnimation(FadeAnim);
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
        OnScreenBad++;                                         // Count on screen
        final Button badButton = buttons[randRow][randCol];    //Button in this location
        badButton.setBackgroundResource(R.drawable.badswirl);  //Set image to BadSwirl
        badButton.setEnabled(true);                            //Enable Swirl
        badButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        final int finalI = i;
        CountDownTimer temp = new CountDownTimer(700,700) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 700 == 0)
                {
                    onFinish();
                }
                else{}
            }
            @Override
            public void onFinish() {
                BadArray[finalI] = null;
                OnScreenBad--;
                badButton.setVisibility(View.INVISIBLE);
                badButton.setEnabled(false);
            }
        }.start();
        BadArray[i] =  new buttonDisappear(badButton, temp);
        badButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
            {
                Animation FadeAnim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                FadeAnim.setDuration(200);
                playBad(tapBad);                        // Play tapBad
                vibration.vibrate(300);                 // Vibrate device for 300 milliseconds
                BadArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                BadArray[finalI].ButtonId = null;       // Remove Button ID
                Bad_Pressed++;
                OnScreenBad--;
                v.setBackgroundResource(R.drawable.badswirl_break); //Set image to goodswirl
                v.startAnimation(FadeAnim);
                BadArray[finalI] = null;                // Remove from array
                v.setVisibility(View.INVISIBLE);        // Make Swirl disappear when clicked
                v.setEnabled(false);                    // Disable button
                Score -= 5;                             // Subtract 5 from score
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
        OnScreenGood2++;
        final Button twiceButton = buttons[randRow][randCol];     //Button in this location
        twiceButton.setBackgroundResource(R.drawable.twiceswirl); //Set image to goodswirl
        twiceButton.setEnabled(true);                            //Enable Swirl
        twiceButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        final int finalI = i;
        CountDownTimer temp = new CountDownTimer(700,700) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 700 == 0)
                {
                    onFinish();
                }
                else{}
            }

            @Override
            public void onFinish() {
                SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                SpecialArray[finalI] = null;
                OnScreenGood2--;
                twiceButton.setVisibility(View.INVISIBLE);
                twiceButton.setEnabled(false);
            }
        }.start();
        SpecialArray[i] =  new buttonDisappear(twiceButton, temp);
        twiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                {
                    Animation FadeAnim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    FadeAnim.setDuration(200);
                    playGood2(tapGood);
                    SpecialArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                    SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                    SpecialArray[finalI] = null;                // Remove from array
                    Good2_Pressed++;
                    OnScreenGood2--;
                    v.setBackgroundResource(R.drawable.twiceswirl_break); //Set image to goodswirl
                    v.startAnimation(FadeAnim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    v.setEnabled(false);                     // Disable button
                    Score+=2;                                 // Add 2 to score
                }
            }
        });

    }
    else if(luckArray[randRow][randCol] == "addTime") {
            if (addTime == true /*&& Extra_Time_Counter <= Extra_Time_Max*/) {
                if (Extra_Time_Counter < Extra_Time_Max)
                {
                    for (i = 0; i < 10; i++) {        // Find empty spot in SpecialArray
                        if (SpecialArray[i] == null) {
                            break;
                        }
                        if (i == 9) {
                            i = 0;
                        }
                    }
                    OnScreenTime++;
                    final Button timeButton = buttons[randRow][randCol];     //Button in this location
                    timeButton.setBackgroundResource(R.drawable.fivetime); //Set image to goodswirl
                    timeButton.setEnabled(true);                            //Enable Swirl
                    timeButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                    final int finalI = i;
                    CountDownTimer temp = new CountDownTimer(900, 900) { // Set timer for disappearance
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 900 == 0) {
                                onFinish();
                            } else {
                            }
                        }

                        @Override
                        public void onFinish() {
                            SpecialArray[finalI].ButtonId = null;   // Remove Button ID
                            SpecialArray[finalI] = null;
                            OnScreenTime--;
                            timeButton.setVisibility(View.INVISIBLE);
                            timeButton.setEnabled(false);
                        }
                    }.start();
                    SpecialArray[i] = new buttonDisappear(timeButton, temp);
                    timeButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Animation FadeAnim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                            FadeAnim.setDuration(200);
                            addTime = false;                              // Stop more time buttons from popping up
                            playSpecial(tapTimeAdd);                      // Play time up sound
                            SpecialArray[finalI].TimerId.cancel();        // Cancel it's disappear Timer
                            SpecialArray[finalI].ButtonId = null;         // Remove Button ID
                            SpecialArray[finalI] = null;                  // Remove from array
                            v.startAnimation(FadeAnim);
                            v.setVisibility(View.INVISIBLE);              // Make Swirl disappear when clicked
                            v.setEnabled(false);                          // Disable button
                            SwirlEngine.cancel();                         // Cancel old Timers
                            Updater.cancel();
                            TimeCountdown.cancel();
                            Current_Time += 5000;                         // Add 5 seconds to time
                            GameTimers(Current_Time);                     // Update Timers
                            Time_Pressed++;
                            OnScreenTime--;                               // Remove 5 seconds from on-screen clock
                            Extra_Time_Counter++;                         // Add one to counter (max amount = 4)
                        }
                    });
                }//end 'if'
            } else if (addTime == false) {
                for(i = 0; i < 10; i++) {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 9) {i = 0;}
                }
                OnScreenGood++;
                final Button goodButton = buttons[randRow][randCol];    //Button in this location
                goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
                goodButton.setEnabled(true);                            //Enable Swirl
                goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                final int finalI = i;
                CountDownTimer temp = new CountDownTimer(700,700) { // Set timer for disappearance
                    public void onTick(long millisUntilFinished)
                    {
                        if (millisUntilFinished / 700 == 0)
                        {
                            onFinish();
                        }
                        else{}
                    }

                    @Override
                    public void onFinish() {
                        GoodArray[finalI].ButtonId = null;       // Remove Button ID
                        GoodArray[finalI] = null;
                        OnScreenGood--;
                        goodButton.setVisibility(View.INVISIBLE);
                        goodButton.setEnabled(false);
                    }
                }.start();
                GoodArray[i] =  new buttonDisappear(goodButton, temp);
                goodButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        {
                            Animation FadeAnim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                            FadeAnim.setDuration(200);
                            playGood2(tapGood);                      // Play good swirl sound
                            GoodArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                            GoodArray[finalI].ButtonId = null;       // Remove Button ID
                            GoodArray[finalI] = null;                // Remove from array
                            Good_Pressed++;
                            OnScreenGood--;
                            goodButton.setBackgroundResource(R.drawable.goodswirl_break); //Set image to goodswirl
                            v.startAnimation(FadeAnim);
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
        TextView SpeedPercent = (TextView)findViewById(R.id.SpeedLabel);
        if (((Good_Pressed + Good2_Pressed) % Speed_Increment) == 0 && Score != 0 && Current_Speed > Speed_Limit) //every (Speed_Increment) points, and the speed isn't going past the speed limit
        {
            Current_Speed -= Game_Speed_Add;           //Speed up the game by 50 frames/second
            Speed_Increment_Set += Speed_Increment_Add; // Set points to increment
            Speed_Increment += Speed_Increment_Set; //Add more points to increment to make it harder to speed up
            addTime = true;                         //Allows adding one add time button
            SwirlEngine.cancel();                   // cancel the old timer with the old speed
            SwirlEngine = new CountDownTimer(Current_Time, Current_Speed) { // start new timer with changed speed
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
            // Set the progress of the speedbar and Speed Label
            Speed_Bar.setProgress((100/((Start_Speed -Speed_Limit)/Game_Speed_Add))*(((Start_Speed -Speed_Limit)/Game_Speed_Add)- ((Current_Speed - Speed_Limit)/Game_Speed_Add)));
            if(Current_Speed == Speed_Limit) {
                SpeedPercent.setText("Velocity Maxed!");
                Speed_Bar.setProgress(100);
            }
            else if (Current_Speed == Start_Speed) {
                SpeedPercent.setText("Velocity: 1%");
                Speed_Bar.setProgress(1);
            }
            else {
                SpeedPercent.setText("Velocity: " + (100 / ((Start_Speed - Speed_Limit) / Game_Speed_Add)) * (((Start_Speed - Speed_Limit) / Game_Speed_Add) - ((Current_Speed - Speed_Limit) / Game_Speed_Add)) + "%");
                Speed_Bar.setProgress((100 / ((Start_Speed - Speed_Limit) / Game_Speed_Add)) * (((Start_Speed - Speed_Limit) / Game_Speed_Add) - ((Current_Speed - Speed_Limit) / Game_Speed_Add)));
            }
        } else {
        }
    } //End Speed_Engine

    public void DestroySwirls() {

        for (int i = 0; i < 10; i++) {
            if (GoodArray[i] != null) {
                GoodArray[i].TimerId.cancel();
                GoodArray[i].ButtonId.setVisibility(View.INVISIBLE);
                GoodArray[i].ButtonId.setEnabled(false);
            }
            if (BadArray[i] != null) {
                BadArray[i].TimerId.cancel();
                BadArray[i].ButtonId.setVisibility(View.INVISIBLE);
                BadArray[i].ButtonId.setEnabled(false);
            }
            if (SpecialArray[i] != null) {
                SpecialArray[i].TimerId.cancel();
                SpecialArray[i].ButtonId.setVisibility(View.INVISIBLE);
                SpecialArray[i].ButtonId.setEnabled(false);
            }
            GoodArray[i] = null;
            BadArray[i] = null;
            SpecialArray[i] = null;
        }
    }

    public void ExplodeNuke() {
        paused = true;
        SwirlEngine.cancel();                   // Cancel All timers
        NukeBoom.start();                       // Play Nuke Explosion sound
        vibration.vibrate(1000);                 // Vibrate device for 500 milliseconds
        Updater.cancel();
        TimeCountdown.cancel();
        Score += OnScreenGood;                  // Add All Scores and Time
        Score -= OnScreenBad;
        Score += (OnScreenGood2 * 2);
        Current_Time += (OnScreenTime * 5000);
        Good_Pressed += OnScreenGood;           // Add all the swirls that were pressed
        Good2_Pressed += OnScreenGood2;
        Bad_Pressed += OnScreenBad;
        Time_Pressed += OnScreenTime;
        DestroySwirls();                        // Destroy the Swirls
        Nuke.setEnabled(false);                 // Disable Nuke
        Nuke.setImageResource(R.drawable.nuke_button_inactive);
        new CountDownTimer(1000,1000) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished == 0)
                {
                    onFinish();
                }
                else{}
            }
            @Override
            public void onFinish() {
                GameTimers(Current_Time);               // Start Timers
                paused = false;
            }
        }.start();
    }

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
    //Sound Players ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.pause_button:
                PauseActivate();                // Pause the game
                break;
            case R.id.Nuke:
                ExplodeNuke();
                break;
        }
    }

    //Pause Menu Options +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void PauseActivate() {
        if(!paused) {
            gameBG.pause();                      // Pause the game music
            paused = true;
            for (int i = 0; i < 10; i++) {        // cancel all the timers in disappear array
                if (GoodArray[i] != null) {
                    GoodArray[i].TimerId.cancel();
                }
                if (BadArray[i] != null) {
                    BadArray[i].TimerId.cancel();
                }
                if (SpecialArray[i] != null) {
                    SpecialArray[i].TimerId.cancel();
                }
            }
            SwirlEngine.cancel();               // cancel the rest of the timers
            Updater.cancel();
            TimeCountdown.cancel();
            PopupPauseMenu();                   //Popup the Pause menu
        }
    }

    //Continues game from Pause Menu
    public void Continue(View v) {
        new CountDownTimer(30,30) {
            int i;
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 30 == 0)
                {
                    onFinish();
                }
                else
                {
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

            @Override
            public void onFinish() {
                paused = false;
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

    //PAUSE MENU ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    // Game Start and Game Over ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void GameStart() {
        paused = true;
        Countdown_active = true;
        try {
            LayoutInflater inflater = (LayoutInflater) SinglePlayer.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.game_start_screen, (ViewGroup)findViewById(R.id.countdown_layout));
            popupWindow = new PopupWindow(layout, getWindow().getAttributes().width, getWindow().getAttributes().height, true);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        }catch(Exception e) {
            e.printStackTrace();
        }
        final TextView CountdownText = (TextView) popupWindow.getContentView().findViewById (R.id.count_down_text);

        CountdownTimer = new CountDownTimer(4000,1000) {

            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 1000 == 0)
                {
                    onFinish();
                }
                else
                {
                    playSpecial(CountdownSound);                                  // Play sound
                    CountdownText.setText("" + millisUntilFinished / 1000 ); // Display Countdown
                }
            }
            public void onFinish()
            {
                playSpecial(CountdownGoSound);
                popupWindow.dismiss();      // Dismiss Countdown
                gameBG.start();             // Start background song
                GameTimers(StartTime);      // Start game timers
                paused = false;
                Countdown_active = false;
            }
        }.start();
    }

    public void GameOver() {
        paused = true;
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
                intentAgain.putExtra("GoodSwirls", Good_Pressed);                     //
                intentAgain.putExtra("BadSwirls", Bad_Pressed);
                intentAgain.putExtra("Good2Swirls", Good2_Pressed);
                intentAgain.putExtra("TimeSwirls", Time_Pressed);
                startActivity(intentAgain);                                           //go to PlayAgain activity/menu
                finish();
            }
        }.start();
    }

    // Game Start and Game Over(end) ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    protected void onResume() {
        super.onResume();

        GoodSound = new MediaPlayer();     // Setup MediaPlayer for good sound
        GoodSound2 = new MediaPlayer();    // Setup MediaPlayer for good sound
        BadSound = new MediaPlayer();      // Setup MediaPlayer for bad sound
        SpecialSound = new MediaPlayer();  // Setup MediaPlayer for Special button sounds
        gameBG = MediaPlayer.create(this, R.raw.game_song); //get background song
        gameBG.setLooping(true);    //make background song loop

        if (Countdown_active) { GameStart(); } // restart countdown timer

    }

    protected void onPause() {
        super.onPause();

        if (Countdown_active == false) { PauseActivate(); }                // Pause the game
        else { CountdownTimer.cancel(); popupWindow.dismiss(); }
        GoodSound.release();
        GoodSound2.release();
        BadSound.release();
        SpecialSound.release();

    }

    public void onBackPressed() {
        PauseActivate();                // Pause the game
    }

}
