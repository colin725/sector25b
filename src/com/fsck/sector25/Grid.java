package com.fsck.sector25;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Grid {

    private static int mWidth;
    private static int mHeight;
    private Point mPosition;

    /*
     * Experimental class to show the user how they are moving in relation to
     * foreground.  Movement feels better, but aesthetics suck.  Hopefully movement feels
     * more natural once there is more stuff going on.
     */

    public Grid() {
        mPosition = new Point(0, 0);
    }

    public void set(int screenWidth, int screenHeight) {
        mWidth = screenWidth;
        mHeight = screenHeight;
    }

    public void update(Vector charVelocity) {
        mPosition.subtract(charVelocity);
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setAlpha(30);
        for(int i = 0; i <= mHeight / 125; i++){
            canvas.drawLine(0, i*125 + mPosition.getY() % 125, mWidth, i*125 + mPosition.getY() % 125, paint);
        }
        for(int i = 0; i <= mWidth / 125; i++){
            canvas.drawLine(i*125 + mPosition.getX() % 125, 0, i*125 + mPosition.getX() % 125, mHeight, paint);
        }
        paint.setAlpha(255);
    }
}
