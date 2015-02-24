package com.example.spencer.swirlytap;

/**
 * Created by Eberechi on 2/23/2015.
 */
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;

public class CustomView extends View
    //class to display the color of the page
{
    public CustomView(Context context)
    {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawRGB(39, 111,184);
    }
}
