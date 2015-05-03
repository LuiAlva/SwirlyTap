package com.gmail.dianaupham.swirlytap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gmail.dianaupham.swirlytap.swirlytap.R;

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
    int doubleCount = 0;
    boolean doublePoints = false;
    int lifeCount = 0;
    boolean heartGiven = false;     //used to limit the number of times the player recieves perks
    boolean destroyGoodSwirls = false;
    int swirlPointsLeft = 0;        //to determine if all swirls have been destroyed on screen
    int Current_Time = 61000;       //Current in-game time
    int Game_Speed = 400;           //Speed of the game
    int randCell;
    int goodCount = 0; //initialize score of lightning count. This will increase as good buttons appear on screen and decrease as good
    //buttons leave the screen. When a player taps the lightning bolt it will get all good swirls and 2x swirls
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
    TextView leveldisplay;
    TextView showLevel;
    TextView scoredisplay;
    Runnable buttonRunnable;
    Handler buttonHandler = new Handler();
    buttonDisappear[] GoodArray = new buttonDisappear[20];
    buttonDisappear[] BadArray = new buttonDisappear[20];
    buttonDisappear[] SpecialArray = new buttonDisappear[20];
    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes Title Bar
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
        leveldisplay.setVisibility(View.INVISIBLE);//start invisible and make visible for 2 seconds at beginning of each level
        setLives(lives);//start game with 3 lives displayed as hearts on screen
        setMissed(missedSwirls);
        //set all button arrays to null when game started
        for(int i = 0; i < 20; i++){
            GoodArray[i] = null;
            BadArray[i] = null;
            SpecialArray[i] = null;
        }
        UpdateTimer(60000);    // Start the Score Update Timer
        new LoadViewTask().execute();

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
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {
            //Create a new progress dialog
            progressDialog = new ProgressDialog(levelPlay.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //Set the dialog title to 'Loading...'
            progressDialog.setTitle("Loading...");
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Loading application View, please wait...");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(false);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            //The maximum number of items is 100
            progressDialog.setMax(100);
            //Set the current progress to zero
            progressDialog.setProgress(0);
            //Display the progress dialog
            progressDialog.show();
        }
        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {
            /* This is just a code that delays the thread execution 4 times,
             * during 850 milliseconds and updates the current progress. This
             * is where the code that is going to be executed on a background
             * thread must be placed.
             */
            try
            {
                //Get the current thread's token
                synchronized (this)
                {
                    //Initialize an integer (that will act as a counter) to zero
                    populateButtons(); //add buttons to grid
                    populateLuckArrays(); //create all luck arrays prior to game starting
                    int counter = 0;
                    //While the counter is smaller than four
                    while(counter <= 4)
                    {
                        //Wait 850 milliseconds
                        this.wait(850);
                        //Increment the counter
                        counter++;
                        //Set the current progress.
                        //This value is going to be passed to the onProgressUpdate() method.
                        publishProgress(counter*25);
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            //close the progress dialog
            progressDialog.dismiss();
            //initialize the View
            goToLevel(1); //start game at level 1
        }
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
    @Override
    public void onClick(View v) { }
    public void goToLevel(int lvl)
    {
        showLevel.setText("Level: " + level);
        //depending on level do certain things
        switch(lvl)
        {
            /*++++++++++++++++++++++++++++++++++++LEVEL 1++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 1:
                if(tempLevel < level)//if this is the first time the level has been called then do the below functions.
                {
                    luckCount = 0;
                    tempLevel = level;
                    if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                    else llayout.setBackgroundResource(R.drawable.levelone);
                    //call function that displays level
                    displayLevel(level);
                    Game_Speed = 1500; //gameTimer(60000);
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed)
                    { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();
                }
                //after every 20 buttons displayed, re declare luck arrays...make more random

                displayButton(1);  //display button from luckarray 1
                break;
           /*++++++++++++++++++++++++++++++++++++LEVEL 2++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 2:
                level = 2;
                if(tempLevel < level)
                {
                    luckCount = 0; //set luck count to 0 at start of every level
                    tempLevel = level;
                    llayout.setBackgroundResource(R.drawable.leveltwo);
                    displayLevel(level);//display level animation
                    Game_Speed = 1200; //ramp up game speed
                   // gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed)
                    { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();
                }
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(2); //using the second luck array
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 3++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 3:
                level = 3;
                if(tempLevel < level)
                {
                   luckCount = 0;
                   tempLevel = level;
                   llayout.setBackgroundResource(R.drawable.levelthree);
                   displayLevel(level); //display the level animation
                   Game_Speed = 1150;
                  // gameTimer(60000);
                   SwirlEngine.cancel();                   // cancel the old timer with the old speed
                   SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished)
                        {
                            if (millisUntilFinished / 30 == 0)
                            {
                                onFinish();
                            }
                            else
                            {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();
                }
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(2);
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 4++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 4:
                level = 4;
                if(tempLevel < level)
                {
                    luckCount =0;
                    tempLevel = level;
                    llayout.setBackgroundResource(R.drawable.levelfour);
                    displayLevel(level);//display the level animation
                    Game_Speed = 1050; //ramp up the speed of the game
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();
                }
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(3);
                break;
           /*++++++++++++++++++++++++++++++++++++LEVEL 5++++++++++++++++++++++++++++++++++++++++++++*/
           /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 5:
                level = 5;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                    else llayout.setBackgroundResource(R.drawable.levelfive);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 900;
                   // gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(3);
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 6++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 6:
                level = 6;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                    else llayout.setBackgroundResource(R.drawable.levelsix);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 850;
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(3);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 7++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 7:
                level = 7;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                    else llayout.setBackgroundResource(R.drawable.levelseven);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 800;
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelseven);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(4);
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 8++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 8:
                level = 8;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.leveleight);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 750;
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.leveleight);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(4);
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 9++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 9:
                level = 9;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelnine);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 700;
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelnine);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(4);
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 10++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 10:
                level = 10;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelten);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 650;
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelten);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(4);
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 11++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 11:
                level = 11;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.leveleleven);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 625;
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.leveleleven);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(4);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 12++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 12:
                level = 12;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.leveltwelve);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 600;
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.leveltwelve);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(4);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 13++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 13:
                level = 13;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelthirteen);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 575;
                   // gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelthirteen);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(4);
                break;
           /*++++++++++++++++++++++++++++++++++++LEVEL 14++++++++++++++++++++++++++++++++++++++++++++*/
           /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 14:
                level = 14;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelfourteen);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 550;
                  //  gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                //after 20 buttons displayed, re declare luck array
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelfourteen);
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(5);
                break;
             /*++++++++++++++++++++++++++++++++++++LEVEL 15++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 15:
                level = 15;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelfifteen);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 525;
                   // gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelfifteen);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(5);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 16++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 16:
                level = 16;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelsixteen);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 500;
                   // gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelsixteen);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(5);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 17++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 17:
                level = 17;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelseventeen);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 475;
                   // gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelseventeen);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(5);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 18++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 18:
                level = 18;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.leveleighteen);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 450;
                  //  gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.leveleighteen);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(5);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 19++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 19:
                level = 19;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelnineteen);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 425;
                   // gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelnineteen);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(5);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL 20++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 20:
                level = 20;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.leveltwenty);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 400;
                    //gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.leveltwenty);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(5);
                break;
            /*++++++++++++++++++++++++++++++++++++LEVEL INFINITE++++++++++++++++++++++++++++++++++++++++++++*/
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            case 21:
                level = 21;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelinfinite);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 375;
                   // gameTimer(60000);
                    SwirlEngine.cancel();                   // cancel the old timer with the old speed
                    SwirlEngine = new CountDownTimer(Current_Time, Game_Speed) { // start new timer with changed speed
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished / 30 == 0) {
                                onFinish();
                            } else {
                                levelUpdate();
                            }
                        }
                        @Override
                        public void onFinish() {
                            levelUpdate();
                            SwirlEngine.start();
                        }
                    }.start();

                }
                if(doublePoints == true)llayout.setBackgroundResource(R.drawable.doublepointbackround);
                else llayout.setBackgroundResource(R.drawable.levelinfinite);
                //after 20 buttons displayed, re declare luck array
                if(luckCount == 20)
                {
                    populateLuckArrays();
                    luckCount = 0;
                }
                else
                    luckCount++;
                displayButton(5);
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
                displayGoodSwirl(goodButton, i);

                //set 1 second timer... if timer reached then make button disappear
            }
        }
        else if(luckArrayType == 5)
        {
            if(luckArray5[randRow][randCol] == "bad")
            {
                for(i = 0; i < 20; i++) {        // Find empty spot in BadArray
                    if(BadArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button badButton = buttons[randRow][randCol];     //Button in this location
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

            else //GOOD BUTTON +1 POINT IF CLICKED
            {
                for(i = 0; i < 20; i++)
                {        // Find empty spot in GoodArray
                    if(GoodArray[i] == null){break;}
                    if(i == 19) {i = 0;}
                }
                Button goodButton = buttons[randRow][randCol];     //Button in this location
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
            case 0: //open end screen activity
                lifeOne.setBackgroundResource(R.drawable.grey);
                lifeTwo.setBackgroundResource(R.drawable.grey);
                lifeThree.setBackgroundResource(R.drawable.grey);

                DestroySwirls();            //destroy all swirls in array
                Updater.cancel();           //Cancel the Score Update Timer
                CompareScore();             // Get HighScore
                Intent intentAgain = new Intent(levelPlay.this, PlayAgain_Level.class);  //create intent (to go to PlayAgain menu)
                intentAgain.putExtra("score", score);
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
    public void levelUpdate() //will update level dependin
    // g on score...
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
                else
                    luckArray5[row][col] = "good"; //much higher chance to receive good button

            }
        }

    }
    public void displayLevel(int levelnum) //function used to display the level animation based on current level
    {
        leveldisplay.setText("Level " + levelnum);
        //leveldisplay.setVisibility(View.VISIBLE);
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
        anim.setDuration(2000);
        leveldisplay.startAnimation(anim);
    }


    public void displayGoodSwirl(final Button good, int i)
    {

        goodCount++; //add to number of good swirls on screen
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
                GoodArray[finalI].ButtonId = null;       // Remove Button ID
                GoodArray[finalI] = null;
                missedSwirls++;
                setMissed(missedSwirls);        //count off if swirl disappeared
                good.setVisibility(View.INVISIBLE);
                good.setEnabled(false);
                if(goodCount > 0)
                    goodCount--;

            }
        }.start();
        GoodArray[i] =  new buttonDisappear(good, temp);
        good.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                {
                    //leveldisplay.setVisibility(View.VISIBLE);
                    //add a plus one animation by score

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
                    v.startAnimation(anim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                                                 // Add one to score
                    if(goodCount > 0)
                        goodCount--; //take from number of good swirls on screen

                }
            }
        });
    }

    public void displayTwiceSwirl(final Button twice, int i)
    {


        goodCount+=2;
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
                SpecialArray[finalI].ButtonId = null;       // Remove Button ID
                SpecialArray[finalI] = null;
                twice.setVisibility(View.INVISIBLE);
                twice.setEnabled(false);
                if(goodCount > 0)
                    goodCount-=2;

            }
        }.start();
        SpecialArray[i] =  new buttonDisappear(twice, temp);
        twice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                {

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
                    v.startAnimation(anim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    // v.setEnabled(false);                     // Disable button
                    score+=2;                                 // Add one to score
                    if(goodCount > 0)
                        goodCount-=2;
                    // scoreUpdate();
                }
            }
        });

    }
    public void displayDoublePoints(final Button doubleButton, int i) {
        if (doubleCount == 0) {
            doubleButton.setBackgroundResource(R.drawable.doublepoints); //Set image to goodswirl
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

                    {

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

                    {
                        if(lives < 3)
                        {
                            lives++;
                            setLives(lives);
                        }
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
                    }
                }

            });
            lifeCount++; /*once displayed, takes 10 calls to appear again*/
        }
        else
        {
            if(lifeCount == 10)
            {
                lifeCount = 0;
            }
            else lifeCount++;
        }
    }
    public void displayBadSwirl(final Button badButton, int i) {

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
                    BadArray[finalI] = null;
                    badButton.setVisibility(View.INVISIBLE);
                    badButton.setEnabled(false);

                }
            }.start();
            BadArray[i] =  new buttonDisappear(badButton, temp);
            badButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    {
                        BadArray[finalI].TimerId.cancel();      // Cancel it's disappear Timer
                        BadArray[finalI].ButtonId = null;       // Remove Button ID
                        v.setEnabled(false);                     // Disable button
                        v.setBackgroundResource(R.drawable.badswirl_break); //change to +1 and make dis
                        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                        anim.setDuration(500);
                        v.startAnimation(anim);
                        BadArray[finalI] = null;
                        v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                        v.setEnabled(false);                     // Disable button
                        score -= 2;                                 // Add one to score
                        lives -= 1;
                        setLives(lives);
                    }
                }
            });

        }
        public void DestroySwirls()
        {

            for (int i = 0; i < 20; i++)
            {
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

    }



