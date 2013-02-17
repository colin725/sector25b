package com.fsck.sector25;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Grid {

    private static int width;
    private static int height;

    private Point position;

    public Grid() {
        position = new Point(0, 0);
    }

    public void set(int screenWidth, int screenHeight) {
        width = screenWidth;
        height = screenHeight;
    }

    public void update(Vector charVelocity) {
        position.subtract(charVelocity);
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setAlpha(30);
        for(int i = 0; i <= height / 125; i++){
            canvas.drawLine(0, i*125 + position.getY() % 125, width, i*125 + position.getY() % 125, paint);
        }
        for(int i = 0; i <= width / 125; i++){
            canvas.drawLine(i*125 + position.getX() % 125, 0, i*125 + position.getX() % 125, height, paint);
        }
        paint.setAlpha(255);
    }
}
