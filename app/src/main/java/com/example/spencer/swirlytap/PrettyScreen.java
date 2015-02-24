package com.example.spencer.swirlytap;

/**
 * Created by Eberechi on 2/23/2015.
 */

import android.app.Activity;
import android.os.Bundle;


public class PrettyScreen extends Activity
    //This class represents the screen that will be displayed
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(new CustomView(this));
    }
}