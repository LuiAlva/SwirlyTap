package com.example.spencer.swirlytap;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class SinglePlayer extends Activity implements View.OnClickListener {
    int Score = 0; //this is total score
    boolean addTime = false;    //Allows Time button to appear
    boolean Paused = false;     //True if game is paused
    //Grid & Storage ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    private static final int NUM_ROWS = 6; //instantiated size of grid
    private static final int NUM_COLS = 4;
    private static final int ARRAY_ROWS = NUM_ROWS * 5;
    private static final int ARRAY_COLS = NUM_COLS * 5;
    private static int FINAL_COL; //set col and row location to pass to gridButton
    private static int FINAL_ROW; //this sends location of button
    Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];   //created total number of grid buttons
    String[][] luckArray = new String[ARRAY_ROWS][ARRAY_COLS]; //array containing good and bad buttons
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //Time & Speed ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    int StartTime = 60000;       //Set start time, 60000 = 60 seconds temporary set to 20 seconds
    int Current_Time = 60000;    //Current in-game time
    int Game_Speed = 800;        //Speed of the game
    int Speed_Limit = 400;       //Highest Speed
    int Game_Speed_Add = 15;     //Add speed every increment
    int Speed_Increment = 8;     //Points needed to increment speed
    int Speed_Increment_Add = 4; //Add to Speed_Increment to make it harder to speed up
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    ImageButton PauseButton; //create image button for pause
    //Music & Sounds ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    MediaPlayer gameBG;  //for music
    MediaPlayer tapGood; //sound when good swirl is tapped
    MediaPlayer tapBad;  //sound when bad swirl is tapped
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    CountDownTimer Updater;        //Timer that updates test and values at 30 frames/ms(millisecond)
    CountDownTimer TimeCountdown;  //Timer that updates the timer every second
    CountDownTimer SwirlEngine;    //Timer that lets the swirls appear. Update depends on Game_Speed

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes Title Bar
        setContentView(R.layout.activity_single_player); //show res/layout/activity_single_player.xml
        gameBG = MediaPlayer.create(this, R.raw.game_song); //get song
        PauseButton= (ImageButton)findViewById(R.id.pause_button);
        PauseButton.setOnClickListener(this); //sets an onClickListener on PauseButton
