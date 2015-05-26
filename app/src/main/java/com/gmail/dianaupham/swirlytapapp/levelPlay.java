package com.gmail.dianaupham.swirlytapapp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gmail.dianaupham.swirlytapapp.swirlytap.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class levelPlay extends Activity implements View.OnClickListener
{
    //A ProgressDialog object
    private ProgressDialog progressDialog;

    private static final int NUM_ROWS = 6;      //instantiated size of grid
    private static final int NUM_COLS = 4;
    /*+++++++++++++++Instantiate all luck arrays++++++++++++++++*/
    String[][] luckArray1 = new String[NUM_ROWS][NUM_COLS];
    String[][] luckArray2 = new String[NUM_ROWS][NUM_COLS];
    String[][] luckArray3 = new String[NUM_ROWS][NUM_COLS];
    String[][] luckArray4 = new String[NUM_ROWS][NUM_COLS];
    String[][] luckArray5 = new String[NUM_ROWS][NUM_COLS];
    String[][] luckArray6 = new String[NUM_ROWS][NUM_COLS];
    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];        //created total number of grid buttons
    /*++++++++++++++++counters, scores, levels+++++++++++++++++++*/
    public static final String PREFS_NAME = "PREFS_FILE"; // Name of preference file
    int lives = 3;      //number of lives that player has at start of game
    int score = 0;      //initiating score
    int SCORE, ScorePass;          // for highscore comparison
    String NAME, NamePass;        // For highscore comparison
    int level = 1;      //start at level 1
    int missedSwirls = 0;
    int luckCount = 0;
    int tempLevel = 0;      //set temp level to 0
    int lightningCount = 0;
    int lightningAppearedCount = 0; //lightning bolts that have appeared
    int nukeAppearedCount = 0; //nukes that have appeared
    int doubleCount = 0;
    int badCount = 0;
    int heartCount = 0;
    int on_screen_goodCount = 0; //count score of good swirls currently on screen
    int on_screen_allCount = 0;  //count score of both good and bad swirls currently on screen
    boolean doublePoints = false;
    boolean levelLoaded = false;
    boolean paused = false;
    int lifeCount = 0;
    int nuke_button_count = 0;
    boolean heartGiven = false;     //used to limit the number of times the player recieves perks
    boolean destroyGoodSwirls = false;
    PopupWindow popupWindow; // Popup Window for Countdown, Pause menu, and Time over
    int swirlPointsLeft = 0;        //to determine if all swirls have been destroyed on screen
    //int HighScore;                  //For HighScore
    int Good_Pressed = 0;       // GoodSwirls pressed
    int Good2_Pressed = 0;      // 2xGoodSwirls pressed
    int Bad_Pressed = 0;        // BadSwirls pressed
    int GoodSwirlTotalPass = 0; //For passing old GoodSwirl total
    int TwiceSwirlTotalPass = 0;//For passing old TwiceSwirl total
    int BadSwirlTotalPass = 0;  //For passing old BadSwirl total
    int TimeAddTotalPass = 0;   //For passing old TimeAdd total
    int LP_GamesPlayedTotalPass = 0;//passing old SP_GamesPlayed total
    int lpFirstGamePlayed = 1;  // Set #game played to 1 after game finish, if first game played
    int Current_Time = 610000;       //Current in-game time
    int Game_Speed = 400;           //Speed of the game
    int randCell;
    int goodCount = 0; //initialize score of lightning count. This will increase as good buttons appear on screen and decrease as good
    int doublePointCount = 0;
    int twiceCount =0;
    //buttons leave the screen. When a player taps the lightning bolt it will get all good swirls and 2x swirls
    Uri tapGood;              // Sound when Good swirl is tapped
    Uri tapBad;               // Sound for Bad swirl
    Uri tapAddLife;           // Sound for Another Life
    Uri NukeBoom, ThunderBoom; // For Nuke Explosion
    MediaPlayer GoodSound;    // MediaPlayer for playing good sound
    MediaPlayer GoodSound2;   // MediaPlayer for playing good sound - for faster sound
    MediaPlayer BadSound;     // MediaPlayer for playing Bad sound
    MediaPlayer SpecialSound; // MediaPlayer for playing Special button sounds
    //MediaPlayer gameBG;       // For Background music
    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    /*++++++++++++++++++++++++++++++++Initializing buttons, images, timers, button arrays+++++++++++++++++++++++++++++++*/
    LinearLayout llayout; //set it up after declaration
    CountDownTimer SwirlEngine;
    CountDownTimer Updater;
    ImageButton lifeOne;
    ImageButton lifeTwo;
    ImageButton lifeThree;
    ImageButton missedOne;
    ImageButton missedTwo;
    ImageButton missedThree;
    ImageButton missedFour;
    ImageButton missedFive;
    ImageButton showscoreanim;
    ImageButton PauseButton; // create image button for pause
    TextView leveldisplay;
    TextView showLevel;
    TextView scoredisplay;
    Runnable buttonRunnable;
    Handler buttonHandler = new Handler();
    Handler mHandler = new Handler();
    buttonDisappear[] GoodArray = new buttonDisappear[20];
    buttonDisappear[] BadArray = new buttonDisappear[20];
    buttonDisappear[] SpecialArray = new buttonDisappear[20];

    private static final boolean AUTO_HIDE = true;          // Auto hide UI (ActionBar)
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; // Hide system UI after 1000 milliseconds
    private static final boolean TOGGLE_ON_CLICK = true;    // If UI is clicked show it
    private static final int HIDER_FLAGS = 0;   // The flags to pass to {@link com.gmail.dianaupham.swirlytap.SystemUiHider#getInstance}.
    private com.gmail.dianaupham.swirlytapapp.SystemUiHider mSystemUiHider;

    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //Hides the action and title bars!
        getActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes Title Bar
        setContentView(R.layout.level_play); //show res/layout/activity_single_player.xml
        /*Swirls displayed at the bottom of the screen that depict how many missed swirls user has*/
        llayout = (LinearLayout)findViewById(R.id.layout);
        lifeOne = (ImageButton)findViewById(R.id.lifeone);
        lifeTwo = (ImageButton)findViewById(R.id.lifetwo);
        missedOne = (ImageButton)findViewById(R.id.missedone);
        missedTwo = (ImageButton)findViewById(R.id.missedtwo);
        missedThree = (ImageButton)findViewById(R.id.missedthree);
        missedFour = (ImageButton)findViewById(R.id.missedfour);
        missedFive = (ImageButton)findViewById(R.id.missedfive);

        showscoreanim = (ImageButton)findViewById(R.id.scoreanimation);
        lifeThree = (ImageButton)findViewById(R.id.lifethree);
        leveldisplay = (TextView)findViewById(R.id.leveldisplay);
        showLevel = (TextView)findViewById(R.id.level);
        PauseButton = (ImageButton)findViewById(R.id.pause_button);
        PauseButton.setOnClickListener(this); //sets an onClickListener on PauseButton
        leveldisplay.setVisibility(View.INVISIBLE);//start invisible and make visible for 2 seconds at beginning of each level
        setLives(lives);//start game with 3 lives displayed as hearts on screen
        setMissed(missedSwirls);

        //gameBG = MediaPlayer.create(this, R.raw.game_song); //get background song
        //gameBG.setLooping(true);           //make background song loop
        GoodSound = new MediaPlayer();     // Setup MediaPlayer for good sound
        GoodSound2 = new MediaPlayer();    // Setup MediaPlayer for good sound
        BadSound = new MediaPlayer();      // Setup MediaPlayer for bad sound
        SpecialSound = new MediaPlayer();  // Setup MediaPlayer for Special button sounds
        GoodSound.setVolume(12,12);
        GoodSound2.setVolume(12,12);
        tapGood = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_good);    //Setup sound for Good swirl
        tapBad = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_bad);      //Setup sound for Bad swirl
        tapAddLife = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tap_time); //Setup sound for add Life
        NukeBoom = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.nuke_explosion);    //Setup sound for Nuke Boom
        ThunderBoom = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bomb_freeze);    //Setup sound for Nuke Boom
        //set all button arrays to null when game started
        for(int i = 0; i < 20; i++){
            GoodArray[i] = null;
            BadArray[i] = null;
            SpecialArray[i] = null;
        }
      //  gameTimer(60000);
        populateButtons(); //add buttons to grid
        populateLuckArrays(); //create all luck arrays prior to game starting
        goToLevel(level);

        UpdateTimer(60000);    // Start the Score Update Timer

    }
    class buttonDisappear
    {
        public Button ButtonId;
        CountDownTimer TimerId;
        public buttonDisappear(Button button, CountDownTimer timer){
            ButtonId = button;
            TimerId = timer;
        }
    }

    //To use the AsyncTask, it must be subclassed

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
                Button Swirl = new Button(this); //create button to display correctly
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
                        //score++;                                 // Add one to score
                    }
                });
                tableRow.addView(Swirl);
                buttons[row][col] = Swirl;
            }
        }//end 'for'
    }//end private void populateButtons

    public void goToLevel(int lvl)
    {
        showLevel.setText("Level: " + level);
        //depending on level do certain things
        switch(lvl)
        {
            /*++++++++++++++++++++++++++++++++++++LEVEL 1++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 1:
                if(tempLevel < level) {
                    luckCount = 0; //set luck count to 0 at start of every level
                    tempLevel = level;
                    PauseButton.setEnabled(false);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            // when level loaded = true then set boolean to true
                            speed_engine(Game_Speed);
                            PauseButton.setEnabled(true);
                        }
                    }, 2000);
                    displayLevel(level);
                    llayout.setBackgroundResource(R.drawable.levelone);
                    //call function that displays level

                    Game_Speed = 900; //gameTimer(60000);

                    //if (levelLoaded == true)

                }
                break;
           /*++++++++++++++++++++++++++++++++++++LEVEL 2++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 2:
                level = 2;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.leveltwo);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 600; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                //after 20 buttons displayed, re declare luck array
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 3++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 3:
                level = 3;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelthree);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 500; //gameTimer(60000); 925
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*+++++++++++++++++++++++++++++++++++++LEVEL 4++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 4:
                level = 4;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelfour);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 450; //gameTimer(60000); //875
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
           /*++++++++++++++++++++++++++++++++++++LEVEL 5++++++++++++++++++++++++++++++++++++++++++++*/
           /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 5:
                level = 5;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelfive);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 400; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*+++++++++++++++++++++++++++++++++++++LEVEL 6++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 6:
                level = 6;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelsix);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 375; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 7++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 7:
                level = 7;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelseven);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 350; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*+++++++++++++++++++++++++++++++++++++LEVEL 8++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 8:
                level = 8;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.leveleight);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 325; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*+++++++++++++++++++++++++++++++++++++LEVEL 9++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 9:
                level = 9;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelnine);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 300; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*+++++++++++++++++++++++++++++++++++++LEVEL 10++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 10:
                level = 10;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelten);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 275; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*+++++++++++++++++++++++++++++++++++++LEVEL 11++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 11:
                level = 11;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.leveleleven);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 260; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 12++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 12:
                level = 12;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.leveltwelve);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 245; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 13++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 13:
                level = 13;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelthirteen);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 230; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
           /*++++++++++++++++++++++++++++++++++++LEVEL 14++++++++++++++++++++++++++++++++++++++++++++*/
           /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 14:
                level = 14;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelfourteen);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 220; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*+++++++++++++++++++++++++++++++++++++LEVEL 15++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 15:
                level = 15;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelfifteen);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 210; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 16++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 16:
                level = 16;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelsixteen);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 200; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 17++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 17:
                level = 17;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelseventeen);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 190; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 18++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 18:
                level = 18;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.leveleighteen);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 185; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 19++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 19:
                level = 19;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelnineteen);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 180; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 20++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 20:
                level = 20;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.leveltwenty);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 175; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL INFINITE++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 21:
                level = 21;
                if(tempLevel < level)
                {
                    tempLevel = level;
                    luckCount = 0; //set luck count to 0 at start of every level
                    llayout.setBackgroundResource(R.drawable.levelinfinite);
                    displayLevel(level);//display the level animation
                    //call function that displays level
                    Game_Speed = 170; //gameTimer(60000);
                    //if (levelLoaded == true)
                    speed_engine(Game_Speed);
                }
                break;
            //go to infinite level in which will only end if user loses all 3 lives
            //introduce death swirl..loses all 3 lives if clicked
        }
    }

    public void displayButton(int luckArrayType) //will call when button needs to be displayed
    {
        int i;
        final Runnable buttonRunnable;
        final Handler buttonHandler = new Handler();
        Random r = new Random(System.currentTimeMillis()); //randomly select location in luck array
        int randRow = r.nextInt(NUM_ROWS);
        int randCol = r.nextInt(NUM_COLS);
        /*+++++++++++++++++++++++++++++++++++++++++LUCK ARRAY 1++++++++++++++++++++++++++++++++++++*/
        if(luckArrayType == 1) //DISPLAY ALL BUTTONS FOR LUCKARRAY 1
        {
            if (luckArray1[randRow][randCol] == "good") //GOOD BUTTON +1 POINT IF CLICKED
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                final Button goodButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayGoodSwirl(goodButton, i);
            }
        }
        /*+++++++++++++++++++++++++++++++++++++++LUCK ARRAY 2++++++++++++++++++++++++++++++++++++++*/
        else if(luckArrayType == 2) //DISPLAY ALL BUTTONS FOR LUCKARRAY 2
        {
            if(luckArray2[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                final Button goodButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayGoodSwirl(goodButton, i);
                //set 1 second timer... if timer reached then make button disappear
            }
            else if(luckArray2[randRow][randCol] == "bad")
            {
                for(i = 0; i < 20; i++) {        // Find empty spot in BadArray
                    if(BadArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button badButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayBadSwirl(badButton, i);
            }
        }
        /*++++++++++++++++++++++++++++++++LUCK ARRAY 3++++++++++++++++++++++++++++++++++++++++++++++++*/
        else if(luckArrayType == 3)
        {
            if(luckArray3[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
               Button goodButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayGoodSwirl(goodButton, i);

                //set 1 second timer... if timer reached then make button disappear
            }
            else if(luckArray3[randRow][randCol] == "bad")
            {
                for(i = 0; i < 20; i++) {        // Find empty spot in BadArray
                    if(BadArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button badButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayBadSwirl(badButton, i);
            }
            else if(luckArray3[randRow][randCol] == "twicePoints")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
               Button twiceButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayTwiceSwirl(twiceButton, i);
            }
        }
        /*+++++++++++++++++++++++++++++++++++++LUCK ARRAY 4++++++++++++++++++++++++++++++++++++++++*/
        else if(luckArrayType == 4) //DISPLAY ALL BUTTONS FOR LUCKARRAY 3
        {

            if(luckArray4[randRow][randCol] == "bad")
            {
                for(i = 0; i < 20; i++) {        // Find empty spot in BadArray
                    if(BadArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button badButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayBadSwirl(badButton, i);
            }
            else if(luckArray4[randRow][randCol] == "twicePoints")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
               Button twiceButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayTwiceSwirl(twiceButton, i);
            }
            else if(luckArray4[randRow][randCol] == "doublePoints")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button doubleButton = buttons[randRow][randCol];     //Button in this location
                //final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayDoublePoints(doubleButton, i);
            }
            else //GOOD BUTTON +1 POINT IF CLICKED
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button goodButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayGoodSwirl(goodButton, i);

                //set 1 second timer... if timer reached then make button disappear
            }
        }
        /*++++++++++++++++++++++++++++++++++++LUCK ARRAY 5++++++++++++++++++++++++++++++++++++++++++*/
        else if(luckArrayType == 5)
        {
            if(luckArray5[randRow][randCol] == "bad")
            {
                for(i = 0; i < 20; i++) {        // Find empty spot in BadArray
                    if(BadArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button badButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayBadSwirl(badButton, i);
            }
            else if(luckArray5[randRow][randCol] == "twicePoints")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button twiceButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayTwiceSwirl(twiceButton, i);
            }
            else if(luckArray5[randRow][randCol] == "doublePoints")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button doubleButton = buttons[randRow][randCol];     //Button in this location
                //final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayDoublePoints(doubleButton, i);
            }
            else if(luckArray5[randRow][randCol] == "addLife")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button lifeButton = buttons[randRow][randCol];     //Button in this location
                //final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayAddLife(lifeButton, i);

            }
            else if(luckArray5[randRow][randCol] == "lightning")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button lightningButton = buttons[randRow][randCol];     //Button in this location
                //final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayLightning(lightningButton, i);

            }

            else //GOOD BUTTON +1 POINT IF CLICKED
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button goodButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayGoodSwirl(goodButton, i);

                //set 1 second timer... if timer reached then make button disappear
            }
        }
        /*++++++++++++++++++++++++++++++++++LUCK ARRAY 6+++++++++++++++++++++++++++++++++++++++++++*/
        else if(luckArrayType == 6)
        {
            if(luckArray6[randRow][randCol] == "bad")
            {
                for(i = 0; i < 20; i++) {        // Find empty spot in BadArray
                    if(BadArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button badButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayBadSwirl(badButton, i);
            }
            else if(luckArray6[randRow][randCol] == "twicePoints")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button twiceButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayTwiceSwirl(twiceButton, i);
            }
            else if(luckArray6[randRow][randCol] == "doublePoints")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button doubleButton = buttons[randRow][randCol];     //Button in this location
                //final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayDoublePoints(doubleButton, i);
            }
            else if(luckArray6[randRow][randCol] == "addLife")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button lifeButton = buttons[randRow][randCol];     //Button in this location
                //final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayAddLife(lifeButton, i);

            }
            else if(luckArray6[randRow][randCol] == "lightning")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button lightningButton = buttons[randRow][randCol];     //Button in this location
                //final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayLightning(lightningButton, i);
            }
            else if(luckArray6[randRow][randCol] == "nuke")
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in SpecialArray
                    if(SpecialArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button nukeButton = buttons[randRow][randCol];     //Button in this location
                //final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayNuke(nukeButton, i);
            }
            else //GOOD BUTTON +1 POINT IF CLICKED
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button goodButton = buttons[randRow][randCol];     //Button in this location
                if(buttons[randRow][randCol].isEnabled())
                {} //if button is enabled in location already then do nothing, else display new button
                else
                    displayGoodSwirl(goodButton, i);

                //set 1 second timer... if timer reached then make button disappear
            }
        }
    } //End of displaybutton

    //GAME TIMERS BELOW
    public void gameTimer(final int Time) //used to speed up button display
    {
        SwirlEngine = new CountDownTimer(Time, Game_Speed)
        {
            int newScore = score;
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / Game_Speed == 0)
                    onFinish();
                else
                    levelUpdate(); //speed at which buttons are displayed
            }
            @Override
            public void onFinish()
            {
                //if finishes then end game and say level was not completed quickly enough
                //each level must be completed in under 1 minute
                levelUpdate();
                SwirlEngine.start();
            }
        }.start();
    }

    public void UpdateTimer(final int Time) {
        Updater = new CountDownTimer(Time, 30)
        {
            TextView totalScore= (TextView) findViewById(R.id.scoreAnimation);
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 30 == 0)
                    onFinish();
                else
                {
                    totalScore.setText("" + score); //Update Score Counter
                    levelUpdate();
                }
            }
            public void onFinish()
            {

                totalScore.setText("" + score); //Update Score Counter
                UpdateTimer(60000);
            }
        }.start();
    }
    /////////////////////////FUNCTION DISPLAYS PROPER AMOUNT OF LIVES (HEARTS)/////////////////////
    public void setLives(int numLives)
    {
        switch(numLives)
        {
            case 3: //user has 3 lives ...display all lives as red hearts
                lifeOne.setBackgroundResource(R.drawable.heart);
                lifeTwo.setBackgroundResource(R.drawable.heart);
                lifeThree.setBackgroundResource(R.drawable.heart);
                break;
            case 2: //user has 2 lives...display 2 red hearts and 1 grey heart
                lifeOne.setBackgroundResource(R.drawable.heart);
                lifeTwo.setBackgroundResource(R.drawable.heart);
                lifeThree.setBackgroundResource(R.drawable.grey);
                break;
            case 1: //user has 1 life left..display 1 red heart and 2 grey hearts
                lifeOne.setBackgroundResource(R.drawable.heart);
                lifeTwo.setBackgroundResource(R.drawable.grey);
                lifeThree.setBackgroundResource(R.drawable.grey);
                break;
            case 0: //open end screen activity  //GAME OVER
                lifeOne.setBackgroundResource(R.drawable.grey);
                lifeTwo.setBackgroundResource(R.drawable.grey);
                lifeThree.setBackgroundResource(R.drawable.grey);
                ScreenShot(levelPlay.this); //take a screenshot of end of LevelPlay
                DestroySwirls();            //destroy all swirls in array
                Updater.cancel();           //Cancel the Score Update Timer
                CompareScore();             //Get HighScore
                lpGamesPlayed();            //Get updated #LevelPlay games played
                TotalTapped();              //Update total tapped counts from LP
                Intent intentAgain = new Intent(levelPlay.this, PlayAgain_Level.class);  //create intent (to go to PlayAgain menu)
                intentAgain.putExtra("score", score); //send score
                intentAgain.putExtra("good_swirl", goodCount); //send number of good swirls hit
                intentAgain.putExtra("bad_swirl", badCount); //send number of bad swirls hit
                intentAgain.putExtra("twice_swirl", twiceCount); //send number of twice swirls hit
                intentAgain.putExtra("double_points", doublePointCount); //send number of double xp buttons hit
                intentAgain.putExtra("lightning_bolt", lightningCount); //send number of lightning bolts hit
                intentAgain.putExtra("add_life", heartCount);
                intentAgain.putExtra("nuke", nuke_button_count);
                startActivity(intentAgain);                                           //go to PlayAgain activity/menu
                finish();
                break;
        }
    }

    public void CompareScore() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        NAME = prefs.getString("PlayerName", "Player");
        SCORE = score;
        if( prefs.getInt("HighScore", 0) < SCORE ) {                       // Player High Score
            editor = prefs.edit();
            editor.putInt("HighScore", SCORE);
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore1", 0) < SCORE ) {                       // HighScore 1
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore1", 0);
            NamePass = prefs.getString("LevelHighName1", "Player");
            editor.putInt("LevelHighScore1", SCORE);
            editor.putString("LevelHighName1", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore2", 0) < SCORE ) {                       // HighScore 2
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore2", 0);
            NamePass = prefs.getString("LevelHighName2", "Player");
            editor.putInt("LevelHighScore2", SCORE);
            editor.putString("LevelHighName2", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore3", 0) < SCORE ) {                       // HighScore 3
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore3", 0);
            NamePass = prefs.getString("LevelHighName3", "Player");
            editor.putInt("LevelHighScore3", SCORE);
            editor.putString("LevelHighName3", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore4", 0) < SCORE ) {                       // HighScore 4
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore4", 0);
            NamePass = prefs.getString("LevelHighName4", "Player");
            editor.putInt("LevelHighScore4", SCORE);
            editor.putString("LevelHighName4", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore5", 0) < SCORE ) {                       // HighScore 5
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore5", 0);
            NamePass = prefs.getString("LevelHighName5", "Player");
            editor.putInt("LevelHighScore5", SCORE);
            editor.putString("LevelHighName5", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore6", 0) < SCORE ) {                       // HighScore 6
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore6", 0);
            NamePass = prefs.getString("LevelHighName6", "Player");
            editor.putInt("LevelHighScore6", SCORE);
            editor.putString("LevelHighName6", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore7", 0) < SCORE ) {                       // HighScore 7
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore7", 0);
            NamePass = prefs.getString("LevelHighName7", "Player");
            editor.putInt("LevelHighScore7", SCORE);
            editor.putString("LevelHighName7", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore8", 0) < SCORE ) {                       // HighScore 8
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore8", 0);
            NamePass = prefs.getString("LevelHighName8", "Player");
            editor.putInt("LevelHighScore8", SCORE);
            editor.putString("LevelHighName8", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore9", 0) < SCORE ) {                       // HighScore 9
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore9", 0);
            NamePass = prefs.getString("LevelHighName9", "Player");
            editor.putInt("LevelHighScore9", SCORE);
            editor.putString("LevelHighName9", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
        if( prefs.getInt("LevelHighScore10", 0) < SCORE ) {                       // HighScore 10
            editor = prefs.edit();
            ScorePass = prefs.getInt("LevelHighScore10", 0);
            NamePass = prefs.getString("LevelHighName10", "Player");
            editor.putInt("LevelHighScore10", SCORE);
            editor.putString("LevelHighName10", NAME);
            SCORE = ScorePass;
            NAME = NamePass;
            editor.commit();
        }
    }

    public void TotalTapped() {
        SharedPreferences prefsTotals = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTotals;
        NAME = prefsTotals.getString("PlayerName", "Player");

        //+++++++++GOOD SWIRL+++++++++++++++++++++++++++++
        if (prefsTotals.getInt("GoodSwirlTotal", 0) == 0) { //First game (or first green swirl tapped)
            editorTotals = prefsTotals.edit();
            editorTotals.putInt("GoodSwirlTotal", Good_Pressed);
            //editorTotals.putString("GoodSwirlTotalName", NAME);
            editorTotals.commit();
        } else if (prefsTotals.getInt("GoodSwirlTotal", 0) > 0) { //Add GoodSwirls clicked in this game to Total
            editorTotals = prefsTotals.edit();
            GoodSwirlTotalPass = prefsTotals.getInt("GoodSwirlTotal", 0);
            //NamePass = prefsTotals.getString("GoodSwirlTotal", "Player");
            GoodSwirlTotalPass = GoodSwirlTotalPass + Good_Pressed;
            editorTotals.putInt("GoodSwirlTotal", GoodSwirlTotalPass);
            //editorTotals.putString("GoodSwirlTotalName", NAME);
            editorTotals.commit();
        }
        //+++++++++TWICE SWIRL++++++++++++++++++++++++
        if (prefsTotals.getInt("TwiceSwirlTotal", 0) == 0) { //First game (or first yellow swirl tapped)
            editorTotals = prefsTotals.edit();
            editorTotals.putInt("TwiceSwirlTotal", Good2_Pressed);
            editorTotals.commit();
        } else if (prefsTotals.getInt("TwiceSwirlTotal", 0) > 0) { //Add TwiceSwirls clicked in this game to Total
            editorTotals = prefsTotals.edit();
            TwiceSwirlTotalPass = prefsTotals.getInt("TwiceSwirlTotal", 0);
            TwiceSwirlTotalPass = TwiceSwirlTotalPass + Good2_Pressed;
            editorTotals.putInt("TwiceSwirlTotal", TwiceSwirlTotalPass);
            editorTotals.commit();
        }
        //+++++++++BAD SWIRL++++++++++++++++++++++++
        if (prefsTotals.getInt("BadSwirlTotal", 0) == 0) { //First game (or first red swirl tapped)
            editorTotals = prefsTotals.edit();
            editorTotals.putInt("BadSwirlTotal", Bad_Pressed);
            editorTotals.commit();
        } else if (prefsTotals.getInt("BadSwirlTotal", 0) > 0) { //Add BadSwirls clicked in this game to Total
            editorTotals = prefsTotals.edit();
            BadSwirlTotalPass = prefsTotals.getInt("BadSwirlTotal", 0);
            BadSwirlTotalPass = BadSwirlTotalPass + Bad_Pressed;
            editorTotals.putInt("BadSwirlTotal", BadSwirlTotalPass);
            editorTotals.commit();
        }
    }//end TotalTapped()

    public void lpGamesPlayed() {
        SharedPreferences prefsTotals = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTotals;
        NAME = prefsTotals.getString("PlayerName", "Player");
        if (prefsTotals.getInt("lpGamesPlayedTotal", 0) == 0) { //First game played
            editorTotals = prefsTotals.edit();
            editorTotals.putInt("lpGamesPlayedTotal", lpFirstGamePlayed);
            editorTotals.commit();
        } else if (prefsTotals.getInt("lpGamesPlayedTotal", 0) > 0) { //More than one game played, increment count +1
            editorTotals = prefsTotals.edit();
            LP_GamesPlayedTotalPass = prefsTotals.getInt("lpGamesPlayedTotal", 0);
            LP_GamesPlayedTotalPass = LP_GamesPlayedTotalPass + 1;  //+1 SP game played
            editorTotals.putInt("lpGamesPlayedTotal", LP_GamesPlayedTotalPass);
            editorTotals.commit();
        }//end 'else if'
    }//end spGamesPlayed()

    public void setMissed(int numMissed)
    {
        switch(numMissed)
        {
            case 5:
                missedSwirls = 0; //reset
                lives--;
                setLives(lives);
                setMissed(missedSwirls);
                break;
            case 4: //user has 3 lives ...display all lives as red hearts
                missedOne.setBackgroundResource(R.drawable.missed);
                missedTwo.setBackgroundResource(R.drawable.missed);
                missedThree.setBackgroundResource(R.drawable.missed);
                missedFour.setBackgroundResource(R.drawable.missed);
                missedFive.setBackgroundResource(R.drawable.goodswirl);
                break;
            case 3: //user has 2 lives...display 2 red hearts and 1 grey heart
                missedOne.setBackgroundResource(R.drawable.missed);
                missedTwo.setBackgroundResource(R.drawable.missed);
                missedThree.setBackgroundResource(R.drawable.missed);
                missedFour.setBackgroundResource(R.drawable.goodswirl);
                missedFive.setBackgroundResource(R.drawable.goodswirl);
                break;
            case 2: //user has 1 life left..display 1 red heart and 2 grey hearts
                missedOne.setBackgroundResource(R.drawable.missed);
                missedTwo.setBackgroundResource(R.drawable.missed);
                missedThree.setBackgroundResource(R.drawable.goodswirl);
                missedFour.setBackgroundResource(R.drawable.goodswirl);
                missedFive.setBackgroundResource(R.drawable.goodswirl);
                break;
            case 1: //open end screen activity
                missedOne.setBackgroundResource(R.drawable.missed);
                missedTwo.setBackgroundResource(R.drawable.goodswirl);
                missedThree.setBackgroundResource(R.drawable.goodswirl);
                missedFour.setBackgroundResource(R.drawable.goodswirl);
                missedFive.setBackgroundResource(R.drawable.goodswirl);
                break;
            case 0:
                missedOne.setBackgroundResource(R.drawable.goodswirl);
                missedTwo.setBackgroundResource(R.drawable.goodswirl);
                missedThree.setBackgroundResource(R.drawable.goodswirl);
                missedFour.setBackgroundResource(R.drawable.goodswirl);
                missedFive.setBackgroundResource(R.drawable.goodswirl);
        }
    }
    public void levelUpdate() //will update level depending on score ...
    { //start music for each level here
        if (score >= 0 && score < 5) goToLevel(1);
        else if (score >= 5 && score < 15) goToLevel(2);
        else if (score >=15 && score < 30) goToLevel(3);
        else if (score >= 30 && score < 50) goToLevel(4);
        else if (score >= 50 && score < 75) goToLevel(5);
        else if (score >=75 && score < 105) goToLevel(6);
        else if (score >=105 && score < 140)goToLevel(7);
        else if (score >=140 && score < 180)goToLevel(8);
        else if (score >=180 && score < 225)goToLevel(9);
        else if (score >=225 && score < 275)goToLevel(10);
        else if (score >=275 && score < 330)goToLevel(11);
        else if (score >=330 && score < 390)goToLevel(12);
        else if (score >=390 && score < 485)goToLevel(13);
        else if (score >=485 && score < 585)goToLevel(14);
        else if (score >=585 && score < 690)goToLevel(15);
        else if (score >=690 && score < 800)goToLevel(16);
        else if (score >=800 && score < 915)goToLevel(17);
        else if (score >=915 && score < 1035)goToLevel(18);
        else if (score >=1035 && score < 1160)goToLevel(19);
        else if (score >=1160 && score < 1290)goToLevel(20);
        else if (score >=1290 )goToLevel(21); //level 21 is an infinite level that will continue until player loses all lives
    }

    public void populateLuckArrays()
    {
        Random rand = new Random(System.currentTimeMillis()); //randomly select location in luck array

        for(int row = 0; row<NUM_ROWS; row++)
        {
            for(int col = 0; col<NUM_COLS; col++)
            {
                /*+++++++++++++++++++++++++++++++FIRST LUCK ARRAY++++++++++++++++++++++++++++++++++*/
                luckArray1[row][col] = "good"; //all items in luckArray1 are good buttons
                /*+++++++++++++++++++++++++++++++SECOND LUCK ARRAY++++++++++++++++++++++++++++++++++*/
                randCell = rand.nextInt(NUM_ROWS*NUM_COLS); //random selection of numbers
                if(randCell%5 ==0) //items in luck array 2 are both good and bad buttons
                    luckArray2[row][col] = "bad"; //place bad button in array
                else
                    luckArray2[row][col] = "good"; //much higher chance to receive good button
                /*+++++++++++++++++++++++++++++++THIRD LUCK ARRAY++++++++++++++++++++++++++++++++++*/
                randCell = rand.nextInt(NUM_ROWS*NUM_COLS); //items in luck array 3 are good, bad, and double point buttons
                if(randCell%5 ==0) //much less chance to receive bad button
                    luckArray3[row][col] = "bad"; //place bad button in array
                else if(randCell%8 == 0)
                    luckArray3[row][col] = "twicePoints";
                else
                    luckArray3[row][col] = "good"; //much higher chance to receive good button
                /*+++++++++++++++++++++++++++++++FOURTH LUCK ARRAY++++++++++++++++++++++++++++++++++*/
                randCell=rand.nextInt(NUM_ROWS*NUM_COLS);
                if(randCell == 22)
                    luckArray4[row][col] = "doublePoints";
                else if(randCell%5 ==0) //much less chance to receive bad button
                    luckArray4[row][col] = "bad"; //place bad button in array
                else if(randCell%8 == 0)
                    luckArray4[row][col] = "twicePoints";
                else
                    luckArray4[row][col] = "good"; //much higher chance to receive good button
                /*+++++++++++++++++++++++++++++++FIFTH LUCK ARRAY++++++++++++++++++++++++++++++++++*/
                randCell=rand.nextInt(NUM_ROWS*NUM_COLS);
                if(randCell == 22)
                    luckArray5[row][col] = "doublePoints";
                else if(randCell == 23)
                    luckArray5[row][col] = "addLife";
                else if(randCell%5 ==0) //much less chance to receive bad button
                    luckArray5[row][col] = "bad"; //place bad button in array
                else if(randCell%8 == 0)
                    luckArray5[row][col] = "twicePoints";
                else if(randCell == 19)
                    luckArray5[row][col] = "lightning";
                else
                    luckArray5[row][col] = "good"; //much higher chance to receive good button
                /*++++++++++++++++++++++++++++++SIXTH LUCK ARRAY+++++++++++++++++++++++++++++++++++*/
                randCell=rand.nextInt(NUM_ROWS*NUM_COLS);
                if(randCell == 22)
                    luckArray6[row][col] = "doublePoints";
                else if(randCell == 23)
                    luckArray6[row][col] = "addLife";
                else if(randCell%5 ==0) //much less chance to receive bad button
                    luckArray6[row][col] = "bad"; //place bad button in array
                else if(randCell%8 == 0)
                    luckArray6[row][col] = "twicePoints";
                else if(randCell == 19)
                    luckArray6[row][col] = "lightning";
                else if(randCell == 17)
                    luckArray6[row][col] = "nuke";
                else
                    luckArray6[row][col] = "good"; //much higher chance to receive good button
            }
        }
    }
    public void displayLevel(int levelnum) //function used to display the level animation based on current level
    {
        if(levelnum == 21) leveldisplay.setText("Level Infinite...");
        else leveldisplay.setText("Level " + levelnum);
        //leveldisplay.setVisibility(View.VISIBLE);
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
        anim.setDuration(2000);
        leveldisplay.startAnimation(anim);
    }

    /*+++++++++++++++++++++++++++++++++++++DISPLAY GOOD SWIRL BUTTON++++++++++++++++++++++++++++++++++++++++*/
    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    public void displayGoodSwirl(final Button good, int i)
    {
        on_screen_goodCount++;
        on_screen_allCount++;
        good.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
        good.setEnabled(true);                            //Enable Swirl
        good.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        final int finalI = i;
        final CountDownTimer temp = new CountDownTimer(1700,1700) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 30 == 0)
                {
                    onFinish();
                }
                else
                {

                }
            }

            @Override
            public void onFinish() {
//                AlphaAnimation anim3 = new AlphaAnimation(1.0f, 0.0f);//fade out the text
//                anim3.setDuration(200);
//                good.startAnimation(anim3);
                on_screen_goodCount--;
                on_screen_allCount--;
                GoodArray[finalI].ButtonId = null;       // Remove Button ID
                GoodArray[finalI] = null;
                missedSwirls++;
                setMissed(missedSwirls);        //count off if swirl disappeared
                good.setVisibility(View.INVISIBLE);
                good.setEnabled(false);


            }
        }.start();
        GoodArray[i] =  new buttonDisappear(good, temp);
        good.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                {   //leveldisplay.setVisibility(View.VISIBLE);
                    //add a plus one animation by score
                    goodCount++; //add to number of good swirls on screen
                    on_screen_goodCount--;
                    on_screen_allCount--;
                    if(doublePoints == true)
                    {

                        score+=2;
                        showscoreanim.setBackgroundResource(R.drawable.plustwo);//will appear when button is clicked
                        AlphaAnimation anim2 = new AlphaAnimation(1.0f, 0.0f);
                        anim2.setDuration(600);
                        showscoreanim.startAnimation(anim2); //start animation
                        showscoreanim.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        score++;
                        showscoreanim.setBackgroundResource(R.drawable.plusone);//will appear when button is clicked
                        AlphaAnimation anim2 = new AlphaAnimation(1.0f, 0.0f);
                        anim2.setDuration(600);
                        showscoreanim.startAnimation(anim2); //start animation
                        showscoreanim.setVisibility(View.INVISIBLE);
                    }
                    GoodArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                    GoodArray[finalI].ButtonId = null;       // Remove Button ID
                    GoodArray[finalI] = null;                // Remove from array
                    v.setEnabled(false);                     // Disable button
                    v.setBackgroundResource(R.drawable.goodswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(200);
                    playGood(tapGood);
                    v.startAnimation(anim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                                                 // Add one to score
                    Good_Pressed++;              //+1 goodswirl tapped

                }
            }
        });
    }
    /*++++++++++++++++++++++++++++++++++++DISPLAY DOUBLE SWIRL BUTTON++++++++++++++++++++++++++++++++++++++*/
    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public void displayTwiceSwirl(final Button twice, int i)
    {
        on_screen_goodCount+=2;
        on_screen_allCount+=2;
        twice.setBackgroundResource(R.drawable.twiceswirl); //Set image to goodswirl
        twice.setEnabled(true);                            //Enable Swirl
        twice.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        final int finalI = i;
        final CountDownTimer temp = new CountDownTimer(1700,1700) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 30 == 0)
                {
                    onFinish();
                }
                else
                {

                }
            }

            @Override
            public void onFinish() {
//                AlphaAnimation anim4 = new AlphaAnimation(1.0f, 0.0f);//fade out the text
//                anim4.setDuration(200);
//                twice.startAnimation(anim4);
                on_screen_goodCount-=2;
                on_screen_allCount-=2;
                SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                SpecialArray[finalI] = null;
                twice.setVisibility(View.INVISIBLE);
                twice.setEnabled(false);

            }
        }.start();
        SpecialArray[i] =  new buttonDisappear(twice, temp);
        twice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                {
                    on_screen_goodCount-=2;
                    on_screen_allCount-=2;
                    twiceCount++; //add to number of good swirls on screen
                    if(doublePoints == true)
                    {
                        score+=4;
                        showscoreanim.setBackgroundResource(R.drawable.plusfour);//will appear when button is clicked
                        AlphaAnimation anim2 = new AlphaAnimation(1.0f, 0.0f);
                        anim2.setDuration(600);
                        showscoreanim.startAnimation(anim2); //start animation
                        showscoreanim.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        score+=2;
                        showscoreanim.setBackgroundResource(R.drawable.plustwo);//will appear when button is clicked
                        AlphaAnimation anim2 = new AlphaAnimation(1.0f, 0.0f);
                        anim2.setDuration(600);
                        showscoreanim.startAnimation(anim2); //start animation
                        showscoreanim.setVisibility(View.INVISIBLE);
                    }
                    //leveldisplay.setVisibility(View.VISIBLE);
                    //add a plus one animation by score
                    SpecialArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                    SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                    SpecialArray[finalI] = null;                // Remove from array
                    v.setEnabled(false);                     // Disable button
                    v.setBackgroundResource(R.drawable.twiceswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(200);
                    playGood2(tapGood);
                    v.startAnimation(anim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    // v.setEnabled(false);                  // Disable button
                    score+=2;                                // Add one to score
                    Good2_Pressed++;                         //+1 TwiceSwirl tapped

                    // scoreUpdate();
                }
            }
        });
    }
    /*++++++++++++++++++++++++++++++++++++++DISPLAY DOUBLE POINTS BUTTON+++++++++++++++++++++++++++++++++*/
    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public void displayDoublePoints(final Button doubleButton, int i) {
        if (doubleCount == 0) {
            doubleButton.setBackgroundResource(R.drawable.double_button_active); //Set image to goodswirl
            doubleButton.setEnabled(true);                            //Enable Swirl
            doubleButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
            final int finalI = i;
            final CountDownTimer temp = new CountDownTimer(1700, 1700) { // Set timer for disappearance
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished / 30 == 0) {
                        onFinish();
                    } else {
                    }
                }

                @Override
                public void onFinish() {
                    SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                    SpecialArray[finalI] = null;
                    doubleButton.setVisibility(View.INVISIBLE);
                    doubleButton.setEnabled(false);
                }
            }.start();
            SpecialArray[i] =  new buttonDisappear(doubleButton, temp);
            doubleButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    doublePointCount++; //add to number of doublePoint buttons that appeared
                    v.setEnabled(false);                     // Disable button
                    //v.setBackgroundResource(R.drawable.badswirl_break); //change to +1 and make dis
                    SpecialArray[finalI].TimerId.cancel();        // Cancel it's disappear Timer
                    SpecialArray[finalI].ButtonId = null;         // Remove Button ID
                    SpecialArray[finalI] = null;                  // Remove from array
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(200);
                    v.startAnimation(anim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    v.setEnabled(false);                     // Disable button
                    //start timer for five seconds
                    final CountDownTimer temp = new CountDownTimer(5000, 1000) { // Set timer for disappearance
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                doublePoints = true;
                            }
                        }
                        @Override
                        public void onFinish() {
                            doublePoints = false;
                        }
                    }.start();
                }
            });
            doubleCount++;
        }
        else
        {
            if(doubleCount == 10)
            {
                doubleCount = 0;
            }
            else doubleCount++;
        }
    }
    /*++++++++++++++++++++++++++++++++++++DISPLAY ADD LIFE BUTTON++++++++++++++++++++++++++++++++++++*/
    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public void displayAddLife(final Button lifeButton, int i)
    {
        if (lifeCount == 0) {
            lifeButton.setBackgroundResource(R.drawable.heart); //Set image to goodswirl
            lifeButton.setEnabled(true);                            //Enable Swirl
            lifeButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
            final int finalI = i;
            final CountDownTimer temp = new CountDownTimer(1700, 1700) { // Set timer for disappearance
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished / 30 == 0) {
                        onFinish();
                    } else {
                    }
                }

                @Override
                public void onFinish() {
                    SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                    SpecialArray[finalI] = null;
                    lifeButton.setVisibility(View.INVISIBLE);
                    lifeButton.setEnabled(false);
                }
            }.start();
            SpecialArray[i] =  new buttonDisappear(lifeButton, temp);
            lifeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    heartCount++;
                    if(lives < 3)
                    {
                        lives++;
                        setLives(lives);
                    }
                    else
                    {
                        missedSwirls = 0;
                        setMissed(missedSwirls); //update missed swirl display
                    }

                    v.setEnabled(false);                     // Disable button
                    //v.setBackgroundResource(R.drawable.badswirl_break); //change to +1 and make dis
                    SpecialArray[finalI].TimerId.cancel();        // Cancel it's disappear Timer
                    SpecialArray[finalI].ButtonId = null;         // Remove Button ID
                    SpecialArray[finalI] = null;                  // Remove from array
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(200);
                    playSpecial(tapAddLife);
                    v.startAnimation(anim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    v.setEnabled(false);                     // Disable button
                }

            });
            lifeCount++; /*once displayed, takes 10 calls to appear again*/
        }
        else
        {
            if(lifeCount == 20)
            {
                lifeCount = 0;
            }
            else lifeCount++;
        }
    }
    /*++++++++++++++++++++++++++++++++++++++++DISPLAY BAD SWIRL BUTTON+++++++++++++++++++++++++++++++++++*/
    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public void displayBadSwirl(final Button badButton, int i) {
            on_screen_allCount-=2;
            badButton.setBackgroundResource(R.drawable.badswirl); //Set image to goodswirl
            badButton.setEnabled(true);                            //Enable Swirl
            badButton.setVisibility(View.VISIBLE);                 //Make Swirl
            final int finalI = i;
            final CountDownTimer temp = new CountDownTimer(1200, 1200) { // Set timer for disappearance
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished / 30 == 0) {
                        onFinish();
                    } else {
                    }
                }

                @Override
                public void onFinish() {
                    on_screen_allCount+=2;
                    BadArray[finalI] = null;
                    badButton.setVisibility(View.INVISIBLE);
                    badButton.setEnabled(false);
                }
            }.start();
            BadArray[i] =  new buttonDisappear(badButton, temp);
            badButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                {
                    on_screen_allCount+=2;
                    badCount++;
                    BadArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                    BadArray[finalI].ButtonId = null;       // Remove Button ID
                    v.setEnabled(false);                     // Disable button
                    v.setBackgroundResource(R.drawable.badswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(500);
                    playBad(tapBad);
                    v.startAnimation(anim);
                    BadArray[finalI] = null;
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    v.setEnabled(false);                     // Disable button
                    score -= 2;                                 // Add one to score
                    lives -= 1;
                    Bad_Pressed++;
                    setLives(lives);
                }
                }
            });
        }

    public void displayLightning(final Button lightningButton, int i)
        {
            if(lightningAppearedCount == 0) {
                lightningButton.setBackgroundResource(R.drawable.lightningbolt); //Set image to goodswirl
                lightningButton.setEnabled(true);                            //Enable Swirl
                lightningButton.setVisibility(View.VISIBLE);                 //Make Swirl
                final int finalI = i;
                final CountDownTimer temp = new CountDownTimer(1200, 1200) { // Set timer for disappearance
                    public void onTick(long millisUntilFinished) {
                        if (millisUntilFinished / 30 == 0) {
                            onFinish();
                        } else {
                        }
                    }

                    @Override
                    public void onFinish() {
                        SpecialArray[finalI] = null;
                        lightningButton.setVisibility(View.INVISIBLE);
                        lightningButton.setEnabled(false);

                    }
                }.start();
                SpecialArray[i] = new buttonDisappear(lightningButton, temp);
                lightningButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        {
                            playSpecial(ThunderBoom);
                            if(doublePoints == true)
                            {
                                score+=on_screen_goodCount*2;
                            }
                            else score+=on_screen_goodCount;
                            lightningCount++; //add to number of good swirls on screen
                            SpecialArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                            SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                            v.setEnabled(false);                     // Disable button
                            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                            anim.setDuration(500);
                            v.startAnimation(anim);
                            SpecialArray[finalI] = null;
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            DestroyGoodSwirls(); //will destroy all swirls currently on screen
                        /*add all points by good swirls on screen*/
                        /*check if doublePoints = true. If so multiply score by 2*/
                        /*add the total amount to total*/
                        }
                    }
                });
                lightningAppearedCount++;
            }
            else
            {
                if(lightningAppearedCount == 7)
                {
                    lightningAppearedCount = 0;
                }
                else lightningAppearedCount++;
            }
        }
    /*++++++++++++++++++++++++++++++++DISPLAY NUKE BUTTON++++++++++++++++++++++++++++++++++++++*/
    public void displayNuke(final Button nukeButton, int i)
    {
        if(nukeAppearedCount == 0) {
            nukeButton.setBackgroundResource(R.drawable.nukebutton); //Set image to goodswirl
            nukeButton.setEnabled(true);                            //Enable Swirl
            nukeButton.setVisibility(View.VISIBLE);                 //Make Swirl
            final int finalI = i;
            final CountDownTimer temp = new CountDownTimer(1200, 1200) { // Set timer for disappearance
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished / 30 == 0) {
                        onFinish();
                    } else {
                    }
                }

                @Override
                public void onFinish() {
                    SpecialArray[finalI] = null;
                    nukeButton.setVisibility(View.INVISIBLE);
                    nukeButton.setEnabled(false);

                }
            }.start();
            SpecialArray[i] = new buttonDisappear(nukeButton, temp);
            nukeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    {
                        nuke_button_count++;
                        playSpecial(NukeBoom);
                        if(doublePoints == true)
                        {
                            score+=on_screen_allCount*2;
                        }
                        else score+=on_screen_allCount;

                        SpecialArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                        SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                        v.setEnabled(false);                     // Disable button
                        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                        anim.setDuration(500);
                        v.startAnimation(anim);
                        SpecialArray[finalI] = null;
                        v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                        v.setEnabled(false);                     // Disable button
                        DestroySwirls(); //will destroy all swirls currently on screen
                        /*add all points by good swirls on screen*/
                        /*check if doublePoints = true. If so multiply score by 2*/
                        /*add the total amount to total*/
                    }
                }
            });
            nukeAppearedCount++;
        }
        else
        {
            if(nukeAppearedCount == 7)
            {
                nukeAppearedCount = 0;
            }
            else nukeAppearedCount++;
        }
    }
    /*++++++++++++++++++++++++++++++++DESTROY ALL BUTTONS++++++++++++++++++++++++++++++++++++++*/
    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public void DestroySwirls()
        {
            for (int i = 0; i < 20; i++)
            {
                if (GoodArray[i] != null) {
                    GoodArray[i].TimerId.cancel();
                    GoodArray[i].ButtonId.setBackgroundResource(R.drawable.goodswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(200);
                    GoodArray[i].ButtonId.startAnimation(anim);
                    GoodArray[i].ButtonId.setVisibility(View.INVISIBLE);
                    GoodArray[i].ButtonId.setEnabled(false);
                }
                if (BadArray[i] != null) {
                    BadArray[i].TimerId.cancel();
                    BadArray[i].ButtonId.setBackgroundResource(R.drawable.badswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(200);
                    BadArray[i].ButtonId.startAnimation(anim);
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
    /*+++++++++++++++++++++++++++++USED FOR LIGHTNING BUTTON++++++++++++++++++++++++++++++++++++*/
    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public void DestroyGoodSwirls()
    {
            for (int i = 0; i < 20; i++) {
                if (GoodArray[i] != null) {
                    GoodArray[i].TimerId.cancel();
                    // GoodArray[i].ButtonId.setVisibility(View.INVISIBLE);
                    GoodArray[i].ButtonId.setBackgroundResource(R.drawable.goodswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(200);
                    GoodArray[i].ButtonId.startAnimation(anim);
                    GoodArray[i].ButtonId.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    GoodArray[i].ButtonId.setEnabled(false);
                }


                if (SpecialArray[i] != null) {
                    SpecialArray[i].TimerId.cancel();
                    SpecialArray[i].ButtonId.setVisibility(View.INVISIBLE);
                    SpecialArray[i].ButtonId.setEnabled(false);
                }
                GoodArray[i] = null;
                SpecialArray[i] = null;
            }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause_button:
                PauseActivate();                // Pause the game
                break;
        }
    }
    public void PauseActivate() {
        if(!paused) {
            paused = true;
            PauseButton.setEnabled(false);
            PauseButton.setClickable(false);
            //gameBG.pause();                      // Pause the game music
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
           // TimeCountdown.cancel();
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
                //gameBG.start();
                speed_engine(Game_Speed);
                Updater.start();
                PauseButton.setEnabled(true);
                PauseButton.setClickable(true);
            }
        }.start();
    }
    //Restarts game from Pause Menu
    public void Restart(View v) {
        popupWindow.dismiss();
        startActivity(new Intent(levelPlay.this, levelPlay.class));
        finish();
    }
    public void Quit(View v) {
        popupWindow.dismiss();
        startActivity(new Intent(levelPlay.this, MainActivity.class));
        finish();
    }
    public void PopupPauseMenu() {
        try {
            LayoutInflater inflater = (LayoutInflater) levelPlay.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    /*+++++++++++++++++++++++++++++++++++CONTROL SPEED OF BUTTONS+++++++++++++++++++++++++++++++++++*/
    void speed_engine(int speed)
    {
        if(level != 1)
            SwirlEngine.cancel();                   // cancel the old timer with the old speed
        SwirlEngine = new CountDownTimer(Current_Time, speed) { // start new timer with changed speed
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 0) {
                    onFinish();
                } else {
                    if(level ==1) displayButton(1);
                    else if(level ==2)displayButton(2);
                    else if(level ==3)displayButton(3);
                    else if(level ==4)displayButton(3);
                    else if(level ==5)displayButton(3);
                    else if(level ==6)displayButton(3);
                    else if(level ==7)displayButton(4);
                    else if(level ==8)displayButton(4);
                    else if(level ==9)displayButton(4);
                    else if(level ==10)displayButton(5);
                    else if(level ==11)displayButton(5);
                    else if(level ==12)displayButton(5);
                    else if(level ==13)displayButton(5);
                    else if(level ==14)displayButton(5);
                    else if(level ==15)displayButton(6);
                    else if(level ==16)displayButton(6);
                    else if(level ==17)displayButton(6);
                    else if(level ==18)displayButton(6);
                    else if(level ==19)displayButton(6);
                    else if(level ==20)displayButton(6);
                    else if(level ==21)displayButton(6);

                    if(luckCount == 20)
                    {
                        populateLuckArrays();
                        luckCount = 0;
                    }
                    else
                        luckCount++;
                }
            }
            @Override
            public void onFinish() {

            }
        }.start();
    }
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
    public void ScreenShot(Activity activity)
    {
        String screenshotPathLevel = Environment.getExternalStorageDirectory().toString() + "/" + "screenshotLevel.png";
        View view = getWindow().getDecorView().getRootView();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmapLevel;
        bitmapLevel = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        OutputStream fout = null;
        File imageFile = new File(screenshotPathLevel);
        try { //save bitmp as PNG file
            fout = new FileOutputStream(imageFile);
            bitmapLevel.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();

        GoodSound = new MediaPlayer();     // Setup MediaPlayer for good sound
        GoodSound2 = new MediaPlayer();    // Setup MediaPlayer for good sound
        BadSound = new MediaPlayer();      // Setup MediaPlayer for bad sound
        SpecialSound = new MediaPlayer();  // Setup MediaPlayer for Special button sounds
    }

    protected void onPause() {
        super.onPause();

        if (!paused) { PauseActivate(); }                // Pause the game
        else { SwirlEngine.cancel(); Updater.cancel();  popupWindow.dismiss(); }
        GoodSound.release();
        GoodSound2.release();
        BadSound.release();
        SpecialSound.release();
    }

    public void onBackPressed() {
        PauseActivate();                // Pause the game
    }

}



