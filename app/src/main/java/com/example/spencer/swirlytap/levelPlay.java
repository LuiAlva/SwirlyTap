package com.example.spencer.swirlytap;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Random;

public class levelPlay extends Activity implements View.OnClickListener
{
    final int NUM_ROWS = 7;
    final int NUM_COLS = 10;
    int level = 1;
    LinearLayout llayout; //set it up after declaration

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes Title Bar
        setContentView(R.layout.level_play); //show res/layout/activity_single_player.xml
        llayout = (LinearLayout)findViewById(R.id.layout);

        populateButtons(); //add buttons to grid
        goToLevel(level); //go to level 1 at start

        ///////////////////////////////////////POPULATE LUCK ARRAY///////////////////////////////////

        //POPULATE luck array with different types of buttons
        Random rand = new Random(); //randomly select location in luck array

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

            }
        }//end 'for'
    }//end private void populateButtons

    @Override
    public void onClick(View v) {

    }

    public void goToLevel(int lvl)
    {
        //depending on level do certain things
        switch(lvl)
        {
            case 1:
            llayout.setBackgroundResource(R.drawable.levelone);
            break;
        }
    }
    //switch the level cases...for certain levels change background and run animation

}

