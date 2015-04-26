package com.gmail.dianaupham.swirlytap;
import android.app.Activity;
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

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class levelPlay extends Activity implements View.OnClickListener
{
    //A ProgressDialog object
    private ProgressDialog progressDialog;

    private static final int NUM_ROWS = 6; //instantiated size of grid
    private static final int NUM_COLS = 4;
    String[][] luckArray1 = new String[NUM_ROWS][NUM_COLS]; //instantiate all luck arrays
    String[][] luckArray2 = new String[NUM_ROWS][NUM_COLS];
    String[][] luckArray3 = new String[NUM_ROWS][NUM_COLS];
    String[][] luckArray4 = new String[NUM_ROWS][NUM_COLS];
    Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];   //created total number of grid buttons
    int lives = 3; //number of lives that player has at start of game
    int score = 0; //initiating score
    int level = 1; //start at level 1
    int luckCount = 0;
    int tempLevel = 0; //set temp level to 0
    boolean heartGiven = false; //used to limit the number of times the player recieves perks
    boolean lightningGiven = false; //same as above
    boolean destroyGoodSwirls = false;
    int swirlPointsLeft = 0; //to determine if all swirls have been destroyed on screen
    int Current_Time = 61000;    //Current in-game time
    int Game_Speed = 400;        //Speed of the game
    int randCell;
    int goodCount = 0; //initialize score of lightning count. This will increase as good buttons appear on screen and decrease as good
    //buttons leave the screen. When a player taps the lightning bolt it will get all good swirls and 2x swirls
    LinearLayout llayout; //set it up after declaration
    CountDownTimer SwirlEngine;
    CountDownTimer Updater;
    ImageButton lifeOne;
    ImageButton lifeTwo;
    ImageButton lifeThree;
    ImageButton showscoreanim;
    TextView leveldisplay;
    TextView scoredisplay;
    Runnable buttonRunnable;
    Handler buttonHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes Title Bar
        setContentView(R.layout.level_play); //show res/layout/activity_single_player.xml

        llayout = (LinearLayout)findViewById(R.id.layout);
        lifeOne = (ImageButton)findViewById(R.id.lifeone);
        lifeTwo = (ImageButton)findViewById(R.id.lifetwo);
        showscoreanim = (ImageButton)findViewById(R.id.scoreanimation);
        lifeThree = (ImageButton)findViewById(R.id.lifethree);
        leveldisplay = (TextView)findViewById(R.id.leveldisplay);
        leveldisplay.setVisibility(View.INVISIBLE);//start invisible and make visible for 2 seconds at beginning of each level
        setLives(lives);//start game with 3 lives displayed as hearts on screen

        new LoadViewTask().execute();

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
                        //Score++;                                 // Add one to score
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
        //depending on level do certain things
        switch(lvl)
        {
            ///////////////////////////////LEVEL ONE////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////
            case 1:
                if(tempLevel < level)//if this is the first time the level has been called then do the below functions.
                {
                    luckCount = 0;
                    tempLevel = level;
                    llayout.setBackgroundResource(R.drawable.levelone);
                    //call function that displays level
                    displayLevel(level);
                    Game_Speed = 1500; gameTimer(60000);
                }
                //after every 20 buttons displayed, re declare luck arrays...make more random

                displayButton(1);  //display button from luckarray 1
                break;
            ///////////////////////////////LEVEL TWO////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////
            case 2:
                level = 2;
                if(tempLevel < level)
                {
                    luckCount = 0; //set luck count to 0 at start of every level
                    tempLevel = level;
                    llayout.setBackgroundResource(R.drawable.leveltwo);
                    displayLevel(level);//display level animation
                    Game_Speed = 900; //ramp up game speed
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
                displayButton(2); //using the second luck array
                break;
            ///////////////////////////////LEVEL THREE////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////
            case 3:
                level = 3;
                if(tempLevel < level)
                {
                   luckCount = 0;
                   tempLevel = level;
                   llayout.setBackgroundResource(R.drawable.levelthree);
                   displayLevel(level); //display the level animation
                   Game_Speed = 800;
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
            ///////////////////////////////LEVEL FOUR////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////
            case 4:
                level = 4;
                if(tempLevel < level)
                {
                    luckCount =0;
                    tempLevel = level;
                    llayout.setBackgroundResource(R.drawable.levelfour);
                    displayLevel(level);//display the level animation
                    Game_Speed = 700; //ramp up the speed of the game
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
            ///////////////////////////////LEVEL FIVE////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////
            case 5:
                level = 5;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelfive);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 650;
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
            ///////////////////////////////LEVEL SIX////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////
            case 6:
                level = 6;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelsix);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 600;
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
                displayButton(4);
                break;
            //////////////////////////////////////LEVEL SEVEN///////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////
            case 7:
                level = 7;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelseven);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 600;
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
                displayButton(4);
                break;
            ///////////////////////////////////LEVEL 8/////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////
            case 8:
                level = 8;
                if(tempLevel < level)
                {
                    luckCount = 0;
                    llayout.setBackgroundResource(R.drawable.levelseven);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 600;
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
                displayButton(4);
                break;
            //go to infinite level in which will only end if user loses all 3 lives
            //introduce death swirl..loses all 3 lives if clicked

        }
    }

    public void displayButton(int luckArrayType) //will call when button needs to be displayed
    {

        final Runnable buttonRunnable;
        final Handler buttonHandler = new Handler();
        Random r = new Random(System.currentTimeMillis()); //randomly select location in luck array
        int randRow = r.nextInt(NUM_ROWS);
        int randCol = r.nextInt(NUM_COLS);

        if(luckArrayType == 1) //DISPLAY ALL BUTTONS FOR LUCKARRAY 1
        {
            if (luckArray1[randRow][randCol] == "good") //GOOD BUTTON +1 POINT IF CLICKED
            {


                final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayGoodSwirl(goodButton);

            }
        }
        else if(luckArrayType == 2) //DISPLAY ALL BUTTONS FOR LUCKARRAY 2
        {
            if(luckArray2[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
            {
                final Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayGoodSwirl(goodButton);
                //set 1 second timer... if timer reached then make button disappear
            }
            else if(luckArray2[randRow][randCol] == "bad")
            {
                displayBadSwirl();
            }
        }
                else if(luckArrayType == 3) //DISPLAY ALL BUTTONS FOR LUCKARRAY 3
        {
            if(luckArray3[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
            {
               Button goodButton = buttons[randRow][randCol];     //Button in this location
               displayGoodSwirl(goodButton);

                //set 1 second timer... if timer reached then make button disappear
            }
            else if(luckArray3[randRow][randCol] == "bad")
            {
               displayBadSwirl();
            }
            else if(luckArray3[randRow][randCol] == "twicePoints")
            {
               Button twiceButton = buttons[randRow][randCol];     //Button in this location
               displayTwiceSwirl(twiceButton);
            }
        }
        else if(luckArrayType == 4) //DISPLAY ALL BUTTONS FOR LUCKARRAY 3
        {

            if(luckArray4[randRow][randCol] == "bad")
            {
               displayBadSwirl();
            }
            else if(luckArray4[randRow][randCol] == "twicePoints")
            {
               Button twiceButton = buttons[randRow][randCol];     //Button in this location
               displayTwiceSwirl(twiceButton);
            }
            else if(luckArray4[randRow][randCol] == "lightning" && lightningGiven == false)
            {
                final Button lightningButton = buttons[randRow][randCol];     //Button in this location
                lightningButton.setBackgroundResource(R.drawable.lightningbolt); //Set image to goodswirl
                lightningButton.setEnabled(true);                            //Enable Swirl
                lightningButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        lightningButton.setVisibility(View.INVISIBLE);
                        lightningButton.setEnabled(false);


                    }
                };
                if(lightningButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1600); //will disappear after 2 seconds
                }
                lightningButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score+=goodCount;
                            destroyGoodSwirls = true;
                            lightningGiven = true; //dont allow any more lightnings to appear until set back to false
                            // scoreUpdate();
                        }
                    }
                });
            }
            else if(luckArray4[randRow][randCol] == "addLife" && heartGiven == false)
            {
                final Button lifeButton= buttons[randRow][randCol];     //Button in this location
                lifeButton.setBackgroundResource(R.drawable.hearttwo); //Set image to goodswirl
                lifeButton.setEnabled(true);                            //Enable Swirl
                lifeButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        lifeButton.setVisibility(View.INVISIBLE);
                        lifeButton.setEnabled(false);


                    }
                };
                if(lifeButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1600); //will disappear after 2 seconds
                }
                lifeButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            if(lives <3)
                            {
                                lives++;
                                setLives(lives);
                            }
                            heartGiven = true;
                            // scoreUpdate();
                        }
                    }
                });
            }
            else //GOOD BUTTON +1 POINT IF CLICKED
            {
                Button goodButton = buttons[randRow][randCol];     //Button in this location
                displayGoodSwirl(goodButton);

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

        Updater = new CountDownTimer(30, 30)
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
                Updater.start();
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
                break;
        }
    }

    public void levelUpdate() //will update level depending on score...
    {
        if (score >= 0 && score < 10) goToLevel(1);
        else if (score > 9 && score < 25) goToLevel(2);
        else if (score > 24 && score < 45) goToLevel(3);
        else if (score > 44 && score < 75) goToLevel(4);
        else if (score > 74 && score < 115) goToLevel(5);
        else if (score > 114 && score < 160) goToLevel(6);
        else if (score > 159 )goToLevel(7);
    }

    public void populateLuckArrays()
    {
        Random rand = new Random(System.currentTimeMillis()); //randomly select location in luck array

        for(int row = 0; row<NUM_ROWS; row++)
        {
            for(int col = 0; col<NUM_COLS; col++)
            {

                //////////////////////FIRST LUCK ARRAY///////////////////////////////////
                luckArray1[row][col] = "good"; //all items in luckArray1 are good buttons
                /////////////////////SECOND LUCK ARRAY///////////////////////////////////
                randCell = rand.nextInt(NUM_ROWS*NUM_COLS); //random selection of numbers
                if(randCell%5 ==0) //items in luck array 2 are both good and bad buttons
                    luckArray2[row][col] = "bad"; //place bad button in array
                else
                    luckArray2[row][col] = "good"; //much higher chance to receive good button
                /////////////////////THIRD LUCK ARRAY//////////////////////////////////////
                randCell = rand.nextInt(NUM_ROWS*NUM_COLS); //items in luck array 3 are good, bad, and double point buttons
                if(randCell%5 ==0) //much less chance to receive bad button
                    luckArray3[row][col] = "bad"; //place bad button in array
                else if(randCell%8 == 0)
                    luckArray3[row][col] = "twicePoints";
                else
                    luckArray3[row][col] = "good"; //much higher chance to receive good button
                /////////////////////FOURTH LUCK ARRAY/////////////////////////////////////
                randCell=rand.nextInt(NUM_ROWS*NUM_COLS);
                if(randCell == 22)
                    luckArray4[row][col] = "lightning";
                else if(randCell ==21)
                {
                    luckArray4[row][col] = "addLife";
                }
                else if(randCell%5 ==0) //much less chance to receive bad button
                    luckArray4[row][col] = "bad"; //place bad button in array
                else if(randCell%8 == 0)
                    luckArray4[row][col] = "twicePoints";
                else
                    luckArray4[row][col] = "good"; //much higher chance to receive good button
                /////////////////////FIFTH LUCK ARRAY//////////////////////////////////////

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


    public void displayGoodSwirl(final Button good)
    {

        goodCount++; //add to number of good swirls on screen
        good.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
        good.setEnabled(true);                            //Enable Swirl
        good.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        final CountDownTimer temp = new CountDownTimer(1200,1200) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 30 == 0)
                {
                    onFinish();
                }
                else
                {
                //check if lightning bolt is true
                    //if lightning button has been hit and total buttons is less than total points available
                    if(destroyGoodSwirls == true && swirlPointsLeft <= goodCount)
                    {
                        good.setEnabled(false);                     // Disable button
                        good.setBackgroundResource(R.drawable.goodswirl_break); //change to +1 and make dis
                        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                        anim.setDuration(300);
                        good.startAnimation(anim);
                        good.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                        swirlPointsLeft++;
                        //temp.cancel(); //cancel timer
                    }
                    else if(swirlPointsLeft >= goodCount)
                    {
                        destroyGoodSwirls = false;
                        swirlPointsLeft = 0;
                    }
                }
            }

            @Override
            public void onFinish() {
                AlphaAnimation anim3 = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                anim3.setDuration(200);
                good.startAnimation(anim3);
                good.setVisibility(View.INVISIBLE);
                good.setEnabled(false);
                if(goodCount > 0)
                    goodCount--;
            }
        }.start();

        good.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                {
                    //leveldisplay.setVisibility(View.VISIBLE);
                    //add a plus one animation by score
                    temp.cancel(); //cancel timer
                    showscoreanim.setBackgroundResource(R.drawable.plusone);//will appear when button is clicked
                    AlphaAnimation anim2 = new AlphaAnimation(1.0f, 0.0f);
                    anim2.setDuration(600);
                    showscoreanim.startAnimation(anim2); //start animation
                    showscoreanim.setVisibility(View.INVISIBLE);

                    v.setEnabled(false);                     // Disable button
                    v.setBackgroundResource(R.drawable.goodswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(300);
                    v.startAnimation(anim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    score++;                                 // Add one to score
                    if(goodCount > 0)
                        goodCount--; //take from number of good swirls on screen
                }
            }
        });
    }

    public void displayTwiceSwirl(final Button twice)
    {


        goodCount+=2;
        twice.setBackgroundResource(R.drawable.twiceswirl); //Set image to goodswirl
        twice.setEnabled(true);                            //Enable Swirl
        twice.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        final CountDownTimer temp = new CountDownTimer(1200,1200) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 30 == 0)
                {
                    onFinish();
                }
                else{
                    //checks if lightning bolt has been selected
                    //if it has then it destroys all shown green buttons and adds the total
                    if(destroyGoodSwirls == true && swirlPointsLeft <= goodCount)
                    {
                        twice.setEnabled(false);                     // Disable button
                        twice.setBackgroundResource(R.drawable.twiceswirl_break); //change to +1 and make dis
                        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                        anim.setDuration(300);
                        twice.startAnimation(anim);
                        twice.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                        swirlPointsLeft+=2;
                    }
                    else
                    {
                        destroyGoodSwirls = false;
                        swirlPointsLeft = 0;
                    }

                }
            }

            @Override
            public void onFinish() {
                AlphaAnimation anim4 = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                anim4.setDuration(200);
                twice.startAnimation(anim4);
                twice.setVisibility(View.INVISIBLE);
                twice.setEnabled(false);
                if(goodCount > 0)
                    goodCount-=2;

            }
        }.start();
        twice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                {
                    temp.cancel();
                    //leveldisplay.setVisibility(View.VISIBLE);
                    //add a plus one animation by score
                    showscoreanim.setBackgroundResource(R.drawable.plustwo);//will appear when button is clicked
                    AlphaAnimation anim5 = new AlphaAnimation(1.0f, 0.0f);
                    anim5.setDuration(600);
                    showscoreanim.startAnimation(anim5); //start animation
                    showscoreanim.setVisibility(View.INVISIBLE);


                    v.setEnabled(false);                     // Disable button
                    v.setBackgroundResource(R.drawable.twiceswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(300);
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

    public void displayBadSwirl()
    {
        final Runnable buttonRunnable;
        final Handler buttonHandler = new Handler();
        Random r = new Random(System.currentTimeMillis()); //randomly select location in luck array
        int randRow = r.nextInt(NUM_ROWS);
        int randCol = r.nextInt(NUM_COLS);

        final Button badButton = buttons[randRow][randCol];     //Button in this location
        badButton.setBackgroundResource(R.drawable.badswirl); //Set image to goodswirl
        badButton.setEnabled(true);                            //Enable Swirl
        badButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
        final CountDownTimer temp = new CountDownTimer(1200,1200) { // Set timer for disappearance
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 30 == 0)
                {
                    onFinish();
                }
                else{}
            }

            @Override
            public void onFinish() {

                badButton.setVisibility(View.INVISIBLE);
                badButton.setEnabled(false);

            }
        }.start();
        badButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                {
                    temp.cancel();
                    v.setEnabled(false);                     // Disable button
                    v.setBackgroundResource(R.drawable.badswirl_break); //change to +1 and make dis
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);//fade out the text
                    anim.setDuration(500);
                    v.startAnimation(anim);
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    v.setEnabled(false);                     // Disable button
                    score -= 2;                                 // Add one to score
                    lives -= 1;
                    setLives(lives);
                }
            }
        });

    }

}

