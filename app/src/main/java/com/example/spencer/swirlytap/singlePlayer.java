package com.example.spencer.swirlytap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class singlePlayer extends ActionBarActivity
{
    int count = 0; //this is total score

    private static final int NUM_ROWS = 12; //instantiated size of grid
    private static final int NUM_COLS = 13;
    Button buttons[][] = new Button[NUM_ROWS][NUM_COLS]; //created total number of grid buttons
    String[][] luckArray = new String[NUM_ROWS][NUM_COLS]; //array containing good and bad buttons


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        //Fill luck array with certain good and bad buttons
        for(int row = 0; row<NUM_ROWS; row++)
        {
            for(int col = 0; col<NUM_COLS; col++)
            {
                if((col + row)%10 ==0) //much less chance to receive bad button
                {
                    luckArray[row][col] = "bad";
                }
                else
                    luckArray[row][col] = "good"; //much higher chance to receive good button
            }
        }

        // This Timer updates every 30 milliseconds, used for updating changing texts and
        // Images that constantly change
        new CountDownTimer(60000, 30)
        {
            //
            //Get access to totalScore Textbox
            TextView totalScore= (TextView) findViewById(R.id.totalScore);

            public void onTick(long millisUntilFinished) {

                //Update totalScore Textbox with current score, end at 60 seconds
                if (millisUntilFinished / 30 == 0) {
                    onFinish();
                } else {
                    // Update Textfield
                    totalScore.setText("Score: " + count);
                }
            }

            // Show text at end of timer
            public void onFinish() {
                totalScore.setText(count + " Congratulations!");
            }
        }.start();

        populateButtons(); //add buttons to grid


        new CountDownTimer(60000, 1000)
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
                    //display seconds left in text field
                    mTextField.setText("Time: " + millisUntilFinished / 1000);
                    //get a random number and modulo it to ROW and COL sizes
                    //this gets random element in array
                    Random r = new Random();
                    int randRow = r.nextInt(NUM_ROWS);
                    int randCol = r.nextInt(NUM_COLS);

                    if(luckArray[randRow][randCol]=="good")
                    {
                        Button goodButton = buttons[randRow][randCol];
                        goodButton.setBackgroundResource(R.drawable.goodswirl); //make this grid block location
                        //have the image of goodswirl
                        //Scale image to button: this makes all swirls small to fit grid block size
                        int newWidth = goodButton.getWidth();
                        int newHeight = goodButton.getHeight();
                        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.goodswirl);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                        Resources resource = getResources();
                        goodButton.setBackground(new BitmapDrawable(resource, scaledBitmap));

                    }

            }

            //stop time/game when time is up
            public void onFinish()
            {
                mTextField.setText("Finished!");
            }

            //player clicks on swirl add point

        }.start();




    }
    private void populateButtons() //creating grid of buttons
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
                final int FINAL_COL = col; //set col and row location to pass to gridButton
                final int FINAL_ROW = row; //this sends location of button
                Button Swirl = new Button(this); //create button to display correctly
                Swirl.setBackgroundColor(Color.TRANSPARENT);
                Swirl.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,1.0f));


                Swirl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_COL, FINAL_ROW);
                    }
                });
                tableRow.addView(Swirl);
                buttons[row][col] = Swirl;
            }
        }
    }

    private void gridButtonClicked(int row, int col) //any time a button clicked do something
    {
        //display message when a button on grid is clicked...saying where button location
       // buttons[row][col].setBackgroundColor(Color.TRANSPARENT);
       // Toast.makeText(this, "Button Clicked: " + row + "," + col, Toast.LENGTH_LONG).show();
        count++;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
/* updating file - me too*/