//        gameBG.start(); //start song
        tapGood = MediaPlayer.create(this, R.raw.tap_good); //get sound for a tap on a good swirl
        tapBad = MediaPlayer.create(this, R.raw.tap_bad);   //get sound for a tap on a bad swirl
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

        GameTimers(StartTime);      //start game timers

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
                    Current_Time = (int) millisUntilFinished;             //Updates current time
                mTextField.setText("" + millisUntilFinished / 1000); //display seconds left in text field
            }
            //stop time/game when time is up
            public void onFinish()
            {
                gameBG.stop();                                                        //stop song
                mTextField.setText("0");                                              //Set end of timer
                Intent intentAgain = new Intent(SinglePlayer.this, PlayAgain.class);  //create intent (to go to PlayAgain menu)
                intentAgain.putExtra("score", Score);                                 //Send variable Score (score) to new intent (PlayAgain)
                startActivity(intentAgain);                                           //go to PlayAgain activity/menu
                finish();
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
                    if (Score > 1)
                        displayButton();

                }

            }
            public void onFinish()
            {

            }

        }.start();

    }

    public void displayButton() //will call when button needs to be displayed
    {
        Random r = new Random(); //randomly select location in luck array
        int randRow = r.nextInt(NUM_ROWS);
        int randCol = r.nextInt(NUM_COLS);

        if(luckArray[randRow][randCol]=="good") //GOOD BUTTON +1 POINT IF CLICKED
        {
            final Button goodButton = buttons[randRow][randCol];     //Button in this location
            goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
            goodButton.setEnabled(true);                            //Enable Swirl
            goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
            goodButton.postDelayed(new Runnable() { //after 3 seconds make button disappear
                public void run() {
                    goodButton.setVisibility(View.INVISIBLE);       //Make Swirl disappear after no click
                    goodButton.setEnabled(false);                   //Disable button
                }
            }, 3000); //button disappears after 3 seconds (3000 ms) with no click
            goodButton.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    tapGood.start();                         // Play short confirmation sound
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    v.setEnabled(false);                     // Disable button
                    Score++;                                 // Add one to score
                }
            });
            //set 1 second timer... if timer reached then make button disappear
        }
        else if(luckArray[randRow][randCol] == "bad")
    {
        final Button badButton = buttons[randRow][randCol];   //Button in this location
        badButton.setBackgroundResource(R.drawable.badswirl); //Set image to badswirl
        badButton.setEnabled(true);                           //Enable badSwirl
        badButton.setVisibility(View.VISIBLE);                //Make badSwirl visible
        badButton.postDelayed(new Runnable() { //after 2 seconds make button disappear
            public void run() {
                badButton.setVisibility(View.INVISIBLE);      //Make Swirl disappear after no click
                badButton.setEnabled(false);                  //Disable button
            }
        }, 2000); //button disappears after 2 seconds (2000 ms) with no click
        badButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapBad.start();                         // Play short confirmation sound
                v.setVisibility(View.INVISIBLE);        // Make Swirl disappear when clicked
                v.setEnabled(false);                    // Disable button
                Score -=5;                               // subtract 5 from score
            }
        });
    }
    else if(luckArray[randRow][randCol] == "twicePoints")
    {
        final Button twiceButton = buttons[randRow][randCol];   //Button in this location
        twiceButton.setBackgroundResource(R.drawable.twiceswirl); //Set image to badswirl
        twiceButton.setEnabled(true);                           //Enable badSwirl
        twiceButton.setVisibility(View.VISIBLE);                //Make badSwirl visible
        twiceButton.postDelayed(new Runnable() { //after 2 seconds make button disappear
            public void run() {
                twiceButton.setVisibility(View.INVISIBLE);      //Make Swirl disappear after no click
                twiceButton.setEnabled(false);                  //Disable button
            }
        }, 2000); //button disappears after 2 seconds (2000 ms) with no click
        twiceButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapGood.start();                         // Play short confirmation sound
                v.setVisibility(View.INVISIBLE);        // Make Swirl disappear when clicked
                v.setEnabled(false);                    // Disable button
                Score +=2;                               // subtract 5 from score
            }
        });
    }
    else if(luckArray[randRow][randCol] == "addTime")
    {
        if(addTime == true)
        {
            final Button timeButton = buttons[randRow][randCol];   //Button in this location
            timeButton.setBackgroundResource(R.drawable.fivetime); //Set image to badswirl
            timeButton.setEnabled(true);                           //Enable badSwirl
            timeButton.setVisibility(View.VISIBLE);                //Make badSwirl visible
            timeButton.postDelayed(new Runnable() { //after 2 seconds make button disappear
                public void run() {
                    timeButton.setVisibility(View.INVISIBLE);      //Make Swirl disappear after no click
                    timeButton.setEnabled(false);                  //Disable button
                }
            }, 2000); //button disappears after 2 seconds (2000 ms) with no click
            timeButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addTime = false;                        // Stop more time buttons from popping up
                    tapGood.start();                        // Play short confirmation sound
                    v.setVisibility(View.INVISIBLE);        // Make Swirl disappear when clicked
                    v.setEnabled(false);                    // Disable button
                    SwirlEngine.cancel();                   // Cancel old Timers
                    Updater.cancel();
                    TimeCountdown.cancel();
                    Current_Time+=5000;                             // Add 5 seconds to time
                    GameTimers(Current_Time);                           // Restart all timers
                }
            });
        }
        else if(addTime == false)
        {
            final Button goodButton = buttons[randRow][randCol];     //Button in this location
            goodButton.setBackgroundResource(R.drawable.goodswirl); //Set image to goodswirl
            goodButton.setEnabled(true);                            //Enable Swirl
            goodButton.setVisibility(View.VISIBLE);                 //Make Swirl Visible
            goodButton.postDelayed(new Runnable() { //after 3 seconds make button disappear
                public void run() {
                    goodButton.setVisibility(View.INVISIBLE);       //Make Swirl disappear after no click
                    goodButton.setEnabled(false);                   //Disable button
                }
            }, 3000); //button disappears after 3 seconds (3000 ms) with no click
            goodButton.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    tapGood.start();                         // Play short confirmation sound
                    v.setVisibility(View.INVISIBLE);         // Make Swirl disappear when clicked
                    v.setEnabled(false);                     // Disable button
                    Score++;                                 // Add one to score
                }
            });
        }

    }

        //could find other items such as 2x button
        //speedControl = 0;
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

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.pause_button:
                Paused = true;
                //gameBG.pause();
                //SwirlEngine.cancel();
                //Updater.cancel();
                //TimeCountdown.cancel();

                //Intent i = new Intent(this,PauseMenu.class);
                //startActivityForResult(i, 0);
                startActivity(new Intent(SinglePlayer.this, PauseMenu.class));
                break;
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        OnPaused(bundle.getBoolean("continue"));
        Paused = false;
        GameTimers(Current_Time);
        gameBG.start();
    }


    private void OnPaused(boolean Continue ) {
        Paused = Continue;
        GameTimers(Current_Time);
        gameBG.start();
    }*/

}
