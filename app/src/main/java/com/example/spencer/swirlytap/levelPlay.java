package com.example.spencer.swirlytap;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class levelPlay extends Activity implements View.OnClickListener
{
    private static final int NUM_ROWS = 6; //instantiated size of grid
    private static final int NUM_COLS = 4;
    String[][] luckArray = new String[NUM_ROWS][NUM_COLS];
    Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];   //created total number of grid buttons
    int lives = 3; //number of lives that player has at start of game
    int score = 0; //initiating score
    int level = 1; //start at level 1
    int lvl1=9, lvl2 = 19, lvl3 = 29, lvl4 = 49;
    int Current_Time = 61000;    //Current in-game time
    int Game_Speed = 400;        //Speed of the game
    LinearLayout llayout; //set it up after declaration
    CountDownTimer SwirlEngine;
    CountDownTimer Updater;
//    TextView mTextField = (TextView) findViewById(R.id.displayLives);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes Title Bar
        setContentView(R.layout.level_play); //show res/layout/activity_single_player.xml
        llayout = (LinearLayout)findViewById(R.id.layout);
        populateButtons(); //add buttons to grid
        GameTimers(61000);

        //goToLevel(level); //go to level 1 at start

        ///////////////////////////////////////POPULATE LUCK ARRAY///////////////////////////////////

        //POPULATE luck array with different types of buttons
        Random rand = new Random(); //randomly select location in luck array
        boolean isDisplay = false; //used to display certain buttons when true of same mod value
        for(int row = 0; row<NUM_ROWS; row++)
        {
            for(int col = 0; col<NUM_COLS; col++)
            {
                //bad button should display in a more random fashion...please test
                int randCell = rand.nextInt(NUM_ROWS*NUM_COLS); //random selection of numbers
                if(randCell%5 ==0) //much less chance to receive bad button
                    luckArray[row][col] = "bad"; //place bad button in array
                else if(randCell%6 == 0)
                    luckArray[row][col] = "twicePoints"; //place 2x button in array
                else
                    luckArray[row][col] = "good"; //much higher chance to receive good button
            }
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
                //Swirl.setBackgroundResource(R.drawable.goodswirl); //make this grid block location have the image of goodswirl
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
    public void onClick(View v) {

    }

    void GameTimers(int Time) { // contains the timers of the game
        Updater = new CountDownTimer(Time, 30)
        {
            //Get access to totalScore Textbox
            TextView totalLives= (TextView) findViewById(R.id.displayLives);

            public void onTick(long millisUntilFinished)
            {
                //Update totalScore Textbox with current score, end at 60 seconds
                if (millisUntilFinished / 30 == 0)
                    onFinish();

                else
                    totalLives.setText("" + lives); //Update Score Counter
                    //Speed_Engine(Score);           //Update the Speed
            }
            // Show text at end of timer
            public void onFinish() {
                totalLives.setText("" + lives); //Update Score Counter
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
                    if(score >= 0 && score < 10) goToLevel(1);
                    else if(score > 9 && score < 25) goToLevel(2);
                    else if(score > 24 && score < 45) goToLevel(3);
                    else if(score > 44 && score < 85) goToLevel(4);
                }
            }
            public void onFinish()
            {

            }

        }.start();

    } // End of GameTimers

    public void goToLevel(int lvl)
    {
        //depending on level do certain things
        switch(lvl)
        {
            case 1:
                level = 1;
                llayout.setBackgroundResource(R.drawable.levelone);
                displayButton(level);
                break;
            case 2:
                level = 2;
                llayout.setBackgroundResource(R.drawable.leveltwo);
                //SwirlEngine.cancel();
                displayButton(level);
                break;
            case 3:
                level = 3;
                llayout.setBackgroundResource(R.drawable.levelthree);
                //SwirlEngine.cancel();
                displayButton(level);
                break;
            case 4:
                level = 4;
                llayout.setBackgroundResource(R.drawable.levelthree);
                displayButton(level);
                break;

        }
    }

    public void gameTimer(final int currentLevel, final int gameTime)
    {


        SwirlEngine = new CountDownTimer(60000, gameTime)
        {
            int newScore = score;
            public void onTick(long millisUntilFinished)
            {
                if (score > newScore + 9 * currentLevel)
                {
                    onFinish();
                }
                else
                    displayButton(currentLevel);

            }

            @Override
            public void onFinish() {
                goToLevel(currentLevel+1);
            }



        }.start();

    }
    public void displayLives(int showLives)
    {
       // mTextField.setText(showLives);
    }
    public void displayButton(int levelNum) //will call when button needs to be displayed
    {

        final Runnable buttonRunnable;
        final Handler buttonHandler = new Handler();
        Random r = new Random(); //randomly select location in luck array
        int randRow = r.nextInt(NUM_ROWS);
        int randCol = r.nextInt(NUM_COLS);
        levelNum = level;

        if(luckArray[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
        {
            final Button goodButton = buttons[randRow][randCol];     //Button in this location
            goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
            goodButton.setEnabled(true);                            //Enable Swirl
            goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
            buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                public void run()
                {

                    goodButton.setVisibility(View.INVISIBLE);
                    goodButton.setEnabled(false);

                }
            };
            if(goodButton.isEnabled() == true) {
                buttonHandler.postDelayed(buttonRunnable, 1600); //will disappear after 2 seconds
            }
            goodButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {

                    {
                        //tapGood.start();                         // Play short confirmation sound
                       // playGood(tapGood);
                        //v.startAnimation(disabledAnimation());
                        v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                        v.setEnabled(false);                     // Disable button
                        score++;                                 // Add one to score
                    }
                }
            });


            //set 1 second timer... if timer reached then make button disappear
        }
        else if(luckArray[randRow][randCol] == "bad" && levelNum > 1)
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
                        //tapBad.start();                         // Play short confirmation sound
                      //  playBad(tapBad);
                        //v.startAnimation(disabledAnimation());
                        v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                        v.setEnabled(false);                     // Disable button
                        score -= 5;                                 // Add one to score
                        lives -= 1;
                        displayLives(lives);
                        //check if lives == 0...if so then call end game activity
                    }
                }
            });

        }
        else if(luckArray[randRow][randCol] == "twicePoints" && levelNum >= 3)
        {
            final Button twiceButton = buttons[randRow][randCol];     //Button in this location
            twiceButton.setBackgroundResource(R.drawable.twiceswirl); //Set image to goodswirl
            twiceButton.setEnabled(true);                            //Enable Swirl
            twiceButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
            buttonRunnable = new Runnable() { //what will be called if button has not been clicked
                public void run()
                {

                    twiceButton.setVisibility(View.INVISIBLE);
                    twiceButton.setEnabled(false);

                }
            };
            if(twiceButton.isEnabled() == true) {
                buttonHandler.postDelayed(buttonRunnable, 1600); //will disappear after 2 seconds
            }
            twiceButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {

                    {
                        //tapGood.start();                         // Play short confirmation sound
                      //  playGood2(tapGood);
                        //v.startAnimation(disabledAnimation());
                        v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                        v.setEnabled(false);                     // Disable button
                        score+=2;                                 // Add one to score
                    }
                }
            });

        }


    } //End of displaybutton

    //switch the level cases...for certain levels change background and run animation

}

