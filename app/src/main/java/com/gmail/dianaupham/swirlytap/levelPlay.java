package com.gmail.dianaupham.swirlytap;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.example.spencer.swirlytap.R;

import java.sql.Time;
import java.util.Random;

public class levelPlay extends Activity implements View.OnClickListener
{
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
    int tempLevel = 0; //set temp level to 0
    boolean heartGiven = false; //used to limit the number of times the player recieves perks
    boolean lightningGiven = false; //same as above
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
    TextView leveldisplay;


//    TextView mTextField = (TextView) findViewById(R.id.displayLives);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes Title Bar
        setContentView(R.layout.level_play); //show res/layout/activity_single_player.xml

        llayout = (LinearLayout)findViewById(R.id.layout);
        lifeOne = (ImageButton)findViewById(R.id.lifeone);
        lifeTwo = (ImageButton)findViewById(R.id.lifetwo);
        lifeThree = (ImageButton)findViewById(R.id.lifethree);
        leveldisplay = (TextView)findViewById(R.id.leveldisplay);
        leveldisplay.setVisibility(View.INVISIBLE);//start invisible and make visible for 2 seconds at beginning of each level
        setLives(lives);//start game with 3 lives displayed as hearts on screen
        populateButtons(); //add buttons to grid
        populateLuckArrays(); //create all luck arrays prior to game starting
        goToLevel(1); //start game at level 1
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
            case 1:
                if(tempLevel < level)
                {
                    tempLevel = level;
                    llayout.setBackgroundResource(R.drawable.levelone);
                    //call function that displays level
                    displayLevel(level);
                    Game_Speed = 1000; gameTimer(60000);
                }
                displayButton(1);
                break;
            ///////////////////////////////LEVEL TWO////////////////////////////////////////////
            case 2:
                level = 2;
                if(tempLevel < level)
                {
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
                //SwirlEngine.cancel();
                displayButton(2); //using the second luck array
                break;
            ///////////////////////////////LEVEL THREE////////////////////////////////////////////
            case 3:
                level = 3;
                if(tempLevel < level)
                {
                   tempLevel = level;
                   llayout.setBackgroundResource(R.drawable.levelthree);
                   displayLevel(level); //display the level animation
                   Game_Speed = 800;
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
                displayButton(2);
                break;
            ///////////////////////////////LEVEL FOUR////////////////////////////////////////////
            case 4:
                level = 4;
                if(tempLevel < level)
                {
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
                displayButton(3);
                break;
            ///////////////////////////////LEVEL FIVE////////////////////////////////////////////
            case 5:
                level = 5;
                if(tempLevel < level)
                {
                    llayout.setBackgroundResource(R.drawable.levelfive);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 800;
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
                displayButton(3);
                break;
            ///////////////////////////////LEVEL SIX////////////////////////////////////////////
            case 6:
                level = 6;
                if(tempLevel < level)
                {
                    llayout.setBackgroundResource(R.drawable.levelsix);
                    displayLevel(level);//display the level animation
                    tempLevel = level;
                    Game_Speed = 900;
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
                goodCount++; //add to number of good swirls on screen
                goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
                goodButton.setEnabled(true);                            //Enable Swirl
                goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run() {

                        goodButton.setVisibility(View.INVISIBLE);
                        goodButton.setEnabled(false);
                        if(goodCount > 0)
                            goodCount--;

                    }
                };
                if (goodButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1700); //will disappear after 2 seconds
                }
                goodButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score++;                                 // Add one to score
                            goodCount--; //take from number of good swirls on screen
                        }
                    }
                });

            }
        }
        else if(luckArrayType == 2) //DISPLAY ALL BUTTONS FOR LUCKARRAY 2
        {
            if(luckArray2[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
            {

                final Button goodButton = buttons[randRow][randCol];     //Button in this location
                goodCount++;
                goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
                goodButton.setEnabled(true);                            //Enable Swirl
                goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        goodButton.setVisibility(View.INVISIBLE);
                        goodButton.setEnabled(false);
                        if(goodCount > 0)
                            goodCount--;

                    }
                };
                if(goodButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1700); //will disappear after 2 seconds
                }
                goodButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score++;                                 // Add one to score
                            if(goodCount > 0)
                                goodCount--;
                        }
                    }
                });


                //set 1 second timer... if timer reached then make button disappear
            }
            else if(luckArray2[randRow][randCol] == "bad")
            {
                final Button badButton = buttons[randRow][randCol];     //Button in this location
                badButton.setBackgroundResource(R.drawable.badswirl); //Set image to goodswirl
                badButton.setEnabled(true);                            //Enable Swirl
                badButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        badButton.setVisibility(View.INVISIBLE);
                        badButton.setEnabled(false);

                    }
                };
                if(badButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1700); //will disappear after 2 seconds
                }
                badButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
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
                else if(luckArrayType == 3) //DISPLAY ALL BUTTONS FOR LUCKARRAY 3
        {
            if(luckArray3[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
            {

                final Button goodButton = buttons[randRow][randCol];     //Button in this location
                goodCount++;
                goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
                goodButton.setEnabled(true);                            //Enable Swirl
                goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        goodButton.setVisibility(View.INVISIBLE);
                        goodButton.setEnabled(false);
                        if(goodCount > 0)
                            goodCount--;

                    }
                };
                if(goodButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1700); //will disappear after 2 seconds
                }
                goodButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score++;                                 // Add one to score
                            if(goodCount > 0)
                                goodCount--;
                        }
                    }
                });


                //set 1 second timer... if timer reached then make button disappear
            }
            else if(luckArray3[randRow][randCol] == "bad")
            {
                final Button badButton = buttons[randRow][randCol];     //Button in this location
                badButton.setBackgroundResource(R.drawable.badswirl); //Set image to goodswirl
                badButton.setEnabled(true);                            //Enable Swirl
                badButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        badButton.setVisibility(View.INVISIBLE);
                        badButton.setEnabled(false);

                    }
                };
                if(badButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1700); //will disappear after 2 seconds
                }
                badButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score -= 2;                                 // Add one to score
                            lives -= 1;
                            setLives(lives);
                        }
                    }
                });

            }
            else if(luckArray3[randRow][randCol] == "twicePoints")
            {
                final Button twiceButton = buttons[randRow][randCol];     //Button in this location
                goodCount+=2;
                twiceButton.setBackgroundResource(R.drawable.twiceswirl); //Set image to goodswirl
                twiceButton.setEnabled(true);                            //Enable Swirl
                twiceButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        twiceButton.setVisibility(View.INVISIBLE);
                        twiceButton.setEnabled(false);
                        if(goodCount > 0)
                            goodCount-=2;

                    }
                };
                if(twiceButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1600); //will disappear after 2 seconds
                }
                twiceButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score+=2;                                 // Add one to score
                            if(goodCount > 0)
                                goodCount-=2;
                            // scoreUpdate();
                        }
                    }
                });
            }
        }
        else if(luckArrayType == 4) //DISPLAY ALL BUTTONS FOR LUCKARRAY 3
        {

            if(luckArray4[randRow][randCol] == "bad")
            {
                final Button badButton = buttons[randRow][randCol];     //Button in this location
                badButton.setBackgroundResource(R.drawable.badswirl); //Set image to goodswirl
                badButton.setEnabled(true);                            //Enable Swirl
                badButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        badButton.setVisibility(View.INVISIBLE);
                        badButton.setEnabled(false);

                    }
                };
                if(badButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1700); //will disappear after 2 seconds
                }
                badButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score -= 2;                                 // Add one to score
                            lives -= 1;
                            setLives(lives);
                        }
                    }
                });

            }
            else if(luckArray4[randRow][randCol] == "twicePoints")
            {
                final Button twiceButton = buttons[randRow][randCol];     //Button in this location
                goodCount+=2;
                twiceButton.setBackgroundResource(R.drawable.twiceswirl); //Set image to goodswirl
                twiceButton.setEnabled(true);                            //Enable Swirl
                twiceButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        twiceButton.setVisibility(View.INVISIBLE);
                        twiceButton.setEnabled(false);
                        if(goodCount > 0)
                            goodCount-=2;

                    }
                };
                if(twiceButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1600); //will disappear after 2 seconds
                }
                twiceButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score+=2;                                 // Add one to score
                            if(goodCount > 0)
                                goodCount-=2;
                            // scoreUpdate();
                        }
                    }
                });
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

                final Button goodButton = buttons[randRow][randCol];     //Button in this location
                goodCount++;
                goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
                goodButton.setEnabled(true);                            //Enable Swirl
                goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
                buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                    public void run()
                    {

                        goodButton.setVisibility(View.INVISIBLE);
                        goodButton.setEnabled(false);
                        if(goodCount > 0)
                            goodCount--;

                    }
                };
                if(goodButton.isEnabled() == true) {
                    buttonHandler.postDelayed(buttonRunnable, 1700); //will disappear after 2 seconds
                }
                goodButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        {
                            v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                            v.setEnabled(false);                     // Disable button
                            score++;                                 // Add one to score
                            if(goodCount > 0)
                                goodCount--;
                        }
                    }
                });


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
            TextView totalScore= (TextView) findViewById(R.id.displayScore);
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
        else if (score > 114) goToLevel(6);
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
}

